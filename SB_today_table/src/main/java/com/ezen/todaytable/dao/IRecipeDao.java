package com.ezen.todaytable.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRecipeDao {

	void getRecipeBannerList(HashMap<String, Object> paramMap);

	void recipeCategory(HashMap<String, Object> paramMap);

	void addRecipeView(HashMap<String, Object> paramMap);

	void getRecipe(HashMap<String, Object> paramMap);
	
	void deleteRecipe(HashMap<String, Object> paramMap);

	void recipeFavoriteAndRec(HashMap<String, Object> paramMap);

	void addReply(HashMap<String, Object> paramMap);

	void deleteReply(int replyseq);

	void likeRecipe(HashMap<String, Object> paramMap);

	void reportRecipe(HashMap<String, Object> paramMap);


}
