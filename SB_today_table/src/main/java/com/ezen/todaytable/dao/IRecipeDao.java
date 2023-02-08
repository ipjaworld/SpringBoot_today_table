package com.ezen.todaytable.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRecipeDao {

	void getAllRecipe(HashMap<String, Object> paramMap);

	void getVariableRecipe(HashMap<String, Object> paramMap); // ListName 에 type, recipe 등등의 이름을 넣어서 조회할 수 있습니다.

	void getBannerList(HashMap<String, Object> paramMap);

}
