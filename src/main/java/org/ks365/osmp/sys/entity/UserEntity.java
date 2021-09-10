package org.ks365.osmp.sys.entity;

import lombok.Data;
import org.ks365.osmp.common.entity.BaseEntity;
import org.ks365.osmp.common.utils.AuthUtils;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}), @UniqueConstraint(columnNames = {"mobile"}), @UniqueConstraint(columnNames = {"id_card"})})
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = 7979908991330544237L;

    @Size(max = 100, groups = {UserGroup.class})
    @Column(name = "nike_name", length = 100)
    private String nikeName;

    @Size(max = 100, min = 1, groups = {UserGroup.class})
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Pattern(regexp = "^[^\\u4e00-\\u9fa5]*$", message = "账号不能为中文！", groups = {UserGroup.class})
    @Size(max = 20, min = 6, message = "账号必须为6-20位！", groups = {UserGroup.class})
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Size(max = 11, min = 11, message = "手机号格式不正确！", groups = {UserGroup.class})
    @Column(name = "mobile", nullable = false, unique = true, length = 11)
    private String mobile;

    @Size(max = 18, min = 5, message = "身份证格式不正确！", groups = {UserGroup.class})
    @Column(name = "id_card", nullable = false, unique = true, length = 18)
    private String idCard;

    @Pattern(regexp = "^[^\\u4e00-\\u9fa5]*$", message = "密码不能为中文！", groups = {UserGroup.class})
    @Size(max = 16, min = 6, message = "密码必须为6-16位！", groups = {UserGroup.class})
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "photo")
    private String photo;

    @Column(name = "email")
    private String email;

    @Column(name = "login_ip")
    private String loginIp;

    @Column(name = "login_date")
    private Date loginDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roleList;

    @Transient
    private List<Integer> roleIdList;

    @Transient
    private String token;

    @Transient
    private boolean rememberMe;

    public UserEntity() {
    }

    public UserEntity(Integer id) {
        super(id);
    }

    public UserEntity(String name, String username, String mobile, String idCard, String password) {
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.idCard = idCard;
        if (password.length() > 5) {
            password = password.substring(password.length() - 6);
        }
        this.password = AuthUtils.entryptPassword(password);
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Integer id) {
        return id != null && id == 1;
    }

    public List<Integer> getRoleIdList() {
        if (this.roleIdList == null) {
            if (roleList != null && roleList.size() > 0) {
                this.roleIdList = roleList.stream().map(RoleEntity::getId).collect(Collectors.toList());
            }
        }
        return this.roleIdList;
    }

    public void setRoleIdList(List<Integer> roleIdList) {
        List<RoleEntity> roleList = new ArrayList<>();
        if (roleIdList != null && roleIdList.size() > 0) {
            roleIdList.forEach(id -> {
                roleList.add(new RoleEntity(id));
            });
        }
        this.roleIdList = roleIdList;
        this.roleList = roleList;
    }

}
