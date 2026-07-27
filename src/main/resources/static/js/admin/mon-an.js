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

    if (!tableBody || !btnSearch || !searchKeyword || !filterCategory || !filterStatus) {
        return;
    }

    const rows = Array.from(tableBody.querySelectorAll('tr[data-name]'));

    const filterRows = () => {
        const keyword = searchKeyword.value.trim().toLowerCase();
        const categoryId = filterCategory.value;
        const status = filterStatus.value.trim().toLowerCase();

        rows.forEach(row => {
            const name = (row.dataset.name || '').toLowerCase();
            const rowCategoryId = row.dataset.categoryId || '';
            const rowStatus = (row.dataset.status || '').toLowerCase();

            const matchesKeyword = !keyword || name.includes(keyword);
            const matchesCategory = !categoryId || rowCategoryId === categoryId;
            const matchesStatus = !status || rowStatus === status;

            row.style.display = matchesKeyword && matchesCategory && matchesStatus ? '' : 'none';
        });
    };

    btnSearch.addEventListener('click', filterRows);
    searchKeyword.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            filterRows();
        }
    });
    searchKeyword.addEventListener('input', filterRows);
    filterCategory.addEventListener('change', filterRows);
    filterStatus.addEventListener('change', filterRows);
});
