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


    <div class="col-lg-12" style="margin: 10px"></div>
            <div class="row">
                <form  class="form-inline" action="<%=contextPath%>/post/find" method="GET">
                    <div class="form-group">
                        <label class="control-label" style="margin-left: 15px"></label>
                       <select name="section" id="section" class="form-control" >
                           <option value="">--请选择版块--</option>
                       </select>

                        <input type="text" name="title"  class="control-label" style="margin-left: 15px; height: 34px" value="${title}" placeholder="请输入标题"/>
                        <button type="submit" class="btn"><span class="glyphicon glyphicon-search"></span> 查询</button>
                    </div>
                </form>
            </div>

            <!--页面操作详细内容 开始-->
            <div class="row">
                <div class="col-lg-12">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <td>发帖人</td>
                            <td>版块</td>
                            <td>标题</td>
                            <td style="width:18%">内容</td>
                            <td>发帖时间</td>
                            <td>浏览量</td>
                            <td>评论数量</td>
                            <td>点赞数量</td>
                            <td>帖子状态</td>
                            <td>帖子类型</td>
                            <td>审核状态</td>
                            <td>是否置顶</td>
                            <td>操作</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${posts}" var="post">
                            <tr>
                                <td>${post.user}</td>
                                <td>${post.section}</td>
                                <td>${post.title}</td>
                                <td>${post.contentCopy}</td>
                                <td><fmt:formatDate value="${post.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>${post.browseNum}</td>
                                <td>${post.commentNum}</td>
                                <td>${post.thumbNum}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${post.status == '0'}">
                                            <span class="label label-default">普通</span>
                                        </c:when>
                                        <c:when test="${post.status == '1'}">
                                            <span class="label label-warning">精华</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${post.type == '0'}">
                                            <span class="label label-default">讨论帖</span>
                                        </c:when>
                                        <c:when test="${post.type == '1'}">
                                            <span class="label label-warning">投票贴</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${post.isApprove == '0'}">
                                            <span class="label label-default">未审核</span>
                                        </c:when>
                                        <c:when test="${post.isApprove == '1'}">
                                            <span class="label label-success">已审核</span>
                                        </c:when>
                                    </c:choose>

                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${post.top == '0'}">
                                            <span class="label label-default">否</span>
                                        </c:when>
                                        <c:when test="${post.top == '1'}">
                                            <span class="label label-warning">是</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="<%=contextPath%>/post/detail/${post.id}" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span>编辑</a>
                                    <a href="javascript:void(0)" type="button" onclick="deleteById('<%=contextPath%>/post/delete/${post.id}')" class="btn btn-primary" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-trash"></span>删除</a>
                                    <c:if test="${post.isApprove == '0'}" >
                                    <a id="${post.id}"  class="btn btn-primary approve"><span class="glyphicon glyphicon-align-center"></span>审核</a>
                                    </c:if>
                                    <c:if test="${post.isApprove == '1'}" >
                                    <a id="${post.id}" class="btn btn-primary approve"><span class="glyphicon glyphicon-align-center"></span>取消审核</a>
                                    </c:if>
                                    <c:if test="${post.top == '0'}">
                                    <a href="<%=contextPath%>/post/top/${post.id}" class="btn btn-primary"><span class="glyphicon glyphicon-top"></span>置顶</a>
                                    </c:if>
                                    <c:if test="${post.top == '1'}">
                                        <a href="<%=contextPath%>/post/top/${post.id}" class="btn btn-primary"><span class="glyphicon glyphicon-top"></span>取消置顶</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>

<script>

    $(function(){

        var section = '${section}';
        $.ajax({
            url: '<%=contextPath%>/post/getSections',
            type: 'GET',
            success: function(result) {
                if(result.code == 200){
                    $.each(result.data, function(i, val){
                        $("#section").append("<option value='"+val.id+"'>"+val.name+"</option>");
                        if(section == val.id){
                            $("#section").val(section);
                        }
                    });
                }

            },
            error: function(xhr, textStatus, errorThrown){

            }
        });
    })

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
                }
            },
            error: function(xhr, textStatus, errorThrown){
                $("#delete_fail").css("display", "block").hide(3000);
            }
        });
    }

    $(".approve").click(function () {
        var postId = $('#'+$(this).attr('id')+'');
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "<%=contextPath%>/post/approve",
            data: {id: $(this).attr('id')},
            success: function (req) {
                if(req.code == 200){
                    window.location.href = '<%=contextPath%>/post/find';
                }else{
                    BootstrapDialog.show({
                        title: '审批失败',
                        message: req.message
                    });
                }
            },
            error: function () {
            }
        });
    })


</script>