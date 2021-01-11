package org.ks365.osmp.common.entity;

/**
 * 响应实体
 *
 * @author tianslc
 */
public class ResponseEntity<T> {

    public static final String OK = "1";
    public static final String ERROR = "-1";

    private String code = ERROR;
    private String msg = "";
    private T result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ResponseEntity<T> result(T result) {
        this.result = result;
        return this;
    }

    public ResponseEntity<T> ok(String message) {
        this.code = OK;
        this.msg = message;
        return this;
    }

    public ResponseEntity<T> faild(String message) {
        this.code = ERROR;
        this.msg = message;
        return this;
    }
}
