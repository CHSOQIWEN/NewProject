package com.chaoqiwen.jerrymouse.http;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 21:20
 */
public enum  Status {
    OK(200,"ok"),
    BAD_REQUEST(400,"Bad Request"),
    NOT_FOUND(404,"Not Found"),
    METHOD_NOT_ALLOWED(405,"Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500,"Internal Server Error")
            ;

    private int code;
    private String reason;

    Status(int code, String reason) {
        this.code=code;
        this.reason=reason;
    }
    public int getCode(){
        return code;
    }
    public String getReason(){
        return reason;
    }
}
