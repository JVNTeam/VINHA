// ==========================
// HỒ SƠ CÁ NHÂN - hoso.js
// ==========================

// Toggle giữa chế độ xem và chế độ chỉnh sửa
function toggleEditMode() {
    const viewMode = document.getElementById('viewMode');
    const editMode = document.getElementById('editMode');
    const editBtn  = document.getElementById('btnEdit');

    if (!viewMode || !editMode) return;

    const isEditing = editMode.style.display !== 'none';

    if (isEditing) {
        // Quay về xem
        viewMode.style.display = 'block';
        editMode.style.display = 'none';
        editBtn.innerHTML = '<i class="fa-solid fa-pen"></i> Chỉnh sửa';
    } else {
        // Vào chế độ sửa
        viewMode.style.display = 'none';
        editMode.style.display = 'block';
        editBtn.innerHTML = '<i class="fa-solid fa-xmark"></i> Hủy';
    }
}

// Preview avatar trước khi upload
function previewAvatar(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.getElementById('avatarPreview');
            if (img) img.src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// Tự ẩn alert sau 4 giây
document.addEventListener('DOMContentLoaded', function () {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity   = '0';
            setTimeout(function () { alert.remove(); }, 500);
        }, 4000);
    });
});
