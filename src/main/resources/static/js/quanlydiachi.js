document.addEventListener('DOMContentLoaded', function () {
    // Xử lý nút Thêm địa chỉ mới
    const btnAddAddress = document.getElementById('btnOpenAddModal');
    if (btnAddAddress) {
        btnAddAddress.addEventListener('click', function () {
            alert('Mở modal thêm địa chỉ mới!');
            // Viết logic hiển thị Form/Modal thêm địa chỉ tại đây
        });
    }
});

// Hàm đặt làm địa chỉ mặc định
function setDefaultAddress(button) {
    const addressId = button.getAttribute('data-id');
    if (confirm('Bạn có muốn đặt địa chỉ này làm mặc định?')) {
        // Gọi API backend (Fetch / Axios)
        console.log('Đặt địa chỉ mặc định ID:', addressId);

        /*
        fetch(`/api/addresses/${addressId}/set-default`, { method: 'PUT' })
            .then(res => res.json())
            .then(data => location.reload());
        */
        alert('Đã cập nhật địa chỉ mặc định!');
    }
}

// Hàm sửa địa chỉ
function editAddress(button) {
    const addressId = button.getAttribute('data-id');
    console.log('Chỉnh sửa địa chỉ ID:', addressId);
    alert('Mở form sửa địa chỉ ID: ' + addressId);
}

// Hàm xóa địa chỉ
function deleteAddress(button) {
    const addressId = button.getAttribute('data-id');
    if (confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')) {
        console.log('Xóa địa chỉ ID:', addressId);

        /*
        fetch(`/api/addresses/${addressId}`, { method: 'DELETE' })
            .then(res => res.json())
            .then(data => location.reload());
        */
        alert('Đã xóa địa chỉ!');
    }
}