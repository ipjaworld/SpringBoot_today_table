<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="UTF-8">

<nav id="qna_sub_menu">
	<ul>
		<li><a href="recipe.do?command=qnaList&page=1">전체 Q&amp;A</a></li>

		<c:if test="${loginAdmin == null && loginUser != null}">
		<li><a href="recipe.do?command=myqnaList&page=1">나의 Q&amp;A</a></li>
		<li><a href="recipe.do?command=qnaWriteForm&page=1">Q&amp;A 작성</a></li>
		</c:if>
	</ul>
</nav>
