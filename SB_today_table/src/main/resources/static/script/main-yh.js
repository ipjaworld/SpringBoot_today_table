function selectBoxChange(value){
document.formm.action="qnaWriteForm&secrett="+value;
document.formm.submit();
}

function pwdcheck(qnapwd,qseq,refer){
	alert(refer);
   let pwd = prompt('비밀번호를 입력해주세요' , '비밀번호');
   if(pwd.valueOf()==qnapwd.valueOf()){
      location.href = "qnaDetail?qseq="+qseq+"&refer="+refer;
      
   }else{
      alert("비밀번호가 맞지 않습니다")
      location.href="qnaList";
   }
}

function chgChk(checkbox){
   
     const tbox = document.getElementById('pass');
     tbox.disabled = checkbox.checked ? false : true;
     
     if(tbox.disabled) {
       tbox.value = null;
       
     }else {
       tbox.focus();
     }
     }
     
function deleteQna(qseq,refer){
  let result = prompt('삭제하시려면 삭제라고 작성해주세요', '삭제');
  
   if ( result=='삭제'){
      location.href="deleteQna?qseq="+qseq+"&refer="+refer;
      }else{
      alert('똑같이 작성해 주세요')
         return;
      }
   }
     
     