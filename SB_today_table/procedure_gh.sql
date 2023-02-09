-- 겟 올 레시피
create or replace procedure getAllRecipe(
    p_cur OUT SYS_REFCURSOR
)
is 
begin
    open p_cur FOR
        select * from recipe_page_view;
end;



-- 겟 배너 레시피
CREATE OR REPLACE PROCEDURE getRecipeBannerList(
    p_cur OUT   SYS_REFCURSOR
)
IS
BEGIN
    open p_cur for
        select * from recipebanner order by useyn desc, order_seq asc;
END;

-- 레시피 배너 테이블 생성
create table recipebanner(
    bseq number(5) primary key,
    name varchar2(100),
    order_seq number(1),
    image varchar2(50),
    useyn char(1) default 'y',
    indate date default sysdate
);

-- 레시피 배너 시퀀스 생성
create sequence tt_banner_seq start with 1;


-- 레시피 카테고리 프로시져
create or replace procedure recipeCategory(
    p_recipekey IN varchar,
    p_cur OUT SYS_REFCURSOR
)
is 
begin
    if p_recipekey='recipe' then
        open p_cur FOR select * from recipe_page_view;
    elsif p_recipekey='type' then
        open p_cur FOR select * from type_page_view;
    elsif p_recipekey='theme' then
        open p_cur FOR select * from theme_page_view;
    elsif p_recipekey='ing' then
        open p_cur FOR select * from ing_page_view;
    end if;
    
end;







