<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/admin/admin_header.jsp"%>


<article class="">
<div class="admin-main">
	<div class="container">
		<div class="main-top-area">
			<h2>관리자 대시보드</h2>
			<div class="main-today-summary">
				<div class="main-today-summary-top-area">
					<h5>오늘의 요약</h5>
				</div>
				<div class="main-today-summary-bot-area">
					등록된 레시피 총 열람수 :&nbsp;<b>${viewcnt}</b> &nbsp;&nbsp;/&nbsp;&nbsp; 관리자 추천수 :&nbsp;<b>${adminrec}</b> &nbsp;&nbsp;/&nbsp;&nbsp; 문의 답변률 :&nbsp;<b>${RepRate}%</b>
				</div>
			</div>
			<div class="row main-mid-area">
				<div class="col-md-6 col-sm-12 main-mid-area-card-box" id="">
					<div class="main-mid-area-card">
						<div>
							<h3>전체 게시물 수 : ${countRecipe}</h3>
						</div>
						<div>
							상위 조회수 게시물 3개
							<table>
								<tr><th>레시피</th><th>작성자</th><th>조회수</th><th>작성날짜</th></tr>
								<c:forEach items="${bestViewList}" var="bestViewVO">
								<tr><td><a href="#" onClick="go_view('recipeDetailView','rnum','${bestViewVO.rnum}')">${bestViewVO.subject}</a></td><td>${bestViewVO.id}</td><td>${bestViewVO.views}</td>
								<td><fmt:formatDate value="${bestViewVO.indate}"/></td>
								</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>
				<div class="col-md-3 col-sm-12 main-mid-area-card-box" id="">
					<div class="main-mid-area-card">
						<h3>전체 회원수</h3>
						<p>${countMember}</p>
						<h3>휴면 회원수</h3>
						<p>${sleepMember}</p>
					</div>
				</div>
				<div class="col-md-3 col-sm-12 main-mid-area-card-box" id="">
					<div class="main-mid-area-card">
						<h3>전체 문의수</h3>
						<p>${countQna}</p>
						<h3>전체 답변률</h3>
						<p><fmt:formatNumber value="${RepRate}" pattern=".0" type="percent"/>%</p>
						<!-- <div class="pie-chart1" id="pieChart"></div> -->
					</div>
				</div>
			</div>
			<div class="row main-btm-area">
				<div class="main-btm-area-box col-md-8 col-sm-12">
					<div class="main-btm-area-card" id="">
						<div>
							<h3>전체 댓글 수 : ${countReply}</h3>
						</div>
						<div>
							최근 댓글 3개
							<table>
								<tr><td>아이디</td><td>글번호</td><td>댓글내용</td><td>작성날짜</td></tr>
								<c:forEach items="${recentReplyList}" var="recentReplyVO" begin="0" end="2">
								<tr><td>${recentReplyVO.id}</td><td>${recentReplyVO.rnum}</td>
								<td><a href="#" onClick="go_view('recipeDetailView','rnum','${recentReplyVO.rnum}')">${recentReplyVO.content}</a></td>
								<td><fmt:formatDate value="${recentReplyVO.replydate}"/></td>
								</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>
				<div class="main-btm-area-box col-md-4 col-sm-12">
					<div class="main-btm-area-card">
						<h3></h3>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</article>

<%@ include file="../include/admin/footer.jsp"%>