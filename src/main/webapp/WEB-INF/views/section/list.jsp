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

    <!-- 删除提示信息 -->
    <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">提示信息</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要删除吗？</p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="delete_url"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" onclick="urlDelete()" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>

    <div class="alert alert-success" id="delete_success" role="alert" style="display: none">成功</div>
    <div class="alert alert-warning" id="delete_fail" role="alert" style="display: none">失败</div>


    <!--页面操作详细内容 开始-->
            <div class="row">
                <div class="col-lg-12">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <td>版块名称</td>
                            <td>操作</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${sections}" var="section">
                            <tr>
                                <td>${section.name}</td>
                                <td>
                                    <c:if test="${section.id != '1'}">
                                    <a href="<%=contextPath%>/section/detail/${section.id}" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span>编辑</a>
                                    <a href="javascript:void(0)" type="button" onclick="deleteById('<%=contextPath%>/section/delete/${section.id}')" class="btn btn-primary" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-trash"></span>删除</a>
                                    </c:if>

                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
</div>

<jsp:include page="../include/footer.jsp"/>
<script>

    function deleteById(url){
        $('#delete_url').val(url);
        $('#deleteModal').modal();
    }

    function urlDelete(){
        var url = $.trim($("#delete_url").val());//获取会话中的隐藏属性URL
        $.ajax({
            async : true,
            url: url,
            type: 'GET',
            success: function(result) {
                if(result.code == 200){
                    $("#delete_success").css("display", "block").hide(3000);
                    window.location.reload();
                }else{
                    BootstrapDialog.show({
                        title: '删除失败',
                        message: result.message
                    });
                }
            },
            error: function(xhr, textStatus, errorThrown){
                $("#delete_fail").css("display", "block").hide(3000);
            }
        });
    }
</script>