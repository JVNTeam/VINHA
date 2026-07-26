document.addEventListener('DOMContentLoaded', function() {

    // 1. Xử lý sự kiện click Nút "Thêm món ăn mới"
    const btnThemMon = document.getElementById('btnThemMon');
    if(btnThemMon) {
        btnThemMon.addEventListener('click', function() {
            // Chuyển hướng sang trang form thêm món ăn (Bạn có thể đổi URL cho phù hợp)
            window.location.href = '/admin/mon-an/them-moi';
        });
    }

    // 2. Xử lý sự kiện click Nút "Xóa" (Biểu tượng thùng rác)
    const deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();

            // Tìm tên món ăn trên cùng dòng đó để hiển thị thông báo
            const row = this.closest('tr');
            const foodName = row.querySelector('.text-main').innerText;

            const isConfirm = confirm(`Bạn có chắc chắn muốn xóa món: "${foodName}" không?`);

            if (isConfirm) {
                // TODO: Gọi API hoặc Submit Form để xóa món ăn trong CSDL
                alert('Đã xóa thành công!');
                // row.remove(); // Code tạm thời ẩn dòng dữ liệu đi
            }
        });
    });

    // 3. Xử lý sự kiện click Nút "Chỉnh sửa"
    const editButtons = document.querySelectorAll('.btn-edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            // TODO: Chuyển hướng sang màn hình sửa món ăn
            alert("Chuyển sang giao diện chỉnh sửa món ăn!");
        });
    });
});