import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Auth.css";

const API = import.meta.env.VITE_BACKEND_URL;

function Login({ onSwitch }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleLogin() {
    setError("");
    if (!form.email || !form.password) {
      setError("Email y contraseña son obligatorios."); return;
    }
    setLoading(true);
    try {
      const res = await fetch(`${API}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.message);
      //if (!res.ok) {
      //  const data = await res.json();
      //  throw new Error(data.error || "Credenciales incorrectas");
      //}
      //const usuario = await res.json();
      sessionStorage.setItem("usuario", JSON.stringify(json.data));
      navigate("/envios");
    } catch (e) { setError(e.message); }
    finally { setLoading(false); }
  }

  return (
    <div className="auth-box">
      <div className=""><img className="logo" src={"/vantus.png"}  alt="Logo de Vantus" /></div>
      <h1 className="auth-title">VantusExpress</h1>
      <p className="auth-sub">Inicia sesión para continuar</p>

      <div className="auth-form">
        <div className="field">
          <label className="field__label">Email</label>
          <input
            className="field__input"
            type="email"
            placeholder="correo@ejemplo.com"
            value={form.email}
            onChange={e => setForm(p => ({ ...p, email: e.target.value }))}
            onKeyDown={e => e.key === "Enter" && handleLogin()}
          />
        </div>
        <div className="field">
          <label className="field__label">Contraseña</label>
          <input
            className="field__input"
            type="password"
            placeholder="••••••••"
            value={form.password}
            onChange={e => setForm(p => ({ ...p, password: e.target.value }))}
            onKeyDown={e => e.key === "Enter" && handleLogin()}
          />
        </div>

        {error && <div className="form-error">{error}</div>}

        <button className="btn-primary auth-btn" onClick={handleLogin} disabled={loading}>
          {loading ? "Entrando..." : "Iniciar sesión"}
        </button>

        <p className="auth-switch">
          ¿No tienes cuenta?{" "}
          <button className="auth-link" onClick={onSwitch}>Regístrate</button>
        </p>
      </div>
    </div>
  );
}

export default Login;