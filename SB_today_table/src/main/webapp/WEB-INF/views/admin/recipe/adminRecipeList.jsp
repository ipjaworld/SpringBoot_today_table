<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/admin/admin_header.jsp"%>

<article class="admin-board container">
<h1>레시피 리스트</h1>
<jsp:include page="adminRecipeNav.jsp"/>

<form name="frm" method="post">
	<table class="admin-search-area">
		<tr><td>제목+내용 검색 
			<input type="text" name="key" value="${key1}" > 
			    <input class="btn" type="button" value="검색"   onClick="go_search('adminRecipeList');">
			    <input class="btn" type="button" name="btn_total" value="전체보기 "  onClick="go_total('adminRecipeList');">		    
		    </td></tr>
		<tr>    		    
	</table>

<table id="adminRecipeList">
		<tr><th><input type='checkbox' name='rnum' value='selectall' onclick='selectAll(this,name)'/>전체선택/해제</th>
		<th>글번호</th><th colspan="2">레시피</th><th>작성자</th><th>작성날짜</th><th>좋아요</th><th>신고횟수(50회)</th><th>조회수</th><th>추천하기</th></tr>
	  	<c:forEach items="${recipeList}" var="recipeVO">	  		
	    	<tr>
	    	<td><input type="checkbox" name="rnum" value="${recipeVO.rnum}">
	    	<c:choose>
	      	<c:when test="${recipeVO.rec==1}">
	      	<span style="font-weight:bold; color:red;">(추천)</span>
	      	</c:when>
	      	<c:otherwise>
	      	<span style="font-weight:bold; color:blue;">(일반)</span>
	      	</c:otherwise>
	      	</c:choose>
	    	</td>
	    	<td>${recipeVO.rnum}</td>
	    	<td>	    	
	    	<img src="${recipeVO.thumbnail}" style="width:100px;"/>	    	
	    	</td>
	      	<td><a href="#" onClick="go_view('recipeDetailView','rnum','${recipeVO.rnum}')">${recipeVO.subject}</a></td>
	      	<td>${recipeVO.id}</td>
	      	<td> <fmt:formatDate value="${recipeVO.indate}"/></td>
	      	<td>${recipeVO.likes}</td>     	
	      	<c:choose>
	      		<c:when test="${recipeVO.report>=50}">
	      			<td style="color:red; font-weight: bold;">${recipeVO.report}</td>
	      		</c:when>
	      		<c:otherwise>
	      			<td>${recipeVO.report}</td>
	      		</c:otherwise>
	      	</c:choose>	
	      	<td>${recipeVO.views}</td>
	      	<td><input type="button" value="추천하기" onclick="go_1recommend('${recipeVO.rnum}','all')"></td></tr>
	    </c:forEach>
	    		<tr>
	    		<th><a href="#" onClick="go_adminRecipe_delete()">게시글 삭제하기</a></th></tr>
	    		<tr>
	    		<th><a href="#" onClick="go_recommendlist('all');">추천하기</a></th></tr>
</table><br>
</form>
<div class="paging-area">
	<jsp:include page="/admin/paging/paging.jsp">
		<jsp:param name="command" value="recipe.do?command=adminRecipeList" />
	</jsp:include>
</div>
</article>
	
<%@ include file="/admin/footer.jsp"%>