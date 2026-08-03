document.addEventListener('DOMContentLoaded', function () {
    console.log("Trang Lịch Sử Đơn Hàng đã sẵn sàng.");
});

// Lọc danh sách đơn hàng theo Tab
function filterOrder(status, btnElement) {
    // Đổi active button tab
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    btnElement.classList.add('active');

    // Lọc theo class đơn hàng
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

    // Extract orderId từ orderCode (#VN-1 => 1)
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
            alert('Hủy đơn hàng thành công');
            location.reload();
        } else {
            alert('Lỗi: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Lỗi khi hủy đơn hàng');
    });
}

// Xem chi tiết đơn hàng
function viewDetail(orderCode) {
    // Extract orderId từ orderCode (#VN-1 => 1)
    const orderId = orderCode.replace('#VN-', '');
    window.location.href = '/tai-khoan/chi-tiet-don-hang/' + orderId;
}