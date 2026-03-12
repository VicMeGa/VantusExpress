import { useState } from "react";
import Login from "./Login";
import Registro from "./Registro";
import "./Auth.css";
 
function Auth() {
  const [modo, setModo] = useState("login");
 
  return (
    <div className="auth-page">
      {modo === "login"
        ? <Login onSwitch={() => setModo("registro")} />
        : <Registro onSwitch={() => setModo("login")} />
      }
    </div>
  );
}
 
export default Auth;
 