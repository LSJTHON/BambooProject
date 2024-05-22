// const modifyName = document.getElementById("modifyInfo");
//
//
// if(modifyName){
//     console.log("여기로 오기는 하나? 스크립트")
//     modifyName.addEventListener("click",(event) => {
//         fetch("/modifyMember",{
//             method : "PUT",
//             headers: {
//                 "Content-Type" : "application/json",
//             },
//             body : JSON.stringify({
//                 name:document.getElementById("modifyName").value,
//                 email:document.getElementById("hiddenEmail").value,
//                 password: document.getElementById("modifyPassword").value,
//                 confirmPassword: document.getElementById("confirmModifyPassword").value,
//             }),
//         }).then(()=>{
//             alert("사용자 수정 완료했습니다.");
//         });
//     });
// }