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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.Paging;
import com.ezen.todaytable.dto.QnaVO;
import com.ezen.todaytable.service.QnaService;

@Controller
public class QnaController {

	@Autowired
	QnaService qs;
	
	@RequestMapping("/qnaList")
	public ModelAndView qnaList(Model model, HttpServletRequest request, HttpSession session){

	ModelAndView mav = new ModelAndView();
	HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
	if ( loginUser == null) mav.setViewName("member/login");
	else{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("request", request);
		paramMap.put("ref_cursor",null);
		qs.listQna(paramMap);
		
		ArrayList<HashMap<String,Object>> list
		= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		
		mav.addObject("qnaList",list);
		mav.addObject("paging",(Paging)paramMap.get("paging"));
		mav.setViewName("qna/qnaList");
	
	}
		return mav;
	}
	
	@RequestMapping("/passCheck")
	public ModelAndView passCheck( @RequestParam("qseq")int qseq) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("qseq",qseq);
		mav.setViewName("qna/checkPass");
		
		return mav;
	}
	
	@RequestMapping(value="/qnaCheckPass", method=RequestMethod.POST)
	public ModelAndView qnapassCheck( 
			@RequestParam("qseq")int qseq,
			@RequestParam("pass")String pass) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("qseq", qseq);
		paramMap.put("ref_cursor", null);
		qs.getQna(paramMap);
		
		ArrayList<HashMap<String,Object>> list
		= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		
		HashMap<String,Object> qvo = list.get(0);
		mav.addObject("qseq",qseq);
		if ( qvo.get("PASS").equals(pass)){
			mav.setViewName("qna/checkPassSuccess");
		}else {
			mav.addObject("message","비밀번호가 맞지 않습니다");
			mav.setViewName("qna/checkPass");
		}
		return mav;
	}
	
	@RequestMapping(value="/qnaView")
	public ModelAndView qnaView( HttpServletRequest request,
			@RequestParam("qseq")int qseq ) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
		if ( loginUser == null) mav.setViewName("member/login");
		else{
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("qseq", qseq);
			paramMap.put("ref_cursor",null);
			qs.getQna(paramMap);
			ArrayList<HashMap<String,Object>> list
			= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
			mav.addObject("qnaVO",list.get(0));
			mav.setViewName("qna/qnaView");
		}
		return mav;
	}
	
	@RequestMapping(value="/qnaWriteForm")
	public String qnaWriteForm (	HttpSession session){
	
		HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
		if ( loginUser == null) return "member/login";				
		return "qna/qnaWrite";
	}
	
	@RequestMapping("qnaWrite")
	   public ModelAndView qna_write(
	         @ModelAttribute("dto") @Valid QnaVO qnavo,
	         @RequestParam(value="pass", required=false)String pass,
	         @RequestParam(value="secret", required=false) String secret,
	         BindingResult result, HttpServletRequest request) {
	      
	      ModelAndView mav = new ModelAndView();
	      HttpSession session = request.getSession();
	         HashMap<String, Object>loginUser 
	         = (HashMap<String, Object>)session.getAttribute("loginUser");
	         
	         if (loginUser == null){
	            mav.setViewName("member/login");
	         }else{
	            if(result.getFieldError("subject")!=null)
	               mav.addObject("message", result.getFieldError("subject").getDefaultMessage());
	            else if(result.getFieldError("content")!=null)
	               mav.addObject("message", result.getFieldError("content").getDefaultMessage());
	            else {
	            	System.out.println("secret:" + secret+", pass:"+pass);
	               if( secret == null) {
	            	   secret="N";
	                   pass="1234";
	               }
	               HashMap<String, Object>paramMap = new HashMap<String, Object>();
	               qnavo.setId((String)loginUser.get("ID") );
	               paramMap.put("qnavo", qnavo);
	               paramMap.put("secret", secret);
	               paramMap.put("pass", pass);
	               qs.insertQna(paramMap);
	               mav.setViewName("redirect:/qnaList");
	            }     
	         }
	         return mav;
	   }
}
