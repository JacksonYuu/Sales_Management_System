package com.ssm.controller;

import com.ssm.domain.Permission;
import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.RoleQuery;
import com.ssm.query.UsersQuery;
import com.ssm.service.IRoleService;
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

@RequestMapping("/role")
@Controller
public class RoleController {

    private IRoleService roleService;

    @Autowired
    public void setRoleService(IRoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping("/deleteRole.do")
    public String deleteRole(@RequestParam(name="id",required = true) String roleId) throws Exception {
        roleService.deleteRoleById(roleId);
        return "redirect:findAll.do";
    }

    //给角色添加权限
    @RequestMapping("/addPermissionToRole.do")
    public String addPermissionToRole(@RequestParam(name = "roleId", required = true) String roleId, @RequestParam(name = "ids", required = true) String[] permissionIds) throws Exception {
        roleService.addPermissionToRole(roleId, permissionIds);
        return "redirect:findAll.do";
    }

    //根据roleId查询role，并查询出可以添加的权限
    @RequestMapping("/findRoleByIdAndAllPermission.do")
    public ModelAndView findRoleByIdAndAllPermission(@RequestParam(name = "id", required = true) String roleId) throws Exception {
        ModelAndView mv = new ModelAndView();
        //根据roleId查询role
        Role role = roleService.findById(roleId);
        //根据roleId查询可以添加的权限
        List<Permission> otherPermissions = roleService.findOtherPermissions(roleId);
        mv.addObject("role", role);
        mv.addObject("permissionList", otherPermissions);
        mv.setViewName("role-permission-add");
        return mv;

    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<Role> findAll() throws Exception {
        return roleService.findAll();
    }

    @RequestMapping("/findByQuery")
    @ResponseBody
    public PageUi<Role> findByQuery(RoleQuery roleQuery) throws Exception {
        return roleService.findByQuery(roleQuery);
    }

    //查询指定id的角色
    @RequestMapping("/findById")
    @ResponseBody
    public Role findById(String id) throws Exception {
        return roleService.findById(id);
    }

    //修改指定id的账户状态
    @RequestMapping("/updateStatus")
    @ResponseBody
    public JsonResult updateStatus(Role role) throws Exception {
        try {
            roleService.updateStatus(role);
            return new JsonResult(true, "修改角色状态成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, "修改角色状态失败！");
        }
    }

    //角色添加
    @RequestMapping("/save")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public JsonResult save(Role role) {
        try {
            if (role.getId() != null) {
                //有id就是修改
                roleService.updateRole(role);
                return new JsonResult(true, "修改角色成功！");
            } else {
                roleService.saveRole(role);
                return new JsonResult(true, "新增角色成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, "新增/修改角色失败！");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(String[] ids) {
        try {
            // 删除角色
            roleService.delete(ids);
            return new JsonResult(true, "删除角色成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, "删除角色失败！");
        }
    }
}
