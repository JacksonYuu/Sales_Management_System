package com.ssm.controller;

import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.UsersQuery;
import com.ssm.service.IRoleService;
import com.ssm.service.IUserService;
import com.ssm.utils.JsonResult;
import com.ssm.utils.PageUi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    //给用户添加角色
    @RequestMapping("/addRoleToUser.do")
    public String addRoleToUser(@RequestParam(name = "userId", required = true) String userId, @RequestParam(name = "ids", required = true) String[] roleIds) {
        userService.addRoleToUser(userId, roleIds);
        return "redirect:findAll.do";
    }

    //查询用户以及用户可以添加的角色
    @RequestMapping("/findUserByIdAndAllRole.do")
    public ModelAndView findUserByIdAndAllRole(@RequestParam(name = "id", required = true) String userid) throws Exception {
        ModelAndView mv = new ModelAndView();
        //1.根据用户id查询用户
        UserInfo userInfo = userService.findById(userid);
        //2.根据用户id查询可以添加的角色
        List<Role> otherRoles = userService.findOtherRoles(userid);
        mv.addObject("user", userInfo);
        mv.addObject("roleList", otherRoles);
        mv.setViewName("user-role-add");
        return mv;
    }

    //查询指定id的用户
    @RequestMapping("/findById")
    @ResponseBody
    public UserInfo findById(String id) throws Exception {
        return userService.findById(id);
    }

    //修改指定id的账户状态
    @RequestMapping("/updateStatus")
    @ResponseBody
    public JsonResult updateStatus(UserInfo userInfo) throws Exception {
        try {
            userService.updateStatus(userInfo);
            return new JsonResult(true, "修改账户状态成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, "修改账户状态失败！");
        }
    }

    //用户添加
    @RequestMapping("/save")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public JsonResult save(UserInfo userInfo, String[] roleIds) {
        try {
            if (userInfo.getId() != null) {
                //有id就是修改
                userService.updateUserAndRole(userInfo, roleIds);
                return new JsonResult(true, "修改用户成功！");
            } else {
                userService.saveUserAndRole(userInfo, roleIds);
                return new JsonResult(true, "新增用户成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, "新增/修改用户失败！");
        }
    }

    @RequestMapping("/findAll.do")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public List<UserInfo> findAll() throws Exception {
        return userService.findAll();
    }

    @RequestMapping("/findByQuery")
    @ResponseBody
    public PageUi<UserInfo> findByQuery(UsersQuery usersQuery) throws Exception {
        return userService.findByQuery(usersQuery);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(String[] ids) {
        try {
            // 删除用户-角色表
            userService.deleteFromUser_Role(ids);
            // 删除用户
            userService.delete(ids);
            return new JsonResult(true, "删除用户以及用户的角色成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, "删除用户以及用户的角色失败！");
        }
    }
}
