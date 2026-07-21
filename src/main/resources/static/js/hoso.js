// ==========================
// HỒ SƠ CÁ NHÂN
// ==========================

document.addEventListener("DOMContentLoaded", function () {

    // ==========================
    // Sidebar Active
    // ==========================

    const menuItems = document.querySelectorAll(".sidebar li");

    menuItems.forEach(item => {

        item.addEventListener("click", function () {

            menuItems.forEach(i => i.classList.remove("active"));

            this.classList.add("active");

        });

    });

    // ==========================
    // Chỉnh sửa thông tin
    // ==========================

    const editBtn = document.querySelector(".edit-btn");

    if (editBtn) {

        editBtn.addEventListener("click", function () {

            alert("Chức năng chỉnh sửa thông tin sẽ được cập nhật.");

            // Sau này có thể chuyển trang
            // window.location.href = "/hoso/chinhsua";

        });

    }

    // ==========================
    // Đăng xuất
    // ==========================

    const logout = document.querySelector(".logout");

    if (logout) {

        logout.addEventListener("click", function (e) {

            e.preventDefault();

            const result = confirm("Bạn có chắc chắn muốn đăng xuất?");

            if (result) {

                window.location.href = "/logout";

            }

        });

    }

});