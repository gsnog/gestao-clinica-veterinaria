// scripts/consulta.js
document.addEventListener('DOMContentLoaded', function () {
    const input  = document.getElementById('dataConsultaInput');
    const hidden = document.getElementById('dataConsultaHidden');

    if (!input) return; // sai se não estiver na página de consulta

    function setData() {
        if (input.value) hidden.value = input.value + ':00';
    }

    input.addEventListener('change', setData);
    document.querySelector('form').addEventListener('submit', setData);
    setData();
});