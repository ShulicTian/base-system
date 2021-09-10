
package org.ks365.osmp.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * 字典Entity
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_dict", uniqueConstraints = {@UniqueConstraint(name = "param_name_unique_index", columnNames = {"type", "value", "label"})})
@EqualsAndHashCode(callSuper = true)
public class DictEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "value", length = 100)
    private String value;

    @NotNull
    @Column(name = "label", length = 100)
    private String label;

    @NotNull
    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "color", length = 100)
    private String color;

    @NotNull
    @Column(name = "sort", length = 10)
    private Integer sort;

    public DictEntity() {
        super();
    }

    public DictEntity(Integer id) {
        super(id);
    }

    public DictEntity(String value, String label) {
        this.value = value;
        this.label = label;
    }
}