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
    if (confirm('Bạn có chắc chắn muốn hủy đơn hàng ' + orderCode + ' không?')) {
        alert('Đã gửi yêu cầu hủy đơn hàng ' + orderCode);
        // Có thể bổ sung fetch API gửi yêu cầu hủy lên Controller tại đây
    }
}

// Xem chi tiết đơn hàng
function viewDetail(orderCode) {
    alert('Mở chi tiết đơn hàng: ' + orderCode);
    // window.location.href = '/tai-khoan/don-hang/' + orderCode;
}