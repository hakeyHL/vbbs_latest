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

    <div class="col-lg-12" style="margin: 10px"></div>

    <div class="row">
        <form  class="form-inline" action="<%=contextPath%>/award/record/find" method="GET">
            <div class="form-group">
                <label class="control-label" style="margin-left: 15px"></label>
                <select name="award" id="award" class="form-control" >
                    <option value="">--请选择奖项--</option>
                </select>
                <label class="control-label" style="margin-left: 15px"></label>
                <input name="nickName"  class="form-control" placeholder="请输入用户昵称" value="${nickName}"/>
                <label class="control-label" style="margin-left: 15px"></label>
                <input name="date" id="award_time"  class="form-control datetimepicker" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" placeholder="请选择日期" />
                    <script>
                    $("#award_time").datepicker({ dateFormat: 'yy-mm-dd' });
                </script>
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
                    <td>用户</td>
                    <td>中奖内容</td>
                    <td>中奖时间</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${records}" var="record">
                    <tr>
                        <td>
                            <c:forEach items="${users}" var="user">
                                <c:if test="${record.userId == user.id}">
                                    ${user.nickName}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>${record.awardsContent}</td>
                        <td><fmt:formatDate value="${record.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
        var award = '${award}';
        $.ajax({
            url: '<%=contextPath%>/award/getAwards',
            type: 'GET',
            success: function(result) {
                if(result.code == 200){
                    $.each(result.data, function(i, val){
                        $("#award").append("<option value='"+val.id+"'>"+val.awardsName+"</option>");
                        if(award == val.id){
                            $("#award").val(award);
                        }
                    });
                }

            },
            error: function(xhr, textStatus, errorThrown){

            }
        });
    })
</script>
