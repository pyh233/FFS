package com.example.flyfishshop.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class JsonResult {
    private final int code;
    private final boolean success;
    private final String msg;
    private final Object data;
    @Setter
    private int count;
    @Getter
    @Setter
    private Object pageInfo;
    public JsonResult(int code, boolean success, String msg, Object data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResult success(String msg, Object data) {
        return new JsonResult(0, true, msg, data);
    }

    public static JsonResult fail(String msg) {
        return new JsonResult(-1, false, msg, null);
    }
}
