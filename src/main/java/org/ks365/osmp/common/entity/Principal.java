package org.ks365.osmp.common.entity;

import org.ks365.osmp.sys.entity.UserEntity;
import org.ks365.osmp.sys.utils.UserUtils;

import java.io.Serializable;

/**
 * 授权用户信息
 *
 * @author tianslc
 */
public class Principal implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id; // 编号
    private String loginName; // 登录名
    private String name; // 姓名


//		private Map<String, Object> cacheMap;

    public Principal(UserEntity user) {
        this.id = user.getId();
        this.loginName = user.getUsername();
        this.name = user.getName();
    }

    public Integer getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }


    /**
     * 获取SESSIONID
     */
    public String getSessionid() {
        try {
            return (String) UserUtils.getSession().getId();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }


}
