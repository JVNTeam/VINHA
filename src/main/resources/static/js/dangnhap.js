const toggle = document.getElementById("togglePassword");
const password = document.getElementById("password");

toggle.addEventListener("click", () => {

    if (password.type === "password") {
        password.type = "text";
    } else {
        password.type = "password";
    }

});