package com.ezen.todaytable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IQnaDao;

@Service
public class QnaService {

	@Autowired
	IQnaDao mdao;
}
