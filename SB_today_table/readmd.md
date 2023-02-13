2023.02.13 readme

0. 사용하는 라이브러리 코드

<!--#jstl 관련 임포트  -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!--#폰트어썸 임포트  --> 폰트어썸이란? 무료 아이콘이나 문서 템플릿 등을 이용할 수 있는 라이브러리(현업에서도 자주 사용)
<script src="https://kit.fontawesome.com/74c64a7de1.js" crossorigin="anonymous"></script>

<!--#제이쿼리 임포트  -->
<script src="script/jquery-3.6.3.js" type="text/javascript"></script>

<!--#부트스트랩(css만) 임포트  -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
		integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
		crossorigin="anonymous">


1.
테이블과 더미데이터, 뷰들은 final_table, final_values, final_view 에 최신화할 것


2. 
깃허브로 협업을 할 때 가급적이면 이름이 같은 파일은 작업을 피하고, 피치 못하게 해야할 경우 브랜치를 만들어서 3 way merge를 할 수 있으면 좋고,
그게 쉽지 않다면 파일을 수동으로 비교해서 합치는게 좋습니다. 이런 경우 vscode에서 파일을 복수 선택한 후 compare selected 옵션을 통해 비교하면 실수와 시간낭비를 줄일 수 있습니다.


3.
오류가 났을 때 가장 우선적으로 점검해야할 곳은 주소창과 콘솔창입니다. 콘솔창에서는 가장 위에 뜬 에러 메시지의 오른쪽 끝 부분에 원인이 있는 경우가 많습니다.




