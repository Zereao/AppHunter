package com.zereao.apphunter.controller;

import com.zereao.apphunter.dao.AppInfoDAO;
import com.zereao.apphunter.pojo.dto.AppAddDTO;
import com.zereao.apphunter.pojo.entity.App;
import com.zereao.apphunter.pojo.entity.AppInfo;
import com.zereao.apphunter.service.AppInfoService;
import com.zereao.apphunter.task.FlushAppInfoTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zereao
 * @version 2019/05/10 17:59
 */
@Slf4j
@RestController
@RequestMapping("app")
public class AppController {
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private FlushAppInfoTask flushAppInfoTask;
    @Resource
    private AppInfoDAO appInfoDAO;

    @PostMapping("add")
    public ResponseEntity<String> addApp(@RequestBody AppAddDTO addDTO) {
        addDTO.getUrl().parallelStream().forEach(url -> appInfoService.addApp(url));
        return ResponseEntity.ok("添加成功！");
    }

    @GetMapping("flush")
    public ResponseEntity<String> flush() {
        flushAppInfoTask.flushAppInfo();
        return ResponseEntity.ok("刷新成功！");
    }

    @GetMapping("initInfo")
    public ResponseEntity<String> addFirstAppInfo() {
        List<App> appList = appInfoService.getAllApps();
        appList.parallelStream().forEach(app -> {
            AppInfo appInfo = appInfoService.parseAppInfo(app.getUrl());
            appInfo.setAppId(app.getId());
            appInfoDAO.save(appInfo);
        });
        return ResponseEntity.ok("添加成功！");
    }
}
