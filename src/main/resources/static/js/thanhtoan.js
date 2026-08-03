// ================= AUTO FILL =================

const autoFill = document.querySelector(".auto-fill");

// Disable order button if no items in cart
document.addEventListener("DOMContentLoaded", function() {
    const orderBtn = document.getElementById("orderBtn");
    const cartItems = document.querySelectorAll(".order-item");
    
    if (orderBtn) {
        orderBtn.disabled = cartItems.length === 0;
    }
});

function applyCustomerData() {
    const fullname = autoFill?.dataset.fullname || "";
    const phone = autoFill?.dataset.phone || "";
    const address = autoFill?.dataset.address || "";
    const province = autoFill?.dataset.province || "";

    const fullnameField = document.getElementById("fullname");
    const phoneField = document.getElementById("phone");
    const addressField = document.getElementById("address");
    const provinceSelect = document.getElementById("province");

    if (fullnameField && fullname && !fullnameField.value) {
        fullnameField.value = fullname;
    }

    if (phoneField && phone && !phoneField.value) {
        phoneField.value = phone;
    }

    if (addressField && address && !addressField.value) {
        addressField.value = address;
    }

    if (provinceSelect && province) {
        const hasOption = Array.from(provinceSelect.options).some(option => option.value === province);
        if (hasOption) {
            provinceSelect.value = province;
            provinceSelect.dispatchEvent(new Event("change"));
        }
    }
}

if (autoFill) {
    autoFill.addEventListener("click", applyCustomerData);
}

document.addEventListener("DOMContentLoaded", applyCustomerData);


// ================= QUẬN / HUYỆN =================

const districts = {
    "Hà Nội": [
        "Ba Đình", "Hoàn Kiếm", "Tây Hồ", "Long Biên", "Cầu Giấy", "Đống Đa",
        "Hai Bà Trưng", "Hoàng Mai", "Thanh Xuân", "Sóc Sơn", "Đông Anh", "Gia Lâm",
        "Nam Từ Liêm", "Thanh Trì", "Bắc Từ Liêm", "Mê Linh", "Hà Đông", "Sơn Tây"
    ],
    "Hồ Chí Minh": [
        "Quận 1", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7", "Quận 8",
        "Quận 10", "Quận 11", "Quận 12", "Bình Thạnh", "Phú Nhuận", "Tân Bình",
        "Tân Phú", "Thủ Đức", "Bình Tân", "Bình Chánh", "Cần Giờ", "Củ Chi", "Hóc Môn",
        "Nhà Bè"
    ],
    "Đà Nẵng": [
        "Hải Châu", "Thanh Khê", "Sơn Trà", "Ngũ Hành Sơn", "Liên Chiểu", "Cẩm Lệ",
        "Hòa Vang"
    ],
    "Hải Phòng": [
        "Hồng Bàng", "Ngô Quyền", "Lê Chân", "Kiến An", "Dương Kinh", "Đồ Sơn",
        "An Dương", "An Lão", "Bạch Long Vĩ", "Cát Hải", "Kiến Thụy", "Thủy Nguyên",
        "Tiên Lãng", "Vĩnh Bảo"
    ],
    "Cần Thơ": [
        "Ninh Kiều", "Bình Thủy", "Cái Răng", "Ô Môn", "Thốt Nốt", "Phong Điền",
        "Cờ Đỏ", "Vĩnh Thạnh"
    ],
    "An Giang": ["Long Xuyên", "Châu Đốc", "Tân Châu", "Châu Phú", "Thoại Sơn", "Tri Tôn"],
    "Bà Rịa - Vũng Tàu": ["Vũng Tàu", "Bà Rịa", "Long Điền", "Xuyên Mộc", "Đất Đỏ"],
    "Bắc Giang": ["Bắc Giang", "Lạng Giang", "Yên Thế", "Hiệp Hòa"],
    "Bắc Kạn": ["Bắc Kạn", "Ngân Sơn", "Ba Bể"],
    "Bạc Liêu": ["Bạc Liêu", "Hòa Bình", "Phước Long"],
    "Bắc Ninh": ["Bắc Ninh", "Từ Sơn", "Gia Bình", "Lương Tài"],
    "Bến Tre": ["Bến Tre", "Châu Thành", "Mỏ Cày", "Ba Tri"],
    "Bình Định": ["Quy Nhơn", "An Lão", "Tuy Phước", "Phù Cát"],
    "Bình Dương": ["Thủ Dầu Một", "Dĩ An", "Thuận An", "Bến Cát", "Phú Giáo"],
    "Bình Phước": ["Đồng Xoài", "Bình Long", "Chơn Thành", "Phước Long"],
    "Bình Thuận": ["Phan Thiết", "La Gi", "Tuy Phong", "Hàm Thuận Bắc"],
    "Cà Mau": ["Cà Mau", "U Minh", "Thới Bình", "Năm Căn"],
    "Cao Bằng": ["Cao Bằng", "Bảo Lâm", "Hạ Lang"],
    "Đắk Lắk": ["Buôn Ma Thuột", "Buôn Hồ", "Ea H'leo", "Krông Pắc"],
    "Đắk Nông": ["Gia Nghĩa", "Đắk R'Lấp", "Cư Jút"],
    "Điện Biên": ["Điện Biên Phủ", "Mường Lay", "Điện Biên"],
    "Đồng Nai": ["Biên Hòa", "Long Khánh", "Nhơn Trạch", "Trảng Bom"],
    "Đồng Tháp": ["Cao Lãnh", "Sa Đéc", "Hồng Ngự", "Lai Vung"],
    "Gia Lai": ["Pleiku", "Ayun Pa", "Chư Sê", "Krông Pa"],
    "Hà Giang": ["Hà Giang", "Đồng Văn", "Mèo Vạc"],
    "Hà Nam": ["Phủ Lý", "Kim Bảng", "Lý Nhân"],
    "Hà Tĩnh": ["Hà Tĩnh", "Hồng Lĩnh", "Kỳ Anh"],
    "Hải Dương": ["Hải Dương", "Chí Linh", "Nam Sách"],
    "Hậu Giang": ["Vị Thanh", "Ngã Bảy", "Long Mỹ"],
    "Hòa Bình": ["Hòa Bình", "Mai Châu", "Lạc Sơn"],
    "Hưng Yên": ["Hưng Yên", "Mỹ Hào", "Khoái Châu"],
    "Khánh Hòa": ["Nha Trang", "Cam Ranh", "Ninh Hòa"],
    "Kiên Giang": ["Rạch Giá", "Hà Tiên", "Phú Quốc", "Giồng Riềng"],
    "Kon Tum": ["Kon Tum", "Đắk Glei", "Ngọc Hồi"],
    "Lai Châu": ["Lai Châu", "Tam Đường", "Sìn Hồ"],
    "Lâm Đồng": ["Đà Lạt", "Bảo Lộc", "Đức Trọng", "Lạc Dương"],
    "Lạng Sơn": ["Lạng Sơn", "Hữu Lũng", "Bắc Sơn"],
    "Lào Cai": ["Lào Cai", "Bát Xát", "Mường Khương"],
    "Long An": ["Tân An", "Bến Lức", "Đức Hòa", "Mộc Hóa"],
    "Nam Định": ["Nam Định", "Hải Hậu", "Giao Thủy"],
    "Nghệ An": ["Vinh", "Cửa Lò", "Thanh Chương", "Hưng Nguyên"],
    "Ninh Bình": ["Ninh Bình", "Tam Điệp", "Gia Viễn"],
    "Ninh Thuận": ["Phan Rang - Tháp Chàm", "Ninh Hải", "Thuận Bắc"],
    "Phú Thọ": ["Việt Trì", "Phú Thọ", "Hạ Hoà"],
    "Phú Yên": ["Tuy Hòa", "Sông Cầu", "Đồng Xuân"],
    "Quảng Bình": ["Đồng Hới", "Ba Đồn", "Quảng Ninh"],
    "Quảng Nam": ["Tam Kỳ", "Hội An", "Điện Bàn", "Duy Xuyên"],
    "Quảng Ngãi": ["Quảng Ngãi", "Sơn Tịnh", "Lý Sơn"],
    "Quảng Ninh": ["Hạ Long", "Cẩm Phả", "Móng Cái", "Uông Bí"],
    "Quảng Trị": ["Đông Hà", "Quảng Trị", "Hải Lăng"],
    "Sóc Trăng": ["Sóc Trăng", "Vĩnh Châu", "Long Phú"],
    "Sơn La": ["Sơn La", "Mai Sơn", "Mộc Châu"],
    "Tây Ninh": ["Tây Ninh", "Trảng Bàng", "Hòa Thành"],
    "Thái Bình": ["Thái Bình", "Tiền Hải", "Kiến Xương"],
    "Thái Nguyên": ["Thái Nguyên", "Sông Công", "Định Hóa"],
    "Thanh Hóa": ["Thanh Hóa", "Sầm Sơn", "Bỉm Sơn", "Nghi Sơn"],
    "Thừa Thiên Huế": ["Huế", "Hương Thủy", "Hương Trà", "Phong Điền"],
    "Tiền Giang": ["Mỹ Tho", "Cai Lậy", "Gò Công"],
    "Trà Vinh": ["Trà Vinh", "Duyên Hải", "Càng Long"],
    "Tuyên Quang": ["Tuyên Quang", "Chiêm Hóa", "Lâm Bình"],
    "Vĩnh Long": ["Vĩnh Long", "Bình Minh", "Long Hồ"],
    "Vĩnh Phúc": ["Vĩnh Yên", "Phúc Yên", "Bình Xuyên"],
    "Yên Bái": ["Yên Bái", "Lục Yên", "Mù Cang Chải"]
};

const province = document.getElementById("province");
const district = document.getElementById("district");

province.addEventListener("change", () => {

    district.innerHTML =
        "<option>Chọn Quận / Huyện</option>";

    const list = districts[province.value];

    if (list) {

        list.forEach(item => {

            district.innerHTML +=
                `<option>${item}</option>`;

        });

    }

});


// ================= GIẢM GIÁ =================

const couponBtn = document.getElementById("applyCoupon");

const subtotalElement = document.getElementById("subtotal");
const discountElement = document.getElementById("discount");
const grandTotalElement = document.getElementById("grandTotal");

let subtotal = parseInt((subtotalElement?.textContent || "0").replace(/[^\d]/g, ""), 10) || 0;
let shipping = 0;
let discount = 0;

function parsePrice(value) {
    return Number(value || 0);
}

couponBtn.onclick = function () {

    const code =
        document.getElementById("coupon")
            .value.trim()
            .toUpperCase();

    if (!code) {
        discount = 0;
        shipping = 0;
        alert("Vui lòng nhập mã giảm giá");
        updateMoney();
        return;
    }

    // Call API to validate and apply voucher
    fetch('/api/voucher/apply', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'code': code,
            'subtotal': subtotal
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            discount = data.discountAmount;
            shipping = 0;
            alert(`Áp dụng mã ${code} thành công.`);
        } else {
            discount = 0;
            shipping = 0;
            alert(data.message || "Mã giảm giá không hợp lệ");
        }
        updateMoney();
    })
    .catch(error => {
        console.error('Error:', error);
        discount = 0;
        shipping = 0;
        alert("Lỗi khi xử lý mã giảm giá");
        updateMoney();
    });

};


// ================= TÍNH TIỀN =================

function formatMoney(number){

    return number.toLocaleString("vi-VN")+"đ";

}

function updateMoney(){

    const finalSubtotal = Math.max(0, subtotal);
    const finalDiscount = Math.max(0, discount);
    const finalShipping = shipping;
    const total = finalSubtotal + finalShipping - finalDiscount;

    subtotalElement.innerText = formatMoney(finalSubtotal);
    discountElement.innerText = "-" + formatMoney(finalDiscount);
    grandTotalElement.innerText = formatMoney(Math.max(0, total));

}

updateMoney();


// ================= ĐẶT HÀNG =================

document.querySelector(".order-btn")
    .addEventListener("click", function(e){

        if (this.disabled) {
            alert("Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi đặt hàng.");
            return;
        }

        const fullname =
            document.getElementById("fullname").value.trim();

        const phone =
            document.getElementById("phone").value.trim();

        const address =
            document.getElementById("address").value.trim();

        const note =
            document.getElementById("note").value.trim();

        if(fullname===""){
            alert("Vui lòng nhập họ tên.");
            return;
        }

        if(phone===""){
            alert("Vui lòng nhập số điện thoại.");
            return;
        }

        if(!/^0\d{9}$/.test(phone)){
            alert("Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số.");
            return;
        }

        if(address===""){
            alert("Vui lòng nhập địa chỉ.");
            return;
        }

        const payment =
            document.querySelector(
                "input[name='payment']:checked"
            ).value;

        const province =
            document.getElementById("province").value;

        // Get discount and subtotal from display
        const discountText = document.getElementById("discount").innerText || "0đ";
        const subtotalText = document.getElementById("subtotal").innerText || "0đ";
        
        const finalDiscount = parseInt(discountText.replace(/[^\d]/g, ""), 10) || 0;
        const finalSubtotal = parseInt(subtotalText.replace(/[^\d]/g, ""), 10) || 0;
        
        // Build request data
        const formData = new URLSearchParams({
            'fullname': fullname,
            'phone': phone,
            'address': address,
            'province': province,
            'payment': payment,
            'note': note,
            'discount': finalDiscount,
            'subtotal': finalSubtotal
        });

        // Call checkout API
        fetch('/api/checkout/place-order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Đặt hàng thành công! Đơn hàng của bạn đang được xử lý.");
                window.location.href = data.redirectUrl;
            } else {
                alert(data.message || "Lỗi khi đặt hàng");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Lỗi khi xử lý đơn hàng");
        });

    });


// ================= INPUT EFFECT =================

document
    .querySelectorAll("input,select,textarea")
    .forEach(item=>{

        item.addEventListener("focus",()=>{

            item.style.boxShadow =
                "0 0 8px rgba(255,153,0,.35)";

        });

        item.addEventListener("blur",()=>{

            item.style.boxShadow = "none";

        });

    });

const couponSelect = document.getElementById("couponSelect");
const couponInput = document.getElementById("coupon");

couponSelect.addEventListener("change", function () {
    couponInput.value = this.value;
});

const radios = document.querySelectorAll("input[name='payment']");
const qr = document.getElementById("bankQR");

radios.forEach(radio => {

    radio.addEventListener("change", function(){

        if(this.value==="bank"){
            qr.style.display="flex";
        }else{
            qr.style.display="none";
        }

    });

});