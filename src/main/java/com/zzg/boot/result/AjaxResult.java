package com.zzg.boot.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data:前后端交互时返回成功失败时的数据信息及数据
 * success 为成功返回
 * error为失败返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResult {
    private int code;
    private String msg;
    private Object data;

    public static AjaxResult success(Object data) {
        return new AjaxResult(1, "操作成功", data);
    }

    public static AjaxResult success() {
        return new AjaxResult(1, "操作成功", null);
    }

    public static AjaxResult error(Object data) {
        return new AjaxResult(0, "操作失败", data);
    }

    public static AjaxResult error() {
        return new AjaxResult(0, "操作失败", null);
    }
}
