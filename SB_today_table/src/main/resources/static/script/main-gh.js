const addButton = document.getElementById("add-button");

let taskList = [];
let filterList =[];
let mode = "all";

//addButton.addEventListener("click",addTask);
/*const 내용 = `<div class="recipe-process">
                	<label>Profile</label><input type="file" name="img${}">
                	<textarea name="content" rows="4" cols="50"></textarea>
            	</div>`;
*/


let recipeSaveReply=()=>{
	document.recipeReplyAddForm.action="addReply";
	document.recipeReplyAddForm.submit();
}



let abcd = '#소고기 500g #후추 약간 #김치 반포기 #소금 한큰술 #설탕 적당히많이 #물엿 1큰술';

let words1 = abcd.split(' ');

/**
let abcd = '#소고기 500g #후추 약간 #김치 반포기 #소금 한큰술 #설탕 적당히많이 #물엿 1큰술';

let words1 = abcd.split(' ');

let tag = [];
let quantity = [];

console.log(words1);

for(let i = 0; i<words1.length; i++){
  if(words1[i].indexOf('#')==0){
    let temp = words1[i].substr(1);
    tag.push(temp);
  }else{
    quantity.push(words1[i]);
  }
}



console.log(tag);
console.log(quantity);
*/

// 레시피에서 작동할 자바스크립트 모음

function ILikeThis( rnum ){
	let url = 'likeRecipe?rnum='+rnum;
	location.href=url;
}

function deleteThisReply(replyseq){
   let msg = confirm("댓글 삭제하시겠습니까?")
   if (msg==true){
      document.recipeReplyAddForm.action="deleteReply?replyseq="+replyseq;
      document.recipeReplyAddForm.submit();
   }else {
      return false;
   }
}

function footer_go_to( destination ){
	location.href=destination;
}

