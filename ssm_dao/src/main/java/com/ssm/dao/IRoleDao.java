package com.ssm.dao;

import com.ssm.domain.Permission;
import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.RoleQuery;
import com.ssm.query.UsersQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRoleDao {

    //根据用户id查询出所有对应的角色
    @Select("select * from role where id in (select roleId from users_role where userId=#{userId})")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roleName", column = "roleName"),
            @Result(property = "roleDesc", column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = List.class,many = @Many(select = "com.ssm.dao.IPermissionDao.findPermissionByRoleId"))
    })
    public List<Role> findRoleByUserId(String userId) throws Exception;

    //根据用户id查询出所有对应的角色id
    @Select("<script>" +
            "select roleId from users_role where 1=1 " +
            "<when test= \"ids!=null and ids.length>0\"> and userId in " +
            "<foreach collection='ids' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    public List<String> findRoleIdByUserIds(@Param("ids") String[] ids) throws Exception;

    @Select("select * from role")
    List<Role> findAll() throws Exception;

    @Insert("insert into role(roleName,roleDesc) values(#{roleName},#{roleDesc})")
    void save(Role role);

    @Update("update role set roleName=#{roleName},roleDesc=#{roleDesc} where id=#{id}")
    void updateRole(Role role) throws Exception;

    @Update("update role set status=#{status} " +
            "where id=#{id}")
    void updateStatus(Role role) throws Exception;

    @Insert("<script>" +
            "<when test= \"roles!=null and roles.length>0\"> " +
            "insert into users_role(userId,roleId) values " +
            "<foreach collection='roles' item='item' index='index' separator=','> " +
            "(#{userId},#{item})" +
            "</foreach>" +
            "</when> " +
            "</script>")
    void saveRoles(@Param("userId") String userId, @Param("roles") String[] roles) throws Exception;

    @Select("select * from role where id=#{roleId}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "users", column = "id", javaType = List.class, many = @Many(select = "com.ssm.dao.IUserDao.findUserByRoleId")),
            @Result(property = "permissions",column = "id",javaType = List.class,many = @Many(select = "com.ssm.dao.IPermissionDao.findPermissionByRoleId"))
    })
    Role findById(String roleId);

    @Select("select * from permission where id not in (select permissionId from role_permission where roleId=#{roleId})")
    List<Permission> findOtherPermissions(String roleId);

    @Insert("insert into role_permission(roleId,permissionId) values(#{roleId},#{permissionId})")
    void addPermissionToRole(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    @Delete("delete from users_role where roleId=#{roleId}")
    void deleteFromUser_RoleByRoleId(String roleId);
    @Delete("delete from role_permission where roleId=#{roleId}")
    void deleteFromRole_PermissionByRoleId(String roleId);

    @Delete("delete from role where id=#{roleId}")
    void deleteRoleById(String roleId);

    @Delete("<script>" +
            "<when test= \"roleIds!=null and roleIds.size()>0\"> " +
            "delete from role where 1=1 and id in " +
            "<foreach collection='roleIds' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    void deleteRoleByIds(@Param("roleIds") List<String> roleIds);

    @Delete("<script>" +
            "<when test= \"roleIds!=null and roleIds.size()>0\"> " +
            "delete from users_role where 1=1 and userId=#{userId} and roleId in " +
            "<foreach collection='roleIds' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    void deleteUserRoleByIds(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

    @Select("<script>" +
            "select * from role where 1=1 " +
            "<when test= \"query.username!=null and query.username!=''\"> " +
            "and id in (select DISTINCT r.roleId from users u left join users_role r on u.id=r.userId where 1=1 " +
            "and u.username like #{query.username} " +
            ")" +
            "</when> " +
            "<when test= \"query.roleName!=null and query.roleName!=''\"> and roleName like #{query.roleName} </when> " +
            "<when test= \"query.roleDesc!=null and query.roleDesc!=''\"> and roleDesc like #{query.roleDesc} </when> " +
            "<when test= \"query.status!=null and query.status.length>0\"> and status in " +
            "<foreach collection='query.status' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roleName", column = "roleName"),
            @Result(property = "roleDesc", column = "roleDesc"),
            @Result(property = "status", column = "status"),
            @Result(property = "users", column = "id", javaType = List.class, many = @Many(select = "com.ssm.dao.IUserDao.findUserByRoleId"))
    })
    List<Role> findByPage(@Param("query") RoleQuery query);

    @Delete("<script>" +
            "<when test= \"ids!=null and ids.length>0\"> " +
            "delete from role where 1=1 and id in " +
            "<foreach collection='ids' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    void delete(@Param("ids") String[] ids) throws Exception;
}
