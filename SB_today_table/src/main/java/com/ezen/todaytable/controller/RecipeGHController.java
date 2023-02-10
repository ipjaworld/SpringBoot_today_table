package com.ezen.todaytable.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.ReplyVO;
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
			rs.reportRecipe(paramMap);
		}
		
		return "redirect:/recipeDetailWithoutView?rnum="+rnum;
	}
	
	
}
