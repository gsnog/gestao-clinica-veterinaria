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

        if (telefoneInput) {
            telefoneInput.addEventListener('input', function (e) {
                let value = e.target.value.replace(/\D/g, '');
                if (value.length > 11) value = value.slice(0, 11);
                if (value.length <= 10) {
                    value = value.replace(/^(\d{2})(\d)/g, '($1) $2');
                    value = value.replace(/(\d{4})(\d)/, '$1-$2');
                } else {
                    value = value.replace(/^(\d{2})(\d)/g, '($1) $2');
                    value = value.replace(/(\d{5})(\d)/, '$1-$2');
                }
                e.target.value = value;
            });
        }

        if (crmvInput) {
            crmvInput.addEventListener('input', function (e) {
                let value = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
                value = value.replace(/^CRMV/, '');
                let resultado = 'CRMV-';
                const letras = value.replace(/[^A-Z]/g, '').substring(0, 2);
                const numeros = value.replace(/[^0-9]/g, '').substring(0, 5);
                resultado += letras;
                if (letras.length === 2) resultado += ' ' + numeros;
                e.target.value = resultado;
            });
        }

        roleSelect.addEventListener('change', atualizarCamposPorRole);
        atualizarCamposPorRole();

        form.setAttribute('novalidate', '');
        form.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const nome = document.getElementById('nome');
            const email = document.getElementById('email');
            const senha = document.getElementById('senha');
            const role = document.getElementById('role');

            if (!nome.value.trim()) {
                v.showError(nome, 'Informe seu nome.');
                ok = false;
            } else if (nome.value.trim().length < 2) {
                v.showError(nome, 'Nome deve ter pelo menos 2 caracteres.');
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
