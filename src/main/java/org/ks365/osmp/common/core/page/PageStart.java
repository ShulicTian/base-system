package org.ks365.osmp.common.core.page;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.MapUtils;
import org.ks365.osmp.common.entity.PageEntity;
import org.ks365.osmp.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

public class PageStart {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "page";

    public static final Integer DEFAULT_PAGE_NUM = 1;


    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "size";


    public static final Integer DEFAULT_PAGE_SIZE = 30;

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        Map<String, Object> m = readRaw(ServletUtils.getRequest());
        pageDomain.setPageNum(MapUtils.getInteger(m, PAGE_SIZE));
        pageDomain.setPageSize(MapUtils.getInteger(m, PAGE_SIZE));
        pageDomain.setOrderByColumn(MapUtils.getString(m, ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        return pageDomain;
    }


    //java获取raw
    public static Map<String, Object> readRaw(HttpServletRequest request) {
        try {
            InputStream inputStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inputStream.close();
            String result = new String(outSteam.toByteArray(), "UTF-8");

            Map map = JSON.parseObject(result, Map.class);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 拿到分页的请求
     */
    public static void setPage() {
        PageDomain pageDomain = PageStart.getPageDomain();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 拿到分页的请求
     */
    public static void setPageEntity(PageEntity p) {
        PageDomain pageDomain = new PageDomain();
        Integer pageNum = p.getPage();
        Integer pageSize = p.getSize();
        pageDomain.setPageNum(pageNum);
        pageDomain.setPageSize(pageSize);
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

}