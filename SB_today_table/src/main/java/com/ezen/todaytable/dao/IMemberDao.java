package com.ezen.todaytable.dao;

import java.util.HashMap;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;

import com.ezen.todaytable.dto.MemberVO;

@Mapper
public interface IMemberDao {

	void getMembersList(HashMap<String, Object> paramMap);

	void insertMemberttable(MemberVO membervo);
	

}
