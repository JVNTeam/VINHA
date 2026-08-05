// ==========================
// LỊCH SỬ ĐƠN HÀNG - lichsudonhang.js
// ==========================

document.addEventListener('DOMContentLoaded', function () {
    console.log("Trang Lịch Sử Đơn Hàng đã sẵn sàng.");

    // Thêm hiệu ứng Shadow cho Navbar Header khi cuộn
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

// Lọc danh sách đơn hàng theo Tab
function filterOrder(status, btnElement) {
    // Đổi trạng thái active cho nút tab
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    if (btnElement) {
        btnElement.classList.add('active');
    }

    // Lọc các hàng đơn hàng
    const rows = document.querySelectorAll('.order-row');
    rows.forEach(row => {
        if (status === 'all') {
            row.style.display = '';
        } else {
            if (row.classList.contains(status)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        }
    });
}

// Hủy đơn hàng
function cancelOrder(orderCode) {
    if (!confirm('Bạn có chắc chắn muốn hủy đơn hàng ' + orderCode + ' không?')) {
        return;
    }

    // Lấy orderId từ orderCode (#VN-2024-0812 hoặc #VN-1 => id)
    const orderId = orderCode.replace('#VN-', '');

    fetch('/api/order/cancel/' + orderId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Hủy đơn hàng thành công!');
                location.reload();
            } else {
                alert('Lỗi: ' + (data.message || 'Không thể hủy đơn hàng này.'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi kết nối tới hệ thống.');
        });
}

// Xem chi tiết đơn hàng
function viewDetail(orderCode) {
    const orderId = orderCode.replace('#VN-', '');
    window.location.href = '/chitietdonhang/' + orderId;
}