package com.ffi.api.master.model;

/**
 *
 * @author USER
 */
import java.util.List;

public class ResponseMessage {

    private boolean success;
    private String message;
    private List item;

    public ResponseMessage() {
    }

    public ResponseMessage(boolean success, String message, List item) {
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

    public List getItem() {
        return item;
    }

    public void setItem(List item) {
        this.item = item;
    }
}
