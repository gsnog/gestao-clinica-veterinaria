import apiFetch from "./apiClient";

const domainService = {
  login: (payload) =>
    apiFetch("/api/login", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  registro: (payload) =>
    apiFetch("/api/registro", {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  getDashboard: () => apiFetch("/api/dashboard"),
  listPets: () => apiFetch("/api/pets"),
  getPet: (id) => apiFetch(`/api/pets/${id}`),
  listConsultas: () => apiFetch("/api/consultas"),
  listTutores: () => apiFetch("/api/tutores"),
  listVeterinarios: () => apiFetch("/api/veterinarios"),
  getPerfil: () => apiFetch("/api/perfil"),

  savePet: (payload, id) =>
    apiFetch("/api/pets", {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(id ? { ...payload, id } : payload),
    }),
  deletePet: (id) => apiFetch(`/api/pets/${id}`, { method: "DELETE" }),

  saveConsulta: (payload, id) =>
    apiFetch("/api/consultas", {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(id ? { ...payload, id } : payload),
    }),
  deleteConsulta: (id) =>
    apiFetch(`/api/consultas/${id}`, { method: "DELETE" }),

  saveTutor: (payload, id) =>
    apiFetch("/api/tutores", {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(id ? { ...payload, id } : payload),
    }),
  deleteTutor: (id) => apiFetch(`/api/tutores/${id}`, { method: "DELETE" }),

  saveVeterinario: (payload, id) =>
    apiFetch("/api/veterinarios", {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(id ? { ...payload, id } : payload),
    }),
  deleteVeterinario: (id) =>
    apiFetch(`/api/veterinarios/${id}`, { method: "DELETE" }),

  updatePerfil: (payload) =>
    apiFetch("/api/perfil", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
};

export default domainService;
