select * from recipe;
select * from recipe_page;
select * from recipe_page_view;
select * from reply;
select * from recipeTag;
select * from ingTag;
select * from processImg;

-- ing_view 테이블 생성(재료 불러올 때 필요)
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
