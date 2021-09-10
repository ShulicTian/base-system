
package org.ks365.osmp.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 代码生成Entity
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_generate")
@EqualsAndHashCode(callSuper = true)
public class GenerateEntity extends BaseEntity {

    @NotNull
    @Column(name = "entity_name", nullable = false, length = 100)
    private String entityName;

    @NotNull
    @Column(name = "path", nullable = false, length = 100)
    private String path;

    @NotNull
    @Column(name = "url", nullable = false, length = 100)
    private String url;

    @NotNull
    @Column(name = "permission_prefix", nullable = false, length = 100)
    private String permissionPrefix;

    @NotNull
    @Column(name = "description", nullable = false, length = 100)
    private String description;

}