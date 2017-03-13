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
        <form  class="form-inline" action="<%=contextPath%>/config/find" method="GET">
            <div class="form-group">
                <input name="title" id="post_title" class="form-control" placeholder="请输入帖子标题" value="${title}" style="margin-left: 16px;"/>
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
                    <td>投票贴</td>
                    <td>投票频率</td>
                    <td>每天投票次数</td>
                    <td>最多能投票数</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${voteConfigs}" var="config">
                    <tr>
                        <td>${config.title}</td>
                        <td>
                            <c:choose>
                                <c:when test="${config.voteFreq == '1'}">
                                    仅投一次
                                </c:when>
                                <c:when test="${config.voteFreq == '2'}">
                                    每天都能投
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${config.voteTimes}</td>
                        <td>${config.maxVote}</td>
                        <td>
                            <a href="<%=contextPath%>/config/detail/${config.id}" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span>编辑</a>
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
