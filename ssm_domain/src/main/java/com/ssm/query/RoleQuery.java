package com.ssm.query;

import com.ssm.utils.BaseQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public class RoleQuery extends BaseQuery {

    private String username;
    private String roleName;
    private String roleDesc;
    private String[] status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }
}
