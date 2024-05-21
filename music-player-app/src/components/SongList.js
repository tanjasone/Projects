import "../App.css"
import { act, useState } from "react"
import { baseUrl } from "../environment";


export default function SongList({playlistsList, setPlaylistsList, currentSong, setCurrentSong, songsList, activePlaylist}) {
    var [selectedSongs, setSelectedSongs] = useState(new Set());
    var [isSelectingSongs, setIsSelectingSongs] = useState(false);
    var [showDropdown, setShowDropdown] = useState(false);

    function handleSelectSongItem(s) {
        if(selectedSongs.has(s.songId))
            selectedSongs.delete(s.songId);
        else 
            selectedSongs.add(s.songId);

        setSelectedSongs(new Set(selectedSongs));
    }

    function handleAddToPlaylist(p) {
        console.log("Adding songs to playlist: ", p);

        var body = {
            action: "ADD_SONGS",
            playlistId: p.id,
            songIds: Array.from(selectedSongs)
        }

        fetch(baseUrl + "/playlists", {
            method: "POST",
            mode: "cors",
            body: JSON.stringify(body)
        }).then(async res => {
            var data = await res.json();
            console.log("addToPlaylist data", data);
            setPlaylistsList(data);

        }).catch(rej => {
            console.log(rej);
        })
    }

    return (
        <div className="song-list-container">
            <h3>{activePlaylist.name}</h3>
            <button className="btn" onClick={()=>{
                if(isSelectingSongs) setSelectedSongs(new Set());
                setIsSelectingSongs(!isSelectingSongs)
            }}>
                {!isSelectingSongs ? "Select": "Cancel Select"}
            </button>
            {isSelectingSongs ? 
                <button className="btn" onClick={()=>setShowDropdown(!showDropdown)}>
                    Options
                    <div className="options-dropdown" style={{display: showDropdown ? "block" : "none"}}>
                        <p>Add to playlist</p>
                        <ul className="playlist-options-list">
                            {playlistsList.map(p => {
                                if(p.name === "All songs") return null;
                                return (
                                    <li onClick={()=>handleAddToPlaylist(p)} 
                                        className="options-list-item"
                                    >{p.name}</li>)
                            })}
                        </ul>
                    </div>
                </button>
            : null }
            <hr />
            {songsList === null ? <img src="../../public/spinner.gif" /> :
            (songsList.length  === 0) ? (
                <div>
                    No songs were loaded. {activePlaylist.id === 1 ? 
                    "To add songs, choose a folder containing mp3 files and set the path in the settings."
                    : "You can add songs to this playlist from \"All songs\""}
                </div>
            ) : (
            <table id="song-list-table">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Artist</th>
                        <th>Album</th>
                        <th>Duration</th>
                    </tr>
                </thead>
                <tbody>
                    {songsList.map(s => {
                        if(!s) return null;
                        return (
                            <tr key={s.songId} className={"song-list-item " + (s.title === currentSong.title ? "current-song-item " : "")
                            + (selectedSongs.has(s.songId) ? "selected-song" : "")} 
                            onClick={()=>isSelectingSongs ? handleSelectSongItem(s) : setCurrentSong(s)}>
                                <td className="song-list-info">{s.title}</td>
                                <td className="song-list-info">{s.artist || "---"}</td>
                                <td className="song-list-info">{s.album || "---"}</td>
                                <td className="song-list-info">{s.duration}</td>
                            </tr>
                        )
                    })}
                </tbody>
            </table>)}
        </div>
    )
}