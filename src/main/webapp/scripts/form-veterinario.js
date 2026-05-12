(function () {

    'use strict';

    document.addEventListener('DOMContentLoaded', function () {

        const form = document.getElementById('vetForm');

        if (!form || typeof VetValidation === 'undefined') {
            return;
        }

        const crmvInput = document.getElementById('crmv');

        const select = document.getElementById('especialidadeSelect');

        const custom = document.getElementById('especialidadeCustom');

        const especialidadeFinal = document.getElementById('especialidadeFinal');
        const savedEspecialidade = (form.dataset.especialidade || '').trim();

        VetValidation.bindCrmvFormatter(crmvInput);

        function normalizeText(value) {
            return value
                .trim()
                .toLowerCase()
                .normalize('NFD')
                .replace(/[\u0300-\u036f]/g, '');
        }

        function getMatchingOptionValue(especialidade) {
            const normalizedEspecialidade = normalizeText(especialidade);

            for (const option of select.options) {
                if (!option.value) {
                    continue;
                }

                if (normalizeText(option.value) === normalizedEspecialidade) {
                    return option.value;
                }

                if (normalizeText(option.textContent) === normalizedEspecialidade) {
                    return option.value;
                }
            }

            return '';
        }

        function toggleEspecialidadeCustom() {

            const isOutro = select.value === 'outro';

            custom.classList.toggle('is-hidden', !isOutro);

            custom.required = isOutro;

            if (!isOutro) {
                custom.value = '';
            }
        }

        if (savedEspecialidade) {
            const matchedOption = getMatchingOptionValue(savedEspecialidade);

            if (matchedOption) {
                select.value = matchedOption;
            } else {
                select.value = 'outro';
                custom.value = savedEspecialidade;
            }
        }

        toggleEspecialidadeCustom();

        select.addEventListener('change', toggleEspecialidadeCustom);

        form.addEventListener('submit', function (e) {

            const v = VetValidation;

            v.clearAllErrors(form);

            let ok = true;

            const nome = form.querySelector('[name="nomeVet"]');

            const crmv = document.getElementById('crmv');

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

            let especialidade = '';

            if (select.value === 'outro') {

                especialidade = custom.value.trim();

                if (!especialidade) {

                    v.showError(custom, 'Informe a especialidade.');

                    ok = false;
                }

            } else {

                especialidade = select.value;

                if (!especialidade) {

                    v.showError(select, 'Selecione a especialidade.');

                    ok = false;
                }
            }

            especialidadeFinal.value = especialidade;

            if (!ok) {
                e.preventDefault();
            }
        });
    });

})();