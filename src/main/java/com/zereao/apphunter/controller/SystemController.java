package com.zereao.apphunter.controller;

import com.zereao.apphunter.dao.AppDAO;
import com.zereao.apphunter.dao.AppInfoDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @author Zereao
 * @version 2019/05/10 17:42
 */
@RestController
@RequestMapping("sys")
public class SystemController {
    @Resource
    private AppDAO appDAO;
    @Resource
    private AppInfoDAO appInfoDAO;

    @GetMapping("execDmlSQL")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity execDmlSQL() {
        appDAO.patch_V_1_0();
        appInfoDAO.patch_V_1_0();
        return ResponseEntity.ok().build();
    }
}
