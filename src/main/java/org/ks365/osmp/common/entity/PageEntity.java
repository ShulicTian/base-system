package org.ks365.osmp.common.entity;

import lombok.Data;

import javax.persistence.Transient;

/**
 * 分页实体类
 *
 * @author tianslc
 */
@Data
public abstract class PageEntity {
    @Transient
    protected Integer page;
    @Transient
    protected Integer size = 30;
}
