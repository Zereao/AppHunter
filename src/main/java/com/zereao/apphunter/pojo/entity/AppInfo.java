package com.zereao.apphunter.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * App信息
 *
 * @author Zereao
 * @version 2019/05/09 16:09
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_info")
@EntityListeners(AuditingEntityListener.class)
public class AppInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 对应App表中的id
     */
    private Long appId;
    /**
     * APP名称
     */
    private String name;
    /**
     * APP价格
     */
    private String price;
    /**
     * 支持语言
     */
    private String language;
    /**
     * 当前版本
     */
    private String version;
    /**
     * URL
     */
    private String url;
    /**
     * 创建时间
     */
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @LastModifiedDate
    private Date updateTime;
}
