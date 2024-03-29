
package org.ks365.osmp.common.interceptor;

import org.ks365.osmp.common.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

/**
 * 日志拦截器
 *
 * @author ThinkGem
 * @version 2014-8-19
 */
public class LogInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> startTimeThreadLocal =
            new NamedThreadLocal<Long>("ThreadLocal StartTime");
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            //1、开始时间
            long beginTime = System.currentTimeMillis();
            //线程绑定变量（该数据只有当前请求的线程可见）
            startTimeThreadLocal.set(beginTime);
            logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
                    .format(beginTime), request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            logger.info("ViewName: " + modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        // 保存日志
        if (!request.getRequestURI().equals("/osmp/sys/log/sve")) {
            LogUtils.saveLog(request, handler, ex, request.getParameter("menuName"));
        }

        // 打印JVM信息。
        if (logger.isDebugEnabled()) {
//	        //删除线程变量中的数据，防止内存泄漏
            startTimeThreadLocal.remove();
        }

    }

}
