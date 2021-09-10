package org.ks365.osmp.sys.utils;

import org.ks365.osmp.common.utils.SpringContextHolder;
import org.ks365.osmp.sys.dao.SystemParamDao;
import org.ks365.osmp.sys.entity.SystemParamEntity;
import org.springframework.data.domain.Example;

import java.util.Optional;

/**
 * 系统参数工具类
 *
 * @author tianslc
 */
public class SystemParamUtils {
    private static SystemParamDao systemParamDao = SpringContextHolder.getBean(SystemParamDao.class);

    /**
     * 根据参数名获取参数值
     *
     * @param paramName
     * @return
     */
    public static String getParamValByName(String paramName) {
        SystemParamEntity systemParamEntity = new SystemParamEntity();
        systemParamEntity.setParamName(paramName);
        Example<SystemParamEntity> example = Example.of(systemParamEntity);
        Optional<SystemParamEntity> optional = systemParamDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get().getParamValue();
        }
        return null;
    }
}
