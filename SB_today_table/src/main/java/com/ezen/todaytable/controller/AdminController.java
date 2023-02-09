package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.Paging;
import com.ezen.todaytable.service.AdminService;

@Controller
public class AdminController {

	@Autowired
	AdminService as;
	//어드민 대쉬보드 이동
	@RequestMapping("/admin")
	public String admin() {
		return "admin/adminMain";
	}
	
	
	//멤버리스트 이동
	@RequestMapping(value="/memberList")
	   public ModelAndView memberList (HttpServletRequest request, Model model) {
	      
	      ModelAndView mav = new ModelAndView();
	      HttpSession session = request.getSession();
	      
	     // if(session.getAttribute("loginAdmin")==null) {
	    //     mav.setViewName("admin/adminLoginForm");
	     // }else {
	         
	         HashMap<String,Object> paramMap =new HashMap<String,Object>();
	         paramMap.put("request",request );
	         paramMap.put("ref_cursor", null);
	         
	         as.getAdminMemberList(paramMap);
	         
	         ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	         mav.addObject("paging",(Paging)paramMap.get("paging"));
	         mav.addObject("key", (String)paramMap.get("key"));
	         mav.addObject("membersList", list);
	         
	         mav.setViewName("admin/member/adminMemList");         
	   //   }
	      return mav;      
	   }
	
	//휴면회원 전환
	@RequestMapping(value="/adminSleepMem")
	public String adminSleepMem(@RequestParam("id") String [] id) {
		
		as.adminSleepMem(id);//(id로 멤버리스트를 조회했을때 useyn이 Y면 N/ N이면 Y로)
		
		return "redirect:/memberList?first=1"; //휴면회원 전환후 다시 멤버리스트로 이동
	}
	
	//회원디테일
	@RequestMapping(value="/adminMemDetail")
	   public ModelAndView adminMemDetail (HttpServletRequest request, Model model,
			   @RequestParam("id")String id) {
	      
	      ModelAndView mav = new ModelAndView();
	      HttpSession session = request.getSession();
	      
	     // if(session.getAttribute("loginAdmin")==null) {
	    //     mav.setViewName("admin/adminLoginForm");
	     // }else {
	         
	         HashMap<String,Object> paramMap =new HashMap<String,Object>();
	         paramMap.put("id",id );
	         paramMap.put("ref_cursor", null);
	         
	         as.getAdminMemDetail(paramMap);
	         ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	         mav.addObject("memberVO", list.get(0));
	         
	         mav.setViewName("admin/member/adminMemDetail");         
	   //   }
	      return mav;      
	   }
	//멤버리스트 이동
		@RequestMapping(value="/adminQnaList")
		   public ModelAndView adminQnaList (HttpServletRequest request, Model model) {
		      
		      ModelAndView mav = new ModelAndView();
		      HttpSession session = request.getSession();
		      
		     // if(session.getAttribute("loginAdmin")==null) {
		    //     mav.setViewName("admin/adminLoginForm");
		     // }else {
		         
		         HashMap<String,Object> paramMap =new HashMap<String,Object>();
		         paramMap.put("request",request );
		         paramMap.put("ref_cursor", null);
		         
		         as.getAdminQnaList(paramMap);
		         
		         ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		         mav.addObject("paging",(Paging)paramMap.get("paging"));
		         mav.addObject("key", (String)paramMap.get("key"));
		         mav.addObject("qnaList", list);
		         
		         mav.setViewName("admin/qna/adminQnaList");         
		   //   }
		      return mav;      
		   }
		
		//qna게시글삭제
		@RequestMapping("/adminDeleteQna")
		public String adminDeleteQna(@RequestParam("qseq") int [] qseq) {
			
			as.adminDeleteQna(qseq);
			
			return "redirect:/adminQnaList?first=1";
		}
		
		//qna디테일
		@RequestMapping(value="/adminQnaDetail")
		   public ModelAndView adminQnaDetail (HttpServletRequest request, Model model,
				   @RequestParam("qseq")int qseq) {
		      
		      ModelAndView mav = new ModelAndView();
		      HttpSession session = request.getSession();
		      
		     // if(session.getAttribute("loginAdmin")==null) {
		    //     mav.setViewName("admin/adminLoginForm");
		     // }else {
		         
		         HashMap<String,Object> paramMap =new HashMap<String,Object>();
		         paramMap.put("qseq",qseq );
		         paramMap.put("ref_cursor", null);
		         
		         as.getAdminQnaDetail(paramMap);
		         ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		         System.out.println("size :"+list.size());
		         mav.addObject("qnaVO", list.get(0));
		         
		         mav.setViewName("admin/qna/adminQnaDetail");         
		   //   }
		      return mav;      
		   }
		
		//Qna답변쓰기
		@RequestMapping("/adminSaveReply")
		public String adminSaveReply(@RequestParam("qseq") int qseq,
				@RequestParam("replyQna") String replyQna) {
			System.out.println(qseq + "/adminSaveReply에서 호출");
			as.adminSaveReply(qseq,replyQna);
			
			return "redirect:/adminQnaDetail?qseq="+qseq;
		}
	
	
	
	
	
	
	
	
	
	
}
