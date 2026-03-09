import { useState } from "react";

const API = "http://localhost:8080";

function Modal({ title, onClose, children }) {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal__header">
          <span className="modal__title">{title}</span>
          <button className="modal__close" onClick={onClose}>✕</button>
        </div>
        {children}
      </div>
    </div>
  );
}

function Field({ label, children }) {
  return (
    <div className="field">
      <label className="field__label">{label}</label>
      {children}
    </div>
  );
}

export default function Clientes() {
  const [clientes, setClientes] = useState([]);
  const [error, setError] = useState(null);

  const [telefono, setTelefono] = useState("");
  const [buscando, setBuscando] = useState(false);
  const [buscado, setBuscado] = useState(false);

  const [modalCrear, setModalCrear] = useState(false);
  const [modalEditar, setModalEditar] = useState(null);
  const [modalDetalle, setModalDetalle] = useState(null);

  const [formCrear, setFormCrear] = useState({ nombre: "", telefono: "", direccion: "" });
  const [creando, setCreando] = useState(false);
  const [formError, setFormError] = useState("");

  const [formEditar, setFormEditar] = useState({ nombre: "", telefono: "", direccion: "" });
  const [editando, setEditando] = useState(false);

  async function buscarPorTelefono() {
    if (!telefono.trim()) return;
    setBuscando(true); setError(null);
    try {
      const res = await fetch(`${API}/clientes?telefono=${telefono.trim()}`);
      if (!res.ok) throw new Error("Error al buscar clientes");
      setClientes(await res.json());
      setBuscado(true);
    } catch (e) { setError(e.message); }
    finally { setBuscando(false); }
  }

  async function crearCliente() {
    setFormError("");
    if (!formCrear.nombre || !formCrear.telefono) {
      setFormError("Nombre y teléfono son obligatorios."); return;
    }
    setCreando(true);
    try {
      const res = await fetch(`${API}/clientes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formCrear),
      });
      if (!res.ok) throw new Error("Error al crear cliente");
      setModalCrear(false);
      setFormCrear({ nombre: "", telefono: "", direccion: "" });
      if (buscado && telefono) buscarPorTelefono();
    } catch (e) { setFormError(e.message); }
    finally { setCreando(false); }
  }

  async function guardarEdicion() {
    setEditando(true);
    try {
      const res = await fetch(`${API}/clientes/${modalEditar.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formEditar),
      });
      if (!res.ok) throw new Error("Error al actualizar");
      setModalEditar(null);
      if (buscado && telefono) buscarPorTelefono();
    } catch (e) { alert(e.message); }
    finally { setEditando(false); }
  }

  function abrirEditar(cliente) {
    setFormEditar({ nombre: cliente.nombre, telefono: cliente.telefono, direccion: cliente.direccion || "" });
    setModalEditar(cliente);
  }

  const limpiarBusqueda = () => { setTelefono(""); setClientes([]); setBuscado(false); setError(null); };

  const formatFecha = (f) => f ? new Date(f).toLocaleDateString("es-MX", { day: "2-digit", month: "short", year: "numeric" }) : "—";

  return (
    <div className="envios-page">

      {/* Header */}
      <div className="envios-header">
        <div className="envios-header__inner">
          <div className="envios-header__brand">
            <div className="envios-header__logo">📦</div>
            <span className="envios-header__name">VantusExpress</span>
            <span className="envios-header__sep">/</span>
            <span className="envios-header__section">Clientes</span>
          </div>
          <button className="btn-primary" onClick={() => setModalCrear(true)}>
            <span>+</span> Nuevo cliente
          </button>
        </div>
      </div>

      <div className="envios-content">

        {/* Stats */}
        <div className="envios-stats" style={{ gridTemplateColumns: "repeat(3, 1fr)" }}>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Resultados</div>
            <div className="envios-stat-card__value" style={{ color: "#6366f1" }}>{clientes.length}</div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Teléfono buscado</div>
            <div className="envios-stat-card__value" style={{ color: "#111", fontSize: 18, fontFamily: "'DM Mono', monospace" }}>
              {buscado ? telefono : "—"}
            </div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Estado</div>
            <div className="envios-stat-card__value" style={{ color: buscado ? "#10b981" : "#9ca3af", fontSize: 20 }}>
              {buscado ? "Filtrado" : "Sin filtro"}
            </div>
          </div>
        </div>

        {/* Búsqueda */}
        <div className="envios-search">
          <input
            className="field__input"
            type="tel"
            value={telefono}
            onChange={e => setTelefono(e.target.value)}
            onKeyDown={e => e.key === "Enter" && buscarPorTelefono()}
            placeholder="Buscar cliente por teléfono  (ej. 2461234567)"
          />
          {buscado && (
            <button className="btn-secondary" onClick={limpiarBusqueda} style={{ padding: "9px 14px" }}>✕</button>
          )}
          <button className="btn-primary" onClick={buscarPorTelefono} disabled={buscando || !telefono}>
            {buscando ? "..." : "Buscar"}
          </button>
        </div>

        {/* Tabla */}
        <div className="envios-table">
          <div className="clientes-table__head">
            {["ID", "Nombre", "Teléfono", "Dirección", "Registro", ""].map((h, i) => (
              <span key={i} className="envios-table__head-cell">{h}</span>
            ))}
          </div>

          {error ? (
            <div className="envios-table__error">{error}</div>
          ) : !buscado ? (
            <div className="envios-table__empty">Ingresa un teléfono para buscar clientes.</div>
          ) : buscando ? (
            <div className="envios-table__loading">Cargando...</div>
          ) : clientes.length === 0 ? (
            <div className="envios-table__empty">No se encontró ningún cliente con ese teléfono.</div>
          ) : (
            clientes.map(cliente => (
              <div key={cliente.id} className="clientes-table__row envios-table__row">
                <span className="envios-table__folio">#{cliente.id}</span>
                <span style={{ fontSize: 13, color: "#374151", fontWeight: 500 }}>{cliente.nombre}</span>
                <span style={{ fontSize: 13, color: "#374151", fontFamily: "'DM Mono', monospace" }}>{cliente.telefono}</span>
                <span className="envios-table__contenido">{cliente.direccion || "—"}</span>
                <span className="envios-table__destinatario">{formatFecha(cliente.fechaRegistro)}</span>
                <div className="envios-table__actions">
                  <button className="btn-icon" onClick={() => setModalDetalle(cliente)} title="Ver detalle">👁</button>
                  <button className="btn-icon" onClick={() => abrirEditar(cliente)} title="Editar">✏️</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="envios-count">
          {buscado && `${clientes.length} cliente${clientes.length !== 1 ? "s" : ""}`}
        </div>
      </div>

      {/* Modal Crear */}
      {modalCrear && (
        <Modal title="Nuevo cliente" onClose={() => { setModalCrear(false); setFormError(""); }}>
          <Field label="Nombre">
            <input className="field__input" placeholder="Victor García"
              value={formCrear.nombre} onChange={e => setFormCrear(p => ({ ...p, nombre: e.target.value }))} />
          </Field>
          <Field label="Teléfono">
            <input className="field__input" placeholder="2461234567"
              value={formCrear.telefono} onChange={e => setFormCrear(p => ({ ...p, telefono: e.target.value }))} />
          </Field>
          <Field label="Dirección">
            <input className="field__input" placeholder="Calle Morelos 12, Apizaco, Tlaxcala"
              value={formCrear.direccion} onChange={e => setFormCrear(p => ({ ...p, direccion: e.target.value }))} />
          </Field>
          {formError && <div className="form-error">{formError}</div>}
          <div className="modal__footer">
            <button className="btn-secondary" onClick={() => { setModalCrear(false); setFormError(""); }}>Cancelar</button>
            <button className="btn-primary" onClick={crearCliente} disabled={creando}>
              {creando ? "Creando..." : "Crear cliente"}
            </button>
          </div>
        </Modal>
      )}

      {/* Modal Editar */}
      {modalEditar && (
        <Modal title={`Editar · ${modalEditar.nombre}`} onClose={() => setModalEditar(null)}>
          <Field label="Nombre">
            <input className="field__input" value={formEditar.nombre}
              onChange={e => setFormEditar(p => ({ ...p, nombre: e.target.value }))} />
          </Field>
          <Field label="Teléfono">
            <input className="field__input" value={formEditar.telefono}
              onChange={e => setFormEditar(p => ({ ...p, telefono: e.target.value }))} />
          </Field>
          <Field label="Dirección">
            <input className="field__input" value={formEditar.direccion}
              onChange={e => setFormEditar(p => ({ ...p, direccion: e.target.value }))} />
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
        <Modal title="Detalle del cliente" onClose={() => setModalDetalle(null)}>
          <div className="modal__detail-grid">
            {[
              ["ID",        `#${modalDetalle.id}`],
              ["Registro",  formatFecha(modalDetalle.fechaRegistro)],
              ["Nombre",    modalDetalle.nombre],
              ["Teléfono",  modalDetalle.telefono],
              ["Dirección", modalDetalle.direccion || "—"],
            ].map(([label, val]) => (
              <div key={label}>
                <div className="modal__detail-label">{label}</div>
                <span className="modal__detail-value">{val}</span>
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