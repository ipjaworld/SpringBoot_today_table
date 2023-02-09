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
	         
	         as.getMemberList(paramMap);
	         
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
