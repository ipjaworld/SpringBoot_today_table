package com.ezen.todaytable.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAdminDao {
	//페이징 카운트
	void adminGetAllCount(HashMap<String, Object> cntMap);
	//회원목록
	void getAdminMemberList(HashMap<String, Object> paramMap);
	//휴면전환
	void adminSleepMem(String useid);
	//회원디테일
	void getAdminMemDetail(HashMap<String, Object> paramMap);
	//Qna목록
	void getAdminQnaList(HashMap<String, Object> paramMap);
	//Qna게시글삭제
	void adminDeleteQna(int qseq);
	//Qna디테일
	void getAdminQnaDetail(HashMap<String, Object> paramMap);
	//Qna답변
	void adminSaveReply(int qseq,String replyQna);
	//댓글목록조회
	void getAdminReplyList(HashMap<String, Object> paramMap);
	//댓글삭제
	void adminDeleteReply(int replyseq);

}
