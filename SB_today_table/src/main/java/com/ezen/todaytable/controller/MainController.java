package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.Paging;
import com.ezen.todaytable.service.MainService;
import com.ezen.todaytable.service.RecipeService;

@Controller
public class MainController {
	
	@Autowired
	MainService ms;
	
	@Autowired
	RecipeService rs;
	
	@RequestMapping("/")
	public ModelAndView main() {
		ModelAndView mav = new ModelAndView();
		
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("recipekey", "recipe");
		paramMap.put("ref_cursor", null);
		paramMap.put("ref_cursor3", null);
		
		rs.recipeCategory(paramMap);
		ms.getRecipeBanner(paramMap);
		
		ArrayList<HashMap<String , Object>> allList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor");
		ArrayList<HashMap<String , Object>> bannerList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor3");
		
		mav.addObject("allList", allList);
		mav.addObject("bannerList", bannerList);
		
		mav.setViewName("index");
		return mav;
	}
	
	
	@RequestMapping("/announcement")
	public String announcement() {
		return "main/announcement";
	}
	
	@RequestMapping("/ourstory")
	public String ourstory() {
		return "main/ourstory";
	}
	
	@RequestMapping("/privatePolicy")
	public String privatePolicy() {
		return "main/privatePolicy";
	}
	
	@RequestMapping("/terms")
	public String terms() {
		return "main/terms";
	}
	
	@RequestMapping("/myPageView")
	public String myPageView() {
		return "mypage/mypage";
	}
	
	
	@RequestMapping("/myRecipeList")
	public String myRecipeList() {
		return "myRecipeListForm";
	}

	@RequestMapping("myRecipeListForm")
	public ModelAndView myRecipeListForm(HttpServletRequest request,Model model) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginUser")==null) {
		       mav.setViewName("loginForm");
		     }else {
		         
		         HashMap<String,Object> paramMap =new HashMap<String,Object>();
		         paramMap.put("request",request );
		         paramMap.put("ref_cursor", null);
		         
		        // ms.getMyRecipeListttable(paramMap);
		         
		         ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		         mav.addObject("paging",(Paging)paramMap.get("paging"));
		         mav.addObject("key", (String)paramMap.get("key"));
		         mav.addObject("membersList", list);
		         
		         mav.setViewName("mypage/myRecipeList");         
		      }
		
		
		return mav;
	}
	
}
