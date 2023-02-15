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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.Paging;
import com.ezen.todaytable.dto.ReplyVO;
import com.ezen.todaytable.service.RecipeService;

@Controller
public class RecipeGHController {

	@Autowired
	RecipeService rs;
	
	@RequestMapping("/recipeCategory")
	public ModelAndView recipeCategory(
			@RequestParam("status") String status,
			// @RequestParam("page") int page,
			@RequestParam(value="kind", required=false) String kind,
			HttpServletRequest request
			) {
		System.out.println("컨트롤러에 도착한 kind : " + kind);
		if(kind==null) kind= "0";
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		/*
		if(kind != null) // 카테고리 필터를 통해 전송된 값이 존재
			paramMap.put("kind", kind);
		*/
		
		paramMap.put("kind", Integer.parseInt(kind));
		paramMap.put("ref_cursor", null);	// 카테고리에 해당하는 레시피vo 를 가져올 커서
		paramMap.put("replyCountList", null);
		paramMap.put("request", request);
		
		// 레시피 키라는 이름으로 웹페이지에서 status 라고 넘긴 값을 전송합니다. 이것을 이용해서 sql문에 접근합니다.
		paramMap.put("recipekey", status);
		rs.getCategory( paramMap );
		rs.getReplyCount(paramMap);
		
		ArrayList<HashMap<String , Object>> recipeCategory
		= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor");
		ArrayList<HashMap<String, Object>> replyCountList
		= (ArrayList<HashMap<String , Object>>) paramMap.get("replyCountList");
		
		//RecipeVO rvo = (RecipeVO)recipeCategory.get(0);
		
		mav.addObject("RecipeCategory", recipeCategory);
		mav.addObject("replyCountList", replyCountList);
		// mav.addObject("total", recipeCategory.size());
		mav.addObject("total", Integer.parseInt(String.valueOf(paramMap.get("total"))));
		mav.addObject("status", status);
		mav.addObject("paging", (Paging) paramMap.get("paging"));
		
		mav.setViewName("recipe/recipeCategory");
		
		return mav;
		
	}
	
	
	@RequestMapping("/recipeFavoriteAndRec")
	public ModelAndView recipeFavoriteAndRec(
			@RequestParam("page") int page, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("ref_cursor2", null); // 관리자 추천
		paramMap.put("ref_cursor", null); 	// 단골레시피 상위권
		paramMap.put("replyCountList", null); // 댓글 갯수
		paramMap.put("request", request);
		
		rs.recipeFavoriteAndRec( paramMap );
		rs.getReplyCount(paramMap);
		
		ArrayList<HashMap<String , Object>> recommandList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor2");
		ArrayList<HashMap<String , Object>> favoriteList
			= (ArrayList<HashMap<String , Object>>) paramMap.get("ref_cursor");
		ArrayList<HashMap<String, Object>> replyCountList
		= (ArrayList<HashMap<String , Object>>) paramMap.get("replyCountList");
		
		mav.addObject("recommandList", recommandList);
		mav.addObject("favoriteList", favoriteList);
		mav.addObject("replyCountList", replyCountList);
		mav.addObject("paging", (Paging) paramMap.get("paging"));
		
		mav.setViewName("recipe/recipeFavoriteAndRec");
		return mav;
	}
	
	
	@RequestMapping("/addReply")
	public ModelAndView addreply(
			@ModelAttribute("replyVO") @Valid ReplyVO replyVO, 	BindingResult result,
			@RequestParam("rnum") int rnum,
			@RequestParam("reply") String reply,
			HttpServletRequest request
			) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser 
		= (HashMap<String, Object>)session.getAttribute("loginUser");
		if( loginUser == null ) {
			mav.setViewName("member/login");
		}else {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", loginUser.get("ID"));
			paramMap.put("rnum", rnum );
			paramMap.put("reply", reply );
			
			rs.addReply( paramMap );
			mav.setViewName("redirect:/recipeDetailWithoutView?rnum="+rnum);
		}
		return mav;
	}
	
	/** 덧글 삭제 버튼이 로그인을 했을 때, 덧글 작성자의 아이디와 같을 때만 뜨기 때문에 
	 * 로그인을 했는지, 로그인한 아이디와 덧글 작성자의 아이디가 같은지 유효성 검사는 넘겨도 된다.
	 * */
	
	@RequestMapping("/deleteReply")
	public String deleteReply(
			@RequestParam("rnum") int rnum,
			@RequestParam("replyseq") int replyseq
			) {
		rs.deleteReply(replyseq);
		return "redirect:/recipeDetailWithoutView?rnum="+rnum;
	}
	
	@RequestMapping("/likeRecipe")
	public String likeRecipe(
			@RequestParam("rnum") int rnum,
			HttpServletRequest request
			) {
		
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser 
		= (HashMap<String, Object>)session.getAttribute("loginUser");
		if( loginUser == null ) {
			return "member/login";
		}else {
			
			// 좋아요 세팅에 필요한건 rnum과 id
			// 우선 좋아요를 누른 사람이 이 게시물에 좋아요를 누른적이 있는지 없는지 검사해야함.
			// 분기가 완전히 갈리는데,
			// 그 게시물에 좋아요를 한적이 없는 유저라면 Like는 Y로, Favorite는 N으로 insert가 이루어져야한다.
			// 좋아요를 한적이 있는 유저라면 그때부터는 like, favorite Y, N 바꾸기만 하면 된다.
			// 전부 데이터베이스에서만 왔다갔다 하기 때문에 아웃변수를 잡아줄 필요는 없다.
			
			// 위의 작업이 다 끝난 후에 recipe-page에 해당 rnum의 총 좋아요 갯수를 업데이트한다.
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", loginUser.get("ID"));
			paramMap.put("rnum", rnum );
			rs.likeRecipe(paramMap);
		}
		return "redirect:/recipeDetailWithoutView?rnum="+rnum;
	}
	
	@RequestMapping("/reportRecipe")
	public String reportRecipe(
			@RequestParam("rnum") int rnum,
			HttpServletRequest request
			) {
		String url = "";
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser 
		= (HashMap<String, Object>)session.getAttribute("loginUser");
		if( loginUser == null ) {
			url = "member/login";
			
		}else {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", loginUser.get("ID"));
			paramMap.put("rnum", rnum );
			rs.reportRecipe(paramMap);
			System.out.println("recipe report를 했대요");
			url = "redirect:/recipeDetailWithoutView?rnum="+rnum;
		}
		
		return url;
	}
	
	
}
