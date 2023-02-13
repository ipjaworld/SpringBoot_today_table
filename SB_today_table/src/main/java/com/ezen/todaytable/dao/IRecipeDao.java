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

	void addReply(HashMap<String, Object> paramMap);

	void deleteReply(int replyseq);

	void likeRecipe(HashMap<String, Object> paramMap);

	void reportRecipe(HashMap<String, Object> paramMap);

	void insertRecipe(HashMap<String, Object> paramMap);

	// void insertProcess(int rnum, Integer iseq,  String links, String description);

	void insertProcess(HashMap<String, Object> pvoMap);

	// void insertIng(String ing, int rnum, String qty);
	// void insertIng(HashMap<String, Object> ingMap);
	
	// 상단 insertIng의 과정 세분화
	void getTagCnt(HashMap<String, Object> ingMap);

	void insertTag(HashMap<String, Object> ingMap);

	void insertRecipeTag(HashMap<String, Object> ingMap);

	void updateRecipe(HashMap<String, Object> paramMap);

	void deleteProcess(HashMap<String, Object> paramMap);

	void getCountsByKey(HashMap<String, Object> cntMap);

	void selectListByKey(HashMap<String, Object> paramMap);

	void getReplyCount(HashMap<String, Object> rvo);

	void getMyRecipeCount(HashMap<String, Object> paramMap);


}
