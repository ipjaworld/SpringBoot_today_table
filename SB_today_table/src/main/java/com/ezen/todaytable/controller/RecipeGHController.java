package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.RecipeVO;
import com.ezen.todaytable.service.RecipeService;

@Controller
public class RecipeGHController {

	@Autowired
	RecipeService rs;
	
	@RequestMapping("/recipeCategory")
	public ModelAndView recipeCategory(
			@RequestParam("status") String status,
			@RequestParam("page") int page,
			HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("ref_cursor", null);	// 카테고리에 해당하는 레시피vo 를 가져올 커서
		
		/** 버전 1
		//스위치문으로 다른 메서드를 서비스에서 호출하기
		switch(status) {
			case "recipe" : rs.getAllRecipe(paramMap); break;
			case "type" : rs.getTypeRecipe(paramMap); break;
			case "theme" : rs.getThemeRecipe(paramMap); break;
			case "ing" : rs.getIngRecipe(paramMap); break;
		}*/
		
		// 버전 2 - 컨트롤러, 서비스의 코드를 최소화하고 프로시져에서 if 문으로 분기점을 잡습니다.
		// 레시피 키라는 이름으로 웹페이지에서 status 라고 넘긴 값을 전송합니다. 이것을 이용해서 sql문에 접근합니다.
		paramMap.put("recipekey", status);
		rs.recipeCategory( paramMap );
		
		ArrayList<HashMap<String , Object>> recipeCategory
		= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor");
		//ArrayList<Integer> replyCountList = rs.countReply(recipeCategory);

		
		//RecipeVO rvo = (RecipeVO)recipeCategory.get(0);
		
		mav.addObject("RecipeCategory", recipeCategory);
		//mav.addObject("replyCountList", replyCountList);
		mav.addObject("total", recipeCategory.size());
		 
		mav.setViewName("recipe/recipeCategory");
		return mav;
		
	}
	
	
	@RequestMapping("/recipeFavoriteAndRec")
	public ModelAndView recipeFavoriteAndRec(
			@RequestParam("page") int page) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("ref_cursor", null); // 관리자 추천
		paramMap.put("ref_cursor2", null); 	// 단골레시피 상위권
		
		rs.recipeFavoriteAndRec( paramMap );
		
		ArrayList<HashMap<String , Object>> recommandList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor");
		ArrayList<HashMap<String , Object>> favoriteList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor2");
		
		mav.addObject("recommandList", recommandList);
		mav.addObject("favoriteList", favoriteList);
		
		mav.setViewName("recipe/recipeFavoriteAndRec");
		return mav;
	}
	
	
	@RequestMapping("/addReply")
	public String addreply(
			@RequestParam("rnum") int rnum,
			@RequestParam("reply") String reply
			) {
		
		rs.addReply( rnum, reply );
		
		return "recipeWithoutView?rnum="+rnum;
	}
	
	@RequestMapping("/updateReply")
	public ModelAndView updateReply(
			@RequestParam("rnum") int rnum
			) {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("redirect:/recipeWithoutView?rnum="+rnum);
		return mav;
	}
	
	@RequestMapping("/deleteReply")
	public String deleteReply(
			@RequestParam("rnum") int rnum
			) {

		return "redirect:/recipeWithoutView?rnum="+rnum;
	}
	
	@RequestMapping("/likeRecipe")
	public String likeRecipe(
			@RequestParam("rnum") int rnum
			) {
		
		return "redirect:/recipeWithoutView?rnum="+rnum;
	}
	
	@RequestMapping("/reportRecipe")
	public String reportRecipe(
			@RequestParam("rnum") int rnum
			) {
		return "redirect:/recipeWithoutView?rnum="+rnum;
	}
	
	
}
