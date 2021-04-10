package com.ssm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ssm.dao.IRoleDao;
import com.ssm.domain.Permission;
import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.RoleQuery;
import com.ssm.query.UsersQuery;
import com.ssm.service.IRoleService;
import com.ssm.utils.PageUi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    private IRoleDao roleDao;

    @Autowired
    public void setRoleDao(IRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void deleteRoleById(String roleId) {
        //从user_role表中删除
        roleDao.deleteFromUser_RoleByRoleId(roleId);
        //从role_permission表中删除
        roleDao.deleteFromRole_PermissionByRoleId(roleId);
        //从role表中删除
        roleDao.deleteRoleById(roleId);
    }

    @Override
    public void deleteRoleByIds(List<String> roleIds) throws Exception {
        roleDao.deleteRoleByIds(roleIds);
    }

    @Override
    public void addPermissionToRole(String roleId, String[] permissionIds) {
        for(String permissionId:permissionIds){
            roleDao.addPermissionToRole(roleId,permissionId);
        }
    }

    @Override
    public Role findById(String roleId) {
        return roleDao.findById(roleId);
    }

    @Override
    public List<Permission> findOtherPermissions(String roleId) {
        return roleDao.findOtherPermissions(roleId);
    }

    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    public List<Role> findAll() throws Exception{
        return roleDao.findAll();
    }

    @Override
    public List<String> findRoleIdByUserIds(String[] ids) throws Exception {
        return roleDao.findRoleIdByUserIds(ids);
    }

    @Override
    public PageUi<Role> findByQuery(RoleQuery query) {
        PageUi<Role> pageUi = new PageUi<Role>();
        Page<Role> page = PageHelper.startPage(query.getPage(), query.getLimit());
        //查询当前页的数据
        Page<Role> data = (Page<Role>) roleDao.findByPage(query);
        //获取总条数
        long count = page.getTotal();
        pageUi.setCount(count);
        pageUi.setData(data);
        return pageUi;
    }

    @Override
    public void delete(String[] ids) throws Exception {
        roleDao.delete(ids);
    }

    @Override
    public void saveRole(Role role) throws Exception {
        roleDao.save(role);
    }

    @Override
    public void updateRole(Role role) throws Exception {
        roleDao.updateRole(role);
    }

    @Override
    public void updateStatus(Role role) throws Exception{
        roleDao.updateStatus(role);
    }
}
