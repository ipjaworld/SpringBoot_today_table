package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezen.todaytable.dto.MemberVO;
import com.ezen.todaytable.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	MemberService ms;
	
	@RequestMapping(value="/loginForm")
	public String loginForm() {
		return "member/login";
	}
	
	@RequestMapping(value="/login")
	public String login(@ModelAttribute("dto") @Valid MemberVO membervo, BindingResult result,  
			HttpServletRequest request, Model model) {
		
		String url = "member/login";
		
		if(result.getFieldError("id") !=null ) {
			model.addAttribute("message",result.getFieldError("id").getDefaultMessage());
		}else if(result.getFieldError("pwd") !=null ) {
			model.addAttribute("message",result.getFieldError("pwd").getDefaultMessage());
		}else {
			 
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id",membervo.getId() );
			paramMap.put("ref_cursor", null);
			ms.getMembersList(paramMap); 
			
		      ArrayList<HashMap<String, Object>> list
		         = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
		      
		      if( list.size() == 0) {
		         model.addAttribute("message", "아이디가 없습니다");
		         return "member/login";
		      }
		      HashMap<String, Object> mvo = list.get(0);
		      if( mvo.get("PWD") == null)
		         model.addAttribute("message", "비밀번호 오류. 관리자에게 문의하세요");
		      else if( !mvo.get("PWD").equals(membervo.getPwd()))
		         model.addAttribute("message", "비밀번호가 맞지않습니다");
		      else if( mvo.get("PWD").equals(membervo.getPwd())) {
		         HttpSession session = request.getSession();
		         session.setAttribute("loginUser", mvo);
		         url = "redirect:/";
		      }else if(mvo.get("USEYN").equals("N")) {
		    	  model.addAttribute("message", "휴면 계정입니다. 휴면계정을 복구하려면 관리자에게 문의하세요");
		      }
		     
		}
		 return url;
	}
	
	@RequestMapping("/contract")
	public String contract () {
		return "member/contract";
	}
	
	@RequestMapping("/joinForm")
	public String joinForm() {
		return "membe/joinForm";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session= request.getSession();
		session.removeAttribute("loginUser");
		return "redirec:/";
	}
	
	
	
	
	
	
}
