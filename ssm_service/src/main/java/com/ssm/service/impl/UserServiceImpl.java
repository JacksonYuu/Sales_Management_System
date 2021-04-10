package com.ssm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ssm.dao.IRoleDao;
import com.ssm.dao.IUserDao;
import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.UsersQuery;
import com.ssm.service.IUserService;
import com.ssm.utils.PageUi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService, UserDetailsService {

    private IUserDao userDao;
    private IRoleDao roleDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoleDao(IRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void addRoleToUser(String userId, String[] roleIds) {

        for(String roleId:roleIds){
            userDao.addRoleToUser(userId,roleId);
        }
    }

    @Override
    public PageUi<UserInfo> findByQuery(UsersQuery query) {
        PageUi<UserInfo> pageUi = new PageUi<UserInfo>();
        Page<UserInfo> page = PageHelper.startPage(query.getPage(), query.getLimit());
        //查询当前页的数据
        Page<UserInfo> data = (Page<UserInfo>) userDao.findByPage(query);
        //获取总条数
        long count = page.getTotal();
        pageUi.setCount(count);
        pageUi.setData(data);
        return pageUi;
    }

    @Override
    public List<Role> findOtherRoles(String userId) {
        return userDao.findOtherRoles(userId);
    }

    @Override
    public UserInfo findById(String id) throws Exception{
        return  userDao.findById(id);
    }

    @Override
    public void updateStatus(UserInfo userInfo) throws Exception{
        userDao.updateStatus(userInfo);
    }

    @Override
    public void save(UserInfo userInfo) throws Exception {
        //对密码进行加密处理
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        userDao.save(userInfo);
    }

    @Override
    public void saveUserAndRole(UserInfo userInfo, String[] roles) throws Exception {
        //对密码进行加密处理
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        userDao.save(userInfo);
        roleDao.saveRoles(userInfo.getId(), roles);
    }

    @Override
    public void update(UserInfo userInfo) throws Exception {
        //对密码进行加密处理
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        userDao.update(userInfo);
    }

    @Override
    public void updateUserAndRole(UserInfo userInfo, String[] roles) throws Exception {
        List<String> list = Arrays.asList(roles);
        //对密码进行加密处理
        if (userInfo.getPassword() != null && !userInfo.getPassword().equals("")) {
            userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        }
        userDao.update(userInfo);
        String[] userIds = new String[]{userInfo.getId()};
        List<String> roleIds = roleDao.findRoleIdByUserIds(userIds);
        List<String> deleteRoleIds = new ArrayList<String>();
        List<String> insertRoleIds = new ArrayList<String>();
        for (String role : roles) {
            if (!roleIds.contains(role)) {
                insertRoleIds.add(role);
            }
        }
        for (String roleId : roleIds) {
            if (!list.contains(roleId)) {
                deleteRoleIds.add(roleId);
            }
        }
        if (deleteRoleIds.size() > 0) {
            roleDao.deleteUserRoleByIds(userInfo.getId(), deleteRoleIds);
        }
        if (insertRoleIds.size() > 0) {
            String[] strings = new String[insertRoleIds.size()];
            insertRoleIds.toArray(strings);
            roleDao.saveRoles(userInfo.getId(), strings);
        }
    }

    @Override
    public void delete(String[] ids) throws Exception {
        userDao.delete(ids);
    }

    @Override
    public void deleteFromUser_Role(String[] ids) throws Exception {
        userDao.deleteFromUser_Role(ids);
    }

    @Override
    public List<UserInfo> findAll() throws Exception {
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = null;
        try {
            userInfo = userDao.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //处理自己的用户对象封装成UserDetails
        assert userInfo != null;
        return new User(userInfo.getUsername(), userInfo.getPassword(), userInfo.getStatus() != 0, true, true, true, getAuthority(userInfo.getRoles()));
    }

    //作用就是返回一个List集合，集合中装入的是角色描述
    public List<SimpleGrantedAuthority> getAuthority(List<Role> roles) {

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return list;
    }
}
