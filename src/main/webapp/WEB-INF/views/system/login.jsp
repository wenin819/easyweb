<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/WEB-INF/includes/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <%@include file="/WEB-INF/includes/commonBase.jsp"%>
    <title>通讯录增加或修改</title>
    <style>
        body {
            padding-top: 20px;
            background: transparent;
        }
        .margin-base-vertical {
            margin: 20px 0 40px 0;
        }
        .panel {
            background-color: rgba(255, 255, 255, 0.9);
        }
        #loginForm {
            max-width: 380px;
            margin-left: auto;
            margin-right: auto;
        }
    </style>
    <script>
        if(top != window) {
            top.location.href = location.href;
        }
        $(function() {
            $("#username").focus();
            $("#loginForm").validate(
                    {
                        rules: {
                            username: { required: true },
                            password: { required: true }
                        },
                        submitHandler: function(form){
                            loading('正在提交，请稍等...');
                            form.submit();
                        },
                        errorContainer: "#messageBox",
                        errorPlacement: function(error, element) {
                            $("#messageBox").text("输入有误，请先更正。");
                            if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                                error.appendTo(element.parent().parent());
                            } else {
                                error.insertAfter(element);
                            }
                        }
                    });
            $("#defaultPass").click(function() {
                $("#password").val($("#username").val());
                $("#loginFormSubmit").click();
            });
        });
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3" >

        <div class="panel panel-warning">
            <div class="panel-heading">
                <h3 class="panel-title text-center">系统登陆</h3>
            </div>
    <form id="loginForm" class="form panel-body margin-base-vertical"
          action="./login.html" method="post">
        <tags:message />
        <div class="form-group">
            <div class="col-md-12">
                <label class="control-label" for="username">登录名</label>
                <input type="text" id="username" name="username" class="form-control"
                   value="${username}" placeholder="学号">
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-12">
                <label class="control-label" for="password">密码</label>
                <input type="password" id="password" name="password" class="form-control"
                   placeholder="密码"/>
            </div>
        </div>
        <br />
        <div class="form-group text-center">
            <div class="col-md-12">
                <input id="loginFormSubmit" class="btn btn-primary"
                       type="submit" value="登 录"/>
                <input class="btn" type="reset" value="重 置"/>
                <input id="defaultPass" class="btn"
                       type="button" value="默认密码登陆"/>
            </div>
        </div>
    </form>
        </div>
        </div>
    </div>
</div>
</body>
</html>