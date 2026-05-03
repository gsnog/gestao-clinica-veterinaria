(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('tutorForm');
        if (!form || typeof VetValidation === 'undefined') return;

        const telefoneInput = document.getElementById('telefoneTutor');
        VetValidation.bindTelefoneFormatter(telefoneInput);

        form.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const nome = this.querySelector('[name="nomeTutor"]');
            const telefone = document.getElementById('telefoneTutor');

            if (!nome.value.trim()) {
                v.showError(nome, 'Informe o nome do tutor.');
                ok = false;
            } else if (nome.value.trim().length < 2) {
                v.showError(nome, 'Nome deve ter pelo menos 2 caracteres.');
                ok = false;
            }

            if (!telefone.value.trim()) {
                v.showError(telefone, 'Informe o telefone.');
                ok = false;
            } else if (!v.isValidTelefone(telefone.value)) {
                v.showError(telefone, 'Use o formato (DDD) 99999-9999.');
                ok = false;
            }

            if (!ok) e.preventDefault();
        });
    });
})();
