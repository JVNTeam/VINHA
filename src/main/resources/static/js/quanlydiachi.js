// ==========================
// QUẢN LÝ ĐỊA CHỈ - quanlydiachi.js
// ==========================

// Mở modal thêm địa chỉ mới
document.addEventListener('DOMContentLoaded', function () {
    const btnAdd = document.getElementById('btnOpenAddModal');
    if (btnAdd) {
        btnAdd.addEventListener('click', function () {
            openAddressModal(); // mở modal thêm mới (không có id)
        });
    }

    // Đóng modal khi click ra ngoài
    const overlay = document.getElementById('addressModal');
    if (overlay) {
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) closeAddressModal();
        });
    }
});

// Mở modal (thêm mới hoặc sửa)
function openAddressModal(id, name, phone, detail, isDefault) {
    const modal      = document.getElementById('addressModal');
    const modalTitle = document.getElementById('modalTitle');
    const addrId     = document.getElementById('addrId');
    const addrName   = document.getElementById('addrName');
    const addrPhone  = document.getElementById('addrPhone');
    const addrDetail = document.getElementById('addrDetail');
    const addrDefault= document.getElementById('addrDefault');

    if (id) {
        // Chế độ sửa
        if (modalTitle) modalTitle.textContent = 'Chỉnh sửa địa chỉ';
        if (addrId) addrId.value = id;
        if (addrName) addrName.value = name || '';
        if (addrPhone) addrPhone.value = phone || '';
        if (addrDetail) addrDetail.value = detail || '';
        if (addrDefault) addrDefault.checked = isDefault || false;
    } else {
        // Chế độ thêm mới
        if (modalTitle) modalTitle.textContent = 'Thêm địa chỉ mới';
        if (addrId) addrId.value = '';
        if (addrName) addrName.value = '';
        if (addrPhone) addrPhone.value = '';
        if (addrDetail) addrDetail.value = '';
        if (addrDefault) addrDefault.checked = false;
    }

    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
}

// Đóng modal
function closeAddressModal() {
    const modal = document.getElementById('addressModal');
    if (modal) {
        modal.classList.remove('open');
        document.body.style.overflow = '';
    }
}

// Sửa địa chỉ: lấy data từ card rồi mở modal
function editAddress(button) {
    const card   = button.closest('.address-card');
    const id     = button.getAttribute('data-id');
    const name   = card.querySelector('.user-info .name')?.textContent.trim() || '';
    const phone  = card.querySelector('.address-body .phone span')?.textContent.trim() || '';
    const detail = card.querySelector('.address-body .detail span')?.textContent.trim() || '';
    const isDefault = card.querySelector('.badge-default') !== null;

    openAddressModal(id, name, phone, detail, isDefault);
}

// Xóa địa chỉ
function deleteAddress(button) {
    const id = button.getAttribute('data-id');
    if (confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')) {
        fetch(`/diachi/xoa/${id}`, { method: 'POST' })
            .then(res => {
                if (res.ok || res.redirected) {
                    location.reload();
                } else {
                    alert('Xóa địa chỉ thất bại, vui lòng thử lại!');
                }
            })
            .catch(() => location.reload());
    }
}

// Đặt địa chỉ mặc định
function setDefaultAddress(button) {
    const id = button.getAttribute('data-id');
    if (confirm('Đặt địa chỉ này làm mặc định?')) {
        fetch(`/diachi/mac-dinh/${id}`, { method: 'POST' })
            .then(res => {
                if (res.ok || res.redirected) {
                    location.reload();
                } else {
                    alert('Cập nhật thất bại, vui lòng thử lại!');
                }
            })
            .catch(() => location.reload());
    }
}
