import { useState } from "react";

const API = import.meta.env.VITE_BACKEND_URL;

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

export default function Destinatarios() {
  const [destinatarios, setDestinatarios] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [clienteId, setClienteId] = useState("");
  const [buscando, setBuscando] = useState(false);
  const [buscado, setBuscado] = useState(false);

  const [modalCrear, setModalCrear] = useState(false);
  const [modalEditar, setModalEditar] = useState(null);
  const [modalDetalle, setModalDetalle] = useState(null);

  const [formCrear, setFormCrear] = useState({ clienteId: "", nombre: "", telefono: "", direccion: "" });
  const [creando, setCreando] = useState(false);
  const [formError, setFormError] = useState("");

  const [formEditar, setFormEditar] = useState({ nombre: "", telefono: "", direccion: "" });
  const [editando, setEditando] = useState(false);

  async function buscarPorCliente() {
    if (!clienteId.trim()) return;
    setBuscando(true); setError(null);
    try {
      const res = await fetch(`${API}/destinatarios?clienteId=${clienteId.trim()}`);
      if (!res.ok) throw new Error("Error al buscar destinatarios");
      setDestinatarios(await res.json());
      setBuscado(true);
    } catch (e) { setError(e.message); }
    finally { setBuscando(false); }
  }

  async function crearDestinatario() {
    setFormError("");
    if (!formCrear.clienteId || !formCrear.nombre || !formCrear.telefono) {
      setFormError("ID de cliente, nombre y teléfono son obligatorios."); return;
    }
    setCreando(true);
    try {
      const res = await fetch(`${API}/destinatarios?clienteId=${formCrear.clienteId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre: formCrear.nombre, telefono: formCrear.telefono, direccion: formCrear.direccion }),
      });
      if (!res.ok) throw new Error("Error al crear destinatario");
      setModalCrear(false);
      setFormCrear({ clienteId: "", nombre: "", telefono: "", direccion: "" });
      if (buscado && clienteId) buscarPorCliente();
    } catch (e) { setFormError(e.message); }
    finally { setCreando(false); }
  }

  async function guardarEdicion() {
    setEditando(true);
    try {
      const res = await fetch(`${API}/destinatarios/${modalEditar.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formEditar),
      });
      if (!res.ok) throw new Error("Error al actualizar");
      setModalEditar(null);
      if (buscado && clienteId) buscarPorCliente();
    } catch (e) { alert(e.message); }
    finally { setEditando(false); }
  }

  function abrirEditar(dest) {
    setFormEditar({ nombre: dest.nombre, telefono: dest.telefono, direccion: dest.direccion || "" });
    setModalEditar(dest);
  }

  const limpiarBusqueda = () => { setClienteId(""); setDestinatarios([]); setBuscado(false); setError(null); };

  return (

        <div className="envios-page">

            {/* Header */}
            <div className="envios-header">
            <div className="envios-header__inner">
                <div className="envios-header__brand">
                <div className=""><img className="logo" src={"/vantus.png"}  alt="Logo de Vantus" /></div>
                <span className="envios-header__name">VantusExpress</span>
                <span className="envios-header__sep">/</span>
                <span className="envios-header__section">Destinatarios</span>
                </div>
                <button className="btn-primary" onClick={() => setModalCrear(true)}>
                <span>+</span> Nuevo destinatario
                </button>
            </div>
            </div>

            <div className="envios-content">

            {/* Stats */}
            <div className="envios-stats" style={{ gridTemplateColumns: "repeat(3, 1fr)" }}>
                <div className="envios-stat-card">
                <div className="envios-stat-card__label">Total mostrados</div>
                <div className="envios-stat-card__value" style={{ color: "#6366f1" }}>{destinatarios.length}</div>
                </div>
                <div className="envios-stat-card">
                <div className="envios-stat-card__label">Cliente filtrado</div>
                <div className="envios-stat-card__value" style={{ color: "#111", fontSize: 20 }}>
                    {buscado ? `#${clienteId}` : "—"}
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
                type="number"
                value={clienteId}
                onChange={e => setClienteId(e.target.value)}
                onKeyDown={e => e.key === "Enter" && buscarPorCliente()}
                placeholder="Buscar destinatarios por ID de cliente  (ej. 1)"
                />
                {buscado && (
                <button className="btn-secondary" onClick={limpiarBusqueda} style={{ padding: "9px 14px" }}>✕</button>
                )}
                <button className="btn-primary" onClick={buscarPorCliente} disabled={buscando || !clienteId}>
                {buscando ? "..." : "Buscar"}
                </button>
            </div>

            {/* Tabla */}
            <div className="envios-table">
                <div className="dest-table__head">
                {["ID", "Nombre", "Teléfono", "Dirección", "Cliente", ""].map((h, i) => (
                    <span key={i} className="envios-table__head-cell">{h}</span>
                ))}
                </div>

                {error ? (
                <div className="envios-table__error">{error}</div>
                ) : !buscado ? (
                <div className="envios-table__empty">Ingresa un ID de cliente para ver sus destinatarios.</div>
                ) : buscando ? (
                <div className="envios-table__loading">Cargando...</div>
                ) : destinatarios.length === 0 ? (
                <div className="envios-table__empty">Este cliente no tiene destinatarios registrados.</div>
                ) : (
                destinatarios.map(dest => (
                    <div key={dest.id} className="dest-table__row envios-table__row">
                    <span className="envios-table__folio">#{dest.id}</span>
                    <span style={{ fontSize: 13, color: "#374151", fontWeight: 500 }}>{dest.nombre}</span>
                    <span className="envios-table__destinatario">{dest.telefono}</span>
                    <span className="envios-table__contenido">{dest.direccion || "—"}</span>
                    <span className="envios-table__destinatario">#{dest.cliente?.id || clienteId}</span>
                    <div className="envios-table__actions">
                        <button className="btn-icon" onClick={() => setModalDetalle(dest)} title="Ver detalle">👁</button>
                        <button className="btn-icon" onClick={() => abrirEditar(dest)} title="Editar">✏️</button>
                    </div>
                    </div>
                ))
                )}
            </div>

            <div className="envios-count">
                {buscado && `${destinatarios.length} destinatario${destinatarios.length !== 1 ? "s" : ""}`}
            </div>
            </div>

            {/* Modal Crear */}
            {modalCrear && (
            <Modal title="Nuevo destinatario" onClose={() => { setModalCrear(false); setFormError(""); }}>
                <Field label="ID Cliente">
                <input className="field__input" type="number" placeholder="1"
                    value={formCrear.clienteId} onChange={e => setFormCrear(p => ({ ...p, clienteId: e.target.value }))} />
                </Field>
                <Field label="Nombre">
                <input className="field__input" placeholder="Juan Pérez"
                    value={formCrear.nombre} onChange={e => setFormCrear(p => ({ ...p, nombre: e.target.value }))} />
                </Field>
                <Field label="Teléfono">
                <input className="field__input" placeholder="2461234567"
                    value={formCrear.telefono} onChange={e => setFormCrear(p => ({ ...p, telefono: e.target.value }))} />
                </Field>
                <Field label="Dirección">
                <input className="field__input" placeholder="Av. Hidalgo 45, CDMX"
                    value={formCrear.direccion} onChange={e => setFormCrear(p => ({ ...p, direccion: e.target.value }))} />
                </Field>
                {formError && <div className="form-error">{formError}</div>}
                <div className="modal__footer">
                <button className="btn-secondary" onClick={() => { setModalCrear(false); setFormError(""); }}>Cancelar</button>
                <button className="btn-primary" onClick={crearDestinatario} disabled={creando}>
                    {creando ? "Creando..." : "Crear destinatario"}
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
            <Modal title="Detalle del destinatario" onClose={() => setModalDetalle(null)}>
                <div className="modal__detail-grid">
                {[
                    ["ID",        `#${modalDetalle.id}`],
                    ["Cliente",   `#${modalDetalle.cliente?.id || clienteId}`],
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