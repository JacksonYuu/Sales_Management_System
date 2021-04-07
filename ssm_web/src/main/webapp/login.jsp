<%--
Created by IntelliJ IDEA.
User: 拼命三石
Date: 2021/4/5
Time: 17:43
To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>登录-销售管理系统</title>
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="stylesheet" href="css/font.css">
	<link rel="stylesheet" href="css/xadmin.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script src="layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/xadmin.js"></script>

</head>
<body class="login-bg">
    
    <div class="login">
        <div class="message">销售管理系统-登录</div>
        <div id="darkbannerwrap"></div>

        <form method="post" class="layui-form" action="${pageContext.request.contextPath}/login.do">
            <input name="username" placeholder="用户名"  type="text" lay-verify="required" class="layui-input" >
            <hr class="hr15">
            <input name="password" lay-verify="required" placeholder="密码"  type="password" class="layui-input">
            <hr class="hr15">
            <input value="登录" style="width:100%;" type="submit">
            <hr class="hr10" >
            <div class="layui-form-item">
                <span style="color: red; display: none" id="error_span">帐号或密码错误，请重新输入！</span>
            </div>
        </form>
    </div>
    <script language="javascript">
        //获取页面完整地址
        search = window.location.search;
        if (search !== "") {
            document.getElementById("error_span").style.display="inline";
        }
    </script>
</body>
</html>