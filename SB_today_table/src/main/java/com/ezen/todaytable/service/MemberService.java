package com.ezen.todaytable.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IMemberDao;

@Service
public class MemberService {

	@Autowired
	IMemberDao mdao;

	public void getMembersList(HashMap<String, Object> paramMap) {
		mdao.getMembersList(paramMap);
	}
	
}
