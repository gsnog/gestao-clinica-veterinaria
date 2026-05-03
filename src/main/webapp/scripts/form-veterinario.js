(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('vetForm');
        if (!form || typeof VetValidation === 'undefined') return;

        const crmvInput = document.getElementById('crmv');
        const select = document.getElementById('especialidadeSelect');
        const custom = document.getElementById('especialidadeCustom');

        VetValidation.bindCrmvFormatter(crmvInput);

        if (select && custom) {
            select.addEventListener('change', function () {
                if (select.value === 'outro') {
                    custom.classList.remove('is-hidden');
                    custom.required = true;
                } else {
                    custom.classList.add('is-hidden');
                    custom.required = false;
                }
            });
        }

        form.addEventListener('submit', function (e) {
            if (select && custom && select.value === 'outro') {
                select.value = custom.value;
            }

            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const nome = this.querySelector('[name="nomeVet"]');
            const crmv = document.getElementById('crmv');
            const especialidade = document.getElementById('especialidadeSelect');

            if (!nome.value.trim()) {
                v.showError(nome, 'Informe o nome do veterinario.');
                ok = false;
            } else if (nome.value.trim().length < 2) {
                v.showError(nome, 'Nome deve ter pelo menos 2 caracteres.');
                ok = false;
            }

            if (!crmv.value.trim()) {
                v.showError(crmv, 'Informe o CRMV.');
                ok = false;
            } else if (!v.isValidCrmv(crmv.value)) {
                v.showError(crmv, 'Formato invalido. Use CRMV-UF 12345.');
                ok = false;
            }

            const espFinal = especialidade.value === 'outro' ? custom.value.trim() : especialidade.value;
            if (!espFinal) {
                v.showError(especialidade, 'Selecione ou informe a especialidade.');
                ok = false;
            }

            if (!ok) e.preventDefault();
        });
    });
})();
