import { useNavigate } from "react-router-dom";

export default function Header({setIsDisplayingSettings}) {

    return (
        <header className="App-header">
            <div>
                <h2>Music Player♫</h2>
            </div>

            <div>
                <img src="gear-29.png" onClick={()=>setIsDisplayingSettings(true)} style={{width:"30px", height: "30px"}}></img>
            </div>
            
        </header>
    );
}