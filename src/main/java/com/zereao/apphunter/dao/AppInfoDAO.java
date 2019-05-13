package com.zereao.apphunter.dao;

import com.zereao.apphunter.pojo.entity.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zereao
 * @version 2019/05/09 20:42
 */
@Repository
public interface AppInfoDAO extends JpaRepository<AppInfo, Long> {
    /**
     * 根据AppName进行查找 第一个 匹配到的 AppInfo
     *
     * @param name App name
     * @return 第一个 匹配到的 AppInfo
     */
    List<AppInfo> findFirstByName(String name);

    /**
     * 根据AppId 查找该 App最新的一条AppInfo
     *
     * @param appId AppId
     * @return 该 App最新的一条AppInfo
     */
    AppInfo findFirstByAppIdOrderByCreateTimeDesc(Long appId);

    /**
     * 根据AppId 查到 AppInfo，按创建时间倒序，最新的信息靠前
     *
     * @param appId AppId
     * @return 该APP的信息
     */
    List<AppInfo> findByAppIdOrderByCreateTimeDesc(Long appId);


    /**
     * app_info 表建表语句
     */
    @Modifying
    @SuppressWarnings("all")
    @Query(nativeQuery = true, value = "CREATE TABLE `app_info` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `app_id` int(11) DEFAULT NULL COMMENT '对应 app 表中的ID'," +
            "  `name` varchar(50) DEFAULT NULL COMMENT 'APP名称'," +
            "  `price` varchar(10) DEFAULT NULL COMMENT 'APP价格'," +
            "  `language` varchar(50) DEFAULT NULL COMMENT 'APP支持的语言类型'," +
            "  `version` varchar(10) DEFAULT NULL COMMENT 'APP最新的版本'," +
            "  `url` varchar(100) DEFAULT NULL COMMENT 'APP商店页面地址'," +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间'," +
            "  `update_time` datetime DEFAULT NULL COMMENT '更新时间'," +
            "  PRIMARY KEY (`id`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void patch_V_1_0();
}
