// Lưu số sao đã chọn cho từng món
const ratings = {};

// Chuyển tab "Chờ đánh giá" / "Đã đánh giá"
function switchTab(tab, btnElement) {
    document.querySelectorAll('.switch-btn').forEach(btn => btn.classList.remove('active'));
    btnElement.classList.add('active');

    const pendingList = document.getElementById('pending-list');
    const reviewedList = document.getElementById('reviewed-list');

    if (tab === 'pending') {
        pendingList.style.display = 'flex';
        reviewedList.style.display = 'none';
    } else {
        pendingList.style.display = 'none';
        reviewedList.style.display = 'block';
    }
}

// Mở/Đóng form đánh giá cho món ăn
function toggleReviewForm(itemId) {
    const card = document.getElementById('review-card-' + itemId);
    const formBody = card.querySelector('.review-form-body');
    const btnReview = card.querySelector('.btn-review-now');
    const subtext = card.querySelector('.item-subtext');

    if (formBody.style.display === 'none' || formBody.style.display === '') {
        formBody.style.display = 'block';
        if (btnReview) btnReview.style.display = 'none';
        if (subtext) subtext.style.display = 'none';
    } else {
        formBody.style.display = 'none';
        if (btnReview) btnReview.style.display = 'inline-block';
        if (subtext) subtext.style.display = 'block';
    }
}

// Chọn số sao
function setRating(itemId, ratingValue) {
    ratings[itemId] = ratingValue;
    const starContainer = document.querySelector(`.star-rating[data-id="${itemId}"]`);
    const stars = starContainer.querySelectorAll('i');

    stars.forEach((star, index) => {
        if (index < ratingValue) {
            star.className = 'fa-solid fa-star active';
        } else {
            star.className = 'fa-regular fa-star';
        }
    });
}

// Gửi đánh giá
function submitReview(itemId) {
    const starCount = ratings[itemId] || 0;
    const textarea = document.getElementById('textarea-' + itemId);
    const comment = textarea.value.trim();

    if (starCount === 0) {
        alert('Vui lòng chọn số sao đánh giá!');
        return;
    }

    alert(`Đã gửi đánh giá (${starCount} sao) thành công cho món ăn này!`);

    // Ẩn card món ăn đã đánh giá xong
    const card = document.getElementById('review-card-' + itemId);
    card.style.display = 'none';
}