document.addEventListener('DOMContentLoaded', function() {
    const btnThemMon = document.getElementById('btnThemMon');
    if (btnThemMon) {
        btnThemMon.addEventListener('click', function() {
            window.location.href = '/admin/monAn/them';
        });
    }

    const searchKeyword = document.getElementById('searchKeyword');
    const filterCategory = document.getElementById('filterCategory');
    const filterStatus = document.getElementById('filterStatus');
    const btnSearch = document.getElementById('btnSearch');
    const tableBody = document.querySelector('.admin-table tbody');

    const filterForm = document.getElementById('filterForm');

    if (searchKeyword && filterForm) {
        let debounceTimer;
        searchKeyword.addEventListener('input', function() {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                filterForm.submit();
            }, 800);
        });

        searchKeyword.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                filterForm.submit();
            }
        });
    }
});
