function lazyload() {

    //
    var imgs = $('.anime_icon1_img');
    for (var i = 0; i < imgs.length; i++) {
        imgs[i].src = $('.anime_icon1:nth-child(' + (i + 1) + ') .anime_icon1_img').data('src');
    }

    var lazyloadImages = document.querySelectorAll("img.lazy");
    lazyloadImages.forEach(function (img) {
        img.classList.remove('lazy');
    });
}

document.addEventListener("DOMContentLoaded", function () {
    setTimeout(lazyload, 0.2);
});

