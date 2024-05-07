import { useState } from "react"

export default function SettingsModal({isDisplayingSettings, setIsDisplayingSettings}) {
    const [songFolderText, setSongFolderText] = useState("");

    function saveSettings() {

    }

    function handleSongFolderChange(e) {
        setSongFolderText(e.target.value);
    }


    var modalDisplayStyle = {
        display: isDisplayingSettings ? "block" : "none"
    }
    return (
        <div className="settings-modal-background" style={modalDisplayStyle}>
            <div className="settings-modal">
                <h3>Settings</h3>
                <hr />
                <p>Song folder:</p>
                <input type="text" value={songFolderText} onChange={(e)=>handleSongFolderChange(e)}></input>
                <input type="button" className="btn" onClick={saveSettings}>Save</input>
            </div>
        </div>
    )
}