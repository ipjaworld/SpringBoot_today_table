package com.ezen.todaytable.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IRecipeDao;

@Service
public class RecipeService {

	@Autowired
	IRecipeDao rdao;


	public void getAllRecipe(HashMap<String, Object> paramMap) {
		rdao.getAllRecipe( paramMap );
	}

	public void getTypeRecipe(HashMap<String, Object> paramMap) {
		rdao.getTypeRecipe( paramMap );
	}

	public void getThemeRecipe(HashMap<String, Object> paramMap) {
		rdao.getThemeRecipe( paramMap );
	}

	public void getIngRecipe(HashMap<String, Object> paramMap) {
		rdao.getIngRecipe( paramMap );
	}
	
	
}
