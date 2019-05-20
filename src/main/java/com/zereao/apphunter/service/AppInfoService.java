package com.zereao.apphunter.service;

import com.zereao.apphunter.common.enmu.DeleteFlag;
import com.zereao.apphunter.common.tools.OkHttp3Utils;
import com.zereao.apphunter.common.tools.ThreadPoolUtils;
import com.zereao.apphunter.dao.AppDAO;
import com.zereao.apphunter.dao.AppInfoDAO;
import com.zereao.apphunter.pojo.entity.App;
import com.zereao.apphunter.pojo.entity.AppInfo;
import com.zereao.apphunter.pojo.vo.AppPriceInfoVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zereao
 * @version 2019/05/09 15:19
 */
@Slf4j
@Service
public class AppInfoService {
    @Resource
    private AppInfoDAO appInfoDAO;
    @Resource
    private AppDAO appDAO;

    /**
     * 从URL中匹配 APP 名称的正则
     */
    private static final Pattern APP_NAME_PATTERN = Pattern.compile("^.*cn/app/(.+?)/+");

    /**
     * 从Url 中解析出App信息，如果App表中不存在，则插入；
     *
     * @param url APP详情URL
     */
    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(cacheNames = {"applist"}, beforeInvocation = true, allEntries = true)
    public void addApp(String url) {
        AppInfo appInfo = this.parseAppInfo(url);
        String appName = appInfo.getName();
        App app = appDAO.findFirstByNameAndUrlAndDeleteFlag(appName, url, DeleteFlag.NOT_DELETE.value());
        if (app == null) {
            app = App.builder().name(appInfo.getName()).url(url).build();
            appDAO.save(app);
            Long appId = app.getId();
            appInfo.setAppId(appId);
            appInfoDAO.save(appInfo);
        } else {
            log.info("App表中已存在当前App！请求插入的信息：appInfo = {}，已存在的App信息 = {}", appInfo, app);
        }
    }

    /**
     * 根据ID，将一个App的删除标记置为 1
     *
     * @param id 需要删除的App的ID
     */
    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(cacheNames = {"applist"}, beforeInvocation = true, allEntries = true)
    public void deleteApp(Long id) {
        boolean deleted = appDAO.findById(id).map(app -> {
            app.setDeleteFlag(DeleteFlag.DELETED.value());
            appDAO.save(app);
            return true;
        }).orElse(false);
        log.info("ID = {} 的App信息删除{}！", id, deleted ? "成功" : "失败");
    }

    @CachePut({"applist"})
    public List<App> getAllApps() {
        return appDAO.findAllByDeleteFlag(DeleteFlag.NOT_DELETE.value());
    }

    /**
     * 从 url 中，解析出 对应的APP信息
     *
     * @param url APP URL
     * @return 对应的APP信息
     */
    public AppInfo parseAppInfo(String url) {
        String respStr = OkHttp3Utils.doGet(url);
        if (StringUtils.isBlank(respStr)) {
            log.error("从url = {} 获取到的数据为空！", url);
            return null;
        }
        Document respHtml = Jsoup.parse(respStr);
        Element sectionParent = respHtml.selectFirst("div[class*=\"animation-wrapper is-visible ember-view\"]");
        Element infoRoot = sectionParent.selectFirst("dl[class*=\"information-list information-list--app medium-columns\"]");
        // 解析出 语言信息
        Element languageDd = infoRoot.selectFirst("dd[data-test-app-info-languages]");
        String language = languageDd.getElementsByTag("p").first().text();
        // 解析出 价格信息
        Element priceDd = infoRoot.selectFirst("dd[data-test-app-info-price]");
        String price = priceDd.text();
        // 尝试解析版本信息，可能不存在
        Element whatsNew = sectionParent.selectFirst("p[class*=\"whats-new__latest__version\"]");
        String version = Optional.ofNullable(whatsNew).map(Element::text).map(String::trim).orElse(null);
        // 从 URL 中解析出APP 名称
        Matcher matcher = APP_NAME_PATTERN.matcher(url);
        String appName = matcher.find() ? matcher.group(1).trim() : "";
        try {
            appName = URLDecoder.decode(appName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error("appName = {} URLDecode失败！", appName);
        }
        return AppInfo.builder().language(language.trim()).name(appName).price(price.trim()).version(version).url(url).build();
    }

    /**
     * 解析 AppList 中所有的APP信息，将其分为 价格上升和下降的两组
     *
     * @param appList App List
     * @return AppPriceInfoVO
     */
    @Transactional(rollbackOn = Exception.class)
    public AppPriceInfoVO parseAppInfo(List<App> appList) {
        List<App> upPriceList = new CopyOnWriteArrayList<>();
        List<App> downPriceList = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(appList.size());
        appList.forEach(app -> ThreadPoolUtils.execute(new ParseAppInfoTask(app, upPriceList, downPriceList, latch)));
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("ParseAppInfoTask | CountDownLatch await()中断异常！", e);
            return null;
        }
        Future<Map<String, List<AppInfo>>> upPriceFuture = ThreadPoolUtils.submit(new ParseAppPriceInfoTask(upPriceList));
        Future<Map<String, List<AppInfo>>> downPriceFuture = ThreadPoolUtils.submit(new ParseAppPriceInfoTask(downPriceList));
        Map<String, List<AppInfo>> upPriceMap = null;
        Map<String, List<AppInfo>> downPriceMap = null;
        try {
            upPriceMap = upPriceFuture.get();
            downPriceMap = downPriceFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ParseAppPriceInfoTask | Future.get()中断异常！", e);
            return null;
        }
        return AppPriceInfoVO.builder().upPriceMap(upPriceMap).downPriceMap(downPriceMap)
                .mailTime(LocalDateTime.now().toString()).build();
    }

    /**
     * 处理 App的价格信息的多线程内部类
     */
    @AllArgsConstructor
    private class ParseAppPriceInfoTask implements Callable<Map<String, List<AppInfo>>> {
        private List<App> appList;

        @Override
        public Map<String, List<AppInfo>> call() throws Exception {
            Map<String, List<AppInfo>> appInfoMap = new ConcurrentHashMap<>(16);
            this.appList.parallelStream().forEach(app -> {
                Long appId = app.getId();
                List<AppInfo> appInfoList = appInfoDAO.findByAppIdOrderByCreateTimeDesc(appId);
                appInfoMap.put(app.getName(), appInfoList);
            });
            return appInfoMap;
        }
    }

    /**
     * 内部类，用于多线程场景处理App 信息
     */
    @AllArgsConstructor
    private class ParseAppInfoTask implements Runnable {
        private App app;
        private List<App> upPriceList;
        private List<App> downPriceList;
        private CountDownLatch latch;

        @Override
        public void run() {
            try {
                AppInfo oldInfo = appInfoDAO.findFirstByAppIdOrderByCreateTimeDesc(this.app.getId());
                AppInfo newInfo = parseAppInfo(this.app.getUrl());
                newInfo.setAppId(this.app.getId());
                double oldPrice = getPriceNumber(oldInfo.getPrice());
                double newPrice = getPriceNumber(newInfo.getPrice());
                if (newPrice > oldPrice) {
                    this.upPriceList.add(this.app);
                    appInfoDAO.save(newInfo);
                } else if (newPrice < oldPrice) {
                    this.downPriceList.add(this.app);
                    appInfoDAO.save(newInfo);
                }
            } finally {
                this.latch.countDown();
            }
        }
    }

    /**
     * @param price AppInfo.getPrice，是个字符串，例如：¥448.00
     * @return 价格的数字部分，例如 448.00
     */
    private double getPriceNumber(String price) {
        return Double.parseDouble(price.replaceAll("[$¥,]", ""));
    }
}
