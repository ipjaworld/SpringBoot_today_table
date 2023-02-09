<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/admin/admin_header.jsp"%>

<article class="admin-board">
	<div id="memberdetail">
		<h1>Item</h1>
		<form method="post" name="formm">
		
			<fieldset>
			<legend>member detail Info</legend>			
				<span style="float:left; margin-right:20px">
					<img src="imageProfile/${memberVO.img}" onerror="this.src='imageProfile/basic.jpg'" style="border-radius:20px"/>
					<!-- <img src="imageProfile/${memberVO.img}" style="border-radius:20px"/>-->
				</span>
				<h2>${memberVO.name}</h2>
				<label>이 름: </label><p>${memberVO.name}</p>
				<label>아이디: </label><p>${memberVO.id}</p>
				<label>전화번호: </label><p>${memberVO.phone}</p>
				<label>이메일: </label><p>${memberVO.email}</p>
				<label>닉네임: </label><p>${memberVO.nick}</p>
				<label>주  소: </label><p>${memberVO.address1}<br>${memberVO.address2}</p>
				<label>우편번호: </label><p>${memberVO.zip_num}</p>
				<label>가입일자: </label><p>${memberVO.indate}</p>						
			</fieldset>
			<div class="clear"></div>
			<div id="buttons">
				<input type="button" value="뒤로" class="cancel" onclick="history.go(-1);">
			</div>
		</form>
	</div>
</article>

<%@ include file="/admin/footer.jsp"%>