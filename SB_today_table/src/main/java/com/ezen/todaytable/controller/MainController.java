package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
		
		paramMap.put("ref_cursor", null);
		paramMap.put("ref_cursor2", null);
		paramMap.put("ref_cursor3", null);
		
		ms.startIndex(paramMap);
		
		ArrayList<HashMap<String , Object>> allList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor");
		ArrayList<HashMap<String , Object>> typeList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor2");
		ArrayList<HashMap<String , Object>> bannerList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor3");
		
		mav.addObject("allList", allList);
		mav.addObject("typeList", typeList);
		mav.addObject("bannerList", bannerList);
		
		
		mav.setViewName("index");
		return mav;
	}
}
