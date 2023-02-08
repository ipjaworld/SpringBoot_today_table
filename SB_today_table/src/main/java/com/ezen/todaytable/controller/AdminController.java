package com.ezen.todaytable.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ezen.todaytable.service.AdminService;

@Controller
public class AdminController {

	@Autowired
	AdminService as;
	
}
