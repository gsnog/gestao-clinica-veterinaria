(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('consultaForm');
        if (!form || typeof VetValidation === 'undefined') return;

        form.addEventListener('submit', function (e) {
            const v = VetValidation;
            v.clearAllErrors(this);
            let ok = true;

            const dataInput = document.getElementById('dataConsultaInput');
            const motivo = this.querySelector('[name="motivo"]');
            const diagnostico = this.querySelector('[name="diagnostico"]');
            const petInput = document.getElementById('petIdInput');
            const petHidden = document.getElementById('petIdHidden');
            const vetInput = document.getElementById('vetIdInput');
            const vetHidden = document.getElementById('vetIdHidden');

            if (!dataInput.value) {
                v.showError(dataInput, 'Informe a data e hora da consulta.');
                ok = false;
            } else if (new Date(dataInput.value) < new Date('2000-01-01')) {
                v.showError(dataInput, 'Data invalida.');
                ok = false;
            }

            if (!motivo.value.trim()) {
                v.showError(motivo, 'Informe o motivo da consulta.');
                ok = false;
            } else if (motivo.value.trim().length < 3) {
                v.showError(motivo, 'Motivo deve ter pelo menos 3 caracteres.');
                ok = false;
            }

            if (diagnostico && diagnostico.value.trim().length > 0 && diagnostico.value.trim().length < 5) {
                v.showError(diagnostico, 'O diagnostico deve ter pelo menos 5 caracteres.');
                ok = false;
            }

            if (petHidden && !petHidden.value) {
                v.showError(petInput, 'Selecione um pet valido na lista.');
                ok = false;
            }

            if (vetHidden && !vetHidden.value) {
                v.showError(vetInput, 'Selecione um veterinario valido na lista.');
                ok = false;
            }

            if (!ok) e.preventDefault();
        });
    });
})();
