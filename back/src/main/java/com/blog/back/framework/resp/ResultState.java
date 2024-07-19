package com.blog.back.framework.resp;

/**
 * 处理状态
 */
public enum ResultState {
    SUCCESS(200),SERVERFAIL(500),FAIL(501), AUTHENTICATIONFAIL(502);
    private final int code;

    ResultState(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
