package com.ezen.todaytable.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ezen.todaytable.service.RecipeService;

@Controller
public class RecipeController {

	@Autowired
	RecipeService rs;
	
}
