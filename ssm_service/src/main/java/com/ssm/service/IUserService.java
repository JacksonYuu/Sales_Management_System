package com.ssm.service;

import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.UsersQuery;
import com.ssm.utils.PageUi;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    List<UserInfo> findAll() throws Exception;

    void save(UserInfo userInfo) throws Exception;

    void saveUserAndRole(UserInfo userInfo,String[] roles) throws Exception;

    void update(UserInfo userInfo) throws Exception;

    void updateUserAndRole(UserInfo userInfo, String[] roles) throws Exception;

    void delete(String[] ids) throws Exception;

    void deleteFromUser_Role(String[] ids) throws Exception;

    UserInfo findById(String id) throws Exception;

    void updateStatus(UserInfo userInfo) throws Exception;

    List<Role> findOtherRoles(String userId) throws Exception;

    void addRoleToUser(String userId, String[] roleIds);

    public PageUi<UserInfo> findByQuery(UsersQuery query);
}
