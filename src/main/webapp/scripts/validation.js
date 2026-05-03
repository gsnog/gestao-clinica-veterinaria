/**
 * validation.js
 * Utilitário de validação de formulários — VetCare
 *
 * Expõe window.VetValidation com helpers reutilizáveis por todos os formulários.
 */
(function () {
    'use strict';

    function capitalizeWords(value) {
        return (value || '').replace(/(^|\s)([A-Za-zÀ-ÖØ-öø-ÿ])/g, function (_, space, letter) {
            return space + letter.toUpperCase();
        });
    }

    // Applies to any field marked with .js-proper-name, independent of page-specific scripts.
    document.addEventListener('input', function (event) {
        const field = event.target;
        if (!field || !field.classList || !field.classList.contains('js-proper-name')) {
            return;
        }

        const pos = field.selectionStart;
        const capitalized = capitalizeWords(field.value);
        if (field.value !== capitalized) {
            field.value = capitalized;
            if (typeof pos === 'number' && typeof field.setSelectionRange === 'function') {
                field.setSelectionRange(pos, pos);
            }
        }
    });

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.js-proper-name').forEach(function (field) {
            field.setAttribute('autocapitalize', 'words');
        });
    });

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

    /** Formata telefone para os padrões aceitos */
    function formatTelefone(value) {
        let digits = (value || '').replace(/\D/g, '');
        if (digits.length > 11) digits = digits.slice(0, 11);

        if (digits.length <= 10) {
            digits = digits.replace(/^(\d{2})(\d)/g, '($1) $2');
            digits = digits.replace(/(\d{4})(\d)/, '$1-$2');
            return digits;
        }

        digits = digits.replace(/^(\d{2})(\d)/g, '($1) $2');
        digits = digits.replace(/(\d{5})(\d)/, '$1-$2');
        return digits;
    }

    /** Valida formato CRMV: CRMV-UF 12345 */
    function isValidCrmv(value) {
        return /^CRMV-[A-Z]{2} \d{5}$/.test(value.trim());
    }

    /** Formata CRMV para o padrão CRMV-UF 12345 */
    function formatCrmv(value) {
        let raw = (value || '').toUpperCase().replace(/[^A-Z0-9]/g, '');
        raw = raw.replace(/^CRMV/, '');

        const letras = raw.replace(/[^A-Z]/g, '').substring(0, 2);
        const numeros = raw.replace(/[^0-9]/g, '').substring(0, 5);

        let result = 'CRMV-' + letras;
        if (letras.length === 2) {
            result += ' ' + numeros;
        }
        return result;
    }

    /** Registra máscara de telefone em um input */
    function bindTelefoneFormatter(field) {
        if (!field) return;
        field.addEventListener('input', function (e) {
            e.target.value = formatTelefone(e.target.value);
        });
    }

    /** Registra máscara de CRMV em um input */
    function bindCrmvFormatter(field) {
        if (!field) return;
        field.addEventListener('input', function (e) {
            e.target.value = formatCrmv(e.target.value);
        });
    }

    // Evita repetição de onsubmit inline para confirmações de ações destrutivas.
    document.addEventListener('submit', function (event) {
        const form = event.target;
        if (!form || !form.classList || !form.classList.contains('js-confirm-submit')) {
            return;
        }

        const message = form.getAttribute('data-confirm-message') || 'Tem certeza?';
        if (!window.confirm(message)) {
            event.preventDefault();
        }
    });

    window.VetValidation = {
        showError,
        clearError,
        clearAllErrors,
        isValidEmail,
        isValidTelefone,
        isValidCrmv,
        formatTelefone,
        formatCrmv,
        bindTelefoneFormatter,
        bindCrmvFormatter
    };
})();
