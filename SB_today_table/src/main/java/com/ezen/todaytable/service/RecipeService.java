package com.ezen.todaytable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.todaytable.dao.IRecipeDao;

@Service
public class RecipeService {

	@Autowired
	IRecipeDao mdao;
}
