// Function Bật/Tắt ẩn hiện mật khẩu
function togglePasswordVisibility(inputId, iconElement) {
    const input = document.getElementById(inputId);
    if (input.type === 'password') {
        input.type = 'text';
        iconElement.classList.remove('fa-eye');
        iconElement.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        iconElement.classList.remove('fa-eye-slash');
        iconElement.classList.add('fa-eye');
    }
}

// Function kiểm tra độ mạnh mật khẩu
function checkPasswordStrength(password) {
    const bars = [
        document.getElementById('bar-1'),
        document.getElementById('bar-2'),
        document.getElementById('bar-3'),
        document.getElementById('bar-4')
    ];

    // Reset màu
    bars.forEach(bar => bar.style.backgroundColor = '#e2e2e2');

    if (!password) return;

    let score = 0;
    if (password.length >= 8) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    // Tô màu theo điểm số
    const colors = ['#e53935', '#ff9800', '#2196f3', '#4caf50'];
    for (let i = 0; i < score; i++) {
        bars[i].style.backgroundColor = colors[score - 1];
    }
}

// Function Reset form
function resetForm() {
    document.getElementById('changePasswordForm').reset();
    checkPasswordStrength('');
    document.getElementById('confirmError').style.display = 'none';
}

// Kiểm tra mật khẩu khớp trước khi submit
document.getElementById('changePasswordForm').addEventListener('submit', function(e) {
    const newPwd = document.getElementById('newPassword').value;
    const confirmPwd = document.getElementById('confirmPassword').value;
    const errorMsg = document.getElementById('confirmError');

    if (newPwd !== confirmPwd) {
        e.preventDefault();
        errorMsg.style.display = 'block';
    } else {
        errorMsg.style.display = 'none';
    }
});