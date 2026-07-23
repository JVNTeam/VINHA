document.addEventListener("DOMContentLoaded", function () {

    function setupToggle(toggleId, inputId) {
        const toggle = document.getElementById(toggleId);
        const input = document.getElementById(inputId);

        if (toggle && input) {
            toggle.addEventListener("click", function () {
                const isPassword = input.type === "password";
                input.type = isPassword ? "text" : "password";
                // Đổi icon giữa fa-eye và fa-eye-slash
                this.classList.toggle("fa-eye");
                this.classList.toggle("fa-eye-slash");
            });
        }
    }

    setupToggle("toggleOld", "oldPassword");
    setupToggle("toggleNew", "newPassword");
    setupToggle("toggleConfirm", "confirmPassword");

});

