package com.zereao.apphunter.dao;

import com.zereao.apphunter.pojo.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Zereao
 * @version 2019/05/10 11:23
 */
public interface AppDAO extends JpaRepository<App, Long> {
    /**
     * 根据 appName 和 url 查询第一个查到的 App 信息
     *
     * @param name       APP名称
     * @param url        url
     * @param deleteFlag 删除标记
     * @return 第一个查到的APP信息
     */
    App findFirstByNameAndUrlAndDeleteFlag(String name, String url, Integer deleteFlag);

    /**
     * 根据 deleteFlag 查询所有的App信息
     *
     * @param deleteFlag 删除标记
     * @return 查询出的APP信息
     */
    List<App> findAllByDeleteFlag(Integer deleteFlag);

    /**
     * app表建表语句
     */
    @Modifying
    @SuppressWarnings("all")
    @Query(nativeQuery = true, value = "CREATE TABLE `app` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `name` varchar(50) DEFAULT NULL COMMENT 'APP名称'," +
            "  `url` varchar(100) DEFAULT NULL COMMENT 'APP商店页面地址'," +
            "  `delete_flag` int(1) DEFAULT '0' COMMENT '删除标识，1 - 已删除；0 - 未删除'," +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间'," +
            "  `update_time` datetime DEFAULT NULL COMMENT '更新时间'," +
            "  PRIMARY KEY (`id`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void patch_V_1_0();

    /**
     * 创建 app_hunter 用户，密码为 apphunter123
     */
    @Modifying
    @SuppressWarnings("all")
    @Query(nativeQuery = true, value = "CREATE USER 'app_hunter'@'%' IDENTIFIED BY 'apphunter123';")
    void patch_V_1_1();

    /**
     * 为 app_hunter 用户赋予 app_hunter_dev 数据库下所有表的权限
     */
    @Modifying
    @SuppressWarnings("all")
    @Query(nativeQuery = true, value = "GRANT ALL PRIVILEGES ON app_hunter_dev.* TO 'app_hunter'@'%' WITH GRANT OPTION;")
    void patch_V_1_2();

    /**
     * 为 app_hunter 用户赋予 app_hunter 数据库下所有表的权限
     */
    @Modifying
    @SuppressWarnings("all")
    @Query(nativeQuery = true, value = "GRANT ALL PRIVILEGES ON app_hunter.* TO 'app_hunter'@'%' WITH GRANT OPTION;")
    void patch_V_1_3();
}
