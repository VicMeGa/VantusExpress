import { Package, Users, MapPin, PhoneCall, Radio} from 'lucide-react';
import { useNavigate } from 'react-router-dom';

function Sidebar({ page, setPage }) {
     const navigate = useNavigate();
  return (
    <nav className="sidebar">
      <ul className="sidebar-ul">
        <li>
          <button
            className={`sidebar-item ${page === 'envios' ? 'sidebar-item--active' : ''}`}
            onClick={() => navigate('/envios')}
          >
            <Package className="sidebar-icon" size={20} />
          </button>
        </li>
        <li>
          <button
            className={`sidebar-item ${page === 'clientes' ? 'sidebar-item--active' : ''}`}
            onClick={() => navigate('/clientes')}
          >
            <Users className="sidebar-icon" size={20} />
          </button>
        </li>
        <li>
          <button
            className={`sidebar-item ${page === 'destinatarios' ? 'sidebar-item--active' : ''}`}
            onClick={() => navigate('/destinatarios')}
          >
            <MapPin className="sidebar-icon" size={20} />
          </button>
        </li>
        <li>
          <button
            className={`sidebar-item ${page === 'bitacora' ? 'sidebar-item--active' : ''}`}
            onClick={() => navigate('/bitacora')}
          >
            <PhoneCall className="sidebar-icon" size={20} />
          </button>
        </li>
        <li>
          <button
            className={`sidebar-item ${location.pathname === '/sesiones' ? 'sidebar-item--active' : ''}`}
            onClick={() => navigate('/sesiones')}
          >
            <Radio className="sidebar-icon" size={20} />
          </button>
        </li>
      </ul>
    </nav>
  );
}

export default Sidebar;