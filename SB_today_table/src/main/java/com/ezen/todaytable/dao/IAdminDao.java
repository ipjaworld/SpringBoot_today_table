package com.ezen.todaytable.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAdminDao {

	void adminGetAllCount(HashMap<String, Object> cntMap);

	void getAdminMemberList(HashMap<String, Object> paramMap);

	void adminSleepMem(String useid);

}
