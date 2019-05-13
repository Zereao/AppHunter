package com.zereao.apphunter.pojo.entity;

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
 * @version 2019/05/10 11:19
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app")
@EntityListeners(AuditingEntityListener.class)
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * APP名称
     */
    private String name;
    /**
     * URL
     */
    private String url;
    /**
     * 删除标记   0 - 未删除，默认 ；1 - 已删除
     */
    @Builder.Default
    private Integer deleteFlag = 0;
    /**
     * 创建时间
     */
    @CreatedDate
    private Date createTime;
    /**
     * 更新时间
     */
    @LastModifiedDate
    private Date updateTime;
}
