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
import org.springframework.web.servlet.ModelAndView;

import com.ezen.todaytable.dto.AdminVO;
import com.ezen.todaytable.dto.Paging;
import com.ezen.todaytable.service.AdminService;

@Controller
public class AdminController {

	@Autowired
	AdminService as;
	// 어드민 대쉬보드 이동

	@RequestMapping("/admin")
	public String adminLogin(HttpSession session) {
		if (session.getAttribute("loginAdmin") == null)
			return "admin/member/adminLogin";
		return "redirect:/adminMain";
	}

	@RequestMapping(value = "/adminlogin", method = RequestMethod.POST)
	public String login(@ModelAttribute("dto") @Valid AdminVO adminvo, BindingResult result, HttpServletRequest request,
			Model model) {

		String url = "admin/member/adminLogin";

		if (result.getFieldError("aid") != null) {
			model.addAttribute("message", result.getFieldError("aid").getDefaultMessage());
		} else if (result.getFieldError("pwd") != null) {
			model.addAttribute("message", result.getFieldError("pwd").getDefaultMessage());
		} else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", adminvo.getAid());
			paramMap.put("ref_cursor", null);
			as.getAdminList(paramMap);

			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

			if (list.size() == 0) {
				model.addAttribute("message", "아이디가 없습니다");
				return "admin/member/adminLogin";
			}
			HashMap<String, Object> avo = list.get(0);
			if (avo.get("PWD") == null)
				model.addAttribute("message", "비밀번호 오류. 관리자에게 문의하세요");
			else if (!avo.get("PWD").equals(adminvo.getPwd()))
				model.addAttribute("message", "비밀번호가 맞지않습니다");
			else if (avo.get("PWD").equals(adminvo.getPwd())) {
				HttpSession session = request.getSession();
				session.setAttribute("loginAdmin", avo);
				url = "redirect:/adminMain";
			}

		}
		return url;
	}

	@RequestMapping("/adminMain")
	public String admin(HttpSession session, Model model) {
		if (session.getAttribute("loginAdmin") == null)
			return "admin/member/adminLogin";
		// 카운트 목록조회
		String[][] table = { { "recipe", "recipe" }, { "members", "members" }, { "reply", "reply" }, { "qna", "qna" },
				{ "membersno", "members where useyn = 'N'" }, { "qnarep", "qna where rep = 2" },
				{ "adminrec", "recipe_page_view where rec = 1" }, { "viewcnt", "recipe_page_view" } };
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("result", 0);

		for (String[] t : table) {
			paramMap.put("table", t[1]);
			as.adminGetCounts(paramMap);
			int count = Integer.parseInt(paramMap.get("result") + "");
			model.addAttribute(t[0], count);
		}
		// 최근 게시물 목록조회(상위 조회수 레시피,최근댓글)
		String[][] table1 = { { "bestViewList", "recipe" }, { "recentReplyList", "reply" } };
		HashMap<String, Object> paramMap1 = new HashMap<String, Object>();
		paramMap1.put("result", null);
		for (String[] t : table1) {
			paramMap.put("table", t[1]);
			as.adminDashList(paramMap);
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("result");
			model.addAttribute(t[0], list);
		}

		return "admin/adminMain";
	}

	// 멤버리스트 이동
	@RequestMapping(value = "/memberList")
	public ModelAndView memberList(HttpServletRequest request, Model model) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("request", request);
			paramMap.put("ref_cursor", null);

			as.getAdminMemberList(paramMap);

			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

			mav.addObject("paging", (Paging) paramMap.get("paging"));
			mav.addObject("key", (String) paramMap.get("key"));
			mav.addObject("membersList", list);

			mav.setViewName("admin/member/adminMemList");
		}
		return mav;
	}

	// 휴면회원 전환
	@RequestMapping(value = "/adminSleepMem")
	public String adminSleepMem(@RequestParam("id") String[] id) {

		as.adminSleepMem(id);// (id로 멤버리스트를 조회했을때 useyn이 Y면 N/ N이면 Y로)

		return "redirect:/memberList?first=1"; // 휴면회원 전환후 다시 멤버리스트로 이동
	}

	// 회원디테일
	@RequestMapping(value = "/adminMemDetail")
	public ModelAndView adminMemDetail(HttpServletRequest request, Model model, @RequestParam("id") String id) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", id);
			paramMap.put("ref_cursor", null);

			as.getAdminMemDetail(paramMap);
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("memberVO", list.get(0));

			mav.setViewName("admin/member/adminMemDetail");
		}
		return mav;
	}

	// qna리스트 이동
	@RequestMapping(value = "/adminQnaList")
	public ModelAndView adminQnaList(HttpServletRequest request, Model model) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("request", request);
			paramMap.put("ref_cursor", null);

			as.getAdminQnaList(paramMap);

			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("paging", (Paging) paramMap.get("paging"));
			mav.addObject("key", (String) paramMap.get("key"));
			mav.addObject("qnaList", list);

			mav.setViewName("admin/qna/adminQnaList");
		}
		return mav;
	}

	// qna게시글삭제
	@RequestMapping("/adminDeleteQna")
	public String adminDeleteQna(@RequestParam("qseq") int[] qseq) {

		as.adminDeleteQna(qseq);

		return "redirect:/adminQnaList?first=1";
	}

	// qna디테일
	@RequestMapping(value = "/adminQnaDetail")
	public ModelAndView adminQnaDetail(HttpServletRequest request, Model model, @RequestParam("qseq") int qseq) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("qseq", qseq);
			paramMap.put("ref_cursor", null);

			as.getAdminQnaDetail(paramMap);
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			System.out.println("size :" + list.size());
			mav.addObject("qnaVO", list.get(0));

			mav.setViewName("admin/qna/adminQnaDetail");
		}
		return mav;
	}

	// Qna답변쓰기
	@RequestMapping("/adminSaveReply")
	public String adminSaveReply(@RequestParam("qseq") int qseq, @RequestParam("replyQna") String replyQna) {
		System.out.println(qseq + "/adminSaveReply에서 호출");
		as.adminSaveReply(qseq, replyQna);

		return "redirect:/adminQnaDetail?qseq=" + qseq;
	}

	// 댓글리스트

	@RequestMapping("/adminReplyList")
	public ModelAndView adminReplyList(HttpServletRequest request, Model model) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("request", request);
			paramMap.put("ref_cursor", null);

			as.getAdminReplyList(paramMap);

			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("paging", (Paging) paramMap.get("paging"));
			mav.addObject("key", (String) paramMap.get("key"));
			mav.addObject("replyList", list);

			mav.setViewName("admin/reply/adminReplyList");
		}
		return mav;
	}

	// 댓글삭제
	@RequestMapping("/adminDeleteReply")
	public String adminDeleteReply(@RequestParam("replyseq") int[] replyseq) {

		as.adminDeleteReply(replyseq);

		return "redirect:/adminReplyList?first=1";
	}
	//레시피 리스트
	@RequestMapping("/adminRecipeList")
	public ModelAndView adminRecipeList(HttpServletRequest request, Model model) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("request", request);
			paramMap.put("ref_cursor", null);

			as.getAdminRecipeList(paramMap);

			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("paging", (Paging) paramMap.get("paging"));
			mav.addObject("key", (String) paramMap.get("key"));
			mav.addObject("recipeList", list);

			mav.setViewName("admin/recipe/adminRecipeList");
		}
		return mav;
	}
	//관리자 추천기능 활성화
	@RequestMapping("/adminChangeRecommend")
	public String adminChangeRecommend(@RequestParam("rnum") String[] rnum,
			@RequestParam("gesi") String gesi) {
		
		as.adminChangeRecommend(rnum);
		if(gesi.equals("rs")) {
		return "redirect:/adminRecipeList?first='1'";
		}else {
		return "redirect:/adminPickList?first='1'";
		}
	}
	
	//관리자 추천게시판 이동
	@RequestMapping("/adminPickList")
	public ModelAndView adminPickList(HttpServletRequest request, Model model) {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		if (session.getAttribute("loginAdmin") == null) {
			mav.setViewName("admin/member/adminLogin");
		} else {

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("request", request);
			paramMap.put("ref_cursor", null);

			as.getAdminPickRecipeList(paramMap);

			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("paging", (Paging) paramMap.get("paging"));
			mav.addObject("key", (String) paramMap.get("key"));
			mav.addObject("adminPickList", list);

			mav.setViewName("admin/recipe/adminPickList");
		}
		return mav;
	}
	
	
	

}
