document.addEventListener("DOMContentLoaded", function() {
    // Confirm before deleting a category
    const deleteLinks = document.querySelectorAll(".btn-delete");
    deleteLinks.forEach(function(link) {
        link.addEventListener("click", function(event) {
            const confirmed = confirm("Bạn có chắc chắn muốn xóa danh mục này không?");
            if (!confirmed) {
                event.preventDefault();
            }
        });
    });

    const statusFilter = document.getElementById('statusFilter');
    const tableRows = document.querySelectorAll('.admin-table tbody tr');

    function filterByStatus() {
        if (!statusFilter) {
            return;
        }
        const selectedStatus = statusFilter.value;
        tableRows.forEach(row => {
            const rowStatus = row.getAttribute('data-status');
            if (!rowStatus) {
                row.style.display = '';
                return;
            }
            if (selectedStatus === '' || rowStatus === selectedStatus) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    if (statusFilter) {
        statusFilter.addEventListener('change', function(event) {
            event.preventDefault();
            filterByStatus();
        });
        filterByStatus();
    }
});