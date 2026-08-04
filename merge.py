import re

with open('src/main/resources/templates/customer/chiTietDonHang.html', 'r', encoding='utf-8') as f:
    chitiet = f.read()
    
with open('src/main/resources/templates/customer/lichSuDon.html', 'r', encoding='utf-8') as f:
    lichsu = f.read()

# Extract header from chitiet
header_match = re.search(r'(<nav class="navbar navbar-expand-lg".*?<!-- END HEADER -->)', chitiet, re.DOTALL)
if header_match:
    header = header_match.group(1)
    lichsu = re.sub(r'<!-- HEADER -->.*?</header>', header, lichsu, flags=re.DOTALL)

# Extract footer from chitiet
footer_match = re.search(r'(<!-- FOOTER -->.*?</footer>)', chitiet, re.DOTALL)
if footer_match:
    footer = footer_match.group(1)
    lichsu = re.sub(r'<!-- ================= FOOTER ================= -->.*?</footer>', footer, lichsu, flags=re.DOTALL)

# Extract sidebar from chitiet
sidebar_match = re.search(r'(<aside class="sidebar">.*?</aside>)', chitiet, re.DOTALL)
if sidebar_match:
    sidebar = sidebar_match.group(1)
    lichsu = re.sub(r'<aside class="sidebar">.*?</aside>', sidebar, lichsu, flags=re.DOTALL)

# Add bootstrap CSS/JS
if 'bootstrap.min.css' not in lichsu:
    lichsu = lichsu.replace('<!-- Custom CSS -->', '<!-- Bootstrap 5 CSS -->\n    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">\n    <!-- Custom CSS -->')

if 'bootstrap.bundle.min.js' not in lichsu:
    lichsu = lichsu.replace('</body>', '<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>\n</body>')

# Update active class in sidebar for lichsudonhang
lichsu = re.sub(r'<a th:href="@{/lichsudonhang}" class="nav-item text-decoration-none text-dark">', r'<a th:href="@{/lichsudonhang}" class="nav-item text-decoration-none fw-bold" style="color: var(--primary-color, #d32f2f);">', lichsu)

with open('src/main/resources/templates/customer/lichSuDon.html', 'w', encoding='utf-8', newline='') as f:
    f.write(lichsu)
