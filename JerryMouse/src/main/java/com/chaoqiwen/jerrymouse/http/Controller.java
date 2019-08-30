package com.chaoqiwen.jerrymouse.http;

import java.io.IOException;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 21:19
 */
public abstract class Controller {
    public void doGet(Request request,Response response) throws IOException {
        response.setStatus(Status.METHOD_NOT_ALLOWED);
        response.println(Status.METHOD_NOT_ALLOWED.getReason());
    }
    public void doPost(Request request,Response response) throws IOException {
        response.setStatus(Status.METHOD_NOT_ALLOWED);
        response.println(Status.METHOD_NOT_ALLOWED.getReason());
    }
}
