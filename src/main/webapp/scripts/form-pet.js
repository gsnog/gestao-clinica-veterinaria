(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('petForm');
        if (!form || typeof VetValidation === 'undefined') return;

        form.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const nome = this.querySelector('[name="nomePet"]');
            const raca = this.querySelector('[name="racaPet"]');
            const data = this.querySelector('[name="dataNascimentoPet"]');
            const tutorInput = document.getElementById('tutorIdInput');
            const tutorHidden = document.getElementById('tutorIdHidden');
            const tutor = tutorHidden || this.querySelector('[name="tutorId"]');

            if (!nome.value.trim()) {
                v.showError(nome, 'Informe o nome do pet.');
                ok = false;
            } else if (nome.value.trim().length < 2) {
                v.showError(nome, 'Nome deve ter pelo menos 2 caracteres.');
                ok = false;
            }

            if (!raca.value.trim()) {
                v.showError(raca, 'Informe a raca.');
                ok = false;
            }

            if (!data.value) {
                v.showError(data, 'Informe a data de nascimento.');
                ok = false;
            } else if (new Date(data.value) > new Date()) {
                v.showError(data, 'A data nao pode ser no futuro.');
                ok = false;
            }

            if (tutor && !tutor.disabled && !tutor.value) {
                v.showError(tutorInput || tutor, 'Selecione um tutor valido na lista.');
                ok = false;
            }

            if (!ok) e.preventDefault();
        });
    });
})();
