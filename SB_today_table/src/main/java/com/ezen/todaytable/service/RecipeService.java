package com.ezen.todaytable.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IRecipeDao;
import com.ezen.todaytable.dto.ProcessImgVO;

@Service
public class RecipeService {

	@Autowired
	IRecipeDao rdao;


	public void recipeCategory(HashMap<String, Object> paramMap) {
		rdao.recipeCategory( paramMap );
	}
	
	public void recipeDetailView(HashMap<String, Object> paramMap) {
		// 조회수 증가
		rdao.addRecipeView(paramMap);
		// * 아래 4개의 과정을 하나의 프로시저로
		rdao.getRecipe(paramMap);
		// recipe 조회
		
		// 재료 조회
		
		// 상세과정 조회
		
		// 댓글 리스트 조회
		
		
	}

	public void recipeDetailWithoutView(HashMap<String, Object> paramMap) {
		rdao.getRecipe(paramMap);
	}
	
	public void deleteRecipe(HashMap<String, Object> paramMap) {
		rdao.deleteRecipe(paramMap);
	}

	public void recipeFavoriteAndRec(HashMap<String, Object> paramMap) {
		rdao.recipeFavoriteAndRec(paramMap);
	}

	public void addReply(HashMap<String, Object> paramMap) {
		rdao.addReply(paramMap);
	}

	public void deleteReply(int replyseq) {
		rdao.deleteReply(replyseq);
	}

	public void likeRecipe(HashMap<String, Object> paramMap) {
		rdao.likeRecipe(paramMap);
	}
	public void reportRecipe(HashMap<String, Object> paramMap) {
		rdao.reportRecipe(paramMap);
	}

	public void insertRecipe(HashMap<String, Object> paramMap) {
		/*
		String checkIng = (String) paramMap.get("checkIng");
		System.out.println("service로 전달된 checkIng : " + checkIng);
		String [] ingredients = checkIng.split("\\s");
		for(String str : ingredients) { // 확인용
			System.out.println("ingredients 배열 : " + str);
		}
		ArrayList<String> ingArray = new ArrayList<String>();
		ArrayList<String> qtyArray = new ArrayList<String>();
		for(int i=0; i<ingredients.length; i++) {
			if(ingredients[i].startsWith("#")) {
				String substr = ingredients[i].substring(1);
				ingArray.add(substr);
			}else {
				qtyArray.add(ingredients[i]);
			}
		}
		
		for(String str : ingArray) { // 확인용
			System.out.println("ingArray 내용 : " + str);
		}
		for(String str : qtyArray) { // 확인용
			System.out.println("qtyArray 내용 : " + str);
		}
		*/
		
		
		rdao.insertRecipe(paramMap);
		// 방금 넣은 rnum 조회해서 리턴
		/*
		int rnum = Integer.parseInt(String.valueOf(paramMap.get("max_rnum")));
		System.out.println("rnum : " + rnum);
		ArrayList<ProcessImgVO> processList = (ArrayList<ProcessImgVO>) paramMap.get("processList");
		// process 반복문 프로시져 : processList 반복하며 insertProcess 반복(rnum 활용) 
		for(ProcessImgVO pvo : processList) {
			HashMap<String, Object> pvoMap = new HashMap<String, Object>();
			pvoMap.put("rnum", rnum);
			pvoMap.put("iseq", pvo.getIseq());
			pvoMap.put("links", pvo.getLinks());
			pvoMap.put("description", pvo.getDescription());
			rdao.insertProcess(pvoMap);
			// rdao.insertProcess(rnum, pvo.getIseq(), pvo.getLinks(), pvo.getDescription());
		}
		
		for(int i=0; i<ingArray.size(); i++) {
			rdao.insertIng(ingArray.get(i), rnum, qtyArray.get(i));
			// rdao.insertRecipeTag(qtyArray.get(i));
		}
		*/
		// 재료 반복문 프로시저 : 
		// 1) 기존 ingTag에 있는지 확인 = 있다면 기존 태그 활용
		// = 없다면 : 2) ingTag에 삽입 3) 방금 삽입한 태그 아이디 확인 4) recipeTag에 삽입
		
	}
	
public void insertProcessIng(HashMap<String, Object> paramMap) {
		
		String checkIng = (String) paramMap.get("checkIng");
		System.out.println("service로 전달된 checkIng : " + checkIng);
		String [] ingredients = checkIng.split("\\s");
		ArrayList<String> ingArray = new ArrayList<String>();
		ArrayList<String> qtyArray = new ArrayList<String>();
		for(int i=0; i<ingredients.length; i++) {
			if(ingredients[i].startsWith("#")) {
				String substr = ingredients[i].substring(1);
				ingArray.add(substr);
			}else {
				qtyArray.add(ingredients[i]);
			}
		}
		
		int rnum = Integer.parseInt(String.valueOf(paramMap.get("max_rnum")));
		System.out.println("rnum : " + rnum);
		ArrayList<ProcessImgVO> processList = (ArrayList<ProcessImgVO>) paramMap.get("processList");
		// process 반복문 프로시져 : processList 반복하며 insertProcess 반복(rnum 활용) 
		for(ProcessImgVO pvo : processList) {
			HashMap<String, Object> pvoMap = new HashMap<String, Object>();
			pvoMap.put("rnum", rnum);
			pvoMap.put("iseq", pvo.getIseq());
			pvoMap.put("links", pvo.getLinks());
			pvoMap.put("description", pvo.getDescription());
			rdao.insertProcess(pvoMap);
			// rdao.insertProcess(rnum, pvo.getIseq(), pvo.getLinks(), pvo.getDescription());
		}
		
		for(int i=0; i<ingArray.size(); i++) {
			HashMap<String, Object> ingMap = new HashMap<String, Object>();
			ingMap.put("tag", (String) ingArray.get(i));
			System.out.println("tag : " +  ingArray.get(i));
			ingMap.put("rnum", rnum);
			System.out.println("ing에서의 rnum : " + rnum);
			ingMap.put("quantity", (String) qtyArray.get(i));
			System.out.println("quantity : " +  qtyArray.get(i));
			ingMap.put("cnt", null);
			rdao.getTagCnt(ingMap);
			// rdao.insertIng(ingArray.get(i), rnum, qtyArray.get(i));
			// rdao.insertIng(ingMap);
			int cnt = Integer.parseInt(String.valueOf(ingMap.get("cnt")));
			System.out.println("cnt : " + cnt);
			if(cnt==0) {
				rdao.insertTag(ingMap);
				System.out.println("cnt가 0이므로 태그가 삽입됩니다.");
			}
			else {
				rdao.insertRecipeTag(ingMap);
				System.out.println("cnt가 0이 아니므로 레시피태그만 삽입됩니다.");
			}
			
			
		}
		
	}

	public void updateRecipe(HashMap<String, Object> paramMap) {
		
		String checkIng = (String) paramMap.get("checkIng");
		System.out.println("service로 전달된 checkIng : " + checkIng);
		String [] ingredients = checkIng.split("\\s");
		ArrayList<String> ingArray = new ArrayList<String>();
		ArrayList<String> qtyArray = new ArrayList<String>();
		for(int i=0; i<ingredients.length; i++) {
			if(ingredients[i].startsWith("#")) {
				String substr = ingredients[i].substring(1);
				ingArray.add(substr);
			}else {
				qtyArray.add(ingredients[i]);
			}
		}
		
		for(String str : ingArray) { // 확인용
			System.out.println("ingArray 내용 : " + str);
		}
		for(String str : qtyArray) { // 확인용
			System.out.println("qtyArray 내용 : " + str);
		}
		
		// 1) recipe 테이블 수정
		rdao.updateRecipe(paramMap);
		
		int rnum = Integer.parseInt(String.valueOf(paramMap.get("rnum")));
		
		// 2) processImg 테이블과 recipeTag 테이블의 레코드 삭제
		rdao.deleteProcess(paramMap);
		
		// 3) processImg 레코드 삽입
		ArrayList<ProcessImgVO> processList = (ArrayList<ProcessImgVO>) paramMap.get("processList");
		for(ProcessImgVO pvo : processList) {
			HashMap<String, Object> pvoMap = new HashMap<String, Object>();
			pvoMap.put("rnum", rnum);
			pvoMap.put("iseq", pvo.getIseq());
			pvoMap.put("links", pvo.getLinks());
			pvoMap.put("description", pvo.getDescription());
			rdao.insertProcess(pvoMap);
		}
		
		// 4) 재료 레코드 삽입
		
		
		for(int i=0; i<ingArray.size(); i++) {
			HashMap<String, Object> ingMap = new HashMap<String, Object>();
			ingMap.put("tag", (String) ingArray.get(i));
			System.out.println("tag : " +  ingArray.get(i));
			ingMap.put("rnum", rnum);
			System.out.println("ing에서의 rnum : " + rnum);
			ingMap.put("quantity", (String) qtyArray.get(i));
			System.out.println("quantity : " +  qtyArray.get(i));
			ingMap.put("cnt", null);
			rdao.getTagCnt(ingMap);
			int cnt = Integer.parseInt(String.valueOf(ingMap.get("cnt")));
			System.out.println("cnt : " + cnt);
			if(cnt==0) {
				rdao.insertTag(ingMap);
				System.out.println("cnt가 0이므로 태그가 삽입됩니다.");
			}
			else {
				rdao.insertRecipeTag(ingMap);
				System.out.println("cnt가 0이 아니므로 레시피태그만 삽입됩니다.");
			}
		}

	}
	
	

	
	
	
	
}
