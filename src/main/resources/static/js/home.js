// Sticky Navbar

window.addEventListener("scroll", function () {

    const navbar = document.querySelector(".navbar");

    if (window.scrollY > 50) {

        navbar.classList.add("shadow");

        navbar.style.background = "#ffffff";

    } else {

        navbar.classList.remove("shadow");

        navbar.style.background = "rgba(255,255,255,.95)";
    }

});


// Back To Top

const backToTop = document.getElementById("backToTop");

backToTop.addEventListener("click", function (e) {

    e.preventDefault();

    window.scrollTo({

        top:0,

        behavior:"smooth"

    });

});


// Hover animation

const cards = document.querySelectorAll(".food-card");

cards.forEach(card=>{

    card.addEventListener("mouseenter",()=>{

        card.style.transform="translateY(-10px)";

    });

    card.addEventListener("mouseleave",()=>{

        card.style.transform="translateY(0px)";

    });

});