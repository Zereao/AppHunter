package com.zereao.apphunter.controller;

import com.zereao.apphunter.pojo.dto.AppAddDTO;
import com.zereao.apphunter.service.AppInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zereao
 * @version 2019/05/10 17:59
 */
@RestController
@RequestMapping("app")
public class AppController {
    @Resource
    private AppInfoService appInfoService;

    @PostMapping("add")
    public ResponseEntity addApp(@RequestBody AppAddDTO addDTO) {
        appInfoService.addApp(addDTO.getUrl());
        return ResponseEntity.ok().build();
    }
}
