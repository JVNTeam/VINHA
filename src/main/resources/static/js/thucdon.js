document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.food-btn form').forEach(form => {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();
            event.stopPropagation();

            const formData = new FormData(form);
            const action = form.getAttribute('action') || '/gioHang/them';

            try {
                const response = await fetch(action, {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                });

                if (!response.ok) {
                    throw new Error('Không thể thêm vào giỏ hàng');
                }

                let payload = null;
                const contentType = response.headers.get('content-type') || '';
                if (contentType.includes('application/json')) {
                    payload = await response.json();
                }

                const redirectTo = payload?.redirectTo || formData.get('redirectTo');
                if (redirectTo === '/thanhToan' || redirectTo?.endsWith('/thanhToan')) {
                    window.location.href = '/thanhToan';
                    return;
                }
                if (redirectTo === '/gioHang' || redirectTo?.endsWith('/gioHang')) {
                    window.location.href = '/gioHang';
                    return;
                }

                const badge = document.querySelector('.cart span');
                if (badge) {
                    const current = parseInt(badge.textContent.trim(), 10) || 0;
                    badge.textContent = current + 1;
                }

                const button = form.querySelector('button');
                if (button) {
                    const originalText = button.innerHTML;
                    button.innerHTML = '<i class="fa-solid fa-check"></i> Đã thêm';
                    button.disabled = true;
                    setTimeout(() => {
                        button.innerHTML = originalText;
                        button.disabled = false;
                    }, 1000);
                }
            } catch (error) {
                console.error(error);
                alert('Có lỗi khi thêm vào giỏ hàng');
            }
        });
    });
});
