import { useState, useEffect } from "react";
import "../../styles/App.css";

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

export default function Sesiones() {
  const [sesiones, setSesiones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [busqueda, setBusqueda] = useState("");
  const [buscando, setBuscando] = useState(false);

  const [modalCrear, setModalCrear] = useState(false);
  const [modalEditar, setModalEditar] = useState(null);
  const [modalDetalle, setModalDetalle] = useState(null);

  const [formCrear, setFormCrear] = useState({ callSid: "", pasoActual: "", datos: "" });
  const [creando, setCreando] = useState(false);
  const [formError, setFormError] = useState("");

  const [formEditar, setFormEditar] = useState({ pasoActual: "", datos: "" });
  const [editando, setEditando] = useState(false);

  useEffect(() => { cargarTodos(); }, []);

  async function cargarTodos() {
    setLoading(true); setError(null);
    try {
      const res = await fetch(`${API}/sesion`);
      const json = await res.json();
      //if (!res.ok) throw new Error("Error al cargar sesiones");
      if (!json.success) throw new Error(json.message);
      setSesiones(json.data);
    } catch (e) { setError(e.message); }
    finally { setLoading(false); }
  }

  async function buscarPorCallSid() {
    if (!busqueda.trim()) return cargarTodos();
    setBuscando(true); setError(null);
    try {
      const res = await fetch(`${API}/sesion/${busqueda.trim()}`);
      const json = await res.json();
      //if (res.status === 404) { setSesiones([]); return; }
      //if (!res.ok) throw new Error("Error en búsqueda");
      if (!json.success) {setSesiones([]); return; }
      //const data = await res.json();
      setSesiones(Array.isArray(json.data) ? json.data : [json.data]);
    } catch (e) { setError(e.message); }
    finally { setBuscando(false); }
  }

  async function crearSesion() {
    setFormError("");
    if (!formCrear.callSid || !formCrear.pasoActual) {
      setFormError("CallSid y paso actual son obligatorios."); return;
    }
    let datosObj = {};
    if (formCrear.datos.trim()) {
      try { JSON.parse(formCrear.datos); datosObj = formCrear.datos;}
      catch { setFormError("El campo Datos debe ser JSON válido."); return; }
    }
    setCreando(true);
    try {
      const res = await fetch(`${API}/sesion`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ callSid: formCrear.callSid, pasoActual: formCrear.pasoActual, datos: datosObj }),
      });
      const json= await res.json();
      //if (!res.ok) throw new Error("Error al crear sesión");
      if (!json.success) throw new Error(json.message);
      setModalCrear(false);
      setFormCrear({ callSid: "", pasoActual: "", datos: "" });
      cargarTodos();
    } catch (e) { setFormError(e.message); }
    finally { setCreando(false); }
  }

  async function guardarEdicion() {
    let datosObj = "{}";
    if (formEditar.datos.trim()) {
      try { JSON.parse(formEditar.datos); datosObj = formEditar.datos; }
      catch { alert("El campo Datos debe ser JSON válido."); return; }
    }
    setEditando(true);
    try {
      const res = await fetch(`${API}/sesion/${modalEditar.callSid}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ pasoActual: formEditar.pasoActual, datos: datosObj }),
      });
      const json = await res.json();
      //if (!res.ok) throw new Error("Error al actualizar");
      if (!json.success) throw new Error (json.message);
      setModalEditar(null);
      cargarTodos();
    } catch (e) { alert(e.message); }
    finally { setEditando(false); }
  }

  async function eliminar(callSid) {
    if (!confirm(`¿Eliminar sesión ${callSid}?`)) return;
    try {
      const res = await fetch(`${API}/sesion/${callSid}`, { method: "DELETE" });
      const json = await res.json();
      //if (!res.ok) throw new Error("Error al eliminar");
      if (!json.success) throw new Error(json.messafe);
      cargarTodos();
    } catch (e) { alert(e.message); }
  }

  function abrirEditar(sesion) {
    setFormEditar({
      pasoActual: sesion.pasoActual || "",
      datos: sesion.datos ? JSON.stringify(sesion.datos, null, 2) : "",
    });
    setModalEditar(sesion);
  }

  const limpiarBusqueda = () => { setBusqueda(""); cargarTodos(); };
  const formatFecha = f => f ? new Date(f).toLocaleString("es-MX") : "—";

  return (
    <div className="envios-page">

      <div className="envios-header">
        <div className="envios-header__inner">
          <div className="envios-header__brand">
            <div className="envios-header__logo">📦</div>
            <span className="envios-header__name">VantusExpress</span>
            <span className="envios-header__sep">/</span>
            <span className="envios-header__section">Sesiones</span>
          </div>
          <button className="btn-primary" onClick={() => setModalCrear(true)}>
            <span>+</span> Nueva sesión
          </button>
        </div>
      </div>

      <div className="envios-content">

        <div className="envios-stats" style={{ gridTemplateColumns: "repeat(3, 1fr)" }}>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Total sesiones</div>
            <div className="envios-stat-card__value" style={{ color: "#6366f1" }}>{sesiones.length}</div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Última actualización</div>
            <div className="envios-stat-card__value" style={{ color: "#111", fontSize: 14, fontWeight: 500, marginTop: 4 }}>
              {sesiones[0]?.updatedAt ? formatFecha(sesiones[0].updatedAt) : "—"}
            </div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Búsqueda activa</div>
            <div className="envios-stat-card__value" style={{ color: busqueda ? "#10b981" : "#9ca3af", fontSize: 18 }}>
              {busqueda || "Todas"}
            </div>
          </div>
        </div>

        <div className="envios-search">
          <input
            className="field__input"
            value={busqueda}
            onChange={e => setBusqueda(e.target.value)}
            onKeyDown={e => e.key === "Enter" && buscarPorCallSid()}
            placeholder="Buscar por CallSid  (ej. CA1234567890)"
          />
          {busqueda && (
            <button className="btn-secondary" onClick={limpiarBusqueda} style={{ padding: "9px 14px" }}>✕</button>
          )}
          <button className="btn-primary" onClick={buscarPorCallSid} disabled={buscando}>
            {buscando ? "..." : "Buscar"}
          </button>
        </div>

        <div className="envios-table">
          <div className="sesion-table__head">
            {["CallSid", "Paso actual", "Datos", "Actualizado", ""].map((h, i) => (
              <span key={i} className="envios-table__head-cell">{h}</span>
            ))}
          </div>

          {loading ? (
            <div className="envios-table__loading">Cargando...</div>
          ) : error ? (
            <div className="envios-table__error">{error}</div>
          ) : sesiones.length === 0 ? (
            <div className="envios-table__empty">No hay sesiones registradas.</div>
          ) : (
            sesiones.map(sesion => (
              <div key={sesion.id} className="sesion-table__row envios-table__row">
                <span className="envios-table__folio" style={{ overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>
                    {sesion.callSid}
                </span>
                <span style={{ fontSize: 13, color: "#374151" }}>{sesion.pasoActual || "—"}</span>
                <span className="envios-table__contenido" style={{ fontFamily: "'DM Mono', monospace", fontSize: 11, color: "#9ca3af" }}>
                  {sesion.datos ? JSON.stringify(sesion.datos) : "—"}
                </span>
                <span className="envios-table__destinatario">{formatFecha(sesion.updatedAt)}</span>
                <div className="envios-table__actions">
                  <button className="btn-icon" onClick={() => setModalDetalle(sesion)} title="Ver detalle">👁</button>
                  <button className="btn-icon" onClick={() => abrirEditar(sesion)} title="Editar">✏️</button>
                  <button className="btn-icon" onClick={() => eliminar(sesion.callSid)} title="Eliminar" style={{ color: "#ef4444" }}>🗑</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="envios-count">
          {!loading && `${sesiones.length} sesión${sesiones.length !== 1 ? "es" : ""}`}
        </div>
      </div>

      {/* Modal Crear */}
      {modalCrear && (
        <Modal title="Nueva sesión" onClose={() => { setModalCrear(false); setFormError(""); }}>
          <Field label="CallSid">
            <input className="field__input" placeholder="CA1234567890abcdef"
              value={formCrear.callSid} onChange={e => setFormCrear(p => ({ ...p, callSid: e.target.value }))} />
          </Field>
          <Field label="Paso actual">
            <input className="field__input" placeholder="bienvenida"
              value={formCrear.pasoActual} onChange={e => setFormCrear(p => ({ ...p, pasoActual: e.target.value }))} />
          </Field>
          <Field label="Datos (JSON opcional)">
            <textarea className="field__input" placeholder='{"intentos": 0}' rows={3}
              style={{ resize: "vertical", fontFamily: "'DM Mono', monospace", fontSize: 12 }}
              value={formCrear.datos} onChange={e => setFormCrear(p => ({ ...p, datos: e.target.value }))} />
          </Field>
          {formError && <div className="form-error">{formError}</div>}
          <div className="modal__footer">
            <button className="btn-secondary" onClick={() => { setModalCrear(false); setFormError(""); }}>Cancelar</button>
            <button className="btn-primary" onClick={crearSesion} disabled={creando}>
              {creando ? "Creando..." : "Crear sesión"}
            </button>
          </div>
        </Modal>
      )}

      {/* Modal Editar */}
      {modalEditar && (
        <Modal title={`Editar · ${modalEditar.callSid}`} onClose={() => setModalEditar(null)}>
          <Field label="Paso actual">
            <input className="field__input" value={formEditar.pasoActual}
              onChange={e => setFormEditar(p => ({ ...p, pasoActual: e.target.value }))} />
          </Field>
          <Field label="Datos (JSON)">
            <textarea className="field__input" rows={4}
              style={{ resize: "vertical", fontFamily: "'DM Mono', monospace", fontSize: 12 }}
              value={formEditar.datos} onChange={e => setFormEditar(p => ({ ...p, datos: e.target.value }))} />
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
        <Modal title="Detalle de sesión" onClose={() => setModalDetalle(null)}>
          <div className="modal__detail-grid">
            {[
              ["ID",          `#${modalDetalle.id}`],
              ["Actualizado", formatFecha(modalDetalle.updatedAt)],
              ["CallSid",     modalDetalle.callSid],
              ["Paso actual", modalDetalle.pasoActual || "—"],
            ].map(([label, val]) => (
              <div key={label}>
                <div className="modal__detail-label">{label}</div>
                <span className="modal__detail-value">{val}</span>
              </div>
            ))}
          </div>
          {modalDetalle.datos && (
            <div style={{ marginTop: 16 }}>
              <div className="modal__detail-label">Datos</div>
              <pre style={{
                background: "#f8f8f7", border: "1px solid #efefef",
                borderRadius: 8, padding: 12, fontSize: 12,
                fontFamily: "'DM Mono', monospace", overflowX: "auto",
                marginTop: 4, color: "#374151",
              }}>
                {JSON.stringify(modalDetalle.datos, null, 2)}
              </pre>
            </div>
          )}
          <div className="modal__footer">
            <button className="btn-secondary" onClick={() => setModalDetalle(null)}>Cerrar</button>
          </div>
        </Modal>
      )}
    </div>
  );
}