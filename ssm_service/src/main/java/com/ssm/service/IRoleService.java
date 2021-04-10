package com.ssm.service;

import com.ssm.domain.Permission;
import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.RoleQuery;
import com.ssm.query.UsersQuery;
import com.ssm.utils.PageUi;

import java.util.List;

public interface IRoleService {

    public List<Role> findAll() throws Exception;

    public List<String> findRoleIdByUserIds(String[] ids) throws Exception;

    void save(Role role) throws Exception;

    Role findById(String roleId) throws  Exception;

    List<Permission> findOtherPermissions(String roleId) throws Exception;

    void addPermissionToRole(String roleId, String[] permissionIds) throws Exception;

    void deleteRoleById(String roleId) throws Exception;

    void deleteRoleByIds(List<String> roleIds) throws Exception;

    public PageUi<Role> findByQuery(RoleQuery query) throws Exception;

    void delete(String[] ids) throws Exception;

    void saveRole(Role role) throws Exception;

    void updateRole(Role role) throws Exception;

    void updateStatus(Role role) throws Exception;
}
