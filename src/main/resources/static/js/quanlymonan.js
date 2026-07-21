// Xem chi tiết món ăn
function viewProduct(id) {
    alert("Xem chi tiết món ăn ID: " + id);
}

// Chỉnh sửa món ăn
function editProduct(id) {
    alert("Chỉnh sửa món ăn ID: " + id);
}

// Xóa món ăn
function deleteProduct(id) {
    if (confirm("Bạn có chắc chắn muốn xóa món ăn này không?")) {
        alert("Đã xóa món ăn ID: " + id);
    }
}