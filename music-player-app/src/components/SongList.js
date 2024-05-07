import { useState } from "react"
import { Link } from "react-router-dom";


export default function SongList({currentSong, setCurrentSong, songsList, activePlaylist}) {
    var [selectedSongs, setSelectedSongs] = useState(new Set());
    var [isSelectingSongs, setIsSelectingSongs] = useState(false);

    return (
        <div className="song-list-container">
            <h3>{activePlaylist.name}</h3>
            <hr />
            {songsList.length  === 0 ? (
                <div>
                    No songs were loaded. To add songs, create a folder and set the path in the settings!
                </div>
            ) : null}
            {songsList.map(s => {
                return (
                    <div className={"song-list-item " + (s.title === currentSong.title)}>
                        <p className="song-list-info">{s.title}</p>
                        <p className="song-list-info">{s.artist}</p>
                        <p className="song-list-info">{s.album}</p>
                    </div>
                )
            })}
        </div>
    )
}