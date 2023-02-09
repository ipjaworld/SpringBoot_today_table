package com.ezen.todaytable.service;

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
	
	
	
	
}
