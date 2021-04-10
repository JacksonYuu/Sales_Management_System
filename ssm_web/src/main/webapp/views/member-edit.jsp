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
    <title>销售管理系统-会员修改页面</title>
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
                    <span class="x-red">*</span>用户名
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="L_username" name="username" required="" lay-verify="nikename"
                           autocomplete="off" class="layui-input" placeholder="请输入用户名">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    至少2个字符
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_phoneNum" class="layui-form-label">
                    电话号码
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="L_phoneNum" name="phoneNum" lay-verify="phone"
                           autocomplete="off" class="layui-input" placeholder="请输入电话号码">
                </div>
            </div>
            <div class="layui-form-item">
                <label for="role_select" class="layui-form-label">
                    用户权限
                </label>
                <div class="layui-inline" style="text-align: left" id="role_select" lay-filter="role_select"></div>
            </div>
            <div class="layui-form-item">
                <label for="L_pass" class="layui-form-label">
                    <span class="x-red">*</span>密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="L_pass" name="password" required="" lay-verify="pass"
                           autocomplete="off" class="layui-input" placeholder="请输入密码">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    6到16个字符
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_repass" class="layui-form-label">
                    <span class="x-red">*</span>确认密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="L_repass" name="repassword" required="" lay-verify="repass"
                           autocomplete="off" class="layui-input" placeholder="请再次输入密码">
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_repass" class="layui-form-label">
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

            // 多选
            var role_select;
            // 密码，用来比较密码是否修改
            var oldPassword;

          $.post("${pageContext.request.contextPath}/user/findById", {id: ${param.id}}, function (result) {
              if (result != null) {//查询成功，给form赋值
                  var roleNames = []
                  for (let i = 0; i < result.roles.length; i++) {
                      roleNames.push(result.roles[i].roleName)
                  }
                  role_select = selectPlus.render({
                      el: '#role_select',
                      method: "POST",
                      url: "${pageContext.request.contextPath}/role/findAll",
                      parseData: function (res) {
                          return res;
                      },
                      valueName: "roleName",
                      values: roleNames,
                      valueSeparator: " --- "
                  });
                  oldPassword = result.password
                  form.val("edit_form", {
                      id: result.id,
                      username:result.username,
                      password:result.password,
                      repassword:result.password,
                      phoneNum:result.phoneNum
                  });
              } else {  //查询失败
                  // 获得frame索引
                  var index = parent.layer.getFrameIndex(window.name);
                  //关闭当前frame
                  parent.layer.close(index);
                  layer.alert("查询失败！", {icon: 2});
              }
          });

        //自定义验证规则
        form.verify({
          nikename: function(value){
            if(value.length < 2){
              return '昵称至少得2个字符啊';
            }
          }
          ,pass: [/(.+){6,12}$/, '密码必须6到12位']
          ,repass: function(value){
              if($('#L_pass').val()!=$('#L_repass').val()){
                  return '两次密码不一致';
              }
          }
        });

          //监听提交
          form.on('submit(edit)', function(data){
              var formData = data.field;
              console.debug(formData);
              var roleCheck = role_select.getChecked();
              var roleIds = []
              for (let i = 0; i < roleCheck.data.length; i++) {
                  roleIds.push(roleCheck.data[i].id)
              }
              var username = formData.username,
                  phoneNum = formData.phoneNum,
                  password = formData.password,
                  id = formData.id;
              password = password === oldPassword ? "" : password;
              layer.alert('您确认要修改此用户信息？', {
                  skin: 'layui-layer-molv' //样式类名layui-layer-lan或layui-layer-molv  自定义样式
                  , closeBtn: 1    // 是否显示关闭按钮
                  , anim: 1 //动画类型
                  , btn: ['确定', '取消'] //按钮
                  , icon: 2    // icon
                  , yes: function () {
                      //发送请求到后台
                      $.post("${pageContext.request.contextPath}/user/save",
                          {
                              id: id,
                              username: username,
                              password: password,
                              phoneNum: phoneNum,
                              roleIds: roleIds.join(",")
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