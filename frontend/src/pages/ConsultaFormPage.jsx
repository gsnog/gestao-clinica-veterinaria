import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";

function ConsultaFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [pets, setPets] = useState([]);
  const [veterinarios, setVeterinarios] = useState([]);
  const [form, setForm] = useState({
    petId: "",
    veterinarioId: "",
    dataConsulta: "",
    motivo: "",
    observacao: "",
  });
  const [erro, setErro] = useState("");

  useEffect(() => {
    domainService
      .listPets()
      .then((result) => setPets(result.items || result))
      .catch(() => {
        setPets([]);
        setErro("Não foi possível carregar pets e veterinários.");
      });
    domainService
      .listVeterinarios()
      .then((result) => setVeterinarios(result.items || result))
      .catch(() => {
        setVeterinarios([]);
        setErro("Não foi possível carregar pets e veterinários.");
      });
  }, []);

  useEffect(() => {
    if (!id) return;

    domainService
      .listConsultas()
      .then((result) => {
        const consultas = result.items || result || [];
        const consulta = consultas.find((item) => String(item?.id) === String(id));

        if (!consulta) {
          throw new Error();
        }

        setForm((prev) => ({
          ...prev,
          petId: consulta.pet?.id ? String(consulta.pet.id) : "",
          veterinarioId: consulta.veterinario?.id ? String(consulta.veterinario.id) : "",
          dataConsulta: consulta.dataConsulta ? consulta.dataConsulta.slice(0, 16) : "",
          motivo: consulta.motivo || "",
          observacao: consulta.diagnostico || "",
        }));
        setErro("");
      })
      .catch(() => {
        setErro("Não foi possível carregar os dados da consulta.");
      });
  }, [id]);

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    try {
      await domainService.saveConsulta(
        {
          ...form,
          diagnostico: form.observacao || "",
        },
        id,
      );
      navigate("/consultas");
    } catch (error) {
      setErro(error.message || "Erro ao salvar consulta.");
    }
  }

  return (
    <main className="main">
      <Topbar
        title="Formulário de Consulta"
        subtitle="Registro de atendimento"
      />

      <section className="form-card">
        <aside className="form-sidebar">
          <img
            src={`${import.meta.env.BASE_URL}images/consulta.webp`}
            alt="Consulta"
            className="form-sidebar-image"
          />
        </aside>

        <form className="form-body" onSubmit={onSubmit}>
          <h2 className="form-title">Dados da consulta</h2>
          <p className="form-subtitle">
            Preencha os dados obrigatórios para concluir o registro.
          </p>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="petId">Pet</label>
              <select
                id="petId"
                name="petId"
                value={form.petId}
                onChange={onChange}
                required
              >
                <option value="">Selecione</option>
                {pets.map((pet) => (
                  <option key={pet.id} value={pet.id}>
                    {pet.nome}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="veterinarioId">Veterinário</label>
              <select
                id="veterinarioId"
                name="veterinarioId"
                value={form.veterinarioId}
                onChange={onChange}
                required
              >
                <option value="">Selecione</option>
                {veterinarios.map((veterinario) => (
                  <option key={veterinario.id} value={veterinario.id}>
                    {veterinario.nome}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="dataConsulta">Data e hora</label>
              <input
                id="dataConsulta"
                name="dataConsulta"
                type="datetime-local"
                value={form.dataConsulta}
                onChange={onChange}
                required
              />
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="motivo">Motivo</label>
              <input
                id="motivo"
                name="motivo"
                value={form.motivo}
                onChange={onChange}
                required
              />
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="observacao">Observação</label>
              <textarea
                id="observacao"
                name="observacao"
                value={form.observacao}
                onChange={onChange}
                rows={4}
                placeholder="Anotações adicionais da consulta (opcional)"
              />
            </div>
          </div>

          {erro ? <p className="auth-message auth-error">{erro}</p> : null}

          <div className="form-actions">
            <Link className="btn btn-outline" to="/consultas">
              Cancelar
            </Link>
            <button type="submit" className="btn btn-submit">
              Salvar
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}

export default ConsultaFormPage;
