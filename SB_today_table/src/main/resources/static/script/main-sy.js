// 레시피 검색 후 결과 페이지 이동
$(function(){
	$('#searchByKey').click(function(){
		const selected = $('#condition option:selected').val();
		/*alert("condition : " + selected);*/
		let key = $('#key').val();
		location.href = "recipe.do?command=recipeList&start='Y'&key="+ key + "&condition=" + selected;
	});
});



// 로그인 폼
function loginCheck(){
	if(document.loginFrm.id.value==""){
		alert("아이디는 필수입력사항입니다.");
		document.loginFrm.id.focus();
		return false;
	}else if(document.loginFrm.pwd.value==""){
		alert("비밀번호는 필수입력사항입니다.");
		document.loginFrm.pwd.focus();
		return false;
	}else{
		return true;
	}
}

// 계정 찾기 폼 이동
function find_account(){
	location.href="recipe.do?command=findAccountForm";
}

// 아이디, 비번 찾기 폼 이동
function findAccountBy( param){
	let url = "recipe.do?command=findAccountBy&param=" + param;
	let opt = "toolbar=no, menubar=no, scrollbars=no, resizable=no, width=550, height=300, top=300, left=300";
	window.open(url, param + " 찾기", opt);
	
}


// 전화번호 입력 시 - 자동 추가
function autoHypenPhone(str){
    str = str.replace(/[^0-9]/g, '');
    var tmp = '';
    if( str.length < 4){
        return str;
    }else if(str.length < 7){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3);
        return tmp;
    }else if(str.length < 11){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3, 3);
        tmp += '-';
        tmp += str.substr(6);
        return tmp;
    }else{              
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3, 4);
        tmp += '-';
        tmp += str.substr(7);
        return tmp;
    }
    return str;
}

// 인증번호 타이머 구현 
const timerBtn = document.querySelector("#timerBtn");
const timerspan = document.querySelector("#timerspan");
let timeInterval;
let pomoTime = 10;
const certinoti = document.querySelector("#certinoti");
const certi = document.querySelector('#certi');

function timerStart() {
	if(timerBtn.value == "전송"){
		timerBtn.value = "재전송";
		timerBtn.disabled = true;
		timer();
  		timeInterval = setInterval(timer, 1000);
		certi.disabled= false;
	}else{
		document.querySelector('input[name="certiNum"]').value= "";
		timerBtn.disabled = true;
		certinoti.innerHTML = "";
		pomoTime = 60;
		timer();
		timeInterval = setInterval(timer, 1000);
		certi.disabled= false;
	}
  	
}

function timer() {
  const seconds = String(pomoTime % 60).padStart(2, "0");
  pomoTime -= 1;
  timerspan.innerHTML = `${seconds}`;
  if (pomoTime < 0) {
    clearInterval(timeInterval);
	certinoti.innerHTML = "인증시간이 만료되었습니다.";
	certi.disabled= true;
	timerBtn.disabled = false;
  }
}

function confirmNum(){
	let phone = document.querySelector('input[name="phone"]').value;
	let certiNum = document.querySelector('input[name="certiNum"]').value;
	console.log("certiNum : " + certiNum);
	let phoneNum = phone.slice(9,13);
	console.log("phoneNum : " + phoneNum);
	let searchBtn = document.querySelector('#searchBtn');
	if(certiNum == ""){	// 인증번호가 입력한 전화번호의 끝 4자리와 같다면
		certinoti.innerHTML = "인증번호를 입력하세요."
	} else if( certiNum == phoneNum){
		searchBtn.disabled=false;
	}else{	// 인증번호가 다르다면
		certinoti.innerHTML = "인증번호가 다릅니다.";
	}
		
}

// 아이디, 비밀번호 찾기

function search(param){
		document.frm.action = "recipe.do?command=findAccBy&param="+param;
		document.frm.submit();
}

function updatePass(){
	// 버튼 누르면 id, pwd 전송돼야 함 (비공개로/ submit으로)
	document.frm.action = "recipe.do?command=makeNewPass";
	document.frm.submit();
	// window.opener.location.href = "recipe.do?command=loginForm"
	// self.close(); 
	
}
// 레시피 쓰기에서 '시간' 필드에 숫자 입력만 허용 (현재 사용 x)
function onlyNumberKey(evt) {
			
            // Only ASCII character in that range allowed
            let ASCIICode = (evt.which) ? evt.which : evt.keyCode
            if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
                return false;
            return true;
			
			
}
        

// 공백이 허용되지 않는 input 태그에서 복사 붙여넣기 금지(현재 사용 x)
$(function() {
    $('input.disablecopypaste').bind('copy paste', function (e) {
       e.preventDefault();
    });
  });

// 폼의 특정 태그가 채워졌을 때만 submit button 활성화

$(function(){
	 $("form input, form textarea").on("keyup change",function () {
		let empty = true;
		if($('#writeBtn').length == 1){ // recipeWriteForm이라면
			$('form input, form textarea').each(function(){
			if( $('input[name="subject"]').val() != '' && $('textarea[name="content"]').val() != '' 
				&& $('input[name="checkIng"]').val() != '' && $('input[name="cookingTime"]').val() != ''
				&& $('textarea[name="processDetail1"]').val() != '' && $('input[name="thumbnail"]').val() != ''
				&& $('input[name="processImg1"]').val() != '' && $('input[name="ingredient"]').val() == ''
				){
					empty = false;
				}
			});
			
		}else if($('#updateBtn').length == 1){ // recipeUpdateForm이라면
			$('form input, form textarea').each(function(){
			if( $('input[name="subject"]').val() != '' && $('textarea[name="content"]').val() != '' 
				&& ($('input[name="checkIng"]').val() != '' || $('.ex').text() != '' ) && $('input[name="cookingTime"]').val() != ''
				&& $('textarea[name="processDetail1"]').val() != '' && $('input[name="ingredient"]').val() == ''
				){
					empty = false;
				}
			});
		}
		
		if(empty){
			$('#writeBtn').attr('disabled', 'disabled');
			$('#updateBtn').attr('disabled', 'disabled');
		}else{
			$('#writeBtn').removeAttr('disabled');
			$('#updateBtn').removeAttr('disabled');
		}
		
	});
	
});

/* 백업
$(function(){
	 $("form input, form textarea").on("keyup change",function () {
		let empty = true;
		
		$('form input, form textarea').each(function(){
			if( $('input[name="subject"]').val() != '' && $('textarea[name="content"]').val() != '' 
				&& $('input[name="checkIng"]').val() != '' && $('input[name="cookingTime"]').val() != ''
				&& $('textarea[name="processDetail1"]').val() != '' && $('input[name="thumbnail"]').val() != ''
				&& $('input[name="processImg1"]').val() != ''
				){
					empty = false;
				}
		});
		if(empty){
			$('#writeBtn').attr('disabled', 'disabled');
			$('#updateBtn').attr('disabled', 'disabled');
		}else{
			$('#writeBtn').removeAttr('disabled');
			$('#updateBtn').removeAttr('disabled');
		}
		
	});
	
});
*/

function go_recipe(comm){
	let divCount = $('.recipe-process').length;
	let checkIng = $('input[name="checkIng"]').val();
	if(comm == 'updateRecipe'){
		checkIng += $('.ex').text();
		$('input[name="checkIng"]').val(checkIng);
	}else{
		checkIng = $('input[name="checkIng"]').val();
	} 
	/*alert("checkIng : " + checkIng + "divCount : " + divCount ); // 확인용*/ 
	document.frm.action = "recipe.do?command="+comm + "&count="+ divCount;
	document.frm.submit();
}

/* 백업용
function go_recipe(comm){
	let divCount = $('.recipe-process').length;
	// let divCount = $('#add-button').prev('div').attr('name');
	alert('divCount : ' + divCount);
	let checkIng = document.getElementsByName('checkIng').value;
	if(comm == 'updateRecipe'){
		checkIng += document.getElementsByClassName('ex').value;
	}else{
		checkIng = document.getElementsByName('checkIng').value;
	} 
	alert("checkIng : " + checkIng);
	// document.frm.action = "recipe.do?command="+comm + "&count="+ divCount;
	// document.frm.submit();
}
*/

// const addButton = document.getElementById("add-button");


//addButton.addEventListener("click",addTask);
/*const 내용 = `<div class="recipe-process">
                   <label>Profile</label><input type="file" name="img${}">
                   <textarea name="content" rows="4" cols="50"></textarea>
               </div>`;
*/

/*
let i = 1; // 추후 수정
$(function(){
            $('#add-button').click(function(){
			i++;
            $('#add-button').before(`<div class="recipe-process">
                   <input type="file" name="processImg${i}">
                   <textarea name="processDetail${i}" rows="2" cols="50"></textarea>
               </div>`);
			document.getElementById('count').value=i;
            alert("추가 완료");
			console.log(i);
		 });
      });
*/

$(function(){ // 레시피 쓰기/수정에서 '추가' 버튼 눌렀을 때
            $('#add-button').click(function(){
			let name = $('.recipe-process').length;
			let i = name + 1;
			/*alert("name : " + name);*/
			if( $('.recipe-update-form-process').length >= 1){ // 수정 폼이라면
				$('div[name="' + name + '"]').after(`<div class="recipe-process" class="recipe-update-form-process"  
					name="${i}">
                   <input type="file" name="processImg${i}">
                   <textarea name="processDetail${i}" rows="2" cols="50"></textarea>
               		</div>`);
			}
           	else { // 쓰기 폼이라면
				$('div[name="' + name + '"]').after(`<div class="recipe-process" name="${i}">
                   <input type="file" name="processImg${i}">
                   <textarea name="processDetail${i}" rows="2" cols="50"></textarea>
               </div>`);
			}
			alert("추가 완료");
			console.log('추가된 i : ' +  i);
		 });
      });

$(function(){ 
            $('#delete-button').click(function(){
			// let delete_name = $('#add-button').prev('div').attr('name');
			let delete_name = $('.recipe-process').length;
			// let j = Number(delete_name);
			let j = delete_name;
			console.log("삭제할 div 번호 : "  + j);
            // $('#add-button').prev('div').remove();
			$('div[name="'+j+'"]').remove();
			alert("삭제 완료");
		 });
      });



/*
function countProcess(){
	$(function(){
		let count = $('.recipe-process').length;
		console.log('count : ' + count);
		return count;
		
	})
}
*/

function alertLogin(){
	alert("레시피 작성자만 수정이 가능합니다.");
}

// 재료와 양 입력 칸 공백 방지 (하나로 합치기 가능??)
/*
document.frm.querySelector('input[name="ingredient"]').addEventListener('keydown', function(e) {
  	console.log(e.code);
  if (e.code === 'Space') {
    	e.preventDefault();
  	}
});
document.frm.querySelector('input[name="quantity"]').addEventListener('keydown', function(e) {
  	console.log(e.code);
  if (e.code === 'Space') {
    	e.preventDefault();
  	}
});
*/

function passString(){
	let ingStr = document.frm.ingredient.value; // 재료 칸에 입력된 문자열
	let quanStr = document.frm.quantity.value;
	//if( str == "") return; 
	if(window.event.keyCode == 13){ // 엔터키가 눌렸을 때
		if( (ingStr == '') || (quanStr == '')  ){ 
			alert("재료를 입력하세요"); document.frm.ingredient.focus(); 
		}
		else if(ingStr.startsWith("#")) document.frm.checkIng.value += (ingStr);
		else {
			ingStr = "#" + ingStr; // 입력한 재료 앞에 # 추가
			document.frm.checkIng.value += (ingStr + " " + quanStr + " ");
		}
		document.frm.ingredient.value = ""; 
		document.frm.quantity.value = "";
		document.frm.ingredient.focus();
	}
	
}

function clearIng(){
	document.frm.checkIng.value="";
	document.frm.ingredient.focus();
}

function deleteIng(index, btn){
	document.getElementById("ex" + index).remove();
	let button = btn;
	button.remove();
}




