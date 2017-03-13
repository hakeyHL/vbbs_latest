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

    <div class="col-lg-12" style="margin: 20px"></div>

    <form class="form-horizontal" action="<%=contextPath%>/config/add"id="config_form"  method="POST" >

        <div class="form-group">
            <label  class="col-sm-3 control-label">投票贴</label>
            <div class="col-sm-3">
                <select name="postId" id="vote_post" required="required" class="form-control" >
                    <option value="">--请选择未审批投票贴--</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-3 control-label">投票频率</label>
            <div class="col-sm-3">
                <select  name="voteFreq" id="voteFreq" required="required" class="form-control">
                    <option value="">--请选择投票频率--</option>
                    <option value="1">仅投一次</option>
                    <option value="2">每天都能投</option>
                </select>
            </div>
        </div>


        <div class="form-group">
            <label  class="col-sm-3 control-label">每天投票次数</label>
            <div class="col-sm-3">
                <input type="number" name="voteTimes" id="voteTimes" class="form-control" value="${config.voteTimes}" >
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-3 control-label">最多能投票数</label>
            <div class="col-sm-3">
                <input type="number"  name="maxVote" required="required" class="form-control" value="${config.maxVote}" >
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <input type="hidden" name="id" value="${config.id}">
                <button type="submit" class="btn btn-primary" id="button_submit">确定</button>
            </div>
        </div>

    </form>


</div>

<script>

    $(function(){

        var form = $('#config_form');
        form.validate({
            rules :{
                postId : {
                    required : true
                },

                voteFreq : {
                    required : true
                },

                voteTimes : {
                    required : true
                },

                maxVote : {
                    required : true
                }
            }
        })

        var votePosts = $.parseJSON('${votePosts}');

        var postId = '${config.postId}';
        $.each(votePosts, function(i, val){
            $("#vote_post").append("<option value='"+val.id+"'>"+val.title+"</option>");
            if(postId == val.id){
                $("#vote_post").val(postId);
            }
        });

        var voteFreq = '${config.voteFreq}';
        $("#voteFreq").val(voteFreq);
    });

    $("#voteFreq").change(function(){
        var vf = $("#voteFreq").val();
        if(vf == '1'){
            $('#voteTimes').val("0");
            $('#voteTimes').attr('readonly', true);
        }else{
            $('#voteTimes').attr('readonly', false);
        }
    })

</script>

<jsp:include page="../include/footer.jsp"/>