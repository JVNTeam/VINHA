// ===================== SCROLL REVEAL =====================

const revealElements = document.querySelectorAll(
    ".mission-card, .timeline-item, .why-card, .newsletter-box"
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
    element.style.transform = "translateY(50px)";
    element.style.transition = "all .6s ease";

});

window.addEventListener("scroll", revealOnScroll);

revealOnScroll();


// ===================== SCROLL TO STORY =====================

const storyButton = document.querySelector(".btn-outline");

if (storyButton) {

    storyButton.addEventListener("click", function(e){

        e.preventDefault();

        document.querySelector("#story").scrollIntoView({

            behavior:"smooth"

        });

    });

}


// ===================== NEWSLETTER =====================

const newsletterForm =
    document.querySelector(".newsletter-form");

newsletterForm.addEventListener("submit", function(e){

    e.preventDefault();

    const email =
        document.getElementById("email").value.trim();

    const emailRegex =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(email===""){

        alert("Vui lòng nhập email.");

        return;

    }

    if(!emailRegex.test(email)){

        alert("Email không hợp lệ.");

        return;

    }

    alert("Đăng ký nhận ưu đãi thành công!");

    newsletterForm.reset();

});


// ===================== HOVER CARD =====================

const cards = document.querySelectorAll(
    ".mission-card,.why-card"
);

cards.forEach(card=>{

    card.addEventListener("mouseenter",()=>{

        card.style.transform="translateY(-8px)";

    });

    card.addEventListener("mouseleave",()=>{

        card.style.transform="translateY(0)";

    });

});


// ===================== HERO IMAGE =====================

const heroImage =
    document.querySelector(".hero-image img");

if(heroImage){

    heroImage.addEventListener("mouseenter",()=>{

        heroImage.style.transform="scale(1.03)";

    });

    heroImage.addEventListener("mouseleave",()=>{

        heroImage.style.transform="scale(1)";

    });

    heroImage.style.transition=".4s";

}


// ===================== TIMELINE EFFECT =====================

const timelineItems =
    document.querySelectorAll(".timeline-item");

timelineItems.forEach(item=>{

    item.addEventListener("mouseenter",()=>{

        item.style.paddingLeft="45px";

    });

    item.addEventListener("mouseleave",()=>{

        item.style.paddingLeft="35px";

    });

    item.style.transition=".3s";

});


// ===================== BUTTON EFFECT =====================

const buttons =
    document.querySelectorAll(".btn");

buttons.forEach(button=>{

    button.addEventListener("mouseenter",()=>{

        button.style.transform="translateY(-2px)";

    });

    button.addEventListener("mouseleave",()=>{

        button.style.transform="translateY(0)";

    });

});


// ===================== LOAD =====================

window.addEventListener("load",()=>{

    console.log("Trang Giới thiệu đã tải thành công.");

});