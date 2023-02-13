select * from recipe;
select * from recipe_page;
select * from recipe_page_view;
select * from reply;
select * from recipeTag;
select * from ingTag;
select * from processImg;


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


-- recipe, recipe_page 테이블 삽입
CREATE OR REPLACE PROCEDURE insertRecipe(
    p_id IN recipe.id%TYPE,
    p_subject IN recipe.subject%TYPE,
    p_content IN recipe.content%TYPE,
    p_thumbnail IN recipe.thumbnail%TYPE,
    p_cookingtime IN recipe.time%TYPE,
    p_type IN recipe.type%TYPE,
    p_theme IN recipe.theme%TYPE,
    rnum OUT recipe.rnum%TYPE
)
IS
    max_rnum recipe.rnum%TYPE;
BEGIN
    insert into recipe(rnum, id, subject, content, thumbnail, time, type, theme) 
    values(recipe_seq.nextVal, p_id, p_subject, p_content, p_thumbnail, p_cookingtime, p_type, p_theme);
    select max(rnum) into max_rnum from recipe;
    rnum := max_rnum;
    insert into recipe_page(rnum) values(max_rnum);
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
-- ALTER TABLE 테이블명 DROP CONSTRAINT 제약조건명
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

-- 레시피 디테일 불러오기
CREATE OR REPLACE PROCEDURE getRecipe(
    p_rnum IN recipe.rnum%TYPE,
    p_cur1 OUT SYS_REFCURSOR,
    p_cur2 OUT SYS_REFCURSOR,
    p_cur3 OUT SYS_REFCURSOR,
    p_cur4 OUT SYS_REFCURSOR,
    p_cur5 OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_cur1 FOR 
        select * from recipe_page_view where rnum=p_rnum;
    OPEN p_cur2 FOR
        select tag from ing_view where rnum=p_rnum;
    OPEN p_cur3 FOR
        select quantity from recipeTag where rnum=p_rnum;
    OPEN p_cur4 FOR
        select * from processImg where rnum=p_rnum order by iseq;
    OPEN p_cur5 FOR
        select * from reply where rnum=p_rnum;
END;


-- 참고
SELECT * FROM    ALL_CONSTRAINTS
WHERE    TABLE_NAME = 'favorite';
SELECT *
FROM USER_CONSTRAINTS
WHERE TABLE_NAME = 'favorite';