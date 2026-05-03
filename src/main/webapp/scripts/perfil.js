(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const telefoneInput = document.getElementById('telefone');
        VetValidation.bindTelefoneFormatter(telefoneInput);

        const profileForm = document.querySelector('.profile-form');
        if (!profileForm || typeof VetValidation === 'undefined') return;

        profileForm.setAttribute('novalidate', '');
        profileForm.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const email = document.getElementById('email');
            if (email) {
                if (!email.value.trim()) {
                    v.showError(email, 'Informe o e-mail.');
                    ok = false;
                } else if (!v.isValidEmail(email.value.trim())) {
                    v.showError(email, 'E-mail invalido.');
                    ok = false;
                }
            }

            const tel = document.getElementById('telefone');
            if (tel) {
                if (!tel.value.trim()) {
                    v.showError(tel, 'Informe o telefone.');
                    ok = false;
                } else if (!v.isValidTelefone(tel.value.trim())) {
                    v.showError(tel, 'Use o formato (DDD) 99999-9999.');
                    ok = false;
                }
            }

            if (!ok) e.preventDefault();
        });
    });
})();
