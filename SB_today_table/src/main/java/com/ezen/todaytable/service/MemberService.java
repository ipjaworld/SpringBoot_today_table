package com.ezen.todaytable.service;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IMemberDao;
import com.ezen.todaytable.dto.MemberVO;

@Service
public class MemberService {

	@Autowired
	IMemberDao mdao;

	public void getMembersList(HashMap<String, Object> paramMap) {
		mdao.getMembersList(paramMap);
	}

	public void insertMemberttable( MemberVO membervo) {
		mdao.insertMemberttable(membervo);
	}
	
}
