document.addEventListener("DOMContentLoaded", function () {

    // 1. Toggle Ẩn/Hiện mật khẩu bằng FontAwesome Icon
    const toggleBtn = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    if (toggleBtn && passwordInput) {
        toggleBtn.addEventListener("click", function (e) {
            e.preventDefault();
            const isPassword = passwordInput.type === "password";

            // Thay đổi type
            passwordInput.type = isPassword ? "text" : "password";

            // Chuyển class qua lại giữa mắt gạch (fa-eye-slash) và mắt mở (fa-eye)
            toggleBtn.classList.toggle("fa-eye-slash", !isPassword);
            toggleBtn.classList.toggle("fa-eye", isPassword);
        });
    }

    // 2. Validate Form Đăng Nhập
    const loginForm = document.getElementById("loginForm");
    const usernameInput = document.getElementById("username");
    const usernameError = document.getElementById("usernameError");

    if (loginForm && usernameInput) {
        loginForm.addEventListener("submit", function (e) {
            const val = usernameInput.value.trim();

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const phoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;

            const inputWrap = usernameInput.closest(".login-input-wrap");

            if (!emailRegex.test(val) && !phoneRegex.test(val)) {
                e.preventDefault();

                if (usernameError) {
                    usernameError.textContent = "Vui lòng nhập Email hoặc Số điện thoại hợp lệ!";
                    usernameError.style.display = "block";
                }

                if (inputWrap) {
                    inputWrap.classList.add("error");
                }

                usernameInput.focus();
            } else {
                if (usernameError) usernameError.style.display = "none";
                if (inputWrap) inputWrap.classList.remove("error");
            }
        });

        usernameInput.addEventListener("input", function () {
            if (usernameError) usernameError.style.display = "none";
            const inputWrap = usernameInput.closest(".login-input-wrap");
            if (inputWrap) inputWrap.classList.remove("error");
        });
    }
});