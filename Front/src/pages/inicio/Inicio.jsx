import Cabeza from "../../Componentes/Cabeza";
import Navbar from "../../Componentes/Navbar";
import Envios from "../envios/Envios";
import Destinatarios from "../Destinatarios/Destinatarios";
import Clientes from "../Clientes/Clientes";

function  Inicio (){
    return (
        <>
     {/*   <Cabeza />
        <Navbar />*/}
        <div className="centralizacion">
            <Clientes />
        </div>
        </>
    );
}

export default Inicio;