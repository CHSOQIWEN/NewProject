package com.chaoqiwen;

import com.chaoqiwen.AllException;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/8 16:50
 */
public class RemoteException extends AllException {
    public RemoteException() {

    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }

    protected RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
