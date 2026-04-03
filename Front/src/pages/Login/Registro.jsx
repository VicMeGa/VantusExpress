import { useState } from "react";
import "./Auth.css";

const API = import.meta.env.VITE_BACKEND_URL;

function Registro({ onSwitch }) {
  const [form, setForm] = useState({ nombre: "", email: "", password: "", confirmar: "" });
  const [error, setError] = useState("");
  const [exito, setExito] = useState(false);
  const [loading, setLoading] = useState(false);

  async function handleRegistro() {
    setError("");
    if (!form.nombre || !form.email || !form.password) {
      setError("Todos los campos son obligatorios."); return;
    }
    if (form.password !== form.confirmar) {
      setError("Las contraseñas no coinciden."); return;
    }
    if (form.password.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres."); return;
    }
    setLoading(true);
    try {
      const res = await fetch(`${API}/auth/registro`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre: form.nombre, email: form.email, password: form.password }),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.message);
      //if (!res.ok) {
      //  const data = await res.json();
      //  throw new Error(data.error || "Error al registrar");
      //}
      setExito(true);
    } catch (e) { setError(e.message); }
    finally { setLoading(false); }
  }

  if (exito) return (
    <div className="auth-box">
      <div className="auth-logo">✅</div>
      <h1 className="auth-title">¡Cuenta creada!</h1>
      <p className="auth-sub">Ya puedes iniciar sesión.</p>
      <button className="btn-primary auth-btn" onClick={onSwitch}>Ir al login</button>
    </div>
  );

  return (
    <div className="auth-box">
      <div className=""><img className="logo" src={"/vantus.png"}  alt="Logo de Vantus" /></div>
      <h1 className="auth-title">Crear cuenta</h1>
      <p className="auth-sub">Completa los datos para registrarte</p>

      <div className="auth-form">
        <div className="field">
          <label className="field__label">Nombre</label>
          <input className="field__input" placeholder="Tu nombre"
            value={form.nombre} onChange={e => setForm(p => ({ ...p, nombre: e.target.value }))} />
        </div>
        <div className="field">
          <label className="field__label">Email</label>
          <input className="field__input" type="email" placeholder="correo@ejemplo.com"
            value={form.email} onChange={e => setForm(p => ({ ...p, email: e.target.value }))} />
        </div>
        <div className="field">
          <label className="field__label">Contraseña</label>
          <input className="field__input" type="password" placeholder="Mínimo 6 caracteres"
            value={form.password} onChange={e => setForm(p => ({ ...p, password: e.target.value }))} />
        </div>
        <div className="field">
          <label className="field__label">Confirmar contraseña</label>
          <input className="field__input" type="password" placeholder="Repite la contraseña"
            value={form.confirmar} onChange={e => setForm(p => ({ ...p, confirmar: e.target.value }))}
            onKeyDown={e => e.key === "Enter" && handleRegistro()} />
        </div>

        {error && <div className="form-error">{error}</div>}

        <button className="btn-primary auth-btn" onClick={handleRegistro} disabled={loading}>
          {loading ? "Registrando..." : "Crear cuenta"}
        </button>

        <p className="auth-switch">
          ¿Ya tienes cuenta?{" "}
          <button className="auth-link" onClick={onSwitch}>Inicia sesión</button>
        </p>
      </div>
    </div>
  );
}

export default Registro;