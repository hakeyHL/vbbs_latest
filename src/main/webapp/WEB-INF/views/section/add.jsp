<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String contextPath = request.getContextPath();
%>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/left.jsp"/>

<link rel="stylesheet" href="<%=contextPath%>/static/css/datetime/bootstrap-datetimepicker.min.css">


<div id="page-wrapper">

    <div class="col-lg-12" style="margin: 20px"></div>

    <form class="form-horizontal" id="section_form" action="<%=contextPath%>/section/add" method="POST" >

        <div class="form-group">
            <label  class="col-sm-3 control-label">版块名称</label>
            <div class="col-sm-3">
                <input type="text" name="name" maxlength="40" required="required" class="form-control" value="${section.name}" >
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <input type="hidden" name="id" value="${section.id}">
                <button type="submit" class="btn btn-primary">确定</button>
            </div>
        </div>

    </form>


</div>

<script>

    $(function(){

        var form = $('#section_form');
        form.validate({
            rules :{
                name : {
                    required : true
                }
            }
        })
    });

</script>


<jsp:include page="../include/footer.jsp"/>