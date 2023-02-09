package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.service.RecipeService;

@Controller
public class RecipeController {

	@Autowired
	RecipeService rs;
	
	
	@RequestMapping("/recipeDetailView")
	public ModelAndView recipeDetailView(@RequestParam("rnum") int rnum) {
		ModelAndView mav = new ModelAndView();
		
		// paramMap으로 1,2,3 전달받기
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("rnum", rnum);
		
		// 1. recipe 전달받는 cursor
		paramMap.put("ref_cursor1", null);
		// 2. 재료 정보 (service에서 배열 합친 후 controller로 하나의 배열로 전달) (또는 controller에서 작업)
		paramMap.put("ref_cursor2", null);
		paramMap.put("ref_cursor3", null);
		ArrayList<String> ingArray = (ArrayList<String>) paramMap.get("ref_cursor2"); 
		ArrayList<String> quanArray = (ArrayList<String>) paramMap.get("ref_cursor3"); 
		ArrayList<String> exArray = new ArrayList<String>();
		String str = "";
		for(int i=0; i<ingArray.size(); i++) { // tag + quantity를 하나의 문자열로
			str = (ingArray.get(i) + " " + quanArray.get(i) + " ");
			exArray.add(i, str);
		}
		paramMap.put("exArray", exArray);
		// 3. processImages 
		paramMap.put("ref_cursor4", null);
		// 4. 댓글 리스트
		paramMap.put("ref_cursor5", null);
		
		rs.recipeDetailView(paramMap);
		
		
		mav.addObject("selectedRecipeInfo", paramMap.get("ref_cursor1"));
		mav.addObject("processImgs",paramMap.get("ref_cursor3"));
		mav.addObject("Ings", paramMap.get("exArray"));
		mav.addObject("replyList", paramMap.get("ref_cursor4"));
		mav.addObject("rnum", paramMap.get("rnum"));
		mav.setViewName("recipe/recipeDetail");
		return mav;
	}
	
	
	
	
	
}
