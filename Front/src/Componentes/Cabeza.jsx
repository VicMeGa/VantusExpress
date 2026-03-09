import { useNavigate } from "react-router-dom";
//import vantus from "vantus.png"


function Cabeza (){
    //const navigate = useNavigate();
    //const {session} = useSession();
    return(
        <>
        <header className='arriba'>
            <img className="logo" src={"/vantus.png"}  alt="Logo de Vantus"  
            //onClick={() => navigate("/next")
            //    
            //}
            //    style={{ cursor: "pointer" }}
        />
        </header>
        <br/>
        <br />
        </>
    );
}

export default Cabeza;