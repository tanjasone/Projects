import { useNavigate } from "react-router-dom";

export default function Header() {

    const navigate = useNavigate();

    var navToSettings = () => {
        navigate("/settings");
    }

    return (
        <header className="App-header">
            <div>
                <h2>Music Player</h2>
            </div>

            <div>
                <img src="gear-29.png" onClick={navToSettings} style={{width:"30px", height: "30px"}}></img>
            </div>
            
        </header>
    );
}