package org.ks365.osmp.common.entity;

import java.util.HashMap;

public class R<T> extends ResponseEntity<T> {

    public static R<Object> ok(Object result) {
        R<Object> r = new R<Object>();
        r.ok("获取成功");
        r.result(result);
        return r;
    }

    public static R<Object> ok() {
        return R.ok(new HashMap<>());
    }

    public static R<Object> fail() {
        return R.fail("请求异常!");
    }
    public static R<Object> fail(String message) {
        R r = new R();
        r.faild(message);
        return r;
    }

    public static R<Object> serviceFail(String message) {
        return R.serviceFail(message, null);
    }

    public static R<Object> serviceFail(String message, Object result) {
        R r = new R();
        r.setCode(SERVICE_CODE);
        r.setMsg(message);
        r.setResult(result);
        return r;
    }


    /**
     * 错误
     * @param downLoadPath
     * @return
     */

    public static R<Object> importDownLoadErrMsg(String downLoadPath) {
        R r = new R();
        r.setCode(IMPORT_ERROR_CODE);
        r.setMsg("请求异常,错误信息请查看下载文件!");
        HashMap<Object, Object> rtnMap = new HashMap<>();
        rtnMap.put(IMPORT_ERROR_KEY,downLoadPath);
        r.setResult(rtnMap);
        return r;
    }

}
