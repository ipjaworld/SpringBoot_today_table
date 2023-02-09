package com.ezen.todaytable.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IRecipeDao;

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

	public void recipeFavoriteAndRec(HashMap<String, Object> paramMap) {
		rdao.recipeFavoriteAndRec(paramMap);
	}

	public void addReply(int rnum, String reply) {
		rdao.addReply(rnum, reply);
	}

	
	
	
	
}
