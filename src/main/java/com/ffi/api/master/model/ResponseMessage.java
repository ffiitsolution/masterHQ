package com.ffi.api.master.model;

/**
 *
 * @author USER
 */
import java.util.List;

public class ResponseMessage {

    private boolean success;
    private String message;
    private Object item;

    public ResponseMessage() {
    }

    public ResponseMessage(boolean success, String message, Object item) {
        this.success = success;
        this.message = message;
        this.item = item;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
