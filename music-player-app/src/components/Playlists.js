import {useEffect, useState} from 'react';
import { baseUrl } from '../../environment';

export default function PlayLists({activePlaylist, setActivePlaylist, playlistsList}) {
    var [selectedPlaylists, setSelectedPlaylists] = useState(new Set());
    var [isSelecting, setIsSelecting] = useState(false);
    var [playlistsList, setPlaylistsList] = useState([]);

    function handleSelectBtnClicked() {
        setIsSelecting(!isSelecting);
    }


    return (
        <div className="playlists-container">
            <div>
                <button className="btn" onClick={handleSelectBtnClicked}>Select</button>
                <button className="btn">Options</button>
            </div>
            <ul>
                {playlistsList.map(p => {
                    return (
                        <li className={"playlist-item " + (p.name === activePlaylist.name) ? "active-playlist" : ""} 
                        onClick={()=>!isSelecting ? setActivePlaylist(p) : null}>
                            {p.name + " (" + p.songs.length + ")"}
                        </li>
                    )
                })}
            </ul>
        </div>
    )
} 