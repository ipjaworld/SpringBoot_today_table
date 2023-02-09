<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/admin/admin_header.jsp"%>

<article class="admin-board container">
<h1>Q&amp;A 게시글 리스트</h1>  
<form name="frm" method="post">
	<table class="admin-search-area">
		<tr><td>제목+내용 검색 
			<input type="text" name="key" value="${key}" > 
			    <input class="btn" type="button" value="검색"   onClick="go_search('adminQnaList');">
			    <input class="btn" type="button" name="btn_total" value="전체보기 "  onClick="go_total('adminQnaList');">
		    </td></tr>
	</table>

<table id="orderList">
		<tr><th><input type='checkbox' name='qseq' value='selectall' onclick='selectAll(this,name)'/>전체선택/해제</th><th>번호</th><th>(공개여부)</th><th>번호(답변여부)</th><th>제목</th> <th>작성자</th><th>작성일</th></tr>
	  	<c:forEach items="${qnaList}" var="qnaVO">	  		
	    	<tr>
	    	<td><input type="checkbox" name="qseq" value="${qnaVO.qseq}"></td>
	    	<td>${qnaVO.qseq}</td>
	    	<td>
	    		<c:choose>
	    			<c:when test="${qnaVO.secret==0}">
	    				<span style="font-weight:bold; color:blue;">공개글</span>	    				
	    			</c:when>
	    			<c:otherwise>
	    				<span style="font-weight:bold; color:red;">비공개글</span>
	    			</c:otherwise>
	    		</c:choose></td>
	    	<td> 
	      		<c:choose>          
	        		<c:when test='${qnaVO.rep==1}'>(미처리)</c:when>
	        		<c:otherwise>(답변처리완료)</c:otherwise>
	      		</c:choose></td>
	      		<td><a href="#" onClick="go_view('adminQnaDetail','qseq','${qnaVO.qseq}')">${qnaVO.qsubject}</a></td>
	      		<td> ${qnaVO.id} </td><td> <fmt:formatDate value="${qnaVO.qnadate}"/></td></tr>
	    </c:forEach>
	    		
	    		<tr><td><a href="#" onClick="go_adminQna_delete();">게시글 삭제하기</a></td></tr>
</table><br>
</form>
<div class="paging-area">
	<jsp:include page="/admin/paging/paging.jsp">
		<jsp:param name="command" value="recipe.do?command=adminQnaList" />
	</jsp:include>
</div>
</article>
	
<%@ include file="/admin/footer.jsp"%>