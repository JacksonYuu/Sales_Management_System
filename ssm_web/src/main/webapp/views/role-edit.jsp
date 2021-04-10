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
    <title>销售管理系统-角色修改页面</title>
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
            <input type="hidden" name="id" value="${param.id}"/>
            <div class="layui-form-item">
                <label for="L_username" class="layui-form-label">
                    <span class="x-red">*</span>角色名
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="L_username" name="roleName" required="" lay-verify="required"
                           autocomplete="off" class="layui-input" placeholder="请输入角色名">
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_phoneNum" class="layui-form-label">
                    角色描述
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="L_phoneNum" name="roleDesc"
                           autocomplete="off" class="layui-input" placeholder="请输入角色描述">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">
                </label>
                <button class="layui-btn layui-btn-radius layui-btn-normal" lay-filter="edit" lay-submit="">
                    提交
                </button>
                <button type="reset" class="layui-btn layui-btn-radius layui-btn-primary">重置</button>
            </div>
        </form>
    </div>
    <script>
        layui.config({
            base: '${pageContext.request.contextPath}/layui/'
        }).extend({
            test: 'selectPlus'
        }).use(['selectPlus', 'jquery', 'table', 'form'], function () {
            var $ = layui.$;
            var form = layui.form
            ,selectPlus = layui.selectPlus
            ,layer = layui.layer;

          $.post("${pageContext.request.contextPath}/role/findById", {id: ${param.id}}, function (result) {
              if (result != null) {//查询成功，给form赋值
                  form.val("edit_form", {
                      id: result.id,
                      roleName:result.roleName,
                      roleDesc:result.roleDesc
                  });
              } else {  //查询失败
                  // 获得frame索引
                  var index = parent.layer.getFrameIndex(window.name);
                  //关闭当前frame
                  parent.layer.close(index);
                  layer.alert("查询失败！", {icon: 2});
              }
          });

          //监听提交
          form.on('submit(edit)', function(data){
              var formData = data.field;
              console.debug(formData);
              var roleName = formData.roleName,
                  roleDesc = formData.roleDesc,
                  id = formData.id;
              layer.alert('您确认要修改此角色信息？', {
                  skin: 'layui-layer-molv' //样式类名layui-layer-lan或layui-layer-molv  自定义样式
                  , closeBtn: 1    // 是否显示关闭按钮
                  , anim: 1 //动画类型
                  , btn: ['确定', '取消'] //按钮
                  , icon: 2    // icon
                  , yes: function () {
                      //发送请求到后台
                      $.post("${pageContext.request.contextPath}/role/save",
                          {
                              id: id,
                              roleName: roleName,
                              roleDesc: roleDesc
                          }, function (result) {
                              if (result.success === true) {//修改成功，刷新当前页表格
                                  layer.alert("修改成功", {icon: 6},function () {
                                      // 获得frame索引
                                      var index = parent.layer.getFrameIndex(window.name);
                                      //关闭当前frame
                                      parent.layer.close(index);
                                      window.parent.location.reload();//修改成功后刷新父界面
                                  });
                              } else if (result.success === false) {  //修改失败
                                  layer.alert(result.msg, {icon: 2});
                              }
                          });
                  }
              })
              return false;
          });
      });
    </script>
  </body>
</html>