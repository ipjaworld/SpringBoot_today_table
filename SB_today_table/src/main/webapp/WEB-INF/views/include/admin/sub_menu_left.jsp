<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<meta charset="UTF-8">

<nav id="sub_menu">

<div class="sub_menu_item">
	<%-- <c:choose>
		<c:when test="${command.equals('adminRecipeList')}">
			<div class="smi_title_selected">
				<div><a href="recipe.do?command=adminRecipeList&page=1&key=">레시피 관리</a></div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="smi_title">
				<div><a href="recipe.do?command=adminRecipeList&page=1&key=">레시피 관리</a></div>
			</div>
		</c:otherwise>
	</c:choose> --%>

	<div class="smi_title">
				<div><a href="recipe.do?command=adminRecipeList&page=1&key=">레시피 관리</a></div>
			</div>
    <div class="smi_title">
        <div><a href="memberList?first='1'">회원 관리</a></div>
    </div>

    <div class="smi_title">
        <div><a href="recipe.do?command=adminReplyList&page=1&key=">댓글 관리</a></div>
    </div>

    <div class="smi_title">
        <div><a href="recipe.do?command=adminQnaList&page=1&key=">Q&amp;A 관리</a></div>
    </div>
</div>

</nav>