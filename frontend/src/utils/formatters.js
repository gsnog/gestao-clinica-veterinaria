export function normalizeText(value) {
  return (value || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .trim();
}

export function formatTelefone(value) {
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

export function formatCrmv(value) {
  let raw = (value || '').toUpperCase().replace(/[^A-Z0-9]/g, '');
  raw = raw.replace(/^CRMV/, '');

  const letras = raw.replace(/[^A-Z]/g, '').substring(0, 2);
  const numeros = raw.replace(/[^0-9]/g, '').substring(0, 5);

  let result = `CRMV-${letras}`;
  if (letras.length === 2) result += ` ${numeros}`;
  return result;
}

export function isEmailValid(value) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test((value || '').trim());
}

export function isTelefoneValid(value) {
  return /^\(\d{2}\)\s\d{4,5}-\d{4}$/.test((value || '').trim());
}

export function isCrmvValid(value) {
  return /^CRMV-[A-Z]{2} \d{5}$/.test((value || '').trim());
}
