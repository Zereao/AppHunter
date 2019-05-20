package com.zereao.apphunter.controller;

import com.zereao.apphunter.pojo.entity.App;
import com.zereao.apphunter.pojo.entity.AppInfo;
import com.zereao.apphunter.pojo.vo.AppPriceInfoVO;
import com.zereao.apphunter.service.AppInfoService;
import com.zereao.apphunter.service.HtmlParseService;
import com.zereao.apphunter.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Zereao
 * @version 2019/05/20 10:52
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private HtmlParseService htmlParseService;
    @Resource
    private MailService mailService;

    @GetMapping("mail")
    public String testMail() {
        List<App> appList = appInfoService.getAllApps();
        Map<String, List<AppInfo>> downPriceMap = new HashMap<>(16);
        Map<String, List<AppInfo>> upPriceMap = new HashMap<>(16);
        Random random = new Random();
        appList.forEach(app -> {
            int copy = random.nextInt(5 - 3 + 1) + 3;
            List<AppInfo> appInfoList = new ArrayList<>();
            for (int i = 0; i < copy; i++) {
                AppInfo info = AppInfo.builder().name(app.getName()).url(app.getUrl()).language("测试语言").price("￥342.00")
                        .appId(app.getId()).version("测试版本1").build();
                appInfoList.add(info);
            }
            if (random.nextInt() % 2 == 0) {
                downPriceMap.put(app.getName(), appInfoList);
            } else {
                upPriceMap.put(app.getName(), appInfoList);
            }
        });
        AppPriceInfoVO vo = AppPriceInfoVO.builder().downPriceMap(downPriceMap).upPriceMap(upPriceMap)
                .mailTime(LocalDateTime.now().toString()).build();
        return htmlParseService.parseHtml(vo);
    }
}
