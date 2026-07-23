document.addEventListener("DOMContentLoaded", function () {

    // Toggle show/hide mật khẩu
    const togglePassword = document.getElementById("togglePassword");
    const password = document.getElementById("password");

    if (togglePassword && password) {
        togglePassword.addEventListener("click", function () {
            const isPassword = password.type === "password";
            password.type = isPassword ? "text" : "password";
            this.src = isPassword
                ? this.src.replace("eye-off.svg", "eye.svg")
                : this.src.replace("eye.svg", "eye-off.svg");
        });
    }

    // Toggle show/hide xác nhận mật khẩu
    const toggleConfirm = document.getElementById("toggleConfirmPassword");
    const confirmPassword = document.getElementById("confirmPassword");

    if (toggleConfirm && confirmPassword) {
        toggleConfirm.addEventListener("click", function () {
            const isPassword = confirmPassword.type === "password";
            confirmPassword.type = isPassword ? "text" : "password";
            this.src = isPassword
                ? this.src.replace("eye-off.svg", "eye.svg")
                : this.src.replace("eye.svg", "eye-off.svg");
        });
    }

    // Validate form cơ bản phía client
    const registerForm = document.getElementById("registerForm");

    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            const hoTen = registerForm.querySelector("input[name='hoTen']");
            const email = registerForm.querySelector("input[name='email']");
            const soDienThoai = registerForm.querySelector("input[name='soDienThoai']");
            const matKhau = registerForm.querySelector("input[name='matKhau']");
            const xacNhanMatKhau = registerForm.querySelector("input[name='xacNhanMatKhau']");
            const acceptTerms = document.getElementById("acceptTerms");

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const phoneRegex = /^(0|\+84)[0-9]{9,10}$/;

            let errorMessage = "";

            // clear error state
            registerForm.querySelectorAll(".input-group").forEach(group => group.classList.remove("error"));

            if (!hoTen.value.trim()) {
                errorMessage = "Vui lòng nhập họ và tên.";
                hoTen.closest(".input-group")?.classList.add("error");
            } else if (!email.value.trim() || !emailRegex.test(email.value.trim())) {
                errorMessage = "Email không hợp lệ.";
                email.closest(".input-group")?.classList.add("error");
            } else if (!soDienThoai.value.trim() || !phoneRegex.test(soDienThoai.value.trim())) {
                errorMessage = "Số điện thoại không hợp lệ.";
                soDienThoai.closest(".input-group")?.classList.add("error");
            } else if (!matKhau.value) {
                errorMessage = "Vui lòng nhập mật khẩu.";
                matKhau.closest(".input-group")?.classList.add("error");
            } else if (matKhau.value.length < 6) {
                errorMessage = "Mật khẩu phải có ít nhất 6 ký tự.";
                matKhau.closest(".input-group")?.classList.add("error");
            } else if (matKhau.value !== xacNhanMatKhau.value) {
                errorMessage = "Xác nhận mật khẩu không khớp.";
                xacNhanMatKhau.closest(".input-group")?.classList.add("error");
            } else if (!acceptTerms.checked) {
                errorMessage = "Bạn cần đồng ý với điều khoản sử dụng.";
            }

            if (errorMessage) {
                event.preventDefault();
                alert(errorMessage);
            }
        });
    }

});

