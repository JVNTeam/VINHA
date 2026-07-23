// ================= AUTO FILL =================

const autoFill = document.querySelector(".auto-fill");

autoFill.addEventListener("click", () => {

    document.getElementById("fullname").value = "Nguyễn Văn A";
    document.getElementById("phone").value = "0987654321";
    document.getElementById("province").value = "Hồ Chí Minh";
    document.getElementById("address").value =
        "123 Nguyễn Huệ, Quận 1";

});


// ================= QUẬN / HUYỆN =================

const districts = {
    "Hà Nội": [
        "Ba Đình",
        "Đống Đa",
        "Hai Bà Trưng",
        "Hoàn Kiếm"
    ],

    "Hồ Chí Minh": [
        "Quận 1",
        "Quận 3",
        "Quận 7",
        "Bình Thạnh"
    ],

    "Đà Nẵng": [
        "Hải Châu",
        "Thanh Khê",
        "Liên Chiểu"
    ],

    "Hải Phòng": [
        "Lê Chân",
        "Ngô Quyền"
    ],

    "Cần Thơ": [
        "Ninh Kiều",
        "Cái Răng"
    ]
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

let subtotal = 140000;
let shipping = 0.000;
let discount = 0.000;

couponBtn.onclick = function () {

    const code =
        document.getElementById("coupon")
            .value.trim()
            .toUpperCase();

    if (code === "GIAM10") {

        discount = subtotal * 0.1;

        alert("Áp dụng mã giảm 10% thành công.");

    }

    else if (code === "VINHA15") {

        discount = 15000;

        alert("Giảm 0.000đ");

    }

    else if (code === "FREESHIP") {

        shipping = 0;

        alert("Miễn phí vận chuyển");

    }

    else {

        alert("Mã giảm giá không hợp lệ");

        return;

    }

    updateMoney();

};


// ================= TÍNH TIỀN =================

function formatMoney(number){

    return number.toLocaleString("vi-VN")+"đ";

}

function updateMoney(){

    document.getElementById("subtotal").innerText =
        formatMoney(subtotal);

    document.getElementById("discount").innerText =
        "-" + formatMoney(discount);

    document.getElementById("grandTotal").innerText =
        formatMoney(subtotal + shipping - discount);

}

updateMoney();


// ================= ĐẶT HÀNG =================

document.querySelector(".order-btn")
    .addEventListener("click", ()=>{

        const fullname =
            document.getElementById("fullname").value.trim();

        const phone =
            document.getElementById("phone").value.trim();

        const address =
            document.getElementById("address").value.trim();

        if(fullname===""){

            alert("Vui lòng nhập họ tên.");

            return;

        }

        if(!/^0\d{9}$/.test(phone)){

            alert("Số điện thoại không hợp lệ.");

            return;

        }

        if(address===""){

            alert("Vui lòng nhập địa chỉ.");

            return;

        }

        const payment =
            document.querySelector(
                "input[name='payment']:checked"
            ).nextElementSibling.innerText;

        alert(

            `Đặt hàng thành công!

Khách hàng: ${fullname}

Thanh toán:
${payment}

Tổng tiền:
${document.getElementById("grandTotal").innerText}`

        );

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