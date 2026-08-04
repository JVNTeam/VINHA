import os
import re

replacements = {
    '/tai-khoan/ho-so': '/hoso',
    '/tai-khoan/dia-chi': '/diachi',
    '/tai-khoan/lich-su-don-hang': '/lichsudonhang',
    '/tai-khoan/danh-gia': '/danhgia',
    '/tai-khoan/doi-mat-khau': '/doimatkhau',
    '/tai-khoan/chi-tiet-don-hang': '/chitietdonhang',
    '"/danh-gia"': '"/danhgia"',
    '"/dia-chi"': '"/diachi"',
    '"/doi-mat-khau"': '"/doimatkhau"'
}

def process_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception as e:
        return

    original = content
    for k, v in replacements.items():
        content = content.replace(k, v)
        
    if filepath.endswith('hoSo.html'):
        # Remove avatar wrapper
        avatar_regex = re.compile(r'<!-- Avatar -->\s*<div class="avatar-wrapper">.*?</div>', re.DOTALL)
        content = avatar_regex.sub('', content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8', newline='') as f:
            f.write(content)
        print(f"Modified {filepath}")

for root, dirs, files in os.walk('src/main'):
    for file in files:
        if file.endswith(('.html', '.java', '.js')):
            process_file(os.path.join(root, file))
