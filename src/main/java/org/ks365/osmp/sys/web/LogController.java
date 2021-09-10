
package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.entity.LogEntity;
import org.ks365.osmp.sys.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志Controller
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/sys/log")
public class LogController extends BaseController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @RequiresPermissions("sys:log:view")
    @PostMapping("list")
    public ResponseEntity<Page<LogEntity>> list(@RequestBody LogEntity logEntity) {
        return new ResponseEntity<Page<LogEntity>>().ok("获取成功").result(logService.getPageByColumn(logEntity));
    }

    @RequiresPermissions("sys:log:edit")
    @PostMapping("save")
    public ResponseEntity<Object> save(@RequestBody LogEntity logEntity, HttpServletRequest request) {
        logEntity.setRemoteAddr(StringUtils.getRemoteAddr(request));
        logEntity.setUserAgent(request.getHeader("user-agent"));
        logEntity.setRequestUri(request.getRequestURI());
        logEntity.setParams(request.getParameterMap());
        logEntity.setMethod(request.getMethod());
        return new ResponseEntity<Object>().ok("保存成功").result(logService.save(logEntity));
    }
}
