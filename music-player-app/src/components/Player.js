import { useState, useRef, useEffect } from "react";
import PlayLists from "./Playlists";
import SongList from "./SongList";
import { baseUrl } from "../environment";
import SettingsModal from "./SettingsModal";

export default function Player({isDisplayingSettings, setIsDisplayingSettings}) {
    var [songsObj, setSongsObj] = useState(null);
    var [currentSong, setCurrentSong] = useState({});
    var [playlistsList, setPlaylistsList] = useState([]);
    var [activePlaylist, setActivePlaylist] = useState({
        id: 1,
        name: "All songs",
        songIds: []
    });
    var [settings, setSettings] = useState({});
    var audioRef = useRef(null);

    useEffect(() => {
        async function fetchData() {
            console.log("Fetching data...");
            var res = await fetch(baseUrl + "/playlists", {
                mode: "cors"
            });
            var data = await res.json();
            console.log("playlists", data);
            setPlaylistsList(data);
            setActivePlaylist(data[0]);

            res = await fetch(baseUrl + "/songs", {
                mode: "cors"
            });
            data = await res.json();
            console.log("songs", data);
            setSongsObj(data);

            res = await fetch(baseUrl + "/settings", {
                mode: "cors"
            });
            data = await res.json();
            console.log("settings", data);
            setSettings(data);
        }
        fetchData();
    }, [])

    useEffect(() => {
        if(currentSong != null) {
            console.log("loading song: " + currentSong.filePath)
            audioRef.current.load();
        }
    }, [currentSong, audioRef])

    return (
        <div style={{display: "flex", flexDirection: "column"}}>
            <SettingsModal isDisplayingSettings={isDisplayingSettings} setIsDisplayingSettings={setIsDisplayingSettings}
                settings={settings} setSettings={setSettings} setSongsObj={setSongsObj} setPlaylistsList={setPlaylistsList} />
            <div className="player-container">
                <PlayLists activePlaylist={activePlaylist} setActivePlaylist={setActivePlaylist} 
                    playlistsList={playlistsList} setPlaylistsList={setPlaylistsList} />
                <SongList playlistsList={playlistsList} currentSong={currentSong} setCurrentSong={setCurrentSong} 
                    songsList={activePlaylist && songsObj ?
                        activePlaylist.songIds.map(id => songsObj[id]) : null
                        } activePlaylist={activePlaylist} />
            </div>
            
            <div className="audio-container">
                <audio controls ref={audioRef}>
                    {currentSong.filePath ? 
                        (<source src={currentSong.relativeFilePath} type="audio/mp3" />)
                        : null}
                </audio>
            </div>
        </div>
    )
}