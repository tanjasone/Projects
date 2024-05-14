import {useEffect, useState} from 'react';
import { baseUrl } from '../environment';

export default function PlayLists({activePlaylist, setActivePlaylist, playlistsList}) {
    var [selectedPlaylists, setSelectedPlaylists] = useState(new Set());
    var [isSelecting, setIsSelecting] = useState(false);

    function handleSelectBtnClicked() {
        setIsSelecting(!isSelecting);
    }


    return (
        <div className="playlists-container">
            <h3>Playlists</h3>
            <div>
                <button className="btn" onClick={handleSelectBtnClicked}>Select</button>
                <button className="btn">Options</button>
            </div>
            {playlistsList.map(p => {
                return (
                    <p className={"playlist-item " + ((p.name === activePlaylist.name) ? "active-playlist" : "")} 
                    onClick={()=>!isSelecting ? setActivePlaylist(p) : null}>
                        {p.name + " (" + p.songIds.length + ")"}
                    </p>
                )
            })}
        </div>
    )
} 