/**
 * validation.js
 * Utilitário de validação de formulários — VetCare
 *
 * Expõe window.VetValidation com helpers reutilizáveis por todos os formulários.
 */
(function () {
    'use strict';

    /** Exibe mensagem de erro abaixo de um campo */
    function showError(field, message) {
        let span = field.parentNode.querySelector('.field-error');
        if (!span) {
            span = document.createElement('span');
            span.className = 'field-error';
            field.parentNode.appendChild(span);
        }
        span.textContent = message;
        field.classList.add('field-invalid');
    }

    /** Remove mensagem de erro de um campo */
    function clearError(field) {
        const span = field.parentNode.querySelector('.field-error');
        if (span) span.textContent = '';
        field.classList.remove('field-invalid');
    }

    /** Remove todos os erros de um formulário */
    function clearAllErrors(form) {
        form.querySelectorAll('.field-error').forEach(s => s.textContent = '');
        form.querySelectorAll('.field-invalid').forEach(f => f.classList.remove('field-invalid'));
    }

    /** Valida formato de e-mail */
    function isValidEmail(value) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim());
    }

    /** Valida formato de telefone: (DD) 99999-9999 ou (DD) 9999-9999 */
    function isValidTelefone(value) {
        return /^\(\d{2}\)\s\d{4,5}-\d{4}$/.test(value.trim());
    }

    /** Valida formato CRMV: CRMV-UF 12345 */
    function isValidCrmv(value) {
        return /^CRMV-[A-Z]{2} \d{5}$/.test(value.trim());
    }

    window.VetValidation = {
        showError,
        clearError,
        clearAllErrors,
        isValidEmail,
        isValidTelefone,
        isValidCrmv
    };
})();
