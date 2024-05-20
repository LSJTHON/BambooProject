const modifyButton = document.getElementById("modify-file-Config");
const deleteUser = document.getElementById("delete-user");



if(deleteUser){
    deleteUser.addEventListener("click",(event) => {
        fetch("/deleteMember",{
            method : "PUT",
            headers: {
                "Content-Type" : "application/json",
            },
            body : JSON.stringify({
                email:document.getElementById("userEmail").value,
                isDeleted:1,
            }),
        }).then(()=>{
            alert("사용자 정지 완료했습니다.");
        });
    });
}


if(modifyButton){
    modifyButton.addEventListener("click",(event) => {
        fetch("/modifyFileAllowed",{
            method : "PUT",
            headers: {
                "Content-Type" : "application/json",
            },
            body : JSON.stringify({
                configId: 1,
                maxFileCount:document.getElementById("maxFileCount").value,
                maxFileSize:document.getElementById("maxFileSize").value,
                extensions:tagify.value.map(obj => obj.value),
            }),
        }).then(()=>{
            alert("제한자 수정 완료되었습니다.");
        });
    });
}

var input = document.querySelector('input[name="tags"]');
                        //사진 확장자
var whitelist = ["jpeg","jpg","png","gif",
    //동영상 확장자
    "mp4","avi","mkv","mpg",
    //문서 확장자
    "doc","docx","xls","xlsx","ppt","pptx","pdf","txt","csv","json","rtf","odt",
    //압축파일 확장자
    "zip","tar.gz","rar"];

var tagify = new Tagify(input, {
    whitelist:whitelist,
    maxTags: 100,
    dropdown: {
        maxItems: 20,
        classname: "tags-look",
        enabled: 0,
        closeOnSelect: false
    }
});

tagify.on('add', function() { //태그를 추가했을 시 발생하는 이벤트 처리
    console.log(tagify.value); // 입력된 태그 정보 객체 json 받아옴

    var valuesArray = tagify.value.map(obj => obj.value);

    console.log(valuesArray);
});


