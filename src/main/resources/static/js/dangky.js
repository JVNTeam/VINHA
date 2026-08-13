document.addEventListener("DOMContentLoaded", function () {
    // 1. Xử lý Toggle Ẩn / Hiện Mật khẩu và Đổi Icon FontAwesome
    function setupPasswordToggle(toggleId, inputId) {
        const toggleBtn = document.getElementById(toggleId);
        const passwordInput = document.getElementById(inputId);

        if (toggleBtn && passwordInput) {
            toggleBtn.addEventListener("click", function (e) {
                e.preventDefault();
                const isPassword = passwordInput.type === "password";

                // Đổi type của input
                passwordInput.type = isPassword ? "text" : "password";

                // Mặc định đang để password -> icon gạch chéo (fa-eye-slash)
                // Khi bấm hiện text -> đổi sang icon mở mắt (fa-eye)
                toggleBtn.classList.toggle("fa-eye-slash", !isPassword);
                toggleBtn.classList.toggle("fa-eye", isPassword);
            });
        }
    }

    setupPasswordToggle("togglePassword", "password");
    setupPasswordToggle("toggleConfirmPassword", "confirmPassword");

    // 2. Validate Form Đăng ký (Validate Email/SĐT & Mật khẩu & Tên)
    const registerForm = document.getElementById("registerForm");
    const clientError = document.getElementById("clientError");

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;

    if (registerForm) {
        registerForm.addEventListener("submit", function (e) {
            const hoTenInput = document.getElementById("hoTen");
            const contactInput = document.getElementById("contactInput");
            const passwordInput = document.getElementById("password");
            const confirmInput = document.getElementById("confirmPassword");

            if (!hoTenInput || !contactInput || !passwordInput || !confirmInput) return;

            const hoTenVal = hoTenInput.value.trim();
            const contactVal = contactInput.value.trim();
            const pwd = passwordInput.value;
            const confirmPwd = confirmInput.value;

            const contactWrap = contactInput.closest(".register-input-wrap");
            const passwordWrap = passwordInput.closest(".register-input-wrap");
            const confirmWrap = confirmInput.closest(".register-input-wrap");

            if (contactWrap) contactWrap.classList.remove("error");
            if (passwordWrap) passwordWrap.classList.remove("error");
            if (confirmWrap) confirmWrap.classList.remove("error");

            let errorMsg = "";

            if (hoTenVal === "") {
                errorMsg = "Vui lòng nhập Họ và tên!";
            } else if (contactVal === "") {
                errorMsg = "Vui lòng nhập Email hoặc Số điện thoại!";
            } else if (!emailRegex.test(contactVal) && !phoneRegex.test(contactVal)) {
                errorMsg = "Vui lòng nhập Email hoặc Số điện thoại hợp lệ!";
                if (contactWrap) contactWrap.classList.add("error");
            } else if (pwd === "") {
                errorMsg = "Vui lòng nhập mật khẩu!";
            } else if (confirmPwd === "") {
                errorMsg = "Vui lòng xác nhận mật khẩu!";
            } else if (pwd !== confirmPwd) {
                errorMsg = "Mật khẩu xác nhận không trùng khớp!";
                if (confirmWrap) confirmWrap.classList.add("error");
            }

            if (errorMsg !== "") {
                e.preventDefault();
                alert(errorMsg); // Hiển thị bảng thông báo popup (alert box)

                                if (clientError) {
                    clientError.innerText = errorMsg;
                    clientError.style.display = "block";
                    clientError.style.opacity = '1';
                    
                    // Auto-hide after 4 seconds
                    setTimeout(function() {
                        clientError.style.transition = 'opacity 0.5s ease';
                        clientError.style.opacity = '0';
                        setTimeout(function() {
                            clientError.style.display = 'none';
                        }, 500);
                    }, 4000);
                }
            } else if (clientError) {
                clientError.style.display = "none";
            }
        });
    }
});

