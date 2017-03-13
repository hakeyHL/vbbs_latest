<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>

<script type="text/javascript" src="/static/js/export/jquery.table2excel.js"></script>
<script type="text/javascript" src="/static/js/export/export.js"></script>


<div id="page-wrapper">

    <div class="col-lg-12" style="margin: 10px"></div>

    <div class="row">
        <label class="control-label" style="margin-left: 15px"></label>
        <button type="button" class="btn"><span class="glyphicon glyphicon-export"></span>>导出结果</button>
    </div>

    <!--页面操作详细内容 开始-->
    <div class="row">
        <div class="col-lg-12 table2excel">
            <table id="waitexport" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <td style="width:10%">候选人姓名</td>
                    <td style="width:40%">候选人图像</td>
                    <td style="width:40%">候选人介绍</td>
                    <td>票数</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${candidates}" var="candidate">
                    <tr>
                        <td>${candidate.name}</td>
                        <td>
                            <img src="<%=basePath%>/${candidate.avatar}">
                        </td>
                        <td>${candidate.introduction}</td>
                        <td>${candidate.votes}</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="4" style="text-align: right; color:red;">总票数 : ${voteNum}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <jsp:include page="../include/page.jsp"/>
</div>

<jsp:include page="../include/footer.jsp"/>


<script>

    $(function () {
        $(".btn").click(function () {

            if (getExplorer() == 1) {//ie

                var filename = '${postTitle}' + "投票结果";
                tabletoExcel("waitexport", filename);
            } else {
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "candidate",
                    filename: '${postTitle}' + "投票结果",
                    exclude_img: true,
                    exclude_links: true,
                    exclude_inputs: true
                });
            }


        });

    });


    // 判断浏览器类型 返回1表示IE
    function getExplorer() {
        var explorer = window.navigator.userAgent;
        if (explorer.indexOf("MSIE") >= 0) {
            return 1;
        } else if (explorer.indexOf("Firefox") >= 0) {
            return 0;
        } else if (explorer.indexOf("Chrome") >= 0) {
            return 0;
        } else if (explorer.indexOf("Opera") >= 0) {
            return 0;
        } else if (explorer.indexOf("Safari") >= 0) {
            return 0;
        } else {
            return 1;
        }
    }
</script>