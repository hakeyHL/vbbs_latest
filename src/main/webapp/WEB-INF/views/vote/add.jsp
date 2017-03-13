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

    <!-- 导入结果提示 -->
    <div class="modal fade in" id="import_result" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" style="display: none;top:20%">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel">导入结果</h4>
                </div>
                <div class="modal-body">
                    <ul>
                        <li>本次导入共<span id="totalRows"></span>条</li>
                        <li>导入成功<span id="successRows">条</span></li>
                        <li>失败数量<span id="failRows">条</span></li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button id="confirm_button" type="button" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-12" style="margin: 20px"></div>

    <form class="form-horizontal" action="<%=contextPath%>/vote/add" id="candidate_form" enctype="multipart/form-data"  method="POST" >

        <div class="form-group">
            <label  class="col-sm-3 control-label">投票帖</label>
            <div class="col-sm-2">
                <select name="postId" required="required" class="form-control" id="votePost">
                    <option value="">--请选择投票帖--</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-3 control-label">介绍</label>
            <div class="col-sm-7">
                <textarea name="instructions" maxlength="100" required="required" class="form-control" rows="3">${vote.instructions}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-3 control-label">导入候选人</label>
            <div class="col-sm-7">
                <input type="file" name="candidateFile" id="candidateFile" class="fileupload file-loading"/>
            </div>
        </div>


        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <input type="submit" value="导入" class="btn btn-primary"  id="button_import" />
            </div>
        </div>

    </form>


</div>

<script>

    $(function(){

        var form = $('#candidate_form');
        form.validate({
            rules :{
                postId : {
                    required : true
                }
            }
        })

        $('#candidateFile').change(function(e){
            var file = this.files[0];
            var name = file.name;
            name = name.substring(name.lastIndexOf("."), name.length).toUpperCase();
            if(name != ".XLSX"){
                alert("请选择以.xlsx结尾的文件");
                return false;
            }
        });

        var votePosts = $.parseJSON('${votePosts}');
        $.each(votePosts, function(i, val){
            $("#votePost").append("<option value='"+val.id+"'>"+val.title+"</option>");
        });

        $("#candidate_form").submit(function(){

            var file = $('#candidateFile');
            if(file.val() == ''){
                alert('请选择以.xlsx结尾的文件');
                return false;
            }

            $(this).ajaxSubmit({
                dataType:'json',
                beforeSend: function() {
                    $('#button_import').attr('disabled', true);
                   // $('#button_import').val('提交中...');
                },
                success: function(data) {
                    if(data.code == 200){
                        $('#import_result').modal('show');
                        $('#totalRows').html(data.map.totalRows);
                        $('#successRows').html(data.map.successRows);
                        $('#failRows').html(data.map.failRows);
                        $("#confirm_button").click(function(){
                            $('#import_result').modal('hide');
                            window.location.href = '<%=contextPath%>/vote/list';
                        });

                    }else{
                        BootstrapDialog.show({
                            title: '导入失败',
                            message: '请检查Excel格式，重新导入!'
                        });
                    }


                },

                error : function(XmlHttpRequest, textStatus, errorThrown){
                    BootstrapDialog.show({
                        title: '导入失败',
                        message: '请检查Excel格式，重新导入!'
                    });
                }
            });

            return false;
        });

    });

</script>

<jsp:include page="../include/footer.jsp"/>
