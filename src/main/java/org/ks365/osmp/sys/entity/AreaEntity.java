
package org.ks365.osmp.sys.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 区域Entity
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_area")
@EqualsAndHashCode(callSuper = true)
public class AreaEntity extends BaseEntity {

    @NotNull
    @Column(name = "parent_id", nullable = false, length = 11)
    private Integer parentId;

    @Size(max = 2000, min = 1)
    @Column(name = "parent_ids", nullable = false, length = 2000)
    private String parentIds;

    @Size(max = 20, min = 1)
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Size(max = 100, min = 1)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "sort", nullable = false, length = 100)
    private Integer sort;

    @Size(max = 4, min = 1)
    @Column(name = "type", nullable = false, length = 4)
    private String type;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AreaEntity> children;

    @Transient
    private AreaEntity parent;

    public AreaEntity() {
    }

    public AreaEntity(Integer id) {
        super(id);
    }

    public AreaEntity(Integer id, String code) {
        super(id);
        this.id = id;
        this.code = code;
    }
}