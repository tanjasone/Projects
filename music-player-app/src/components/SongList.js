import "../App.css"
import { useState } from "react"


export default function SongList({currentSong, setCurrentSong, songsList, activePlaylist}) {
    var [selectedSongs, setSelectedSongs] = useState(new Set());
    var [isSelectingSongs, setIsSelectingSongs] = useState(false);

    function handleSelectSongItem(s) {
        if(selectedSongs.has(s)) {
            
        }
    }

    return (
        <div className="song-list-container">
            <h3>{activePlaylist.name}</h3>
            <button className="btn" onClick={()=>setIsSelectingSongs(!isSelectingSongs)}>Select</button>
            <button className="btn">Options</button>
            <hr />
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
                    {songsList === null ? <img src="../../public/spinner.gif" /> :
                    (songsList.length  === 0 ? (
                        <div>
                            No songs were loaded. To add songs, choose a folder containing mp3 files and set the path in the settings.
                        </div>
                    ) : songsList.map(s => {
                        if(!s) return;
                        return (
                            <tr key={s.songId} className={"song-list-item " + (s.title === currentSong.title ? "current-song-item" : "")} 
                            onClick={()=>isSelectingSongs ? handleSelectSongItem(s) : setCurrentSong(s)}>
                                <td className="song-list-info">{s.title}</td>
                                <td className="song-list-info">{s.artist || "---"}</td>
                                <td className="song-list-info">{s.album || "---"}</td>
                                <td className="song-list-info">{s.duration}</td>
                            </tr>
                        )
                    }))}
                </tbody>
            </table>
        </div>
    )
}