package org.ks365.osmp.common.entity;

/**
 * 响应实体
 *
 * @author tianslc
 */
public class ResponseEntity<T> {

    protected static String SERVICE_CODE = "400"; //业务错误,返回错误信息到前端显示

    protected static String IMPORT_ERROR_CODE = "701"; //业务错误,返回错误信息到前端显示

    protected static String IMPORT_ERROR_KEY = "errExportPath"; //业务错误,返回错误信息到前端显示

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

    public ResponseEntity<T> resultByCode(String code) {
        this.code = code;
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
