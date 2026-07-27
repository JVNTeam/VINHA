document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".btn-delete").forEach(function (btn) {
        btn.addEventListener("click", function (e) {
            const ma = this.dataset.ma || "mã này";
            if (!confirm("Bạn có chắc chắn muốn xóa mã [" + ma + "] không?")) {
                e.preventDefault();
            }
        });
    });
});
