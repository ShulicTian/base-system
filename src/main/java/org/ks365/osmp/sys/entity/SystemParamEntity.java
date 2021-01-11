/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.entity;

import lombok.Data;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 系统参数配置Entity
 *
 * @author tianslc
 */

@Data
@Entity
@Table(name = "sys_param", uniqueConstraints = {@UniqueConstraint(name = "param_name_unique_index", columnNames = {"param_name"})})
public class SystemParamEntity extends BaseEntity {

    @NotNull
    @Column(name = "type", length = 2)
    private String type;

    @NotNull
    @Size(max = 100, min = 1)
    @Column(name = "param_name", length = 100)
    private String paramName;

    @NotNull
    @Size(max = 100, min = 1)
    @Column(name = "param_value", length = 100)
    private String paramValue;

    @Size(max = 200)
    @Column(name = "param_desc", length = 200)
    private String paramDesc;

}