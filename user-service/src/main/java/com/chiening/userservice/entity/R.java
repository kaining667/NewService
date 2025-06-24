package com.chiening.userservice.entity;

import lombok.Data;

/**
 * @description: R类，结果反阔，成功为200，失败为500并且打印相关信息
 * @author: ChiefNing
 * @date: 2025年06月19日
 */

@Data
public class R<T> {
    private int code;
    private String message;
    private T data;

    public R() {
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success(T data) {
        return new R<>(200, "操作成功", data);
    }

    public static <T> R<T> success(Integer code, T data) {
        return new R<>(code, "操作成功", data);
    }

    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }

    public static <T> R<T> error(Integer code,String message) {
        return new R<>(code, message, null);
    }
}
