import { useState, useEffect } from "react";
import Modal from "../../Componentes/Modal";
import Field from "../../Componentes/Field";
import Badge from "../../Componentes/Badge";
//import "./Envios.css";

const API = import.meta.env.VITE_BACKEND_URL;

const ESTADOS = ["registrado", "en tránsito", "entregado", "cancelado"];

const estadoColor = {
  "registrado":   "#6366f1",
  "en tránsito":  "#f59e0b",
  "entregado":    "#10b981",
  "cancelado":    "#ef4444",
};

export default function Envios() {
  const [envios, setEnvios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [busqueda, setBusqueda] = useState("");
  const [buscando, setBuscando] = useState(false);

  const [modalCrear, setModalCrear] = useState(false);
  const [modalEditar, setModalEditar] = useState(null);
  const [modalDetalle, setModalDetalle] = useState(null);

  const [formCrear, setFormCrear] = useState({ clienteId: "", destinatarioId: "", contenido: "", valorEstimado: "" });
  const [creando, setCreando] = useState(false);
  const [formError, setFormError] = useState("");

  const [formEditar, setFormEditar] = useState({ contenido: "", valorEstimado: "", estado: "" });
  const [editando, setEditando] = useState(false);

  useEffect(() => { cargarTodos(); }, []);

  async function cargarTodos() {
    setLoading(true); setError(null);
    try {
      const res = await fetch(`${API}/envios`);
      const json = await res.json();
      if (!json.success) throw new Error(json.message);
      setEnvios(json.data);
    } catch (e) { setError(e.message); }
    finally { setLoading(false); }
  }

  async function buscarPorFolio() {
    if (!busqueda.trim()) return cargarTodos();
    setBuscando(true); setError(null);
    try {
      const res = await fetch(`${API}/envios?q=${busqueda.trim()}`);
      const json = await res.json();
      if (!json.success) throw new Error(json.message);
      //const data = await res.json();
      //setEnvios(Array.isArray(data) ? data : [data]);
      setEnvios(json.data);
    } catch (e) { setError(e.message); }
    finally { setBuscando(false); }
  }

  async function crearEnvio() {
    setFormError("");
    if (!formCrear.clienteId || !formCrear.destinatarioId || !formCrear.contenido) {
      setFormError("Cliente, destinatario y contenido son obligatorios."); return;
    }
    setCreando(true);
    try {
      const res = await fetch(
        `${API}/envios?clienteId=${formCrear.clienteId}&destinatarioId=${formCrear.destinatarioId}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ contenido: formCrear.contenido, valorEstimado: formCrear.valorEstimado || 0 }),
        }
      );
      const json = await res.json();
      //if (!res.ok) throw new Error("Error al crear envío");
      if (!json.succes) throw new Error(json.message);
      setModalCrear(false);
      setFormCrear({ clienteId: "", destinatarioId: "", contenido: "", valorEstimado: "" });
      cargarTodos();
    } catch (e) { setFormError(e.message); }
    finally { setCreando(false); }
  }

  async function guardarEdicion() {
    setEditando(true);
    try {
      const res = await fetch(`${API}/envios/${modalEditar.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formEditar),
      });
      const json = await res.json();
      //if (!res.ok) throw new Error("Error al actualizar");
      if (!json.success) throw new Error(json.message);
      setModalEditar(null);
      cargarTodos();
    } catch (e) { alert(e.message); }
    finally { setEditando(false); }
  }

  function abrirEditar(envio) {
    setFormEditar({ contenido: envio.contenido, valorEstimado: envio.valorEstimado, estado: envio.estado });
    setModalEditar(envio);
  }

  const limpiarBusqueda = () => { setBusqueda(""); cargarTodos(); };

  return (
      <div className="envios-page">

        {/* Header */}
        <div className="envios-header">
          <div className="envios-header__inner">
            <div className="envios-header__brand">
              <div className=""><img className="logo" src={"/vantus.png"}  alt="Logo de Vantus" /></div>
              <span className="envios-header__name">VantusExpress</span>
              <span className="envios-header__sep">/</span>
              <span className="envios-header__section">Envíos</span>
            </div>
            <button className="btn-primary" onClick={() => setModalCrear(true)}>
              <span>+</span> Nuevo envío
            </button>
          </div>
        </div>

        <div className="envios-content">

          {/* Stats */}
          <div className="envios-stats">
            {ESTADOS.map(est => (
              <div key={est} className="envios-stat-card">
                <div className="envios-stat-card__label">{est}</div>
                <div className="envios-stat-card__value" style={{ color: estadoColor[est] }}>
                  {envios.filter(e => e.estado === est).length}
                </div>
              </div>
            ))}
          </div>

          {/* Búsqueda */}
          <div className="envios-search">
            <input
              className="field__input"
              value={busqueda}
              onChange={e => setBusqueda(e.target.value)}
              onKeyDown={e => e.key === "Enter" && buscarPorFolio()}
              placeholder="Buscar por folio"
            />
            {busqueda && (
              <button className="btn-secondary" onClick={limpiarBusqueda} style={{ padding: "9px 14px" }}>✕</button>
            )}
            <button className="btn-primary" onClick={buscarPorFolio} disabled={buscando}>
              {buscando ? "..." : "Buscar"}
            </button>
          </div>

          {/* Tabla */}
          <div className="envios-table">
            <div className="envios-table__head">
              {["Folio", "Contenido", "Destinatario", "Valor", "Estado", ""].map((h, i) => (
                <span key={i} className="envios-table__head-cell">{h}</span>
              ))}
            </div>

            {loading ? (
              <div className="envios-table__loading">Cargando...</div>
            ) : error ? (
              <div className="envios-table__error">{error}</div>
            ) : envios.length === 0 ? (
              <div className="envios-table__empty">
                {busqueda ? "No se encontró ningún envío con ese folio." : "No hay envíos registrados aún."}
              </div>
            ) : (
              envios.map(envio => (
                <div key={envio.id} className="envios-table__row">
                  <span className="envios-table__folio">{envio.folio}</span>
                  <span className="envios-table__contenido">{envio.contenido}</span>
                  <span className="envios-table__destinatario">
                    {envio.destinatario?.nombre || `#${envio.destinatario?.id || "—"}`}
                  </span>
                  <span className="envios-table__valor">
                    ${Number(envio.valorEstimado || 0).toLocaleString("es-MX", { minimumFractionDigits: 2 })}
                  </span>
                  <Badge estado={envio.estado} />
                  <div className="envios-table__actions">
                    <button className="btn-icon" onClick={() => setModalDetalle(envio)} title="Ver detalle">👁</button>
                    <button className="btn-icon" onClick={() => abrirEditar(envio)} title="Editar">✏️</button>
                  </div>
                </div>
              ))
            )}
          </div>

          <div className="envios-count">
            {!loading && `${envios.length} envío${envios.length !== 1 ? "s" : ""}`}
          </div>
        </div>

        {/* Modal Crear */}
        {modalCrear && (
          <Modal title="Nuevo envío" onClose={() => { setModalCrear(false); setFormError(""); }}>
            <Field label="ID Cliente">
              <input className="field__input" type="number" placeholder="1"
                value={formCrear.clienteId} onChange={e => setFormCrear(p => ({ ...p, clienteId: e.target.value }))} />
            </Field>
            <Field label="ID Destinatario">
              <input className="field__input" type="number" placeholder="1"
                value={formCrear.destinatarioId} onChange={e => setFormCrear(p => ({ ...p, destinatarioId: e.target.value }))} />
            </Field>
            <Field label="Contenido">
              <input className="field__input" placeholder="Electrónicos, ropa, documentos..."
                value={formCrear.contenido} onChange={e => setFormCrear(p => ({ ...p, contenido: e.target.value }))} />
            </Field>
            <Field label="Valor estimado (MXN)">
              <input className="field__input" type="number" placeholder="0.00"
                value={formCrear.valorEstimado} onChange={e => setFormCrear(p => ({ ...p, valorEstimado: e.target.value }))} />
            </Field>
            {formError && <div className="form-error">{formError}</div>}
            <div className="modal__footer">
              <button className="btn-secondary" onClick={() => { setModalCrear(false); setFormError(""); }}>Cancelar</button>
              <button className="btn-primary" onClick={crearEnvio} disabled={creando}>
                {creando ? "Creando..." : "Crear envío"}
              </button>
            </div>
          </Modal>
        )}

        {/* Modal Editar */}
        {modalEditar && (
          <Modal title={`Editar · ${modalEditar.folio}`} onClose={() => setModalEditar(null)}>
            <Field label="Contenido">
              <input className="field__input" value={formEditar.contenido}
                onChange={e => setFormEditar(p => ({ ...p, contenido: e.target.value }))} />
            </Field>
            <Field label="Valor estimado (MXN)">
              <input className="field__input" type="number" value={formEditar.valorEstimado}
                onChange={e => setFormEditar(p => ({ ...p, valorEstimado: e.target.value }))} />
            </Field>
            <Field label="Estado">
              <select className="field__select" value={formEditar.estado}
                onChange={e => setFormEditar(p => ({ ...p, estado: e.target.value }))}>
                {ESTADOS.map(est => <option key={est} value={est}>{est}</option>)}
              </select>
            </Field>
            <div className="modal__footer">
              <button className="btn-secondary" onClick={() => setModalEditar(null)}>Cancelar</button>
              <button className="btn-primary" onClick={guardarEdicion} disabled={editando}>
                {editando ? "Guardando..." : "Guardar cambios"}
              </button>
            </div>
          </Modal>
        )}

        {/* Modal Detalle */}
        {modalDetalle && (
          <Modal title="Detalle del envío" onClose={() => setModalDetalle(null)}>
            <div className="modal__detail-grid">
              {[
                ["Folio",          modalDetalle.folio,          "folio"],
                ["Estado",         modalDetalle.estado,          "badge"],
                ["Contenido",      modalDetalle.contenido,       "text"],
                ["Valor estimado", `$${Number(modalDetalle.valorEstimado || 0).toLocaleString("es-MX", { minimumFractionDigits: 2 })} MXN`, "text"],
                ["Cliente ID",     modalDetalle.cliente?.id || "—", "text"],
                ["Destinatario",   modalDetalle.destinatario?.nombre || `#${modalDetalle.destinatario?.id || "—"}`, "text"],
                ["Fecha",          modalDetalle.fecha ? new Date(modalDetalle.fecha).toLocaleString("es-MX") : "—", "text"],
              ].map(([label, val, type]) => (
                <div key={label}>
                  <div className="modal__detail-label">{label}</div>
                  {type === "badge"  && <Badge estado={val} />}
                  {type === "folio"  && <span className="modal__detail-folio">{val}</span>}
                  {type === "text"   && <span className="modal__detail-value">{val}</span>}
                </div>
              ))}
            </div>
            <div className="modal__footer">
              <button className="btn-secondary" onClick={() => setModalDetalle(null)}>Cerrar</button>
            </div>
          </Modal>
        )}
      </div>
  );
}