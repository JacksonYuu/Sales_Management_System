package com.ssm.dao;

import com.ssm.domain.Role;
import com.ssm.domain.UserInfo;
import com.ssm.query.UsersQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUserDao {

    @Select("select * from users where username=#{username}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "phoneNum", column = "phoneNum"),
            @Result(property = "status", column = "status"),
            @Result(property = "roles", column = "id", javaType = List.class, many = @Many(select = "com.ssm.dao.IRoleDao.findRoleByUserId"))
    })
    public UserInfo findByUsername(String username) throws Exception;

    //根据角色id查询出所有对应的用户
    @Select("select * from users where id in (select userId from users_role where roleId=#{roleId})")
    public List<UserInfo> findUserByRoleId(String roleId) throws Exception;

    @Select("select * from users")
    List<UserInfo> findAll() throws Exception;

    @Insert("insert into users(username,password,phoneNum) values(#{username},#{password},#{phoneNum})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(UserInfo userInfo) throws Exception;

    @Update("<script>" +
            "update users set username=#{userInfo.username},phoneNum=#{userInfo.phoneNum} " +
            "<when test= \"userInfo.password!=null and userInfo.password!=''\"> " +
            ",password=#{userInfo.password}" +
            "</when> " +
            "where id=#{userInfo.id}" +
            "</script>")
    void update(@Param("userInfo") UserInfo userInfo) throws Exception;

    @Update("update users set status=#{status} " +
            "where id=#{id}")
    void updateStatus(UserInfo userInfo) throws Exception;

    @Delete("<script>" +
            "<when test= \"ids!=null and ids.length>0\"> " +
            "delete from users where 1=1 and id in " +
            "<foreach collection='ids' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    void delete(@Param("ids") String[] ids) throws Exception;

    @Delete("<script>" +
            "<when test= \"ids!=null and ids.length>0\"> " +
            "delete from users_role where 1=1 and userId in " +
            "<foreach collection='ids' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    void deleteFromUser_Role(@Param("ids") String[] ids) throws Exception;

    @Select("select * from users where id=#{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "phoneNum", column = "phoneNum"),
            @Result(property = "status", column = "status"),
            @Result(property = "roles", column = "id", javaType = List.class, many = @Many(select = "com.ssm.dao.IRoleDao.findRoleByUserId"))
    })
    UserInfo findById(String id) throws Exception;

    @Select("select * from role where id not in (select roleId from users_role where userId=#{userId})")
    List<Role> findOtherRoles(String userId);

    @Insert("insert into users_role(userId,roleId) values(#{userId},#{roleId})")
    void addRoleToUser(@Param("userId") String userId, @Param("roleId") String roleId);

    @Select("<script>" +
            "select * from (select DISTINCT u.* from users u left join users_role r on u.id=r.userId where 1=1 " +
            "<when test= \"query.roles!=null and query.roles.length>0\"> and r.roleId in " +
            "<foreach collection='query.roles' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            ")a where 1=1 " +
            "<when test= \"query.username!=null and query.username!=''\"> and username like #{query.username} </when> " +
            "<when test= \"query.phoneNum!=null and query.phoneNum!=''\"> and phoneNum like #{query.phoneNum} </when> " +
            "<when test= \"query.status!=null and query.status.length>0\"> and status in " +
            "<foreach collection='query.status' item='item' index='index' open='(' close=')' separator=','> " +
            "#{item}" +
            "</foreach>" +
            "</when> " +
            "</script>")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "phoneNum", column = "phoneNum"),
            @Result(property = "status", column = "status"),
            @Result(property = "roles", column = "id", javaType = List.class, many = @Many(select = "com.ssm.dao.IRoleDao.findRoleByUserId"))
    })
    List<UserInfo> findByPage(@Param("query") UsersQuery query);
}
