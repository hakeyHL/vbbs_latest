/**
 * Created by Administrator on 2016/9/8.
 */
/*
 var vote = {
 ele : {
 btn_unsel : $(".btn-unsel"),
 number_span : $(".number-span"),
 people_span : $(".people-span"),
 unsel :
 },
 init : function(){
 vote.vote_c();
 },
 //投票点击功能
 vote_c : function(){
 vote.ele.btn_unsel.click(function(){
 $(this).hide();
 $(this).parent().find(".btn-sel").show();
 //每条投票人数增加
 var span = $(this).parent().parent().find(".number-span");
 var vhtml = parseInt(span.html())+1;
 span.html(vhtml) ;
 });
 },
 }
 vote.init();
 */

var sel = $(".sel");
var unsel = $(".unsel");
var btn = $(".vote-btn");
btn.click(function(){
    var count = $(this).parent().parent().find(".count-span"), maxVoteNumber = config.maxVote;
    var length = $(".on").length;

    // 选中
    if($(this).find('.sel').is(":hidden")){
        // 判断是否超过限制
        if(length >= maxVoteNumber){
            layer.open({
                content: '您每日最多只能投' + maxVoteNumber + '票！'
                ,skin: 'msg'
                ,time: 2 //2秒后自动关闭
                ,offset:'200px'
            });
            return false;
        }

        $(this).find(".unsel").hide();
        $(this).find(".sel").show();
        $(this).find(".sel").css("display","inline-block");
        $(this).addClass("btn-sel");
        count.html(parseInt(count.html())+1);
        $(this).addClass("on");

    }else if($(this).find('.unsel').is(":hidden")){
        $(this).removeClass("on");
        $(this).find(".unsel").show();
        $(this).find(".sel").hide();
        $(this).removeClass("btn-sel");
        count.html(parseInt(count.html())-1);
    }
})