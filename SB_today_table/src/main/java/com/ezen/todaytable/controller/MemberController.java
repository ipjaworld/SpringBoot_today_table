package com.ezen.todaytable.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
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

import com.ezen.todaytable.dto.MemberVO;
import com.ezen.todaytable.service.MemberService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class MemberController {

	@Autowired
	MemberService ms;
	
	
	
	@RequestMapping(value="/loginForm")
	public String loginForm() {
		return "member/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
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
		         url = "index";
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
		return "member/joinForm";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session= request.getSession();
		session.removeAttribute("loginUser");
		return "redirect:/";
	}
	
	
	@RequestMapping("idCheckForm")
	public String id_check_form(HttpServletRequest request, 
			Model model, 
			@RequestParam("id") String id /*전달 받은 id*/) {
		// getMemberShop 메서드를 호출해서 전달된 아이디의 유무를 확인하고 member/check.jsp로 
		// 결과와 아이디를 갖고 이동합니다.
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id",id);
		paramMap.put("ref_cursor", null);
		ms.getMembersList(paramMap); // 아이디로 검색해서 나온 결과값
		
		  ArrayList<HashMap<String, Object>> list
	         = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
		  
		if(list.size()==0) { // list.size()와 list.get(0) 느낌이 다르다?
			model.addAttribute("result", -1);
		}else {
			model.addAttribute("result",1);
		}
		model.addAttribute("id", id);
		
		return "member/idcheck";
	}
	@Autowired
	ServletContext context;
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	   public ModelAndView join(
	            @ModelAttribute("dto") @Valid MemberVO membervo, BindingResult result,
	            @RequestParam(value="reid", required=false) String reid,
	            @RequestParam(value="pwdCheck", required=false) String pwdCheck,
	            HttpServletRequest request
	         ) {
	      
	      ModelAndView mav = new ModelAndView();
	      mav.setViewName("member/joinForm");
	      
	      // 매개변수 구성 + 밸리데이션 구성 + 회원가입 실행
	      // 회원가입 프로시져 이름 insertMemberShop
	      if(result.getFieldError("id")!=null)
	         mav.addObject("message", result.getFieldError("id").getDefaultMessage() );
	      else if(result.getFieldError("pwd")!=null)
	         mav.addObject("message", result.getFieldError("pwd").getDefaultMessage() );
	      else if(result.getFieldError("name")!=null)
	         mav.addObject("message", result.getFieldError("name").getDefaultMessage() );
	      else if(result.getFieldError("nick")!=null)
		         mav.addObject("message", result.getFieldError("nick").getDefaultMessage() );
	      else if(result.getFieldError("email")!=null)
	         mav.addObject("message", result.getFieldError("email").getDefaultMessage() );
	      else if(result.getFieldError("phone")!=null)
	         mav.addObject("message", result.getFieldError("phone").getDefaultMessage() );
	      else if( reid == null || ( reid != null && !reid.equals(membervo.getId() )) )  
	         mav.addObject("message", "아이디 중복체크를 하지 않으셨습니다.");
	      else if( pwdCheck == null || ( pwdCheck != null && !pwdCheck.equals(membervo.getPwd() ) ) )
	         mav.addObject("message", "비밀번호 확인이 일치하지 않습니다." );
	      else {
	         ms.insertMemberttable( membervo );
	         mav.addObject("message", "회원가입이 완료되었습니다. 로그인하세요" );
	         mav.setViewName("member/login");
	      }
	      return mav;
	   }
	
	@RequestMapping(value="/updateMemForm")
	public String updateMemForm () {
		return "member/updateMemForm";
	}
	 @RequestMapping(value="/memberUpdate",method=RequestMethod.POST)
	   public ModelAndView memberUpdate(HttpServletRequest request,
			   @ModelAttribute("dto") @Valid MemberVO membervo, BindingResult result,
	            @RequestParam(value="pwdCheck", required=false) String pwdCheck
	            ) {
		  String savepath = context.getRealPath("/imageProfile");
			HashMap<String,Object> paramMap =new HashMap<String, Object>();
	 
		   ModelAndView mav = new ModelAndView();
		   try {
				MultipartRequest multi = new MultipartRequest(
						request,savepath,5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
						);
		   mav.setViewName("member/memberUpdateForm");
		  if(result.getFieldError("pwd")!=null)
		        mav.addObject("message", result.getFieldError("pwd").getDefaultMessage() );
		      else if(result.getFieldError("name")!=null)
		         mav.addObject("message", result.getFieldError("name").getDefaultMessage() );
		      else if(result.getFieldError("nick")!=null)
			         mav.addObject("message", result.getFieldError("nick").getDefaultMessage() );
		      else if(result.getFieldError("email")!=null)
		         mav.addObject("message", result.getFieldError("email").getDefaultMessage() );
		      else if(result.getFieldError("phone")!=null)
		         mav.addObject("message", result.getFieldError("phone").getDefaultMessage() );
		      else if( pwdCheck == null || ( pwdCheck != null && !pwdCheck.equals(membervo.getPwd() ) ) )
		         mav.addObject("message", "비밀번호 확인이 일치하지 않습니다." );
		      else {
		    	  
		    	  paramMap.put("ID",membervo.getId());
		    	  paramMap.put("PWD",membervo.getPwd());
		    	  paramMap.put("NAME",membervo.getName());
		    	  paramMap.put("NICK",membervo.getNick());
		    	  paramMap.put("EMAIL",membervo.getEmail());
		    	  paramMap.put("PHONE",membervo.getPhone());
		    	  paramMap.put("ZIP_NUM",membervo.getZip_num());
		    	  paramMap.put("ADDRESS1",membervo.getAddress1());
		    	  paramMap.put("ADDRESS2",membervo.getAddress2());
		    	  paramMap.put("ADDRESS3",membervo.getAddress3());
		    	  
		    	  if(multi.getFilesystemName("image")==null) {
						paramMap.put("image", multi.getParameter("oldfilenmae"));// 수정하려는 이미지가 없을 경우 이전 이미지 적용
					}else {
						paramMap.put("image", multi.getFilesystemName("image"));
					}
		         ms.updateMemberttable( paramMap );
		         HttpSession session = request.getSession();
		         session.setAttribute("loginUser", paramMap);
		         mav.setViewName("redirect:/");
		      		}
		     
		   }catch (IOException e) {
				e.printStackTrace();
			}
		   return mav;
	 }
}
