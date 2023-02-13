--페이징 카운트(검색기능포함)
CREATE OR REPLACE PROCEDURE adminGetAllCount(
    p_tableName number,
    p_key IN varchar2,
    p_cnt out number
)
IS
v_cnt number;
BEGIN
    if p_tableName =2 then
    select Count(*) into v_cnt from members where name like '%'||p_key||'%' ;
    
    elsif p_tableName =3 then
    select Count(*) into v_cnt from reply where id like '%'||p_key||'%';
    
  elsif p_tableName =4 then
    select Count(*) into v_cnt from qna where qsubject like '%'||p_key||'%' or qcontent like '%'||p_key||'%';
    
  end if;
  p_cnt:=v_cnt;
END;
--멤버리스트(이름검색)
CREATE OR REPLACE PROCEDURE getAdminMemberList(
    p_startNum in number,
    p_endNUm in number,
    p_key in member.name%type,
    p_rc out sys_refcursor
)
IS
BEGIN
    open p_rc for
        select * from ( 
        select * from ( 
        select rownum as rn, m.*from (( select *from members where name like '%'||p_key||'%' order by indate desc ) m)
             ) where rn >=p_startNum 
             ) where rn <=p_endNum; 
END;
--휴면회원전환
CREATE OR REPLACE PROCEDURE adminSleepMem(
    r_id in members.id%type
)
IS
    v_useyn varchar2(10);
BEGIN
    select useyn into  v_useyn from members where id=r_id;
    
    if v_useyn='N' then
    update members set useyn='Y' where id=r_id;
    elsif v_useyn='Y' then
    update members set useyn='N' where id=r_id;
    commit;
end if;
END;
--멤버 디테일
CREATE OR REPLACE PROCEDURE getAdminMemDetail(
    r_id in members.id%type,
    r_rc out sys_refcursor
)
IS
BEGIN
    open r_rc for
        select * from members where id=r_id;
END;

--qna목록조회
CREATE OR REPLACE PROCEDURE getAdminQnaList(
    q_startNum in number,
    q_endNUm in number,
    q_key in qna.qcontent%type,
    q_rc out sys_refcursor
)
IS
BEGIN
    open q_rc for
        select * from ( 
        select * from ( 
        select rownum as rn, q.*from (( select *from qna where qsubject like '%'||q_key||'%' or qcontent like '%'||q_key||'%' order by qseq desc ) q)
             ) where rn >=q_startNum 
             ) where rn <=q_endNum; 
END;

select * from qna order by qnadate desc;


--qna 게시글삭제
CREATE OR REPLACE PROCEDURE adminDeleteQna(
    q_qseq in qna.qseq%type
)
IS
BEGIN
    delete from qna where qseq=q_qseq;
    commit;
END;
--qna디테일
CREATE OR REPLACE PROCEDURE getAdminQnaDetail(
    q_qseq in qna.qseq%type,
    q_rc out sys_refcursor
)
IS
BEGIN
    open q_rc for
        select * from qna where qseq=q_qseq;
END;

--qna답변 및 rep(답변여부 변경 1미답변 2답변)
CREATE OR REPLACE PROCEDURE adminSaveReply(
    q_qseq in qna.qseq%type,
    q_replyQna in qna.replyQna%type
)
IS
BEGIN
   update qna set replyQna=q_replyQna, rep='2' where qseq=q_qseq;
    commit;
END;
--댓글목록 조회

CREATE OR REPLACE PROCEDURE getAdminReplyList(
    r_startNum in number,
    r_endNUm in number,
    r_key in reply.id%type,
    r_rc out sys_refcursor
)
IS
BEGIN
    open r_rc for
        select * from ( 
        select * from ( 
        select rownum as rn, r.*from (( select *from reply where id like '%'||r_key||'%' order by replydate desc ) r)
             ) where rn >=r_startNum 
             ) where rn <=r_endNum; 
END;
--댓글삭제
CREATE OR REPLACE PROCEDURE adminDeleteReply(
    r_replyseq in reply.replyseq%type
)
IS
BEGIN
    delete from reply where replyseq=r_replyseq;
    commit;
END;

--대쉬보드 카운트 목록
CREATE OR REPLACE PROCEDURE adminGetCounts(
    c_table in varchar2,
    c_result out number
)
IS
v_SQL    VARCHAR2(1000);
BEGIN
    if c_table='recipe_page_view' then
        v_SQL := 'SELECT  sum(views) FROM ' || c_table ;
    elsif c_table !='recipe_page_view' then   
        v_SQL := 'SELECT  count(rownum) FROM ' || c_table ;  
   end if;
     EXECUTE IMMEDIATE v_SQL INTO c_result ;
END;

--어드민 회원정보
CREATE OR REPLACE PROCEDURE getAdminList(
    a_id in admins.aid%type,
    a_rc out sys_refcursor
)
IS
BEGIN
    open a_rc for
        select * from admins where aid=a_id;
END;

--대쉬보드 (베스트 조회 레시피, 최근 댓글 (3개씩))

CREATE OR REPLACE PROCEDURE adminDashList(
    a_table in varchar2,
    a_result out sys_refcursor
)
IS
v_SQL    VARCHAR2(1000);
BEGIN
    
    if a_table='recipe' then
        open a_result for
        select * from ( 
        select rownum as rn, r.*from((select * from recipe_page_view order by views desc)r)
        ) where rn <=3  ;
    elsif a_table ='reply' then
        open a_result for
        select * from reply ORDER BY replydate desc  ;  
   end if;
END;







select*from recipe_page;
commit;
select*from members;








