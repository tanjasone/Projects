import { useState, useRef, useEffect } from "react";
import PlayLists from "./Playlists";
import SongList from "./SongList";
import { baseUrl } from "../../environment";

export default function Player() {
    var [isDisplayingSettings, setIsDisplayingSettings] = useState(false);
    var [songsList, setSongsList] = useState([]);
    var [currentSong, setCurrentSong] = useState({});
    var [playlistsList, setPlaylistsList] = useState([]);
    var [activePlaylist, setActivePlaylist] = useState({});
    var audioRef = useRef(null);

    useEffect(() => {
        fetch(baseUrl + "/playlists").then(async res => {
            var data = res.json();
            console.log(data);
            setPlaylistsList(data);
            setActivePlaylist(data[0]);

            fetch(baseUrl + "/songs").then(async res => {
                var data = await res.json();
                console.log(data);
                var allSongsList = activePlaylist.songIds.map(id => data[id]);
                setSongsList(allSongsList);
            }).catch(err => {
                console.error("Error while fetching songs", err);
                alert("An error has occurred while fetching songs");
            })
        }).catch(err => {
            console.err("Error while fetching playlists: ", err);
            alert("An error has occurred while fetching playlists: ");
        })
    })
    return (
        <div>
            
            <div>
                <PlayLists activePlaylist={activePlaylist} setActivePlaylist={setActivePlaylist} 
                    playlistsList={playlistsList} />
                <SongList currentSong={currentSong} setCurrentSong={setCurrentSong} 
                    songsList={songsList} activePlaylist={activePlaylist} />
            </div>
            
            <div className="audio-container">
                <audio ref={audioRef} src={currentSong.path}></audio>
            </div>
        </div>
    )
}