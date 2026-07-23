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

});

