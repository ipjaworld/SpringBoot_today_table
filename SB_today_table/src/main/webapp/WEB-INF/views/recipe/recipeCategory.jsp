<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/headerfooter/header.jsp"%>



<article class="" id="recipe-category-list">
<div class="container">
	<c:if test="${status.equals('recipe')}">
		<h2><b>전체</b> 레시피 게시판 입니다~</h2>
	</c:if>
	<c:if test="${status.equals('type')}">
		<h2><b>종류별</b> 레시피 게시판 입니다~</h2>
	</c:if>
	<c:if test="${status.equals('theme')}">
		<h2><b>테마별</b> 레시피 게시판 입니다~</h2>
	</c:if>
	<c:if test="${status.equals('ing')}">
		<h2><b>재료별</b> 레시피 게시판 입니다~</h2>
	</c:if>
</div>

<div class="categoryListTitle container">
총 <b>${total}</b>개의 레시피가 있습니다.

	<ul class="nav nav-tabs2 pull-right">
		<li><a href="" onclick="sortBy()">정확순</a></li>
		<li><a href="" onclick="sortBy()">최신순</a></li>
		<li><a href="" onclick="sortBy()">추천순</a></li>
	</ul> 
	
</div>
	<div class="main-contents-list">
		<c:forEach items="${RecipeCategory}" var="recipeVO" varStatus="status">
		   	<div class="recipe-card">
		   		<div class="item">
			   		<c:choose>
		      		<c:when test="${loginUser.id == recipeVO.ID}">
		      			<a href="recipeDetailWithoutView?rnum=${recipeVO.RNUM}">
				            	<img src="/${recipeVO.THUMBNAIL}" width=300 height=200>
				         </a>
			         </c:when>
		      		<c:otherwise>
			      		<a href="recipeDetailView?rnum=${recipeVO.RNUM}">
			            		<img src="/${recipeVO.THUMBNAIL}" width=300 height=200>
			          	</a>
			          </c:otherwise>
	        	</c:choose>	
		   		</div>				
		   		<div><h4>${recipeVO.SUBJECT}</h4></div>
		   		<div class="recipe-card-nick-area"><img src="/${recipeVO.IMG}" width=20 height=20>${recipeVO.NICK} 
		   			<c:if test="${replyCountList[status.index]!=0}">
		   				<b>(${replyCountList[status.index]})</b>
		   			</c:if>
		   		</div>
		   		<div>
		   			<h5>
   					<img src="image/likeBtn1.png" class="recipe-card-likes"/> ${recipeVO.LIKES} &nbsp;&nbsp;	
		   			<img src="image/viewIcon.png" class="recipe-card-likes"/> ${recipeVO.VIEWS}</h5>
		   		</div>
		   		<div><h6>조리시간 : ${recipeVO.TIME}분</h6></div>
		   	</div>
		</c:forEach>
	</div>
	<jsp:include page="../paging/paging.jsp">
		<jsp:param name="command" value="recipeCategory?status=${status}" />
	</jsp:include>
</article>

<%@ include file="../include/headerfooter/footer.jsp"%>