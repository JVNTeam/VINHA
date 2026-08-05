// ==========================
// HỒ SƠ CÁ NHÂN - hoso.js
// ==========================

// 1. Toggle giữa chế độ xem và chế độ chỉnh sửa
function toggleEditMode() {
    const viewMode = document.getElementById('viewMode');
    const editMode = document.getElementById('editMode');
    const editBtn  = document.getElementById('btnEdit');

    if (!viewMode || !editMode) return;

    const isEditing = editMode.style.display !== 'none';

    if (isEditing) {
        // Quay về chế độ xem
        viewMode.style.display = 'block';
        editMode.style.display = 'none';
        if (editBtn) {
            editBtn.innerHTML = '<i class="fa-solid fa-pen"></i> Chỉnh sửa';
        }
    } else {
        // Chuyển sang chế độ chỉnh sửa
        viewMode.style.display = 'none';
        editMode.style.display = 'block';
        if (editBtn) {
            editBtn.innerHTML = '<i class="fa-solid fa-xmark"></i> Hủy';
        }
    }
}

// 2. Preview Avatar (Dùng khi thêm ô tải ảnh đại diện)
function previewAvatar(input) {
    if (input && input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.getElementById('avatarPreview');
            if (img) {
                img.src = e.target.result;
            }
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// 3. Khởi tạo sự kiện DOM
document.addEventListener('DOMContentLoaded', function () {

    // Auto-hide alert thông báo sau 4 giây
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            setTimeout(function () {
                alert.remove();
            }, 500);
        }, 4000);
    });

    // Thêm hiệu ứng Shadow cho Navbar khi cuộn chuột
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', function () {
            if (window.scrollY > 20) {
                navbar.classList.add('shadow');
            } else {
                navbar.classList.remove('shadow');
            }
        });
    }

    // Xử lý nút Back To Top mượt mà
    const backToTopBtn = document.getElementById('backToTop');
    if (backToTopBtn) {
        backToTopBtn.addEventListener('click', function (e) {
            e.preventDefault();
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    }
});