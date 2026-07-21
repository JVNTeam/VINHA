// Chỉnh sửa hình ảnh món ăn
function editImage(id) {
    alert("Chỉnh sửa hình ảnh món ăn STT: " + id);
}

// Xóa hình ảnh món ăn
function deleteImage(id) {
    if (confirm("Bạn có chắc chắn muốn xóa hình ảnh này không?")) {
        alert("Đã xóa hình ảnh STT: " + id);
    }
}