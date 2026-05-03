(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('loginForm');
        if (!form || typeof VetValidation === 'undefined') return;

        const emailInput = document.getElementById('email');
        const lembrarCheck = document.getElementById('lembrar');
        if (emailInput && lembrarCheck && emailInput.value.trim() !== '') {
            lembrarCheck.checked = true;
        }

        form.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const email = document.getElementById('email');
            const senha = document.getElementById('senha');

            if (!email.value.trim()) {
                v.showError(email, 'Informe o e-mail.');
                ok = false;
            } else if (!v.isValidEmail(email.value.trim())) {
                v.showError(email, 'E-mail invalido.');
                ok = false;
            }

            if (!senha.value) {
                v.showError(senha, 'Informe a senha.');
                ok = false;
            }

            if (!ok) e.preventDefault();
        });
    });
})();
