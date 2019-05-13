package com.zereao.apphunter.common.builder;

import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * @author Zereao
 * @version 2019/05/10 14:51
 */
public class ContextBuilder {
    private Context context;

    public ContextBuilder() {
        this.context = new Context();
    }

    public ContextBuilder set(String name, Object value) {
        this.context.setVariable(name, value);
        return this;
    }

    public ContextBuilder set(Map<String, Object> variableMap) {
        this.context.setVariables(variableMap);
        return this;
    }

    public Context build() {
        return this.context;
    }
}
