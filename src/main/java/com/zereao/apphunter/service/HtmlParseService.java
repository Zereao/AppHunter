package com.zereao.apphunter.service;

import com.zereao.apphunter.common.builder.ContextBuilder;
import com.zereao.apphunter.pojo.entity.AppInfo;
import com.zereao.apphunter.pojo.vo.AppPriceInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zereao
 * @version 2019/05/10 15:06
 */
@Slf4j
@Service
public class HtmlParseService {
    @Resource
    private TemplateEngine templateEngine;

    /**
     * 将 appPriceInfoVO 中的数据渲染到 latest5ChangeTable 这个模板上
     *
     * @param appPriceInfoVO 包含信息的 AppPriceInfoVO
     * @return 数据渲染成功后的 HTML 字符串
     */
    public String parseHtml(AppPriceInfoVO appPriceInfoVO) {
        appPriceInfoVO = this.getMaxOfLimit(appPriceInfoVO, 5);
        log.info("vo = {}", appPriceInfoVO.toString());
        Context context = new ContextBuilder().set("appPriceInfoVO", appPriceInfoVO).build();
        return templateEngine.process("latest5ChangeTable", context);
    }

    /**
     * 获取 APP 最近最多 limit 次的价格变化信息
     *
     * @param infoVO AppPriceInfoVO
     * @param limit  次数限制
     * @return 新的 AppPriceInfoVO
     */
    @SuppressWarnings("SameParameterValue")
    private AppPriceInfoVO getMaxOfLimit(AppPriceInfoVO infoVO, int limit) {
        Map<String, List<AppInfo>> upPriceMap = infoVO.getUpPriceMap();
        Map<String, List<AppInfo>> upPriceMaxOfLimit = new HashMap<>(upPriceMap.size());
        Map<String, List<AppInfo>> downPriceMap = infoVO.getDownPriceMap();
        Map<String, List<AppInfo>> downPriceMaxOfLimit = new HashMap<>(downPriceMap.size());
        upPriceMap.forEach((appName, appInfoList) -> {
            List<AppInfo> listMaxOfLimit = appInfoList.stream().limit(limit).collect(Collectors.toList());
            upPriceMaxOfLimit.put(appName, listMaxOfLimit);
        });
        downPriceMap.forEach((appName, appInfoList) -> {
            List<AppInfo> listMaxOfLimit = appInfoList.stream().limit(limit).collect(Collectors.toList());
            downPriceMaxOfLimit.put(appName, listMaxOfLimit);
        });
        int upPriceNum = this.getAppNum(upPriceMaxOfLimit);
        int downPriceNum = this.getAppNum(downPriceMaxOfLimit);
        return AppPriceInfoVO.builder().upPriceMap(upPriceMaxOfLimit).downPriceMap(downPriceMaxOfLimit)
                .upNum(upPriceNum).downNum(downPriceNum).mailTime(infoVO.getMailTime()).build();
    }

    /**
     * 提取出的公共方法，获取到 价格变化map对应的App条目数
     *
     * @param priceChangeMap 价格变化的App map
     * @return 对应的条目数
     */
    private int getAppNum(Map<String, List<AppInfo>> priceChangeMap) {
        return priceChangeMap.values().stream().map(List::size).reduce(Integer::sum).orElse(0);
    }
}
