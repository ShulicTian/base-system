package org.ks365.osmp.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_role")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {

    private static final long serialVersionUID = -8999404061222312253L;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "en_name", nullable = false, length = 100)
    private String enName;

    @Column(name = "role_type", nullable = false, length = 2)
    private String roleType;

    @Column(name = "useable", length = 2)
    private String useable;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "sys_role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<MenuEntity> menuList;

    @Transient
    private List<Integer> menuIdList;

    public RoleEntity() {
    }

    public RoleEntity(Integer id) {
        super(id);
    }

    public List<Integer> getMenuIdList() {
        if (this.menuIdList == null) {
            if (menuList != null && menuList.size() > 0) {
                this.menuIdList = menuList.stream().map(MenuEntity::getId).collect(Collectors.toList());
            }
        }
        return this.menuIdList;
    }

    public void setMenuIdList(List<Integer> menuIdList) {
        List<MenuEntity> menuList = new ArrayList<>();
        if (menuIdList != null && menuIdList.size() > 0) {
            menuIdList.forEach(id -> {
                menuList.add(new MenuEntity(id));
            });
        }
        this.menuIdList = menuIdList;
        this.menuList = menuList;
    }

}
