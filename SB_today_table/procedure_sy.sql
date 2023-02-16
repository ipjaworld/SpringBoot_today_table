select * from recipe order by rnum desc;
select * from recipe_page;
select * from recipe_page_view;
select * from reply;
select * from recipeTag;
select * from ingTag;
select * from processImg order by rnum;
select * from members;
select * from interest;
select * from favorite;
select * from RECIPE_REPORT;
select * from ing_view order by rnum desc;
select * from recipeTag order by rnum;
select * from ingTag;

-- 실행 x
insert into recipe(rnum, id, subject, content, thumbnail, time)  
values(recipe_seq.nextVal, 'scott', '국 8', '111', 'imageRecipe/9.jpg', 10);
insert into recipe_page(rnum) values(60);
alter table recipe_page add favorites number(10) default 0;
alter table recipe_page drop column favorites;
update recipe_page set favorites=30 where rnum in(1,2,3,4,5);
update recipe_page set likes=30 where rnum in(1,2,3,4,5, 6, 7,8);
update recipe set type=1 where rnum=60;
update recipe_page set ing=1 where rnum=60;
update recipe set theme=3 where rnum=57;
select likeyn from interest where rnum=1 and id='scott';
select reportyn from recipe_report where rnum=1 and id='scott';
update recipe_page_view set rec=1 where rnum in(6,7,8,9,10,11,12,13,14,15);
select * from recipe_page_view where rec=1;
select * from ing_view where rnum=73;
select quantity from recipeTag where rnum=73;

alter table recipeTag add rtseq number(100) primary key;
create or replace rtseq_seq start with 

-- 수정 : recipeFavoriteAndRec에 페이징 추가
CREATE OR REPLACE PROCEDURE recipeFavoriteAndRec(
    p_cur OUT   SYS_REFCURSOR,
    p_startNum IN NUMBER,
    p_endNum IN NUMBER,
    p_kind IN NUMBER
)
IS
BEGIN
    IF p_kind=1 THEN
        OPEN p_cur FOR
        select * from (select * from (select rownum as rn, r.* from ((select * from recipe_page_view where rec>0 order by rnum desc) r)) where rn>=p_startNum) where rn<=p_endNum;
    ELSE 
        OPEN p_cur FOR
        select * from (select * from (select rownum as rn, f.* from ((select * from recipe_page_view where favorites>0 order by favorites desc) f)) where rn>=p_startNum) where rn<=p_endNum;
    END IF;       
END;


select * from recipe_page_view;
select * from recipe_report;
select * from interest;

-- 없는 사람만 실행
create sequence report_seq increment by 1 start with 1;

CREATE TABLE recipe_report
(
	reportseq number(5) NOT NULL,
	id varchar2(50) NOT NULL,
	rnum number(20) NOT NULL,
	reportyn char(1) DEFAULT 'Y',
	PRIMARY KEY (reportseq)
);

ALTER TABLE recipe_report
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;

ALTER TABLE recipe_report
	ADD FOREIGN KEY (id)
	REFERENCES members (id)
	ON DELETE CASCADE
;


-- 수정 : 페이징을 위해 레시피 카테고리와 count 프로시저 분리, kind에 따라 분리
create or replace procedure getCategory(
    p_recipekey IN varchar,
    p_cur OUT SYS_REFCURSOR,
    p_startNum IN NUMBER,
    p_endNum IN NUMBER,
    p_kind IN NUMBER
)
is
begin
    if p_recipekey='recipe' then
        open p_cur FOR 
        select * from (select * from (select rownum as rn, r.* from ((select * from recipe_page_view) r)) where rn>=p_startNum) where rn<=p_endNum;
    elsif p_recipekey='type' then
        open p_cur FOR 
        select * from (select * from (select rownum as rn, t.* from ((select * from type_page_view where type=p_kind) t)) where rn>=p_startNum) where rn<=p_endNum;
    elsif p_recipekey='theme' then
        open p_cur FOR 
        select * from (select * from (select rownum as rn, h.* from ((select * from theme_page_view where theme=p_kind) h)) where rn>=p_startNum) where rn<=p_endNum;
    elsif p_recipekey='ing' then
        open p_cur FOR 
        select * from (select * from (select rownum as rn, i.* from ((select * from ing_page_view where ing=p_kind) i)) where rn>=p_startNum) where rn<=p_endNum;
    end if;

end;

-- 수정 : kind 값도 고려
create or replace procedure getRecipeCounts(
    p_recipekey IN varchar,
    p_kind IN NUMBER,
    p_cnt OUT NUMBER
)
is 
begin
    if p_recipekey='recipe' then
         select count(*) into p_cnt from recipe_page_view;
    elsif p_recipekey='type' then
         select count(*) into p_cnt from type_page_view where type=p_kind;
    elsif p_recipekey='theme' then
         select count(*) into p_cnt from theme_page_view where theme=p_kind;
    elsif p_recipekey='ing' then
         select count(*) into p_cnt from ing_page_view where ing=p_kind;
    elsif p_recipekey='favorite' then -- favorite에서는 kind값 안 들어오는 것 고려
        select count(*) into p_cnt from recipe_page_view where favorites>0;
    elsif p_recipekey='adminRec' then 
        select count(*) into p_cnt from recipe_page_view where rec>0;
    end if;

end;


-- ID 조회
CREATE OR REPLACE PROCEDURE findId(
    p_name IN members.name%TYPE,
    p_phone IN members.phone%TYPE,
    p_rc OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_rc FOR
    select * from members where name=p_name and phone=p_phone;
END;

-- 비밀번호 수정
CREATE OR REPLACE PROCEDURE updatePwd(
    p_id IN members.id%TYPE,
    p_pwd IN members.pwd%TYPE,
    p_result OUT NUMBER
)
IS
BEGIN
    p_result := 1;
    update members set pwd=p_pwd where id=p_id;
    commit;
    
    EXCEPTION WHEN OTHERS THEN
    p_result := 0;
END;



-- 댓글 달기
CREATE OR REPLACE PROCEDURE addReply(
    p_id recipe.id%TYPE,
    p_rnum recipe.rnum%TYPE,
    p_reply reply.content%TYPE
)
IS
BEGIN
    insert into reply(replyseq, id, rnum, content) values(reply_seq.nextVal, p_id, p_rnum, p_reply);
    commit;
END;


-- 검색 결과 count 리턴
CREATE OR REPLACE PROCEDURE getCountsByKey(
    p_table IN NUMBER,
    p_key IN VARCHAR2,
    p_cnt OUT NUMBER
)
IS
     v_cnt NUMBER;
BEGIN
    IF p_table = 1 THEN
        SELECT COUNT(*) INTO v_cnt FROM ingTag i, recipeTag r WHERE i.tag_id = r.tag_id and i.tag LIKE '%'||p_key||'%';
    ELSIF  p_table = 2 THEN
        SELECT COUNT(*) INTO v_cnt FROM recipe WHERE subject LIKE '%'||p_key||'%';
    ELSIF  p_table = 3 THEN
        SELECT COUNT(*) INTO v_cnt FROM recipe WHERE content LIKE '%'||p_key||'%';
    END IF;
    p_cnt := v_cnt;
END;

-- 검색 단어에 대한 레시피 리스트 리턴
CREATE OR REPLACE PROCEDURE selectListByKey(
    p_table IN NUMBER,
    p_key IN recipe.content%TYPE,
    p_startNum IN NUMBER,
    p_endNum IN NUMBER,
    p_rc OUT SYS_REFCURSOR
)
IS
    v_cursor1 SYS_REFCURSOR;
    v_cursor2 SYS_REFCURSOR;
    v_cursor3 SYS_REFCURSOR;
BEGIN
    IF p_table = 1 THEN
        OPEN v_cursor1 FOR
        select * from (
			select * from (
				select rownum as rn, r.* from (
                    (select distinct t.rnum, v.id, v.subject, v.thumbnail, v.nick, v.img, v.likes, v.views, v.time 
                from recipeTag t, recipe_page_view v, ingTag i 
            where t.rnum = v.rnum and t.tag_id = i.tag_id and i.tag like '%'||p_key||'%' ) r ) 
         ) where rn >=p_startNum 
		) where rn <= p_endNum ;
        p_rc := v_cursor1;
    ELSIF  p_table = 2 THEN
        OPEN v_cursor2 FOR
        select * from (
			select * from (
                select rownum as rn, r.* from (
                    (select distinct rnum, id, subject, thumbnail, nick, img, likes, views, time 
                from recipe_page_view
            where subject like '%'||p_key||'%' ) r ) 
         ) where rn >=p_startNum
        ) where rn <=p_endNum ;
         p_rc := v_cursor2;
    ELSIF  p_table = 3 THEN
        OPEN v_cursor3 FOR
        select * from (
			select * from (
                select rownum as rn, r.* from (
                    (select distinct rnum, id, subject, thumbnail, nick, img, likes, views, time 
                from recipe_page_view
            where content like '%'||p_key||'%' ) r ) 
         ) where rn >=p_startNum
        ) where rn <=p_endNum ;
         p_rc := v_cursor3;
    END IF;
END;

-- 각 레시피에 대한 댓글 갯수 리턴
CREATE OR REPLACE PROCEDURE getReplyCount(
    p_rnum IN recipe.rnum%TYPE,
    p_replycnt OUT NUMBER
)
IS
    v_cnt NUMBER;
BEGIN
    select count(*) into v_cnt from reply where rnum = p_rnum;
    p_replycnt := v_cnt;
END;


-- recipe 테이블 업데이트
CREATE OR REPLACE PROCEDURE updateRecipe(
    p_subject IN recipe.subject%TYPE, 
    p_content IN recipe.content%TYPE,
    p_thumbnail IN recipe.thumbnail%TYPE,
    p_time IN recipe.time%TYPE,
    p_type IN recipe.type%TYPE,
    p_theme IN recipe.theme%TYPE,
    p_rnum IN recipe.rnum%TYPE
)
IS
BEGIN
    update recipe set subject=p_subject, content=p_content, thumbnail=p_thumbnail, time=p_time, type=p_type, theme=p_theme where rnum=p_rnum;
    commit;
END;

-- recipeTag, processImg 테이블 레코드 삭제
CREATE OR REPLACE PROCEDURE deleteProcess(
     p_rnum IN recipe.rnum%TYPE
)
IS
BEGIN
    delete from recipeTag where rnum=p_rnum;
    delete from processImg where rnum=p_rnum;
    commit;
    
    EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
END;


-- 수정본 : recipe, recipe_page 테이블 삽입
CREATE OR REPLACE PROCEDURE insertRecipe(
    p_id IN recipe.id%TYPE,
    p_subject IN recipe.subject%TYPE,
    p_content IN recipe.content%TYPE,
    p_thumbnail IN recipe.thumbnail%TYPE,
    p_cookingtime IN recipe.time%TYPE,
    p_type IN recipe.type%TYPE,
    p_theme IN recipe.theme%TYPE,
    p_ing IN recipe_page.ing%TYPE,
    rnum OUT recipe.rnum%TYPE
)
IS
    max_rnum recipe.rnum%TYPE;
BEGIN
    insert into recipe(rnum, id, subject, content, thumbnail, time, type, theme) 
    values(recipe_seq.nextVal, p_id, p_subject, p_content, p_thumbnail, p_cookingtime, p_type, p_theme);
    -- insert into recipe_page(ing) values(p_ing);  -- 또는 recipe 테이블에 ing 필드 추가
    commit;
    select max(rnum) into max_rnum from recipe;
    rnum := max_rnum;
    insert into recipe_page(rnum, ing) values(max_rnum, p_ing);
    commit;
    
    EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
END;

-- processImg 테이블 삽입
CREATE OR REPLACE PROCEDURE insertProcess(
    p_rnum IN processImg.rnum%TYPE,
    p_iseq IN processImg.iseq%TYPE,
    p_links IN processImg.links%TYPE,
    p_description IN processImg.description%TYPE
)
IS
BEGIN
    insert into processImg(rnum, iseq, links, description) values(p_rnum, p_iseq, p_links, p_description);
    commit;
END;

-- 기존 태그 조회해서 count 리턴
CREATE OR REPLACE PROCEDURE getTagCnt(
     p_tag IN ingTag.tag%TYPE,
     p_cnt OUT NUMBER
)
IS
    v_cnt NUMBER;
BEGIN
     select count(*) into v_cnt from ingTag where tag=p_tag;
     p_cnt := v_cnt;
END;

-- 기존 태그가 없다면 ingTag, recipeTag 테이블에 레코드 삽입
CREATE OR REPLACE PROCEDURE insertTag(
     p_tag IN ingTag.tag%TYPE,
    p_rnum IN recipeTag.rnum%TYPE,
     p_qty IN recipeTag.quantity%TYPE
)
IS
    lastTagId ingTag.tag_id%TYPE;
BEGIN
    insert into ingTag(tag_id, tag) values(ingTag_seq.nextVal, p_tag);
    select max(tag_id) into lastTagId from ingTag;
    insert into recipeTag(rnum, tag_id, quantity) values(p_rnum, lastTagId, p_qty); 
    commit;
    
    EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
END;

-- 기존 태그가 있다면 recipeTag 레코드만 삽입
CREATE OR REPLACE PROCEDURE insertRecipeTag(
    p_tag IN ingTag.tag%TYPE,
    p_rnum IN recipeTag.rnum%TYPE,
     p_qty IN recipeTag.quantity%TYPE
)
IS
    lastTagId ingTag.tag_id%TYPE;
BEGIN
    select tag_id into lastTagId from ingTag where tag=p_tag;
    insert into recipeTag(rnum, tag_id, quantity) values(p_rnum, lastTagId, p_qty);
    commit;
END;



-- 제약조건명 조회
SELECT OWNER, CONSTRAINT_NAME, CONSTRAINT_TYPE, TABLE_NAME
FROM USER_CONSTRAINTS;
-- recipe와 관련한 외래키 삭제 => 다시 생성
-- ALTER TABLE 테이블명 DROP CONSTRAINT 제약조건명 (** 개인별로 제약조건명 다르니 조회 후 이름 변경)
ALTER TABLE favorite DROP CONSTRAINT SYS_C007397;
ALTER TABLE interest DROP CONSTRAINT SYS_C007398;
ALTER TABLE processImg DROP CONSTRAINT SYS_C007399;
ALTER TABLE recipeTag DROP CONSTRAINT SYS_C007400;
ALTER TABLE reply DROP CONSTRAINT SYS_C007402;
ALTER TABLE recipe_page DROP CONSTRAINT SYS_C007401;

-- 외래키 생성 (on delete cascade 있는 구문만 실행)
ALTER TABLE recipeTag
	ADD FOREIGN KEY (tag_id)
	REFERENCES ingTag (tag_id)
	
;
ALTER TABLE favorite
	ADD FOREIGN KEY (id)
	REFERENCES members (id)
;
ALTER TABLE interest
	ADD FOREIGN KEY (id)
	REFERENCES members (id)
;
ALTER TABLE qna
	ADD FOREIGN KEY (id)
	REFERENCES members (id)
;
ALTER TABLE recipe
	ADD FOREIGN KEY (id)
	REFERENCES members (id)
;
ALTER TABLE reply
	ADD FOREIGN KEY (id)
	REFERENCES members (id)
;
ALTER TABLE favorite
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;
ALTER TABLE interest
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;
ALTER TABLE processImg
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;
ALTER TABLE recipeTag
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;
ALTER TABLE recipe_page
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;
ALTER TABLE reply
	ADD FOREIGN KEY (rnum)
	REFERENCES recipe (rnum)
	ON DELETE CASCADE
;





-- ing_view 뷰 생성(재료 불러올 때 필요)
CREATE OR REPLACE VIEW ing_view 
AS
SELECT r.rnum, r.tag_id, i.tag, r.quantity FROM recipeTag r, ingTag i where r.tag_id=i.tag_id;
select * from ing_view;

-- 레시피 조회수 증가
CREATE OR REPLACE PROCEDURE addRecipeView(
    p_rnum IN recipe.rnum%TYPE
)
IS
BEGIN
    update recipe_page set views=views+1 where rnum=p_rnum;
    commit;
END;


-- 최종 getRecipe(레시피 불러오기)
create or replace PROCEDURE getRecipe(
    p_rnum IN recipe.rnum%TYPE,
    p_cur1 OUT SYS_REFCURSOR,
    p_cur2 OUT SYS_REFCURSOR,
    p_cur3 OUT SYS_REFCURSOR,
    p_cur4 OUT SYS_REFCURSOR,
    p_cur5 OUT SYS_REFCURSOR,
    p_likeyn OUT VARCHAR,
    p_reportyn OUT VARCHAR,
    p_id IN recipe.id%TYPE
)
IS  
    v_likeyn VARCHAR(1);
    v_reportyn VARCHAR(1);
BEGIN
    OPEN p_cur1 FOR 
        select * from recipe_page_view where rnum=p_rnum;
    OPEN p_cur2 FOR
        -- select tag from ing_view where rnum=p_rnum;
        select * from (select rownum as rn, t.* from ((select tag from ing_view where rnum=p_rnum) t)) order by rn;
    OPEN p_cur3 FOR
        -- select quantity from recipeTag where rnum=p_rnum;
        select * from (select rownum as rn, q.* from ((select quantity from recipeTag where rnum=p_rnum) q)) order by rn;
    OPEN p_cur4 FOR
        select * from processImg where rnum=p_rnum order by iseq;
    OPEN p_cur5 FOR
        select * from reply where rnum=p_rnum;
    BEGIN
        select likeyn into v_likeyn from interest where rnum=p_rnum and id=p_id;
        EXCEPTION WHEN NO_DATA_FOUND then v_likeyn := 'N'; 
    END;
    p_likeyn := v_likeyn;
    BEGIN
        select reportyn into v_reportyn from recipe_report where rnum=p_rnum and id=p_id;
       EXCEPTION WHEN NO_DATA_FOUND then v_reportyn := 'N';
    END;
    p_reportyn := v_reportyn;
END;

-- 수정을 위해 recipe 불러오기
create or replace PROCEDURE getRecipeForUpdate(
    p_rnum IN recipe.rnum%TYPE,
    p_cur1 OUT SYS_REFCURSOR,
    p_cur2 OUT SYS_REFCURSOR,
    p_cur3 OUT SYS_REFCURSOR,
    p_cur4 OUT SYS_REFCURSOR
)
IS  
BEGIN
    OPEN p_cur1 FOR 
        select * from recipe_page_view where rnum=p_rnum;
    OPEN p_cur2 FOR
        -- select tag from ing_view where rnum=p_rnum;
        select * from (select rownum as rn, t.* from ((select tag from ing_view where rnum=p_rnum) t)) order by rn;
    OPEN p_cur3 FOR
        -- select quantity from recipeTag where rnum=p_rnum;
        select * from (select rownum as rn, q.* from ((select quantity from recipeTag where rnum=p_rnum) q)) order by rn;
    OPEN p_cur4 FOR
        select * from processImg where rnum=p_rnum order by iseq;
END;


-- 참고
SELECT * FROM    ALL_CONSTRAINTS
WHERE    TABLE_NAME = 'favorite';
SELECT *
FROM USER_CONSTRAINTS
WHERE TABLE_NAME = 'favorite';