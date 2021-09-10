
package org.ks365.osmp.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 系统菜单Entity
 *
 * @author tianslc
 * @date 2020/01/15
 */
@Data
@Entity
@Table(name = "sys_menu")
@EqualsAndHashCode(callSuper = true)
public class MenuEntity extends BaseEntity {

    private static final long serialVersionUID = 8008004308673141753L;

    @NotNull
    @Column(name = "parent_id", nullable = false, length = 11)
    private Integer parentId;

    @Size(max = 2000, min = 1)
    @Column(name = "parent_ids", nullable = false, length = 2000)
    private String parentIds;

    @Size(max = 100, min = 1)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 2000)
    @Column(name = "href", length = 2000)
    private String href;

    @Size(max = 100)
    @Column(name = "icon", length = 100)
    private String icon;

    @NotNull
    @Column(name = "sort", nullable = false, length = 11)
    private Integer sort;

    @Size(max = 4, min = 1)
    @Column(name = "is_show", nullable = false, length = 4)
    private String isShow;

    @Size(max = 200)
    @Column(name = "permission", length = 200)
    private String permission;

    @Column(name = "type", length = 4)
    private String type = "0";

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuEntity> children;

    @Transient
    private MenuEntity parent;

    public MenuEntity() {
    }

    public MenuEntity(Integer id) {
        super(id);
    }

    public MenuEntity(Integer id, Integer parentId) {
        super(id);
        this.parentId = parentId;
    }

    @JsonIgnore
    public static String getRootId() {
        return "0";
    }
}