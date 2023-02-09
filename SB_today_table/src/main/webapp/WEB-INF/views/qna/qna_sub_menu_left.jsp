<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="UTF-8">

<nav id="qna_sub_menu">
	<ul>
		<li><a href="qnaList?page=1">전체 Q&amp;A</a></li>
		<!-- 	<c:if test="${loginAdmin == null && loginUser != null}"> 
		나중에 ul 위에다가 위치하기 </c:if> -->
		<li><a href="myqnaList?page=1">나의 Q&amp;A</a></li>
		<li><a href="qnaWriteForm?page=1">Q&amp;A 작성</a></li>
	</ul>
</nav>
