package com.ezen.todaytable.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRecipeDao {

	void recipeCategory(HashMap<String, Object> paramMap);
	 
	void getRecipeBannerList(HashMap<String, Object> paramMap);



}
