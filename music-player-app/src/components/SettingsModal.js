import { useState } from "react"
import { baseUrl } from "../environment";

export default function SettingsModal({isDisplayingSettings, setIsDisplayingSettings, settings, setSettings, setSongsObj, setPlaylistsList}) {
    const [songsFolderText, setSongsFolderText] = useState(settings.songsFolderPath);
    const [status, setStatus] = useState(null);
    const [activeStatusTimeout, setActiveStatusTimeout] = useState(null);

    function saveSettings() {
        console.log("Saving settings");
        displayStatus("Saving...");
        var newSettings = {
            songsFolderPath: songsFolderText
        };
        fetch(baseUrl + "/settings", {
            method: "POST",
            body: JSON.stringify(newSettings)
        }).then(async res => {
            if(res.status === 210) { // performed sync, resetting song list required
                var data = await res.json();
                console.log(data);
                setSongsObj(data.newSongs);
                setPlaylistsList(data.playlists)
            }
            setSettings(newSettings);
            displayStatus("Settings saved successfully", "SUCCESS");
        }).catch(rej => {
            console.error("Error while saving settings", rej);
            displayStatus("An error has occurred", "FAILURE");
        })
    }

    function displayStatus(message, type) {
        setStatus({message, type})
        if(activeStatusTimeout) clearTimeout(activeStatusTimeout);
        setActiveStatusTimeout(setTimeout(() => {
            setStatus(null);
            setActiveStatusTimeout(null)
        }, 5000))
    }

    function handleSongsFolderChange(e) {
        setSongsFolderText(e.target.value);
    }


    var modalDisplayStyle = {
        display: isDisplayingSettings ? "flex" : "none"
    }
    return (
        <div className="settings-modal-background" style={modalDisplayStyle}>
            <div className="settings-modal">
                <button className="btn" style={{float:"right"}} onClick={()=>setIsDisplayingSettings(false)}>X</button>
                <h3>Settings</h3>
                <hr />
                <p>Song folder:</p>
                <input type="text" value={songsFolderText} onChange={(e)=>handleSongsFolderChange(e)}></input>
                <br /><hr />
                <button type="button" className="btn" style={{float: "right"}} onClick={saveSettings}>Save</button>
                {status ? (<p style={{
                    display:"inline-block",
                    color: status.type === "FAILURE" ? "red" : (status.type === "SUCCESS" ? "green" : "")
                }}>{status.message}</p>) : null}
            </div>
        </div>
    )
}