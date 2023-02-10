-- 레시피 배너 테이블 생성
create table recipebanner(
    bseq number(5) primary key,
    name varchar2(100),
    order_seq number(1),
    image varchar2(50),
    useyn char(1) default 'y',
    indate date default sysdate
);

-- 겟 배너 레시피
CREATE OR REPLACE PROCEDURE getRecipeBannerList(
    p_cur OUT   SYS_REFCURSOR
)
IS
BEGIN
    open p_cur for
        select * from recipebanner order by useyn desc, order_seq asc;
END;

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


--// 좋아요 세팅에 필요한건 rnum과 id
--// 우선 좋아요를 누른 사람이 이 게시물에 좋아요를 누른적이 있는지 없는지 검사해야함.
--// 분기가 완전히 갈리는데,
--// 그 게시물에 좋아요를 한적이 없는 유저라면 Like는 Y로, Favorite는 N으로 insert가 이루어져야한다.
--// 좋아요를 한적이 있는 유저라면 그때부터는 like, favorite Y, N 바꾸기만 하면 된다.
--// 전부 데이터베이스에서만 왔다갔다 하기 때문에 아웃변수를 잡아줄 필요는 없다.

--// 위의 작업이 다 끝난 후에 recipe-page에 해당 rnum의 총 좋아요 갯수를 업데이트한다.
CREATE OR REPLACE PROCEDURE likeRecipe(
    p_id IN recipe.id%TYPE,
    p_rnum IN recipe.rnum%TYPE
)
IS
	v_idcount NUMBER;
	v_yn varchar2(10);
	v_likecount NUMBER;
BEGIN
    -- 누른적이 있는지 없는지 검사합니다. 눌렀다면 1 이상의 값이 v_idcount 에 삽입될 것이고 누르지 않았다면 v_idcount는 0입니다.
	select count(*) into v_idcount from interest where rnum = p_rnum and id =p_id;

    if v_idcount>0 then
        select likeyn into v_yn from interest where id=p_id and rnum=p_rnum;
        if v_yn='Y' then
            update interest set likeyn='N' where id = p_id and rnum=p_rnum;
            commit;
        elsif v_yn='N' then
            update interest set likeyn='Y' where id = p_id and rnum=p_rnum;
            commit;
        end if;
    elsif v_idcount=0 then 
        insert into interest (interestnum, rnum , id, likeyn) values( interestnum_seq.nextVal, p_rnum, p_id, 'Y');
        insert into favorite (fnum, rnum, id, fuseyn) values( fnum_seq.nextVal, p_rnum, p_id, 'N');
        commit;
    end if;

	-- 위의 과정까지 종료되었다면, 고유한 interestNum, favoriteNum을 가진 행이 삽입되어 Y또는 N값을 가지게 되었습니다.
	-- 이젠 다시 카운트해서 recipe_page의 값을 업데이트 해줍니다.
	select count(*) into v_likecount from interest where rnum = p_rnum and likeyn='Y';
	update recipe_page set likes = v_likecount where rnum = p_rnum;
    commit;
END;


