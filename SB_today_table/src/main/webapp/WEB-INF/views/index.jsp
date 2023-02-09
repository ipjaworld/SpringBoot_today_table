<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="include/headerfooter/header.jsp" %> 

<nav class="">
	<div class="container main-top-area">
		<div class="main-page-card col-md-6 col-sm-12" id="main-total">
			<a class="" href="recipeCategory?status=recipe&page=1">
				<img src="image/maintable.png" />
			</a>
			<div class="main-page-card-black-area"></div>
		</div>
		<div class="main-page-card col-md-6 col-sm-12" id="main-type">
			<a class="" href="recipeCategory?status=type&page=1">
				<img src="image/typerecipe.png" />
			</a>
			<div class="main-page-card-black-area"></div>
		</div>
		<div class="main-page-card col-md-6 col-sm-12" id="main-theme">
			<a class="" href="recipeCategory?status=theme&page=1">
				<img src="image/themerecipe.png" />
			</a>
			<div class="main-page-card-black-area"></div>
		</div>
		<div class="main-page-card col-md-6 col-sm-12" id="main-ing">
			<a class="" href="recipeCategory?status=ing&page=1">
				<img src="image/ingredientsrecipe.png" />
			</a>
			<div class="main-page-card-black-area"></div>
		</div>
		<!-- 추후에 업데이트 예정
		<div class="main-page-card" id="main-favorite">
			<a class="" href="recipe.do?command=recipeFavoriteAndRec&page=1">
				<img src="image/regularcustomerRecipe.png" />
			</a>
			<div class="main-page-card-black-area"></div>
		</div>
		
		<div class="main-page-card">
			<a class="" href="recipe.do?command=index">사이트맵</a>
		</div> 
		-->
	</div>
</nav>

	<div class="container">
		<div class="slider mt-5 mb-5" id="slider">
	      <div class="subtitle mb-3">
	        <h2>Recent Recipe</h2>
	      </div>
	      <div class="slide" id="slide0">
	        <c:forEach items="${allList}" var="recipeVO" end="20">
		      	<c:choose>
		      		<c:when test="${loginUser.id == recipeVO.id}">
		      			<div class="item">
				        	<a href="recipeDetailWithoutView?rnum=${recipeVO.rnum}">
				            	<img src="${recipeVO.thumbnail}" width=300 height=200>
				            </a>
			            </div>
		        	</c:when>
		      		<c:otherwise>
			      		<div class="item">
			        		<a href="recipeDetailWithoutView?rnum=${recipeVO.rnum}" class="item">
			            		<img src="${recipeVO.thumbnail}" width=300 height=200>
			          		</a>
			          	</div>
		        	</c:otherwise>
	        	</c:choose>
	        </c:forEach>
	      </div>
	      <button class="ctrl-btn pro-prev" id="prevBtn">Prev</button>
	      <button class="ctrl-btn pro-next" id="nextBtn">Next</button>
	    </div>
	</div>


<%@ include file="include/headerfooter/footer.jsp" %> 