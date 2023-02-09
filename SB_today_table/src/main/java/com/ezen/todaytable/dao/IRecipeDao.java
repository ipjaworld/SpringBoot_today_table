package com.ezen.todaytable.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRecipeDao {

	
	 void getAllRecipe(HashMap<String, Object> paramMap);
	  
	 void getTypeRecipe(HashMap<String, Object> paramMap);
	 
	 void getRecipeBannerList(HashMap<String, Object> paramMap);

	

	void getThemeRecipe(HashMap<String, Object> paramMap);

	void getIngRecipe(HashMap<String, Object> paramMap);

	
	 

}
