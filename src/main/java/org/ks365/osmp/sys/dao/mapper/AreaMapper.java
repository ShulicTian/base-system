package org.ks365.osmp.sys.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.ks365.osmp.sys.entity.AreaEntity;

/**
 * 系统区域 Mapper
 *
 * @author tianslc
 */
public interface AreaMapper {

    /**
     * 根据 考区名称 考区等级 匹配系统区域
     *
     * @param name
     * @param level
     * @return
     */
    AreaEntity getByLikeName(@Param("name") String name, @Param("level") String level);

    /**
     * 根据 考区代码 考区等级 匹配系统区域
     *
     * @param name
     * @param level
     * @return
     */
    AreaEntity getByLikeCode(@Param("name") String name, @Param("level") String level);

}
