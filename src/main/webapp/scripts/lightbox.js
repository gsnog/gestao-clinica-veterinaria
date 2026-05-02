// scripts/lightbox.js
document.addEventListener('DOMContentLoaded', function () {
    const lightbox    = document.getElementById('lightbox');
    const lightboxImg = document.getElementById('lightbox-img');
    const closeBtn    = document.getElementById('lightbox-close');

    if (!lightbox) return; // sai se não existir lightbox na página

    document.querySelectorAll('.gallery-item img').forEach(img => {
        img.addEventListener('click', () => {
            lightboxImg.src = img.src;
            lightbox.classList.add('open');
        });
    });

    closeBtn.addEventListener('click', () => lightbox.classList.remove('open'));

    lightbox.addEventListener('click', e => {
        if (e.target === lightbox) lightbox.classList.remove('open');
    });
});