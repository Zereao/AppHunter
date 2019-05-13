package com.zereao.apphunter.common.enmu;

/**
 * 删除标识
 *
 * @author Zereao
 * @version 2019/05/10 11:42
 */
public enum DeleteFlag {
    /**
     * 未删除
     */
    NOT_DELETE(0),
    /**
     * 已删除
     */
    DELETED(1);

    DeleteFlag(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer value() {
        return this.value;
    }
}
