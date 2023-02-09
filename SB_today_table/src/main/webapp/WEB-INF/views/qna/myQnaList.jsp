<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file= "../include/headerfooter/header.jsp"%>
<%@ include file="qna_sub_menu_left.jsp" %>

<article id="qna-my-list">
	<h2 align="center" class="pt-3 mt-5"> 나의 Q&amp;A</h2>	
	<h5 class="pt-3 pb-3 mt-3 mb-3"> 내가 했던 질문들을 확인할 수 있습니다. </h5>
	<form name="formm" method="post" >
		<table class="mt-3" id="#" style="width:100%;">
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>등록일</th>
				<th>답변 여부</th>
			</tr>
			<c:forEach items="${myqnaList}" var="myqnaVO">
				<tr>
				<c:choose>
					<c:when test="${myqnaVO.SECRET=='0'}">
					<td>${myqnaVO.QSEQ}</td>
					</c:when>
					<c:otherwise>
					<td>${myqnaVO.QSEQ}</td>
					</c:otherwise>
				</c:choose>
										
					<c:choose>
						<c:when test="${myqnaVO.SECRET=='0'}">
							<td><a href="qnaDetail?qseq=${myqnaVO.QSEQ}">${myqnaVO.QSUBJECT}</a></td>
						</c:when>
						<c:otherwise>
							<td><a href="#" onclick="pwdcheck(${myqnaVO.QNAPASS},${myqnaVO.QSEQ})">${myqnaVO.QSUBJECT}
							<img src="image/key1.png" style="width:13px vertical-align:middle;"></a>	</td>
						</c:otherwise>					
					</c:choose>	
					<td><fmt:formatDate value="${myqnaVO.QNADATE}" type="date" /></td>
					<td>
						<c:choose>
						 	<c:when test="${myqnaVO.rep==1}">no</c:when>
						 	<c:when test="${myqnaVO.rep==2}">yes</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
	<jsp:include page="../paging/paging.jsp">
   <jsp:param name="command" value="myqnaList" />
</jsp:include>
</form>
</article>	

<%@ include file= "../include/headerfooter/footer.jsp"%>