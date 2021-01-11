package org.ks365.osmp.common.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author tianslc
 */
@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1696585240691336635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11, updatable = false)
    protected Integer id;

    @CreatedBy
    @Column(name = "create_by", length = 11, updatable = false, nullable = false)
    protected Integer createBy;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "create_date", updatable = false, nullable = false)
    protected Date createDate;

    @LastModifiedBy
    @Column(name = "update_by", length = 11, nullable = false)
    protected Integer updateBy;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_date", nullable = false)
    protected Date updateDate;

    @Column(name = "flag", columnDefinition = "char(2) not null default 0")
    protected String flag = "0";

    public BaseEntity() {
    }

    public BaseEntity(Integer id) {
        this.id = id;
    }

}
