document.addEventListener('DOMContentLoaded', function () {
    function normalizeText(value) {
        return (value || '')
            .toLowerCase()
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .trim();
    }

    function syncComboboxValue(input) {
        const listId = input.getAttribute('data-list-id');
        const hiddenTarget = input.getAttribute('data-hidden-target');
        if (!listId || !hiddenTarget) {
            return;
        }

        const dataList = document.getElementById(listId);
        const hidden = document.getElementById(hiddenTarget);
        if (!dataList || !hidden) {
            return;
        }

        const inputValue = normalizeText(input.value);
        const options = Array.from(dataList.querySelectorAll('option'));
        const match = options.find(function (option) {
            return normalizeText(option.value) === inputValue;
        });
        if (match) {
            hidden.value = match.getAttribute('data-id') || '';
        } else if (input.value !== '') {
            hidden.value = '';
        }
    }

    document.querySelectorAll('.js-combobox-input').forEach(function (input) {
        const listId = input.getAttribute('data-list-id');
        if (listId) {
            input.setAttribute('list', listId);
        }

        input.addEventListener('input', function () {
            syncComboboxValue(input);
        });

        input.addEventListener('change', function () {
            syncComboboxValue(input);
        });

    });

});

document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('dataConsultaInput');
    const hidden = document.getElementById('dataConsultaHidden');

    if (!input || !hidden) return;

    function setData() {
        hidden.value = input.value ? input.value + ':00' : '';
    }

    input.addEventListener('change', setData);
    document.querySelector('form').addEventListener('submit', setData);
    setData();
});

document.addEventListener('DOMContentLoaded', function () {
    const filterForm = document.getElementById('consultaFiltroForm');
    const filterDate = document.getElementById('filtroDataConsulta');

    if (!filterForm || !filterDate) return;

    function hasValidDate(showAlert) {
        const value = (filterDate.value || '').trim();
        if (!value) return true;

        const validFormat = /^\d{4}-\d{2}-\d{2}$/.test(value);
        if (!validFormat) {
            if (showAlert) {
                alert('Use a data no formato AAAA-MM-DD.');
                filterDate.focus();
            }
            return false;
        }

        const year = value.split('-')[0];
        if (year.length !== 4) {
            if (showAlert) {
                alert('O ano deve conter exatamente 4 digitos.');
                filterDate.focus();
            }
            return false;
        }

        return true;
    }

    filterForm.addEventListener('submit', function (e) {
        if (!hasValidDate(true)) {
            e.preventDefault();
        }
    });
});
