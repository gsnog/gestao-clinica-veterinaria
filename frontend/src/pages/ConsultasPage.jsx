import { Link } from "react-router-dom";
import { useEffect, useMemo, useState } from "react";
import Topbar from "../components/Topbar";
import DataTable from "../components/DataTable";
import domainService from "../services/domainService";
import { useAuth } from "../contexts/authContext";
import { normalizeText } from "../utils/formatters";

const MIN_FILTER_YEAR = 1900;
const MAX_FILTER_YEAR = 2100;

function isValidFilterDate(value) {
  if (!value) return true;
  if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) return false;

  const [yearText, monthText, dayText] = value.split("-");
  const year = Number(yearText);
  const month = Number(monthText);
  const day = Number(dayText);

  if (year < MIN_FILTER_YEAR || year > MAX_FILTER_YEAR) return false;
  if (month < 1 || month > 12) return false;
  if (day < 1 || day > 31) return false;

  const date = new Date(`${value}T00:00:00`);
  return (
    !Number.isNaN(date.getTime()) && date.toISOString().slice(0, 10) === value
  );
}

function ConsultasPage() {
  const { role } = useAuth();
  const canManageConsultas = role === "VETERINARIO";
  const [consultas, setConsultas] = useState([]);
  const [busca, setBusca] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [erroData, setErroData] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    domainService
      .listConsultas()
      .then((result) => {
        setConsultas(result.items || result);
        setErro("");
      })
      .catch((error) =>
        setErro(error.message || "Erro ao carregar consultas."),
      );
  }, []);

  const dataInicioValida = isValidFilterDate(dataInicio);
  const dataFimValida = isValidFilterDate(dataFim);
  const periodoInvalido =
    dataInicioValida &&
    dataFimValida &&
    dataInicio &&
    dataFim &&
    dataInicio > dataFim;

  const consultasFiltradas = useMemo(() => {
    const query = normalizeText(busca);

    return consultas.filter((consulta) => {
      const pet = normalizeText(consulta.pet?.nome);
      const vet = normalizeText(consulta.veterinario?.nome);
      const motivo = normalizeText(consulta.motivo);
      const data = consulta.dataConsulta?.slice(0, 10);

      const matchBusca =
        !query ||
        pet.includes(query) ||
        vet.includes(query) ||
        motivo.includes(query);
      const matchInicio =
        !dataInicio || (dataInicioValida && data >= dataInicio);
      const matchFim = !dataFim || (dataFimValida && data <= dataFim);

      return (
        !periodoInvalido &&
        dataInicioValida &&
        dataFimValida &&
        matchBusca &&
        matchInicio &&
        matchFim
      );
    });
  }, [
    busca,
    consultas,
    dataFim,
    dataFimValida,
    dataInicio,
    dataInicioValida,
    periodoInvalido,
  ]);

  function handleDateChange(setter) {
    return (event) => {
      const value = event.target.value;

      if (!value) {
        setter("");
        setErroData("");
        return;
      }

      const yearText = value.slice(0, 4);
      const hasOnly4YearDigits = /^\d{4}$/.test(yearText);
      const year = Number(yearText);
      const isYearInRange =
        hasOnly4YearDigits &&
        year >= MIN_FILTER_YEAR &&
        year <= MAX_FILTER_YEAR;

      if (!hasOnly4YearDigits || !isYearInRange || !isValidFilterDate(value)) {
        setErroData(
          `Use datas válidas entre ${MIN_FILTER_YEAR} e ${MAX_FILTER_YEAR}.`,
        );
        return;
      }

      setter(value);
      setErroData("");
    };
  }

  function limparFiltros() {
    setBusca("");
    setDataInicio("");
    setDataFim("");
    setErroData("");
  }

  async function remover(id) {
    if (!window.confirm("Deseja excluir esta consulta?")) return;

    try {
      await domainService.deleteConsulta(id);
      setConsultas((prev) => prev.filter((consulta) => consulta.id !== id));
      setErro("");
    } catch (error) {
      setErro(error.message || "Não foi possível excluir a consulta.");
    }
  }

  const columns = [
    {
      key: "data",
      header: "Data",
      render: (consulta) => new Date(consulta.dataConsulta).toLocaleString("pt-BR"),
    },
    { key: "pet", header: "Pet", render: (consulta) => consulta.pet?.nome },
    { key: "veterinario", header: "Veterinário", render: (consulta) => consulta.veterinario?.nome },
    { key: "motivo", header: "Motivo", render: (consulta) => consulta.motivo },
    ...(canManageConsultas
      ? [
          {
            key: "acoes",
            header: "Ações",
            render: (consulta) => (
              <div className="actions">
                <Link className="btn btn-edit" to={`/consultas/${consulta.id}/editar`}>
                  Editar
                </Link>
                <button type="button" className="btn btn-danger" onClick={() => remover(consulta.id)}>
                  Excluir
                </button>
              </div>
            ),
          },
        ]
      : []),
  ];

  return (
    <main className="main">
      <Topbar
        title="Consultas"
        subtitle="Agenda e histórico de consultas"
        action={
          canManageConsultas ? (
            <Link className="btn btn-primary" to="/consultas/nova">
              + Nova consulta
            </Link>
          ) : null
        }
      />

      <div className="filter-bar consulta-filter-bar">
        <div className="filter-group consulta-filter-group">
          <label htmlFor="busca-consulta">Buscar:</label>
          <input
            id="busca-consulta"
            type="text"
            placeholder="Pet, veterinário ou motivo"
            value={busca}
            onChange={(event) => setBusca(event.target.value)}
          />
          <label htmlFor="inicio-consulta">De:</label>
          <input
            id="inicio-consulta"
            type="date"
            value={dataInicio}
            min={`${MIN_FILTER_YEAR}-01-01`}
            max={`${MAX_FILTER_YEAR}-12-31`}
            onChange={handleDateChange(setDataInicio)}
            className={dataInicio && !dataInicioValida ? "field-invalid" : ""}
          />
          <label htmlFor="fim-consulta">Até:</label>
          <input
            id="fim-consulta"
            type="date"
            value={dataFim}
            min={`${MIN_FILTER_YEAR}-01-01`}
            max={`${MAX_FILTER_YEAR}-12-31`}
            onChange={handleDateChange(setDataFim)}
            className={dataFim && !dataFimValida ? "field-invalid" : ""}
          />
        </div>
        <div className="consulta-filter-actions">
          <button
            type="button"
            className="btn btn-outline"
            onClick={limparFiltros}
          >
            Limpar filtros
          </button>
        </div>
      </div>

      {erroData ? <p className="filter-feedback mb-16">{erroData}</p> : null}
      {periodoInvalido ? (
        <p className="filter-feedback mb-16">
          A data inicial não pode ser maior que a data final.
        </p>
      ) : null}
      {erro ? <p className="filter-feedback mb-16">{erro}</p> : null}

      <DataTable
        columns={columns}
        data={consultasFiltradas}
        rowKey={(consulta) => consulta.id}
        emptyMessage="Nenhuma consulta encontrada."
      />
    </main>
  );
}

export default ConsultasPage;
