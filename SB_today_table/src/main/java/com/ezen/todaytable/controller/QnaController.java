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
	
	// qna 리스트
	@RequestMapping("/qnaList")
	public ModelAndView qnaList(HttpServletRequest request, HttpSession session,
			@RequestParam("refer") String refer){
		
		ModelAndView mav = new ModelAndView();
		//HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
		//if ( loginUser == null) mav.setViewName("member/login");
		if(refer.equals("m")) {
			mav.setViewName("redirect:/myqnaList");
		}else {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
				
		//paramMap.put("id",loginUser.get("ID"));
		paramMap.put("id","scott");
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
	
	// qna 게시글 보기
	@RequestMapping("/qnaDetail")
	public ModelAndView qnaDetail(HttpServletRequest request, HttpSession session,
			@RequestParam("qseq") int qseq,
			@RequestParam("refer") String refer){

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
			mav.addObject("refer", refer);
			mav.addObject("qnaVO",list.get(0));
			mav.setViewName("qna/qnaDetail");
		
	//}
			return mav;
		}
	
	// 나의 qna list 보기
	@RequestMapping("/myqnaList")
	public ModelAndView myqnaList(HttpServletRequest request, HttpSession session){

	ModelAndView mav = new ModelAndView();
	//HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
	//if ( loginUser == null) mav.setViewName("member/login");
	//else{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		//paramMap.put("id",loginUser.get("ID"));
		paramMap.put("id","scott");
		paramMap.put("request", request);
		paramMap.put("ref_cursor",null);
		qs.mylistQna(paramMap);
		
		ArrayList<HashMap<String,Object>> list
		= (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		
		mav.addObject("myqnaList",list);
		mav.addObject("paging",(Paging)paramMap.get("paging"));
		mav.setViewName("qna/myQnaList");
	
	//}
		return mav;
	}
	
	// qna 글쓰기폼이동
	@RequestMapping(value="/qnaWriteForm")
	public String qna_writre_form( HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		// HashMap<String, Object> loginUser 
		//	= (HashMap<String, Object>) session.getAttribute("loginUser");
		// if( loginUser == null ) return "member/login";
		
	    return "qna/qnaWriteForm";
	}
	
	// qna 글쓰기
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
			if( qnavo.getSecret()==null) {
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

	// qna 업데이트 폼 이동
	@RequestMapping(value="/qnaUpdateForm")
	public ModelAndView qnaUpdateForm(HttpSession session,
				@RequestParam("qseq") int qseq,
				@RequestParam("refer") String refer){
	
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
			
			mav.addObject("refer",refer);
			mav.addObject("qnaVO",list.get(0));
			mav.setViewName("qna/qnaUpdateForm");
	//}
			return mav;
		}
	
	// qna 수정 작업
	@RequestMapping(value="/qnaUpdate")
	public String qnaUpdate( @ModelAttribute("dto") @Valid QnaVO qnavo,
			BindingResult result,  HttpServletRequest request,
			@RequestParam("refer") String refer) {
		
		
		//HashMap<String, Object> loginUser = (HashMap<String, Object>)session.getAttribute("loginUser");
		//if ( loginUser == null) return "member/login";
		//else{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("qnaVO", qnavo);
		
		qs.qnaUpdate(paramMap);
//}
		
		return "redirect:/qnaDetail?qseq="+qnavo.getQseq()+"&refer="+refer;
	}
	
	// qna게시글삭제
	@RequestMapping("/deleteQna")
	public String deleteQna(@RequestParam("qseq") int qseq,
		@RequestParam("refer") String refer) {

		String url="";
		qs.deleteQna(qseq);
		if(refer.equals("q")){
			url= "redirect:/qnaList";			
		}else {
			url= "redirect:/myqnaList";		
		}
		
		return url;
	}
	
}
	

