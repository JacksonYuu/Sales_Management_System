<%--
Created by IntelliJ IDEA.
User: 拼命三石
Date: 2021/4/5
Time: 17:43
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>销售管理系统-角色查看页面</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/xadmin.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/layui/layui.js" charset="utf-8"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/xadmin.js"></script>
</head>

<body>
<div class="x-body">
  <form class="layui-form" lay-filter="edit_form">
    <input type="hidden" name="id" value="${param.id}" readonly/>
    <div class="layui-form-item">
      <label for="L_username" class="layui-form-label">
        <span class="x-red">*</span>角色名
      </label>
      <div class="layui-input-inline">
        <input type="text" id="L_username" name="roleName" required=""
               autocomplete="off" class="layui-input" readonly>
      </div>
    </div>
    <div class="layui-form-item">
      <label for="L_phoneNum" class="layui-form-label">
        角色描述
      </label>
      <div class="layui-input-inline">
        <input type="text" id="L_phoneNum" name="roleDesc"
               autocomplete="off" class="layui-input" readonly>
      </div>
    </div>
    <div class="layui-form-item">
      <label for="L_users" class="layui-form-label">
        用户名
      </label>
      <div class="layui-input-inline">
        <input type="text" id="L_users" name="users"
               autocomplete="off" class="layui-input" readonly>
      </div>
    </div>
  </form>
</div>
<script>
  layui.use(['form','layer'], function(){
    var $ = layui.jquery;
    var form = layui.form
            ,layer = layui.layer;

    $.post("${pageContext.request.contextPath}/role/findById", {id: ${param.id}}, function (result) {
      if (result != null) {//查询成功，给form赋值
        var userNames = []
        if (result.users != null) {
          for (let i = 0; i < result.users.length; i++) {
            userNames.push(result.users[i].username)
          }
        }
        form.val("edit_form", {
          id: result.id,
          roleName:result.roleName,
          roleDesc:result.roleDesc,
          users:userNames.join(" --- ")
        });
      } else {  //查询失败
        // 获得frame索引
        var index = parent.layer.getFrameIndex(window.name);
        //关闭当前frame
        parent.layer.close(index);
        layer.alert("查询失败", {icon: 2});
      }
    });
  });
</script>
</body>
</html>