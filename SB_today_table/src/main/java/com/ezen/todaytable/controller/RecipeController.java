package com.ezen.todaytable.controller;

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
	
	@RequestMapping("/recipeCategory")
	public ModelAndView recipeCategory(
			@RequestParam("status") String status,
			@RequestParam("page") int page,
			HttpServletRequest request,
			Model model
			) {

		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("ref_cursor", null);
		
		// 버전 1
		//스위치문으로 다른 메서드를 서비스에서 호출하기
		switch(status) {
			case "recipe" : rs.getAllRecipe(paramMap); break;
			case "type" : rs.getTypeRecipe(paramMap); break;
			case "theme" : rs.getThemeRecipe(paramMap); break;
			case "ing" : rs.getIngRecipe(paramMap); break;
		}
		
		// 버전 2 - 
		/** 레시피 키라는 이름으로 웹페이지에서 status 라고 넘긴 값을 전송합니다. 이것을 이용해서 sql문에 접근합니다.
		 * paramMap.put("recipekey", status);
			rs.recipeCategory( paramMap );
		 * */
		
		
		
		
		
		mav.setViewName("recipe/recipeCategory");
		return mav;
	}
	
}
