package com.ezen.todaytable.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IQnaDao;
import com.ezen.todaytable.dto.Paging;

@Service
public class QnaService {

	@Autowired
	IQnaDao qdao;

	public void listQna(HashMap<String, Object> paramMap) {
		HttpServletRequest request = (HttpServletRequest)paramMap.get("request");
		HttpSession session = request.getSession();
		int page=1;
		if( request.getParameter("page")!= null) {
			page = Integer.parseInt( request.getParameter("page") );
			session.setAttribute("page", page);
		}else if( session.getAttribute("page")!=null) {
			page = (Integer)session.getAttribute("page");
		}else {
			session.removeAttribute("page");
		}
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setDisplayPage(5);
		paging.setDisplayRow(5);
		
		paramMap.put("cnt", 0);
		qdao.getAllCount(paramMap);
		int count = Integer.parseInt( paramMap.get("cnt")+"");
		paging.setTotalCount(count);
		paging.paging();
		paramMap.put("startNum", paging.getStartNum() );
		paramMap.put("endNum", paging.getEndNum() );
		paramMap.put("paging", paging);
		qdao.listQna( paramMap );
	}

		public void oneQna(HashMap<String, Object> paramMap) {
			qdao.oneQna(paramMap);
		
	}

		public void insertQnas(HashMap<String, Object> paramMap) {
			qdao.insertQnas(paramMap);
			
		}

		public void qnaupdate(HashMap<String, Object> paramMap) {
			qdao.qnaupdate(paramMap);
			
		}

}

