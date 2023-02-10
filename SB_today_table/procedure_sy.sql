select * from recipe;
select * from recipe_page;
select * from recipe_page_view;
select * from reply;
select * from recipeTag;
select * from ingTag;
select * from processImg;

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