package org.ks365.osmp.sys.entity;

import lombok.Data;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统公告
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_bulletin")
public class BulletinEntity extends BaseEntity {

    @Column(name = "type", nullable = false, length = 4)
    private String type;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content_type", nullable = false, length = 4)
    private String contentType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "status", nullable = false)
    private String status;

}
