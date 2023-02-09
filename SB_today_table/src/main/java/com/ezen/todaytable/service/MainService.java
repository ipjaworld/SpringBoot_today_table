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
	RecipeService rs;

	@Autowired
	IMainDao maindao;
	
	@Autowired
	IMemberDao mdao;
	
	@Autowired
	IRecipeDao rdao;
	
	
	
	public void getRecipeBanner(HashMap<String, Object> paramMap) {
		rdao.getRecipeBannerList(paramMap);
	}
	
}
