document.addEventListener("DOMContentLoaded", function() {

    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");
    const searchBtn = document.getElementById("searchBtn");
    const resetBtn = document.getElementById("resetBtn");
    const tableBody = document.getElementById("employeeTableBody");
    const noDataRow = document.getElementById("noDataRow");
    
    function filterData() {
        const keyword = searchInput.value.toLowerCase().trim();
        const status = statusFilter.value;
        const rows = document.querySelectorAll(".employee-row");
        let visibleCount = 0;

        rows.forEach(function(row) {
            const name = row.querySelector(".employee-name")?.innerText.toLowerCase() || "";
            const email = row.querySelector(".employee-email")?.innerText.toLowerCase() || "";
            const phone = row.querySelector(".employee-phone")?.innerText.toLowerCase() || "";
            const rowStatus = row.querySelector(".employee-status")?.innerText.trim() || "";

            const matchesKeyword = !keyword || name.includes(keyword) || email.includes(keyword) || phone.includes(keyword);
            
            // Map the displayed status text to the dropdown value
            // Dropdown values: "Hoạt Động", "Khóa"
            // Displayed text: "Hoạt động", "Bị khóa"
            let mappedStatus = "";
            if (rowStatus === "Hoạt động") mappedStatus = "Hoạt Động";
            else if (rowStatus === "Bị khóa") mappedStatus = "Khóa";
            else mappedStatus = rowStatus;

            const matchesStatus = !status || mappedStatus === status;

            if (matchesKeyword && matchesStatus) {
                row.style.display = "";
                visibleCount++;
            } else {
                row.style.display = "none";
            }
        });

        if (noDataRow) {
            if (visibleCount === 0 && rows.length > 0) {
                noDataRow.style.display = "";
            } else {
                noDataRow.style.display = "none";
            }
        }
    }

    if (searchBtn) {
        searchBtn.addEventListener("click", filterData);
    }
    
    if (searchInput) {
        searchInput.addEventListener("keyup", function(e) {
            if (e.key === "Enter") {
                filterData();
            }
        });
    }

    if (resetBtn) {
        resetBtn.addEventListener("click", function() {
            searchInput.value = "";
            statusFilter.value = "";
            filterData();
        });
    }
});