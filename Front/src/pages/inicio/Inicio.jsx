import Envios from "../envios/Envios";

import Sidebar from "../../Componentes/Sidebar";
import { Outlet, Navigate } from 'react-router-dom';

function  Inicio (){
    return (
        <>
     {/*   <Cabeza />
        <Navbar />*/}
        <div className="centralizacion">
        <Sidebar />
            <Outlet />
        </div>
        </>
    );
}

export default Inicio;