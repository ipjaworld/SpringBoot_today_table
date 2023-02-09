function go_next(){
   // 자바스크립트에서 jsp 페이지 내의 radio 버튼을 바라볼때, 같은 name의 okon 인것이 여러개라면 
   // name  값에 의한 배열로 자동 인식되어 사용됩니다.
   // 동의함 버튼 : okon[0],  동의안함  버튼 : okon[1]
   
   if( document.contractFrm.okon[1].checked == true ){
      // 동의 안함이 선택되었다면
      alert('회원 약관에 동의 하셔야 회원으로 가입이 가능합니다');
   }else{
      document.contractFrm.action = "joinForm";
      document.contractFrm.submit();
      // contractFrm 폼에  action 도 없고 submit 버튼도 없는 것을 스크립트 명령으로 action 값을 설정하고 
      // submit() 메서드로 이동까지 실행합니다
   }
}

function idcheck(){
   if( document.joinForm.id.value=="" ){
      alert("아이디를 입력하고 중복체크를 진행하세요" );
      documnet.joinForm.id.focus();
      return;
   }
   var url = "idCheckForm&id=" + document.joinForm.id.value;
   var opt = "toolbar=no, menubar=no, resizable=no, width=500, height=250, scrollbars=no";
   window.open(url, "IdCheck", opt);   
}


function idok( userid ){
	opener.formm.id.value = userid;
	opener.formm.reid.value = userid;
	self.close();
}
