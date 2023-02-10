package com.ezen.todaytable.dao;

import java.util.HashMap;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRecipeDao {

	void getRecipeBannerList(HashMap<String, Object> paramMap);

	void recipeCategory(HashMap<String, Object> paramMap);

	void addRecipeView(HashMap<String, Object> paramMap);

	void getRecipe(HashMap<String, Object> paramMap);
	
	void deleteRecipe(HashMap<String, Object> paramMap);

	void recipeFavoriteAndRec(HashMap<String, Object> paramMap);

	void addReply(int rnum, String reply);

	void insertRecipe(HashMap<String, Object> paramMap);

	// void insertProcess(int rnum, Integer iseq,  String links, String description);

	void insertProcess(HashMap<String, Object> pvoMap);

	// void insertIng(String ing, int rnum, String qty);

	void insertIng(HashMap<String, Object> ingMap);


}
