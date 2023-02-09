-- 멤버리스트
create or replace procedure getMembersList(
    p_id IN members.id%type,
    p_curvar out SYS_REFCURSOR
)
is
    result_cur sys_refcursor;
begin
  open result_cur for 
        select * from members where id =p_id;
    p_curvar := result_cur;
end;
select*from members;
--회원가입
create or replace procedure insertMemberttable(
    p_id IN members.id%type,
    p_pwd IN members.pwd%type,
    p_name IN members.name%type,
    p_nick IN members.nick%type,
    p_email IN members.email%type,
    p_phone IN members.phone%type,
    p_zip_num IN members.zip_num%type,
    p_address1 IN members.address1%type,
    p_address2 IN members.address2%type,
    p_address3 IN members.address3%type,
    p_img in members.img%type
    
)
is
   
begin
  insert into members (id,pwd, name,nick ,email, phone, zip_num, address1, address2, address3,img)
  values (p_id,p_pwd,p_name,p_nick,p_email,p_phone,p_zip_num,p_address1,p_address2,p_address3,p_img);
  commit;
end;

alter table members add address3 varchar2(50);

--회원정보수정
create or replace procedure updateMemberttable(
    p_id IN members.id%type,
    p_pwd IN members.pwd%type,
    p_name IN members.name%type,
    p_nick IN members.nick%type,
    p_email IN members.email%type,
    p_phone IN members.phone%type,
    p_zip_num IN members.zip_num%type,
    p_address1 IN members.address1%type,
    p_address2 IN members.address2%type,
    p_address3 IN members.address3%type,
    p_img in members.img%type
)
is
   
begin
  update members set pwd=p_pwd, name=p_name, email=p_email, phone=p_phone, zip_num=p_zip_num, address1=p_address1, address2=p_address2, address3=p_address3, img=p_img
 where id=p_id;
  commit;
end;

