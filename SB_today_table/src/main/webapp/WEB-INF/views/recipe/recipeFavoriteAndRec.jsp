<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/headerfooter/header.jsp"%>

	<div class="main-contents-list">
		<c:forEach items="${recommandList}" var="recRecipeVO" varStatus="status">
		   	<div class="recipe-card">
		   		<div class="item">
			   		<c:choose>
		      		<c:when test="${loginUser.id == recRecipeVO.id}">
		      			<a href="recipeDetailWithoutView?rnum=${recRecipeVO.RNUM}">
				            	<img src="/${recRecipeVO.THUMBNAIL}" width=300 height=200>
				         </a>
			         </c:when>
		      		<c:otherwise>
			      		<a href="recipeDetailWithoutView?rnum=${recRecipeVO.RNUM}">
			            		<img src="/${recRecipeVO.THUMBNAIL}" width=300 height=200>
			          	</a>
			          </c:otherwise>
	        	</c:choose>	
		   		</div>				
		   		<div><h4>${recRecipeVO.SUBJECT}</h4></div>
		   		<div class="recipe-card-nick-area"><img src="/${recRecipeVO.IMG}" width=20 height=20>${recRecipeVO.NICK} 
		   			<c:if test="${replyCountList[status.index]!=0}">
		   				<b>(${replyCountList[status.index]})</b>
		   			</c:if>
		   		</div>
		   		<div>
		   			<h5>
   					<img src="image/likeBtn1.png" class="recipe-card-likes"/> ${recRecipeVO.LIKES} &nbsp;&nbsp;	
		   			<img src="image/viewIcon.png" class="recipe-card-likes"/> ${recRecipeVO.VIEWS}</h5>
		   		</div>
		   		<div><h6>조리시간 : ${recRecipeVO.TIME}분</h6></div>
		   	</div>
		</c:forEach>
	</div>
<br><br><br><br>
	<div class="main-contents-list">
		<c:forEach items="${favoriteList}" var="recipeVO" varStatus="status">
		   	<div class="recipe-card">
		   		<div class="item">
			   		<c:choose>
		      		<c:when test="${loginUser.id == recipeVO.ID}">
		      			<a href="recipeDetailWithoutView?rnum=${recipeVO.RNUM}">
				            	<img src="/${recipeVO.THUMBNAIL}" width=300 height=200>
				         </a>
			         </c:when>
		      		<c:otherwise>
			      		<a href="recipeDetailWithoutView?rnum=${recipeVO.RNUM}">
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

<%@ include file="../include/headerfooter/footer.jsp"%>