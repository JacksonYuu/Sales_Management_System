package com.ssm.service;

import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;

import java.util.List;

public interface IUserService {
    List<UserInfo> findAll() throws Exception;

    void save(UserInfo userInfo) throws Exception;

    UserInfo findById(String id) throws Exception;

    List<Role> findOtherRoles(String userId) throws Exception;

    void addRoleToUser(String userId, String[] roleIds);
}
