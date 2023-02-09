<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="../include/headerfooter/header.jsp" %>
<%@ include file="/mypage/sub_menu_left.jsp"%>

<article>
   <div id="memberdetail" >
     
      <form method="post" name="formm">
      
         <fieldset>
         <legend>Member Detail Info</legend>         
            <span style="float:left; margin-right:20px" id="memberdetail-profile">
               <img src="${loginUser.img}" onerror="this.src='imageProfile/basic.jpg'"/>
            </span>

            <label><b>이 름:&nbsp;&nbsp;&nbsp; ${loginUser.name}</b></label><br/>
            <label><b>아이디:&nbsp;&nbsp;&nbsp; ${loginUser.id}</b></label><br/>
            <label><b>닉네임:&nbsp;&nbsp;&nbsp; ${loginUser.nick}</b></label><br/>
            <label><b>전화번호:&nbsp;&nbsp;&nbsp; ${loginUser.phone}</b></label><br/>
            <label><b>이메일:&nbsp;&nbsp;&nbsp; ${loginUser.email}</b></label><br/>
            <label><b>주  소:&nbsp; ${loginUser.address1} &nbsp; ${loginUser.address2}</b></label><br/>
            <label><b>우편번호:&nbsp;&nbsp;&nbsp; ${loginUser.zip_num}</b></label><br/>
            <label><b>가입일자:&nbsp;&nbsp;&nbsp; ${loginUser.indate}</b></label><br/>                 
         </fieldset>
         <div class="clear"></div>
         <div id="buttons">
            <input type="button" value="정보수정" class="cancel" onclick="updateMemForm'">
			<input type="button" value="회원탈퇴" class="cancel" onclick="withDrawal('${loginUser.id}','${loginUser.pwd }');">
         </div>
      </form>
   </div>
</article>
<%@ include file="../include/headerfooter/footer.jsp" %>