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
            const pet = this.querySelector('[name="petId"]');
            const vet = this.querySelector('[name="vetId"]');

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

            if (pet && !pet.value) {
                v.showError(pet, 'Selecione um pet.');
                ok = false;
            }

            if (vet && !vet.value) {
                v.showError(vet, 'Selecione um veterinario.');
                ok = false;
            }

            if (!ok) e.preventDefault();
        });
    });
})();
