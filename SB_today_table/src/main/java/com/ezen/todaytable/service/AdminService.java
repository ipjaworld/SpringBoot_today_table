package com.ezen.todaytable.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IAdminDao;
import com.ezen.todaytable.dto.Paging;

@Service
public class AdminService {

	@Autowired
	IAdminDao adao;
	//회원목록
	public void getAdminMemberList(HashMap<String, Object> paramMap) {
		 HttpServletRequest request = (HttpServletRequest)paramMap.get("request");
	      HttpSession session = request.getSession();   
	      
	      if(request.getParameter("first")!=null) {
	         session.removeAttribute("page");
	         session.removeAttribute("key");
	      }
	      
	      int page = 1;
	      if (request.getParameter("page") != null) {
	         page = Integer.parseInt(request.getParameter("page"));
	         session.setAttribute("page", page);
	      } else if (session.getAttribute("page") != null) {
	         page = (Integer) session.getAttribute("page");
	      } else {
	         session.removeAttribute("page");
	      }
	      
	      String key="";
	      if (request.getParameter("key") != null) {
	         key = request.getParameter("key");
	         session.setAttribute("key", key);
	      } else if (session.getAttribute("key") != null) {
	         key = (String) session.getAttribute("key");
	      } else {
	         session.removeAttribute("key");
	      }
	      
	      // 페이징 객체 생성
	      Paging paging = new Paging();
	      paging.setPage(page);
	      HashMap<String, Object> cntMap = new HashMap<String, Object>();
	      cntMap.put("cnt", 0);
	      cntMap.put("tableName", 2); 
	      cntMap.put("key", key); 
	      adao.adminGetAllCount(cntMap);
	      int count = (Integer) cntMap.get("cnt"+""); 
	      paging.setTotalCount(count);
	      paging.paging();
	      
	      paramMap.put("key", key);
	      paramMap.put("startNum", paging.getStartNum() );
	      paramMap.put("endNum", paging.getEndNum() );
	      adao.getAdminMemberList(paramMap);
	      
	      paramMap.put("paging", paging);
	      
		
	}
	//휴면회원전환
	public void adminSleepMem(String[] id) {
		
		for(String useid : id) {
			System.out.println(useid);
			adao.adminSleepMem(useid);
		}
		
	}
	//회원디테일
	public void getAdminMemDetail(HashMap<String, Object> paramMap) { 
		adao.getAdminMemDetail(paramMap);
	}
	//Qna게시글 목록
	public void getAdminQnaList(HashMap<String, Object> paramMap) {
		HttpServletRequest request = (HttpServletRequest)paramMap.get("request");
	      HttpSession session = request.getSession();   
	      
	      if(request.getParameter("first")!=null) {
	         session.removeAttribute("page");
	         session.removeAttribute("key");
	      }
	      
	      int page = 1;
	      if (request.getParameter("page") != null) {
	         page = Integer.parseInt(request.getParameter("page"));
	         session.setAttribute("page", page);
	      } else if (session.getAttribute("page") != null) {
	         page = (Integer) session.getAttribute("page");
	      } else {
	         session.removeAttribute("page");
	      }
	      
	      String key="";
	      if (request.getParameter("key") != null) {
	         key = request.getParameter("key");
	         session.setAttribute("key", key);
	      } else if (session.getAttribute("key") != null) {
	         key = (String) session.getAttribute("key");
	      } else {
	         session.removeAttribute("key");
	      }
	      
	      // 페이징 객체 생성
	      Paging paging = new Paging();
	      paging.setPage(page);
	      HashMap<String, Object> cntMap = new HashMap<String, Object>();
	      cntMap.put("cnt", 0);
	      cntMap.put("tableName", 4); 
	      cntMap.put("key", key); 
	      adao.adminGetAllCount(cntMap);
	      int count = (Integer) cntMap.get("cnt"+""); 
	      paging.setTotalCount(count);
	      paging.paging();
	      
	      paramMap.put("key", key);
	      paramMap.put("startNum", paging.getStartNum() );
	      paramMap.put("endNum", paging.getEndNum() );
	      adao.getAdminQnaList(paramMap);
	      
	      paramMap.put("paging", paging);
		
	}
	//Qna게시글삭제
	public void adminDeleteQna(int[] num) {
		
		for(int qseq: num) {
			adao.adminDeleteQna(qseq);
		}
		
	}
	//Qna디테일
	public void getAdminQnaDetail(HashMap<String, Object> paramMap) {
		adao.getAdminQnaDetail(paramMap);
		
	}
	//Qna답변
	public void adminSaveReply(int qseq,String replyQna) {
		adao.adminSaveReply(qseq,replyQna);
		
	}
	public void getAdminReplyList(HashMap<String, Object> paramMap) {
		
		HttpServletRequest request = (HttpServletRequest)paramMap.get("request");
	      HttpSession session = request.getSession();   
	      
	      if(request.getParameter("first")!=null) {
	         session.removeAttribute("page");
	         session.removeAttribute("key");
	      }
	      
	      int page = 1;
	      if (request.getParameter("page") != null) {
	         page = Integer.parseInt(request.getParameter("page"));
	         session.setAttribute("page", page);
	      } else if (session.getAttribute("page") != null) {
	         page = (Integer) session.getAttribute("page");
	      } else {
	         session.removeAttribute("page");
	      }
	      
	      String key="";
	      if (request.getParameter("key") != null) {
	         key = request.getParameter("key");
	         session.setAttribute("key", key);
	      } else if (session.getAttribute("key") != null) {
	         key = (String) session.getAttribute("key");
	      } else {
	         session.removeAttribute("key");
	      }
	      
	      // 페이징 객체 생성
	      Paging paging = new Paging();
	      paging.setPage(page);
	      HashMap<String, Object> cntMap = new HashMap<String, Object>();
	      cntMap.put("cnt", 0);
	      cntMap.put("tableName", 3); 
	      cntMap.put("key", key); 
	      adao.adminGetAllCount(cntMap);
	      int count = (Integer) cntMap.get("cnt"+""); 
	      paging.setTotalCount(count);
	      paging.paging();
	      
	      paramMap.put("key", key);
	      paramMap.put("startNum", paging.getStartNum() );
	      paramMap.put("endNum", paging.getEndNum() );
	      adao.getAdminReplyList(paramMap);
	      
	      paramMap.put("paging", paging);
		
	}
	public void adminDeleteReply(int[] num) {
		
		for(int replyseq: num) {
			adao.adminDeleteReply(replyseq);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
