package com.ezen.todaytable.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IMainDao;
import com.ezen.todaytable.dao.IMemberDao;
import com.ezen.todaytable.dao.IRecipeDao;

@Service
public class MainService {

	@Autowired
	IMainDao maindao;
	
	@Autowired
	IMemberDao mdao;
	
	@Autowired
	IRecipeDao rdao;

	public void startIndex(HashMap<String, Object> paramMap) {
		
		rdao.getAllRecipe(paramMap);
		
		rdao.getTypeRecipe(paramMap);
		
		rdao.getRecipeBannerList(paramMap);
		
	}
	
}
