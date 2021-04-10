package com.ssm.query;

import com.ssm.utils.BaseQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public class UsersQuery extends BaseQuery {

    private String username;
    private String phoneNum;
    private String[] status;
    private String[] roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
