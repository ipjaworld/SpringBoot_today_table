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

import com.ezen.todaytable.dto.Paging;
import com.ezen.todaytable.dto.ProcessImgVO;
import com.ezen.todaytable.dto.RecipeFormVO;
import com.ezen.todaytable.dto.ReplyVO;
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
		String url = "redirect:/recipeCategory?status=recipe&page=1";
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
	
	@RequestMapping(value="editImg", method=RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> editImg(Model model, HttpServletRequest request) {
		System.out.println("editImg 도착 ");
		String path = context.getRealPath("/imageRecipe");
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			MultipartRequest multi = new MultipartRequest(
					request, path, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			result.put("STATUS", 1);
			result.put("FILENAME", multi.getFilesystemName("editImg") );
			System.out.println("processImg의 이름 : " + multi.getFilesystemName("editImg") );
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
	
	@RequestMapping(value="/writeRecipe", method=RequestMethod.POST)
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
		// paramMap.put("nick", recipeformvo.getNick());
		paramMap.put("subject", recipeformvo.getSubject());
		paramMap.put("content", recipeformvo.getContent());
		paramMap.put("cookingtime", recipeformvo.getCookingTime());
		paramMap.put("thumbnail", "imageRecipe/"+recipeformvo.getThumbnail());
		paramMap.put("checkIng", recipeformvo.getCheckIng());
		paramMap.put("type", recipeformvo.getType());
		paramMap.put("theme", recipeformvo.getTheme());
		paramMap.put("ing", recipeformvo.getIng());
		System.out.println("컨트롤러로 도착한 ing : " + recipeformvo.getIng());
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
		
		// paramMap.put("lastTagId", 0);
		// paramMap.put("max_rnum", null);
		rs.insertRecipe(paramMap);
		rs.insertProcessIng(paramMap);
		System.out.println("processIng 성공");
		mav.setViewName("redirect:/recipeCategory?status=recipe&page=1");
		}
		return mav;
	}
	
	@RequestMapping("/recipeUpdateForm")
	public ModelAndView recipeUpdateForm(HttpServletRequest request, @RequestParam("rnum") int rnum) {
		ModelAndView mav = new ModelAndView();
		System.out.println("recipeUpdateForm controller 도착");
		String url = "recipe/recipeUpdateForm";
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser") == null) url = "member/loginForm";
		else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("rnum", rnum);
			// 1. recipe 전달받는 cursor
			paramMap.put("ref_cursor1", null);
			// 2. 재료 정보
			paramMap.put("ref_cursor2", null);
			paramMap.put("ref_cursor3", null);
			// 3. processImages 
			paramMap.put("ref_cursor4", null);
			// 4. 댓글 리스트(받아오지만 전송 X)
			paramMap.put("ref_cursor5", null);
			rs.recipeDetailWithoutView(paramMap);
			
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor1");
			mav.addObject("recipeVO", list.get(0));
			mav.addObject("ingArray", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor2"));
			mav.addObject("qtyArray", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor3"));
			mav.addObject("processImgs", (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor4"));
			// mav.addObject("rnum", paramMap.get("rnum"));
		}
		
		mav.setViewName(url);
		return mav;
	}
	
	/*
	@RequestMapping(value="updateRecipe", method=RequestMethod.POST)
	public ModelAndView updateRecipe(@RequestParam(value="id", required=false) String id, @RequestParam("count") int count, @RequestParam(value="rnum", required=false) int rnum, 
			@RequestParam("subject") String subject, @RequestParam(value="thumbnail", required=false) String thumbnail, @RequestParam("type") int type, @RequestParam("theme") String theme, 
			@RequestParam("checkIng") String checkIng, @RequestParam("cookingTime") int cookingTime, @RequestParam("content") String content, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		System.out.println("id : " + id + "rnum : " + rnum + "thumbnail : " + thumbnail);
		System.out.println("updateRecipe 도착");
		paramMap.put("id", id);
		paramMap.put("subject", subject);
		paramMap.put("content", content);
		paramMap.put("time", cookingTime);
		paramMap.put("thumbnail", "imageRecipe/"+thumbnail);
		paramMap.put("checkIng", checkIng);
		paramMap.put("type", type);
		paramMap.put("theme", theme);
		paramMap.put("count", count);
		paramMap.put("rnum", rnum);
		
		// processImgs와 processDetail들의 수는 미정이어서 우선 request.getParameter 사용
		ArrayList<ProcessImgVO> processList = new ArrayList<ProcessImgVO>();
		for(int i=0; i<count; i++) {
			ProcessImgVO pvo = new ProcessImgVO();
			String fileName = request.getParameter("processImg"+(i+1));
			if(fileName==null || fileName.equals(""))
				pvo.setLinks("imageRecipe/cookingTimer.png");
			else pvo.setLinks(fileName);
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
		// paramMap.put("max_rnum", null);
		
		// paramMap.put("lastTagId", 0);
		rs.updateRecipe(paramMap);
		// rs.updateProcessIng(paramMap);
		System.out.println("processIng 성공");
		mav.setViewName("redirect:/recipeDetailWithoutView?rnum="+rnum);
		
		
		return mav;
	}
	*/
	
	@RequestMapping(value="/updateRecipe", method=RequestMethod.POST)
	public ModelAndView updateRecipe(@ModelAttribute @Valid RecipeFormVO recipeformvo, BindingResult result, 
			HttpServletRequest request, @RequestParam("count") int count) {
		ModelAndView mav = new ModelAndView();
		
		System.out.println("id : " + recipeformvo.getId() + "rnum : " + recipeformvo.getRnum() + "thumbnail : " + recipeformvo.getThumbnail());
		System.out.println("updateRecipe 도착");
		System.out.println("request.getParameter : " + request.getParameter("id"));
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		/*
		System.out.println("id : " + id + "rnum : " + rnum + "thumbnail : " + thumbnail);
		System.out.println("updateRecipe 도착");
		paramMap.put("id", id);
		paramMap.put("subject", subject);
		paramMap.put("content", content);
		paramMap.put("time", cookingTime);
		paramMap.put("thumbnail", "imageRecipe/"+thumbnail);
		paramMap.put("checkIng", checkIng);
		paramMap.put("type", type);
		paramMap.put("theme", theme);
		paramMap.put("count", count);
		paramMap.put("rnum", rnum);
		*/
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
		// paramMap.put("nick", recipeformvo.getNick());
		paramMap.put("subject", recipeformvo.getSubject());
		paramMap.put("content", recipeformvo.getContent());
		paramMap.put("cookingtime", recipeformvo.getCookingTime());
		paramMap.put("thumbnail", recipeformvo.getThumbnail());
		paramMap.put("checkIng", recipeformvo.getCheckIng());
		paramMap.put("type", recipeformvo.getType());
		paramMap.put("theme", recipeformvo.getTheme());
		paramMap.put("count", count);
		paramMap.put("rnum", recipeformvo.getRnum());
		// processImgs와 processDetail들의 수는 미정이어서 우선 request.getParameter 사용
		ArrayList<ProcessImgVO> processList = new ArrayList<ProcessImgVO>();
		for(int i=0; i<count; i++) {
			ProcessImgVO pvo = new ProcessImgVO();
			String fileName = request.getParameter("processImg"+(i+1));
			// if(fileName==null || fileName.equals(""))
			if(fileName.equals("imageRecipe/null") || fileName.equals("imageRecipe/"))
				pvo.setLinks("imageRecipe/cookingTimer.png");
			else pvo.setLinks(fileName);
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
		// paramMap.put("max_rnum", null);
		
		// paramMap.put("lastTagId", 0);
		rs.updateRecipe(paramMap);
		// rs.updateProcessIng(paramMap);
		System.out.println("processIng 성공");
		mav.setViewName("redirect:/recipeDetailWithoutView?rnum="+recipeformvo.getRnum());
		
		}
		return mav;
	}
	
	@RequestMapping("/recipeList")
	public ModelAndView recipeList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
			
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ref_cursor", null);
		paramMap.put("request", request);
		rs.goRecipeList( paramMap );
		
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		System.out.println("list.get(0) rnum 확인용 : " + list.get(0));
		for(HashMap<String, Object> rvo: list) { // 확인용
			
			System.out.println("recipeList 확인용 : " + rvo.get("RNUM"));
		}
		
		paramMap.put("replyCountList", null);
		rs.getReplyCount(paramMap);
		for(Integer replycnt : (ArrayList<Integer>) paramMap.get("replyCountList")) { // 확인용
			System.out.println("replycnt : " + replycnt);
		}
		mav.addObject("recipeList", (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor"));
		mav.addObject("paging", (Paging) paramMap.get("paging"));
		mav.addObject("key", (String) paramMap.get("key"));
		mav.addObject("condition", (String) paramMap.get("condition"));
		mav.addObject("total",  Integer.parseInt(String.valueOf(paramMap.get("total"))));
		mav.addObject("replyCountList", (ArrayList<Integer>) paramMap.get("replyCountList"));
		mav.setViewName("recipe/recipeList");
		return mav;
	}
	
	/* 댓글 달기 ajax => 실패
	@ResponseBody
	@RequestMapping(value="/addReply", method=RequestMethod.POST)
	public HashMap<String, Object> addreply(
			@ModelAttribute("replyVO") @Valid ReplyVO replyVO, 	BindingResult result,
			// @RequestParam("rnum") int rnum,
			// @RequestParam("reply") String reply,
			HttpServletRequest request, Model model
			) {
		
			System.out.println("addReply 도착");
			// 로그인 확인은 jsp에서(로그인되지 않은 유저는 alert("로그인 후 댓글 등록이 가능합니다.");)
			HttpSession session = request.getSession();
			HashMap<String, Object> loginUser 
			= (HashMap<String, Object>)session.getAttribute("loginUser");
			
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			if(replyVO.getRnum()==null) System.out.println("rnum이 넘어오지 않았어요");
			
			String reply = request.getParameter("reply");
			paramMap.put("id", loginUser.get("ID"));
			paramMap.put("rnum", replyVO.getRnum());
			paramMap.put("reply", reply );
			paramMap.put("replyseq", 0); // 방금 넣은 댓글의 번호를 담아옴
			
			try {
				rs.addReply( paramMap );
				resultMap.put("STATUS", 1);
				resultMap.put("ID", loginUser.get("ID"));
				resultMap.put("CONTENT", reply);
				// resultMap.put("replydate", ??);
				// jsp에서 표시 : <jsp:useBean id="now" class="java.util.Date"/>    
				// <fmt:formatDate value="${now}" dateStyle="long"/>
				// <fmt:formatDate value="${now}" pattern="dd-MM-yyyy HH:mm:ss a z" />
				
				// jsp 표시 : id, content, replydate, 삭제 버튼
				resultMap.put("REPLYSEQ", Integer.parseInt(String.valueOf(paramMap.get("replyseq"))));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		return resultMap;
	}
	*/
	
	
	
}
