
document.addEventListener("DOMContentLoaded", function () {
    const deleteUser = document.getElementById("delete-user");
    const dangerMember = document.getElementById("danger-member");
    if (deleteUser) {
        deleteUser.addEventListener("click", (event) => {
            fetch("/deleteMember", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: document.getElementById("deleteEmail").value,
                    isDeleted: 1,
                }),
            }).then(() => {
                alert("사용자 정지 완료했습니다.");
                window.location.href = '/logout';
            });
        });
    }
    if (dangerMember) {
        dangerMember.addEventListener("click", (event) => {
            fetch("/dangerMember", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: document.getElementById("dangerEmail").value,
                    isDeleted: 1,
                }),
            }).then(() => {
                alert("위험한 사용자 정지 완료했습니다.");
                window.location.href = '/';
            });
        });
    }
});




document.addEventListener("DOMContentLoaded", function () {
    const restoreButtons = document.querySelectorAll(".restore-button");

    restoreButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            const email = this.getAttribute("data-email");

            if (confirm("해당 유저를 정말 복원하시겠습니까?")) {
                fetch("/restoredMember", {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        email: email,
                        isDeleted: 0,
                    }),
                }).then(() => {
                    alert("사용자 복원 완료했습니다.");
                    window.location.href = '/memberManagement';
                }).catch(error => {
                    console.error('Error:', error);
                });
            }
        });
    });
});