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
    <title>销售管理系统-角色列表页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/xadmin.js"></script>
  </head>
  <body>
    <div class="x-body">
      <div class="layui-row" style="text-align: center">
        <form class="layui-form layui-form-pane user-select-table">
          <div class="layui-form-item">
            <div class="layui-inline">
              <input type="text" name="username" placeholder="请输入用户名" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-inline">
              <input type="text" name="roleName" placeholder="请输入角色名" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-inline">
              <input type="text" name="roleDesc" placeholder="请输入角色描述" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-inline" style="text-align: left" id="status_select" lay-filter="status_select"></div>
            <button class="layui-btn layui-inline" lay-submit lay-filter="search">
              <i class="layui-icon">&#xe615;</i>
            </button>
            <a class="layui-btn layui-inline" href="javascript:location.replace(location.href);" title="刷新">
              <i class="layui-icon">&#xe669;</i>
            </a>
          </div>
        </form>
      </div>
      <table class="layui-hide" id="user_table" lay-filter="user_table"></table>
    </div>
    <%-- 这里放头工具栏按钮 id和table头的toolbar属性绑定--%>
    <script type="text/html" id="users_toolbar">
      <div class="layui-btn-container">
        <button class="layui-btn layui-btn-danger layui-btn-sm" lay-event="delAll"><i class="layui-icon"></i>批量删除
        </button>
        <button class="layui-btn layui-btn-sm layui-btn-warm" onclick="x_admin_show('添加权限','${pageContext.request.contextPath}/views/role-add.jsp',480,260)"><i class="layui-icon"></i>添加
        </button>
      </div>
    </script>

    <%-- 这里放CRUD行工具栏按钮 id和table行的toolbar属性绑定--%>
    <script type="text/html" id="form_bar">
      <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="statusChange"  title="启用">
        <i class="layui-icon">&#xe601;</i>
      </a>
      <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail" title="查看">
        <i class="layui-icon">&#xe63c;</i>
      </a>
      <a class="layui-btn layui-btn-xs" lay-event="edit" title="编辑">
        <i class="layui-icon">&#xe642;</i>
      </a>
      <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" title="删除">
        <i class="layui-icon">&#xe640;</i>
      </a>
    </script>
    <script>
      layui.config({
        base: '${pageContext.request.contextPath}/layui/'
      }).extend({
        test: 'selectPlus'
      }).use(['selectPlus', 'jquery', 'table', 'form'], function () {
        var $ = layui.$,
                selectPlus = layui.selectPlus,
                table = layui.table,
                form = layui.form;

        // 单选
        var status_select = selectPlus.render({
          el: '#status_select',
          data: [{
            "name": "停用 --- 启用",
            "id": "0,1",
            "text": "stop --- start"
          }, {
            "name": "停用",
            "id": "0",
            "text": "stop"
          }, {
            "name": "启用",
            "id": "1",
            "text": "start"
          }],
          type: "radio",
          valueName: "name",
          values: '停用 --- 启用'
        });

        //方法级渲染
        table.render({
          elem: '#user_table'
          ,url: '${pageContext.request.contextPath}/role/findByQuery'
          ,id: 'userReload'
          ,toolbar: '#users_toolbar' //自定义工具栏
          ,page: true
          ,cols: [[
            {checkbox: true, fixed: true}
            ,{field:'id', title: 'ID', width:80, sort: true, fixed: true}
            ,{field:'roleName', title: '角色名', sort: true}
            ,{field:'roleDesc', title: '角色描述'}
            ,{field:'users', title: '用户名', templet:function (data) {
                userNames = []
                for (let i = 0; i < data.users.length; i++) {
                  userNames.push(data.users[i].username);
                }
                return userNames.join(",")
              }}
            ,{field:'status', title: '角色状态', width:120, align: 'center', templet:function (data) {
                if(data.status === 0){
                  return "<span class='layui-btn layui-btn-disabled layui-btn-sm'>已停用</span>";
                } else {
                  return "<span class='layui-btn layui-btn-normal layui-btn-sm'>已启用</span>";
                }
              }}
            ,{fixed: 'right', width: 200, align: 'center', toolbar: '#form_bar'}
          ]]
        });

        form.on('submit(search)', function (data) {
          var formData = data.field;
          console.debug(formData);
          var statusCheck = status_select.getChecked();
          var roleName = formData.roleName,
                  username = formData.username,
                  roleDesc = formData.roleDesc,
                  status = statusCheck.data[0].id;
          if (roleName === "" && roleDesc === "" && status === "" && username === "") {
            layer.msg('请输入筛选条件！', {icon: 2, time: 1500});
            return false;
          }
          if (roleName !== "") {
            roleName = "%" + roleName + "%"
          }
          if (roleDesc !== "") {
            roleDesc = "%" + roleDesc + "%"
          }
          if (username !== "") {
            username = "%" + username + "%"
          }

          //数据表格重载
          table.reload('userReload', {
            page: {
              curr: 1 //重新从第 1 页开始
            }
            , where: {//这里传参  向后台
              username: username,
              roleName: roleName,
              roleDesc: roleDesc,
              status: status
            }
          });
          return false;//false：阻止表单跳转  true：表单跳转
        });

        //监听头工具栏事件
        table.on('toolbar(user_table)', function (obj) {
          var checkStatus = table.checkStatus(obj.config.id)
          var data = checkStatus.data; //获取选中的数据
          //json字符串转换成Json数据 eval("("+jsonStr+")")  /JSON.parse(jsonStr)
          data = eval("(" + JSON.stringify(data) + ")");
          switch (obj.event) {
            case 'delAll':
              if (data.length === 0) {
                layer.msg('请至少选择1行', {icon: 2, time: 1500});
              } else {
                layer.alert('您确认要删除' + data.length + '条数据吗？', {
                  skin: 'layui-layer-molv' //样式类名layui-layer-lan或layui-layer-molv  自定义样式
                  , closeBtn: 1    // 是否显示关闭按钮
                  , anim: 1 //动画类型
                  , btn: ['确定', '取消'] //按钮
                  , icon: 2    // icon
                  , yes: function () {
                    // layer.msg('确定', { icon: 1, time: 1500 });
                    ids = []
                    for (var i = 0; i < data.length; i++) {
                      console.debug("id:======" + data[i].id)
                      ids.push(data[i].id)
                    }
                    //发送请求到后台
                    $.post("${pageContext.request.contextPath}/role/delete", {ids: ids.join(",")}, function (result) {
                      if (result.success === true) {//删除成功，刷新当前页表格
                        // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                        layer.msg(result.msg, {icon: 1, time: 1500});
                        window.location.reload();
                      } else if (result.success === false) {  //删除失败
                        layer.alert(result.msg, {icon: 2});
                      }
                    });
                  }
                });
              }
              break;
          }
        });

        //监听行工具事件
        table.on('tool(user_table)', function (obj) { //注：tool 是工具条事件名，parts_table 是 table 原始容器的属性 lay-filter="对应的值"
          var data = obj.data //获得当前行数据
                  , layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
          var tr = obj.tr; //获得当前行 tr 的DOM对象
          switch (layEvent) {
            case 'statusChange':
              var status = data.status === 0 ? 1 : 0;
              layer.confirm('您确定更改id：' + data.id + '的角色状态吗？', function (index) {
                //向服务端发送删除指令，在这里可以使用Ajax异步
                $.post("${pageContext.request.contextPath}/role/updateStatus", {id: data.id, status: status}, function (ret) {
                  if (ret.success === true) {//修改状态成功，刷新当前页表格
                    layer.msg(ret.msg, {icon: 1, time: 1500});
                    window.location.reload();
                  } else if (ret.success === false) {  //修改状态失败
                    layer.alert(ret.msg, {icon: 2});
                  }
                });
              });
              break;
            case 'detail':
              x_admin_show('查看角色','${pageContext.request.contextPath}/views/role-detail.jsp?id=' + data.id,480,260);
              break;
            case 'del':
              layer.confirm('您确定删除id：' + data.id + '的角色数据吗？', function (index) {
                //向服务端发送删除指令，在这里可以使用Ajax异步
                $.post("${pageContext.request.contextPath}/role/delete", {ids: data.id}, function (ret) {
                  if (ret.success === true) {//删除成功，刷新当前页表格
                    layer.msg(ret.msg, {icon: 1, time: 1500});
                    window.location.reload();
                  } else if (ret.success === false) {  //删除失败
                    layer.alert(ret.msg, {icon: 2});
                  }
                });
              });
              break;
                  /**
                   * 弹出编辑表格
                   */
            case 'edit':
              console.debug(data);
              x_admin_show('修改角色','${pageContext.request.contextPath}/views/role-edit.jsp?id=' + data.id,480,260);
              break;
          }
        });
      })
    </script>
  </body>
</html>