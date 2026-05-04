(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const form = document.querySelector('.auth-form');

        if (!form || typeof VetValidation === 'undefined') return;

        const roleSelect = document.getElementById('role');
        const tutorFields = document.getElementById('tutorFields');
        const vetFields = document.getElementById('vetFields');
        const telefoneInput = document.getElementById('telefone');
        const crmvInput = document.getElementById('crmv');
        const especialidadeInput = document.getElementById('especialidade');

        function atualizarCamposPorRole() {
            const role = roleSelect.value;
            const isTutor = role === 'TUTOR';
            const isVet = role === 'VETERINARIO';

            tutorFields.style.display = isTutor ? 'block' : 'none';
            vetFields.style.display = isVet ? 'block' : 'none';

            telefoneInput.required = isTutor;
            crmvInput.required = isVet;
            especialidadeInput.required = isVet;
        }

        VetValidation.bindTelefoneFormatter(telefoneInput);
        VetValidation.bindCrmvFormatter(crmvInput);

        roleSelect.addEventListener('change', atualizarCamposPorRole);
        atualizarCamposPorRole();

        form.setAttribute('novalidate', '');
        form.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            function isValidFullName(value) {
                const parts = (value || '').trim().split(/\s+/).filter(Boolean);
                if (parts.length < 2) {
                    return false;
                }

                return parts.every(function (part) {
                    return part.length >= 2;
                });
            }

            const nome = document.getElementById('nome');
            const email = document.getElementById('email');
            const senha = document.getElementById('senha');
            const role = document.getElementById('role');

            if (!nome.value.trim()) {
                v.showError(nome, 'Informe seu nome.');
                ok = false;
            } else if (!isValidFullName(nome.value)) {
                v.showError(nome, 'Informe nome e sobrenome (mínimo 2 palavras).');
                ok = false;
            }

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
            } else if (senha.value.length < 6) {
                v.showError(senha, 'A senha deve ter pelo menos 6 caracteres.');
                ok = false;
            }

            if (!role.value) {
                v.showError(role, 'Selecione o tipo de usuario.');
                ok = false;
            }

            if (role.value === 'TUTOR') {
                const tel = document.getElementById('telefone');
                if (!tel.value.trim()) {
                    v.showError(tel, 'Informe o telefone.');
                    ok = false;
                } else if (!v.isValidTelefone(tel.value.trim())) {
                    v.showError(tel, 'Use o formato (DDD) 99999-9999.');
                    ok = false;
                }
            }

            if (role.value === 'VETERINARIO') {
                const crmvEl = document.getElementById('crmv');
                const espEl = document.getElementById('especialidade');
                if (!crmvEl.value.trim()) {
                    v.showError(crmvEl, 'Informe o CRMV.');
                    ok = false;
                } else if (!v.isValidCrmv(crmvEl.value.trim())) {
                    v.showError(crmvEl, 'Formato invalido. Use CRMV-UF 12345.');
                    ok = false;
                }
                if (!espEl.value.trim()) {
                    v.showError(espEl, 'Informe a especialidade.');
                    ok = false;
                }
            }

            if (!ok) e.preventDefault();
        });
    });
})();
