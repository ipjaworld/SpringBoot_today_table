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
	public ModelAndView qnaList(HttpServletRequest request, HttpSession session){

	ModelAndView mav = new ModelAndView();
	//HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
	//if ( loginUser == null) mav.setViewName("member/login");
	//else{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("request", request);
		paramMap.put("ref_cursor",null);
		qs.listQna(paramMap);
		
		ArrayList<HashMap<String,Object>> list
		= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		
		mav.addObject("qnaList",list);
		mav.addObject("paging",(Paging)paramMap.get("paging"));
		mav.setViewName("qna/qnaList");
	
//}
		return mav;
	}
	
	@RequestMapping("/qnaDetail")
	public ModelAndView qnaDetail(HttpServletRequest request, HttpSession session,
			@RequestParam("qseq") int qseq){

		ModelAndView mav = new ModelAndView();
		//HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
		//if ( loginUser == null) mav.setViewName("member/login");
		//else{
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("qseq", qseq);
			paramMap.put("ref_cursor",null);
			qs.oneQna(paramMap);
			
			ArrayList<HashMap<String,Object>> list
			= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
			
			mav.addObject("qnaVO",list.get(0));
			mav.setViewName("qna/qnaDetail");
		
	//}
			return mav;
		}
	
	@RequestMapping("/myqnaList")
	public ModelAndView myqnaList(HttpServletRequest request, HttpSession session){

	ModelAndView mav = new ModelAndView();
	//HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
	//if ( loginUser == null) mav.setViewName("member/login");
	//else{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("request", request);
		paramMap.put("ref_cursor",null);
		qs.listQna(paramMap);
		
		ArrayList<HashMap<String,Object>> list
		= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		
		mav.addObject("qnaList",list);
		mav.addObject("paging",(Paging)paramMap.get("paging"));
		mav.setViewName("qna/myqnaList");
	
//}
		return mav;
	}
	
	@RequestMapping(value="/qnaWriteForm")
	public String qna_writre_form( HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		// HashMap<String, Object> loginUser 
		//	= (HashMap<String, Object>) session.getAttribute("loginUser");
		// if( loginUser == null ) return "member/login";
		
	    return "qna/qnaWriteForm";
	}
	
	@RequestMapping("qnaWrite")
	public ModelAndView qna_write( @ModelAttribute("dto") @Valid QnaVO qnavo,
			BindingResult result,  HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		// HttpSession session = request.getSession();
		// HashMap<String, Object> loginUser 
		// 	= (HashMap<String, Object>) session.getAttribute("loginUser");
		//	mav.setViewName("qnaWriteForm");
	   //  if (loginUser == null) 
	   // 	mav.setViewName("member/login");
	    // else {
			if(qnavo.getSecret()==null) {
				qnavo.setSecret("0");
				qnavo.setQnapass("");
			}
    		HashMap<String, Object> paramMap = new HashMap<String, Object>();
	    	//	qnavo.setId( (String)loginUser.get("ID") );
    		paramMap.put("qnavo", qnavo);
    		qs.insertQnas( paramMap );
			mav.setViewName("redirect:/qnaList");
	    	
	    return mav;
	}
}
