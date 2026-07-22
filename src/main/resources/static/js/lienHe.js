// ===================== SCROLL REVEAL =====================

const revealElements = document.querySelectorAll(
    ".contact-box, .social-box, .map-box, .gallery img, .footer-col"
);

function revealOnScroll() {

    const trigger = window.innerHeight - 120;

    revealElements.forEach(element => {

        const top = element.getBoundingClientRect().top;

        if (top < trigger) {

            element.style.opacity = "1";
            element.style.transform = "translateY(0)";

        }

    });

}

revealElements.forEach(element => {

    element.style.opacity = "0";
    element.style.transform = "translateY(40px)";
    element.style.transition = "all .6s ease";

});

window.addEventListener("scroll", revealOnScroll);

revealOnScroll();


// ===================== GALLERY EFFECT =====================

const galleryImages = document.querySelectorAll(".gallery img");

galleryImages.forEach(image => {

    image.addEventListener("mouseenter", () => {

        image.style.transform = "scale(1.05)";

    });

    image.addEventListener("mouseleave", () => {

        image.style.transform = "scale(1)";

    });

});


// ===================== SOCIAL EFFECT =====================

const socialIcons = document.querySelectorAll(
    ".social-list a, .footer-social a"
);

socialIcons.forEach(icon => {

    icon.addEventListener("mouseenter", () => {

        icon.style.transform = "translateY(-6px)";

    });

    icon.addEventListener("mouseleave", () => {

        icon.style.transform = "translateY(0)";

    });

});


// ===================== CONTACT ITEMS =====================

const infoItems = document.querySelectorAll(".info-item");

infoItems.forEach(item => {

    item.addEventListener("mouseenter", () => {

        item.style.transform = "translateX(10px)";

    });

    item.addEventListener("mouseleave", () => {

        item.style.transform = "translateX(0)";

    });

    item.style.transition = ".3s";

});


// ===================== MAP EFFECT =====================

const map = document.querySelector(".map-box");

if (map) {

    map.addEventListener("mouseenter", () => {

        map.style.transform = "scale(1.01)";
        map.style.transition = ".4s";

    });

    map.addEventListener("mouseleave", () => {

        map.style.transform = "scale(1)";

    });

}


// ===================== HERO ANIMATION =====================

const heroTitle = document.querySelector(".hero h1");
const heroText = document.querySelector(".hero p");

window.addEventListener("load", () => {

    if (heroTitle) {
        heroTitle.style.opacity = "0";
        heroTitle.style.transform = "translateY(-30px)";
        heroTitle.style.transition = ".8s";

        setTimeout(() => {
            heroTitle.style.opacity = "1";
            heroTitle.style.transform = "translateY(0)";
        }, 200);
    }

    if (heroText) {
        heroText.style.opacity = "0";
        heroText.style.transform = "translateY(30px)";
        heroText.style.transition = ".8s";

        setTimeout(() => {
            heroText.style.opacity = "1";
            heroText.style.transform = "translateY(0)";
        }, 400);
    }

});


// ===================== FOOTER LINK =====================

const footerLinks = document.querySelectorAll(".footer a");

footerLinks.forEach(link => {

    link.addEventListener("mouseenter", () => {

        link.style.paddingLeft = "8px";

    });

    link.addEventListener("mouseleave", () => {

        link.style.paddingLeft = "0";

    });

    link.style.transition = ".3s";

});


// ===================== LOAD =====================

window.addEventListener("load", () => {

    console.log("Trang Liên hệ đã tải thành công.");

});