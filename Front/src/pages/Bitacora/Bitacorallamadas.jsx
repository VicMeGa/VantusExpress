import { useState } from "react";

const API = "http://localhost:8080";

const resultadoConfig = {
  completado:  { color: "#10b981", bg: "#ecfdf5", border: "#10b98122" },
  fallido:     { color: "#ef4444", bg: "#fef2f2", border: "#ef444422" },
  abandonado:  { color: "#f59e0b", bg: "#fffbeb", border: "#f59e0b22" },
  transferido: { color: "#6366f1", bg: "#eef2ff", border: "#6366f122" },
};

function Badge({ resultado }) {
  const cfg = resultadoConfig[resultado?.toLowerCase()] || { color: "#6b7280", bg: "#f3f4f6", border: "#6b728022" };
  return (
    <span style={{
      background: cfg.bg, color: cfg.color,
      border: `1px solid ${cfg.border}`,
      borderRadius: 6, padding: "2px 10px",
      fontSize: 12, fontWeight: 600,
      letterSpacing: "0.02em", textTransform: "capitalize",
      display: "inline-block",
    }}>
      {resultado || "—"}
    </span>
  );
}

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

function formatDuracion(seg) {
  if (!seg && seg !== 0) return "—";
  const m = Math.floor(seg / 60);
  const s = seg % 60;
  return m > 0 ? `${m}m ${s}s` : `${s}s`;
}

function formatFecha(f) {
  if (!f) return "—";
  return new Date(f).toLocaleString("es-MX", {
    day: "2-digit", month: "short", year: "numeric",
    hour: "2-digit", minute: "2-digit",
  });
}

export default function BitacoraLlamadas() {
  const [registros, setRegistros] = useState([]);
  const [error, setError] = useState(null);

  const [modalRegistrar, setModalRegistrar] = useState(false);
  const [modalDetalle, setModalDetalle] = useState(null);

  const [form, setForm] = useState({ callSid: "", telefono: "", duracion: "", resultado: "completado" });
  const [registrando, setRegistrando] = useState(false);
  const [formError, setFormError] = useState("");

  // stats calculadas
  const totalDuracion = registros.reduce((acc, r) => acc + (r.duracion || 0), 0);
  const porResultado = Object.keys(resultadoConfig).reduce((acc, k) => {
    acc[k] = registros.filter(r => r.resultado?.toLowerCase() === k).length;
    return acc;
  }, {});

  async function registrarLlamada() {
    setFormError("");
    if (!form.callSid || !form.telefono || !form.resultado) {
      setFormError("CallSid, teléfono y resultado son obligatorios."); return;
    }
    setRegistrando(true);
    try {
      const res = await fetch(`${API}/bitacora`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...form, duracion: form.duracion ? Number(form.duracion) : null }),
      });
      if (!res.ok) throw new Error("Error al registrar llamada");
      const nueva = await res.json();
      setRegistros(prev => [nueva, ...prev]);
      setModalRegistrar(false);
      setForm({ callSid: "", telefono: "", duracion: "", resultado: "completado" });
    } catch (e) { setFormError(e.message); }
    finally { setRegistrando(false); }
  }

  return (
    <div className="envios-page">

      {/* Header */}
      <div className="envios-header">
        <div className="envios-header__inner">
          <div className="envios-header__brand">
            <div className=""><img className="logo" src={"/vantus.png"}  alt="Logo de Vantus" /></div>
            <span className="envios-header__name">VantusExpress</span>
            <span className="envios-header__sep">/</span>
            <span className="envios-header__section">Bitácora de llamadas</span>
          </div>
          <button className="btn-primary" onClick={() => setModalRegistrar(true)}>
            <span>+</span> Registrar llamada
          </button>
        </div>
      </div>

      <div className="envios-content">

        {/* Stats */}
        <div className="envios-stats">
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Total registros</div>
            <div className="envios-stat-card__value" style={{ color: "#111" }}>{registros.length}</div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Completadas</div>
            <div className="envios-stat-card__value" style={{ color: "#10b981" }}>{porResultado.completado}</div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Fallidas</div>
            <div className="envios-stat-card__value" style={{ color: "#ef4444" }}>{porResultado.fallido}</div>
          </div>
          <div className="envios-stat-card">
            <div className="envios-stat-card__label">Duración total</div>
            <div className="envios-stat-card__value" style={{ color: "#6366f1", fontSize: 22 }}>{formatDuracion(totalDuracion)}</div>
          </div>
        </div>

        {/* Tabla */}
        <div className="envios-table">
          <div className="bitacora-table__head">
            {["CallSid", "Teléfono", "Duración", "Resultado", "Fecha", ""].map((h, i) => (
              <span key={i} className="envios-table__head-cell">{h}</span>
            ))}
          </div>

          {error ? (
            <div className="envios-table__error">{error}</div>
          ) : registros.length === 0 ? (
            <div className="envios-table__empty">No hay llamadas registradas en esta sesión.</div>
          ) : (
            registros.map((r, i) => (
              <div key={r.id ?? i} className="bitacora-table__row envios-table__row">
                <span className="envios-table__folio">{r.callSid}</span>
                <span style={{ fontSize: 13, color: "#374151", fontFamily: "'DM Mono', monospace" }}>{r.telefono}</span>
                <span style={{ fontSize: 13, color: "#374151" }}>{formatDuracion(r.duracion)}</span>
                <Badge resultado={r.resultado} />
                <span className="envios-table__destinatario">{formatFecha(r.fecha)}</span>
                <div className="envios-table__actions">
                  <button className="btn-icon" onClick={() => setModalDetalle(r)} title="Ver detalle">👁</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="envios-count">
          {registros.length > 0 && `${registros.length} registro${registros.length !== 1 ? "s" : ""}`}
        </div>
      </div>

      {/* Modal Registrar */}
      {modalRegistrar && (
        <Modal title="Registrar llamada" onClose={() => { setModalRegistrar(false); setFormError(""); }}>
          <Field label="CallSid">
            <input className="field__input" placeholder="CA1234567890abcdef"
              value={form.callSid} onChange={e => setForm(p => ({ ...p, callSid: e.target.value }))} />
          </Field>
          <Field label="Teléfono">
            <input className="field__input" placeholder="2461234567"
              value={form.telefono} onChange={e => setForm(p => ({ ...p, telefono: e.target.value }))} />
          </Field>
          <Field label="Duración (segundos)">
            <input className="field__input" type="number" placeholder="120"
              value={form.duracion} onChange={e => setForm(p => ({ ...p, duracion: e.target.value }))} />
          </Field>
          <Field label="Resultado">
            <select className="field__select" value={form.resultado}
              onChange={e => setForm(p => ({ ...p, resultado: e.target.value }))}>
              <option value="completado">Completado</option>
              <option value="fallido">Fallido</option>
              <option value="abandonado">Abandonado</option>
              <option value="transferido">Transferido</option>
            </select>
          </Field>
          {formError && <div className="form-error">{formError}</div>}
          <div className="modal__footer">
            <button className="btn-secondary" onClick={() => { setModalRegistrar(false); setFormError(""); }}>Cancelar</button>
            <button className="btn-primary" onClick={registrarLlamada} disabled={registrando}>
              {registrando ? "Registrando..." : "Registrar"}
            </button>
          </div>
        </Modal>
      )}

      {/* Modal Detalle */}
      {modalDetalle && (
        <Modal title="Detalle de llamada" onClose={() => setModalDetalle(null)}>
          <div className="modal__detail-grid">
            {[
              ["CallSid",   modalDetalle.callSid],
              ["Resultado", null],
              ["Teléfono",  modalDetalle.telefono],
              ["Duración",  formatDuracion(modalDetalle.duracion)],
              ["Fecha",     formatFecha(modalDetalle.fecha)],
            ].map(([label, val]) => (
              <div key={label}>
                <div className="modal__detail-label">{label}</div>
                {label === "Resultado"
                  ? <Badge resultado={modalDetalle.resultado} />
                  : <span className="modal__detail-value">{val}</span>
                }
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