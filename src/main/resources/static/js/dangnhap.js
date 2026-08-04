const toggle = document.getElementById("togglePassword");
const password = document.getElementById("password");

// toggle.addEventListener("click", () => {
//
//     if (password.type === "password") {
//         password.type = "text";
//     } else {
//         password.type = "password";
//     }
//
// });

document.addEventListener("DOMContentLoaded", function () {

    const toggle = document.getElementById("togglePassword");
    const password = document.getElementById("password");

    toggle.addEventListener("click", function () {
        password.type = password.type === "password" ? "text" : "password";
    });

    const loginForm = document.getElementById("loginForm");
    const usernameInput = document.getElementById("username");
    const usernameError = document.getElementById("usernameError");

    if(loginForm) {
        loginForm.addEventListener("submit", function(e) {
            const val = usernameInput.value.trim();
            // Email regex
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            // Phone regex: Starts with 0, total 10 digits
            const phoneRegex = /^0\d{9}$/;

            if (!emailRegex.test(val) && !phoneRegex.test(val)) {
                e.preventDefault();
                usernameError.textContent = "Vui lòng nhập đúng định dạng Email hoặc Số điện thoại (10 số, bắt đầu bằng 0).";
                usernameError.style.display = "block";
                usernameInput.focus();
            } else {
                usernameError.style.display = "none";
            }
        });
    }

});