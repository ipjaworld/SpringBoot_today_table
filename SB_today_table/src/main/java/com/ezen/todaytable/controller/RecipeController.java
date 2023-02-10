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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.ProcessImgVO;
import com.ezen.todaytable.dto.RecipeFormVO;
import com.ezen.todaytable.service.RecipeService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class RecipeController {

	@Autowired
	RecipeService rs;
	
	
	@RequestMapping("/recipeDetailView")
	public ModelAndView recipeDetailView(@RequestParam("rnum") int rnum) {
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rnum", rnum);
		
		
		// 1. recipe 전달받는 cursor
		paramMap.put("ref_cursor1", null);
		// 2. 재료 정보
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
	
	@RequestMapping("/recipeDetailWithoutView")
	public ModelAndView recipeDetailWithoutView(@RequestParam("rnum") int rnum) {
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rnum", rnum);
		// 1. recipe 전달받는 cursor
		paramMap.put("ref_cursor1", null);
		// 2. 재료 정보
		paramMap.put("ref_cursor2", null);
		paramMap.put("ref_cursor3", null);
		// 3. processImages 
		paramMap.put("ref_cursor4", null);
		// 4. 댓글 리스트
		paramMap.put("ref_cursor5", null);
		rs.recipeDetailWithoutView(paramMap);
		
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor1");
		mav.addObject("recipeVO", list.get(0));
		mav.addObject("ingArray", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor2"));
		mav.addObject("qtyArray", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor3"));
		mav.addObject("processImgs", (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor4"));
		mav.addObject("replyList", (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor5"));
		mav.addObject("rnum", paramMap.get("rnum"));
		mav.setViewName("recipe/recipeDetail");
		return mav;
		
	}
	
	@RequestMapping("/deleteRecipe")
	public String deleteRecipe(@RequestParam("rnum") int rnum, HttpServletRequest request) {
		String url = "recipe/recipeList";
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser") == null) url = "member/loginForm";
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rnum", rnum);
		rs.deleteRecipe(paramMap);
		return url;
	}
	
	@RequestMapping("/recipeForm")
	public String recipeForm(HttpServletRequest request) {
		String url = "recipe/recipeForm";
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser") == null) url = "member/loginForm";
		return url;
	}
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(value="thumbnailUp", method=RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> thumbnailUp(Model model, HttpServletRequest request) {
		String path = context.getRealPath("/imageRecipe");
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			MultipartRequest multi = new MultipartRequest(
					request, path, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			result.put("STATUS", 1);
			result.put("FILENAME", multi.getFilesystemName("thumbnail") );
			System.out.println("thumbnail의 이름 : " + multi.getFilesystemName("thumbnail") );
		} catch (IOException e) { e.printStackTrace();
		}
		
		return result;
	}
	
	@RequestMapping(value="processImgUp", method=RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> processImgUp(Model model, HttpServletRequest request) {
		String path = context.getRealPath("/imageRecipe");
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			MultipartRequest multi = new MultipartRequest(
					request, path, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			result.put("STATUS", 1);
			result.put("FILENAME", multi.getFilesystemName("processImg") );
			System.out.println("processImg의 이름 : " + multi.getFilesystemName("processImg") );
		} catch (IOException e) { e.printStackTrace();
		}
		
		return result;
	}

	/*
	@RequestMapping(value="writeRecipe", method=RequestMethod.POST)
	public ModelAndView writeRecipe( HttpServletRequest request,
			@RequestParam("") String 
	 	) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		
		return mav;
	}
	*/
	
	@RequestMapping(value="writeRecipe", method=RequestMethod.POST)
	public ModelAndView writeRecipe(
			@ModelAttribute("rvo") @Valid RecipeFormVO recipeformvo, BindingResult result, 
			HttpServletRequest request, 
			@RequestParam("count") int count) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		System.out.println("writeRecipe 도착");
		
		if(result.getFieldError("subject") != null) {
			mav.addObject("message", result.getFieldError("subject").getDefaultMessage());
			System.out.println("message : " + result.getFieldError("subject").getDefaultMessage());
		}
		else if(result.getFieldError("content") != null)
			mav.addObject("message", result.getFieldError("content").getDefaultMessage());
		else if(result.getFieldError("thumbnail") != null)
			mav.addObject("message", result.getFieldError("thumbnail").getDefaultMessage());
		else if(result.getFieldError("checkIng") != null)
			mav.addObject("message", result.getFieldError("checkIng").getDefaultMessage());
		else {
		paramMap.put("id", recipeformvo.getId());
		System.out.println("recipeformvo로 전달된 id : " + recipeformvo.getId());
		// paramMap.put("nick", recipeformvo.getNick());
		paramMap.put("subject", recipeformvo.getSubject());
		paramMap.put("content", recipeformvo.getContent());
		paramMap.put("cookingtime", recipeformvo.getCookingTime());
		paramMap.put("thumbnail", "imageRecipe/"+recipeformvo.getThumbnail());
		paramMap.put("checkIng", recipeformvo.getCheckIng());
		paramMap.put("type", recipeformvo.getType());
		paramMap.put("theme", recipeformvo.getTheme());
		paramMap.put("count", count);
		
		// processImgs와 processDetail들의 수는 미정이어서 우선 request.getParameter 사용
		ArrayList<ProcessImgVO> processList = new ArrayList<ProcessImgVO>();
		for(int i=0; i<count; i++) {
			ProcessImgVO pvo = new ProcessImgVO();
			String fileName = request.getParameter("processImg"+(i+1));
			if(fileName==null || fileName.equals(""))
				pvo.setLinks("imageRecipe/cookingTimer.png");
			else pvo.setLinks("imageRecipe/" + fileName);
			System.out.println("fileName : " + fileName);
			pvo.setIseq(i+1);
			String detail = request.getParameter("processDetail"+ (i+1));
			if(detail == null || detail.equals("")) {
				System.out.println("detail이 null인 경우 : " + detail);
				pvo.setDescription("요리 과정을 입력하지 않았어요.");
			}
			else {
				System.out.println(detail);
				pvo.setDescription(detail);
			}
			processList.add(pvo);
		}
		
		paramMap.put("processList", processList);
		paramMap.put("max_rnum", null);
		paramMap.put("lastTagId", 0);
		// paramMap.put("max_rnum", null);
		rs.insertRecipe(paramMap);
		System.out.println("max_rnum : service 거친 후 : " + Integer.parseInt(String.valueOf(paramMap.get("max_rnum"))));
		rs.insertProcessIng(paramMap);
		mav.setViewName("recipe/recipeList");
		}
		return mav;
	}
	
	
	
	
	
}
