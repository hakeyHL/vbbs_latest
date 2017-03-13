<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String contextPath = request.getContextPath();
%>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>


<div id="page-wrapper">


    <!-- 修改密码结果提示 -->
    <div class="modal fade in" id="modify_result" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" style="display: none;top:20%">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    修改成功！
                </div>
                <div class="modal-footer">
                    <button id="confirm_button" type="button" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-12" style="margin: 20px"></div>

    <form class="form-horizontal" id="pwd_form" action="<%=contextPath%>/password/save" method="POST" >

        <div class="form-group">
            <label  class="col-sm-3 control-label">原密码</label>
            <div class="col-sm-3">
                <input type="password" name="password" maxlength="40" required="required" class="form-control" placeholder="请输入原密码" >
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-3 control-label">新密码</label>
            <div class="col-sm-3">
                <input type="password" name="newPwd" maxlength="40" required="required" class="form-control" placeholder="请输入新密码" >
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-3 control-label">确认新密码</label>
            <div class="col-sm-3">
                <input type="password" name="confirmPwd" maxlength="40" required="required" class="form-control" placeholder="请输入新密码" >
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <input type="submit" class="btn btn-primary" id="btn_modify" value="确定" />
            </div>
        </div>

    </form>


</div>


<script>

    $(function(){

        $("#pwd_form").submit(function(){
            $(this).ajaxSubmit({
                dataType:'json',
                beforeSend: function() {
                   // $('#btn_modify').attr('disabled', true);
                },
                success: function(data) {
                    if(data.code == 200){
                        $('#modify_result').modal('show');
                        $("#confirm_button").click(function(){
                            $('#modify_result').modal('hide');
                            window.location.href = '/';
                        });

                    }else{
                        BootstrapDialog.show({
                            title: '修改失败',
                            message: data.message
                        });
                    }


                }
            });

            return false;
        });

    });

</script>

<jsp:include page="../include/footer.jsp"/>

