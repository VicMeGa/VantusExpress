import Inicio from './pages/inicio/Inicio'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Envios from './pages/envios/Envios';
import Clientes from './pages/Clientes/Clientes';
import Destinatarios from './pages/Destinatarios/Destinatarios';
import BitacoraLlamadas from './pages/Bitacora/Bitacorallamadas';
import './styles/App.css';
import Auth from './pages/Login/Auth';

function App() {

  return (
    <>
      <BrowserRouter>
      <Routes>
        <Route path="auth" element={<Auth />} />
        <Route path="/" element={<Inicio />}>
          <Route index element={<Navigate to="envios" replace />} />
          <Route path="envios"        element={<Envios />} />
          <Route path="clientes"      element={<Clientes />} />
          <Route path="destinatarios" element={<Destinatarios />} />
          <Route path="bitacora"      element={<BitacoraLlamadas />} />
        </Route>
      </Routes>
    </BrowserRouter>
    </>
  )
}

export default App
