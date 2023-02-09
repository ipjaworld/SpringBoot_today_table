package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
		
		// 3. processImages 
		paramMap.put("ref_cursor4", null);
		// 4. 댓글 리스트
		paramMap.put("ref_cursor5", null);
		
		
		/*
		// 1. recipe 전달받는 cursor
		paramMap.put("ref_cursor1", null);
		// 2. 재료 정보 (service에서 배열 합친 후 controller로 하나의 배열로 전달) (또는 controller에서 작업)
		paramMap.put("ing", null);
		
		// 3. processImages 
		paramMap.put("ref_cursor2", null);
		// 4. 댓글 리스트
		paramMap.put("ref_cursor3", null);
		*/
		
		rs.recipeDetailView(paramMap);
		
		ArrayList<HashMap<String, Object>> ingArray = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor2"); 
		System.out.println("전달된 paramMap의 ingArray : " + paramMap.get("ref_cursor2"));
		ArrayList<HashMap<String, Object>> qtyArray = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor3"); 
		/*
		ArrayList<String> exArray = new ArrayList<String>();
		String str = "";
		for(int i=0; i<ingArray.size(); i++) { // tag + quantity를 하나의 문자열로
			System.out.println("ingArray.get(i) : " + ingArray.get(i));
			System.out.println("qtyArray.get(i) : " + qtyArray.get(i));
			str = (ingArray.get(i) + " " + qtyArray.get(i) + " ");
			exArray.add(i, str);
		}
		System.out.println("exArray : " + exArray);
		paramMap.put("exArray", exArray);
		*/
		
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor1");
		mav.addObject("recipeVO", list.get(0));
		// mav.addObject("Ings", paramMap.get("exArray"));
		mav.addObject("ingArray", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor2"));
		mav.addObject("qtyArray", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor3"));
		mav.addObject("processImgs", (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor4"));
		System.out.println("processImgs : " + (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor4"));
		mav.addObject("replyList", (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor5"));
		mav.addObject("rnum", paramMap.get("rnum"));
		mav.setViewName("recipe/recipeDetail");
		return mav;
	}
	
	
	
	
	
}
