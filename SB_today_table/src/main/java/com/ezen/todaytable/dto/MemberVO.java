package com.ezen.todaytable.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MemberVO {
	
	private String id; // 아이디
	private String pwd; // 비밀번호
	private String name; // 이름
	private String phone; // 전화번호
	private String email; // 이메일
	private String nick; // 닉네임
	private String address1; // 주소1
	private String address2; // 주소2
	private String address3; // 주소3
	private String zip_num; // 집우편주소?
	private Timestamp indate; // 가입날짜?
	private String img; // 프로필사진?
	private String useyn; // 휴면회원 여부

}
