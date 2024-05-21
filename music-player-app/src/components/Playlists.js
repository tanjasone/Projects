import {useEffect, useState} from 'react';
import { baseUrl } from '../environment';
import PromptModal from './PromptModal';

export default function PlayLists({activePlaylist, setActivePlaylist, playlistsList, setPlaylistsList}) {
    var [selectedPlaylists, setSelectedPlaylists] = useState(new Set());
    var [isSelecting, setIsSelecting] = useState(false);
    var [isOptionsOpen, setIsOptionsOpen] = useState(false);
    var [isCreatingNewPlaylist, setIsCreatingNewPlaylist] = useState(false);

    function handleSelectBtnClicked() {
        setIsSelecting(!isSelecting);
    }

    function addToSelectedPlaylists(p) {
        if(p.id === 1) return;

        if(selectedPlaylists.has(p)) {
            selectedPlaylists.delete(p);
        } else {
            selectedPlaylists.add(p);
        }
    }

    function handleCreateNewPlaylist(responseData) {
        if(responseData === "cancel") setIsCreatingNewPlaylist(false);

        setPlaylistsList(responseData);
        setTimeout(() => setIsCreatingNewPlaylist(false), 2000);
    }

    return ( 
        <div className="playlists-container">
            {isCreatingNewPlaylist ?
            <PromptModal
                title="Create New PlayList"
                fields={[
                    {name: "Name", type: "text"}
                ]}
                fetchUrlPath="/playlists"
                fetchMethod="PUT"
                responseCallback={handleCreateNewPlaylist}
            /> : null}
            <h3>Playlists</h3>
            <div>
                <button className="btn" onClick={handleSelectBtnClicked}>{isSelecting ? "Cancel Select": "Select"}</button>
                {isSelecting ? 
                    <button className="btn">Options</button> 
                : null }
            </div>
            <hr />
            {playlistsList.map(p => {
                return (
                    <p key={p.name} className={"playlist-item " + ((p.name === activePlaylist.name) ? "active-playlist" : "")} 
                    onClick={()=>!isSelecting ? setActivePlaylist(p) : addToSelectedPlaylists(p)}>
                        {p.name + " (" + p.songIds.length + ")"}
                    </p>
                )
            })}
            <button className="btn" onClick={()=> setIsCreatingNewPlaylist(true)}>+ New Playlist</button>
        </div>
    )
} 