
select * from qna;

commit;

CREATE OR REPLACE PROCEDURE getAllCount(
    p_cnt OUT NUMBER
)
IS
    v_cnt NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_cnt FROM QNA;
    p_cnt := v_cnt;
END;



CREATE OR REPLACE PROCEDURE listQna (
    p_startNum IN NUMBER,
    p_endNum IN NUMBER,
    p_rc   OUT     SYS_REFCURSOR )
IS
BEGIN
    OPEN p_rc FOR
        SELECT * FROM (
        SELECT * FROM (
        SELECT ROWNUM AS rn, q.* FROM ((SELECT * FROM QNA ORDER BY qseq desc)q) 
        ) WHERE rn>= p_startNum
        ) WHERE rn<= p_endNum;
END;

CREATE OR REPLACE PROCEDURE oneQna (
    p_qseq IN   Qna.qseq%TYPE,
    p_rc   OUT     SYS_REFCURSOR )
IS
BEGIN
    OPEN p_rc FOR
        SELECT * FROM qna WHERE qseq=p_qseq;
END;







