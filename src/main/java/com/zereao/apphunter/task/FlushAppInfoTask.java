package com.zereao.apphunter.task;

import com.zereao.apphunter.pojo.entity.App;
import com.zereao.apphunter.pojo.vo.AppPriceInfoVO;
import com.zereao.apphunter.service.AppInfoService;
import com.zereao.apphunter.service.HtmlParseService;
import com.zereao.apphunter.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zereao
 * @version 2019/05/11 12:15
 */
@Service
public class FlushAppInfoTask {
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private HtmlParseService htmlParseService;
    @Resource
    private MailService mailService;

    /**
     * 每天 3点、9点、下午3点、晚上9点刷新数据
     */
    @Scheduled(cron = "0 0 3,9,15,21 * * ?")
    public void flushAppInfo() {
        List<App> appList = appInfoService.getAllApps();
        AppPriceInfoVO appPriceInfoVO = appInfoService.parseAppInfo(appList);
        String result = htmlParseService.parseHtml(appPriceInfoVO);
        mailService.sendHtmlMail(result);
    }
}
