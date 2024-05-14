const fs = require("fs");
const path = require("path");
var { logLevel } = require("../src/environment");
var Logger = require("./logger");
const NodeID3 = require("node-id3");
const getMp3Duration = require("get-mp3-duration");

const logger = new Logger(logLevel, "music-player-file-utils");

const DATA_PATH = "./server/data";
const SETTINGS_DATA_PATH = "./server/data/settings.json";
const PLAYLISTS_DATA_PATH = "./server/data/playlists.json";
const SONGS_DATA_PATH = "./server/data/songs.json";

var settings = null, playlists = null, songs = null;

var formatDuration = (ms) => {
    var totalSec = ms / 1000;
    var mins = Math.floor(totalSec / 60);
    var sec = Math.floor(totalSec % 60);

    return (mins < 10 ? "0": "") + mins + ":" + (sec < 10 ? "0": "") + sec;
}

var savePlaylists = () => {
    logger.info("Saving playlists data");
    fs.writeFile(PLAYLISTS_DATA_PATH, JSON.stringify(playlists), (err) => {
        logger.error(err);
    });
}

var deleteSong = (songId) => {
    delete songs[songId];
    playlists.forEach(p => {
        p.songIds = p.songIds.filter(id => id != songId)
    })
    savePlaylists();
}

module.exports = {
    // check if data files exist, if not, create them
    initFiles: function(){
        if(!fs.existsSync(DATA_PATH)) {
            logger.debug("data folder not found, attempting to create");
            fs.mkdirSync(DATA_PATH);
        }

        if(!fs.existsSync(SETTINGS_DATA_PATH)){
            logger.debug("settings file not found, attempting to create...");
            fs.writeFileSync(SETTINGS_DATA_PATH, JSON.stringify({
                songsFolderPath: null
            }))
        }
        if(!fs.existsSync(PLAYLISTS_DATA_PATH)) {
            logger.debug("playlists file not found, attempting to create...");
            fs.writeFileSync(PLAYLISTS_DATA_PATH, JSON.stringify([
                {
                    "id": 1,
                    "name": "All songs",
                    "songIds": []
                }
            ]))
        }
        if(!fs.existsSync(SONGS_DATA_PATH)) {
            logger.debug("songs file not found, attempting to create...");
            fs.writeFileSync(SONGS_DATA_PATH, JSON.stringify({latestSongId: 0}))
        }
    },
    // read data from JSON files 
    loadData: function(){
        logger.info("Initializing data...");
        try {
            settings = JSON.parse(fs.readFileSync(SETTINGS_DATA_PATH, {"encoding": "utf-8"}));
            logger.info("  - settings loaded");
            logger.debug(settings);

            playlists = JSON.parse(fs.readFileSync(PLAYLISTS_DATA_PATH, {"encoding": "utf-8"}));
            logger.info("  - playlists loaded");
            logger.debug(playlists);

            songs = JSON.parse(fs.readFileSync(SONGS_DATA_PATH, {"encoding": "utf-8"}));
            logger.info("  - songs loaded");
            logger.debug(songs);
        } catch(err) {
            logger.error("Error while reading data files", err);
            process.exit(-1);
        }
    },
    getPlaylists: function() {
        if(playlists !== null) {
            return playlists;
        } else {
            throw Error("PlaylistDataNotLoadedError");
        }
    },
    getSongs: function() {
        if(songs !== null) {
            return songs;
        } else {
            throw Error("SongsDataNotLoadedError");
        }
    },
    getSettings: function() {
        if(settings !== null) {
            return settings;
        } else {
            throw Error("SettingsDataNotLoadedError");
        }
    },
    saveSettings: function(obj) {
        logger.debug("Saving song folder path: ", obj);
        var songsFolderPathChanged = obj.songsFolderPath !== settings.songsFolderPath;
        settings = {
            ...settings,
            ...obj
        };
        try {
            fs.writeFileSync(SETTINGS_DATA_PATH, JSON.stringify(settings));
            // if(songsFolderPathChanged)
                return {newSongs: this.syncSongData(), playlists};
        } catch(err) {
            logger.error("Error occurred while writing to settings data", err);
            throw Error("SaveSettingsError");
        }
    },
    createPlaylist: function(p) {
        logger.info("Creating playlist: " + p.name);

        playlists.push({
            id: playlists.length,
            name: p.name,
            songsIds: []
        })
        savePlaylists();

        return playlists;
    },
    syncSongData: function() {
        logger.info("Synchronizing song files");
        var existingSongsMap = new Map();
        Object.entries(songs).forEach(([id, obj]) => {
            if(id !== "latestSongId")
                existingSongsMap.set(obj.filePath, {deleteFlag: true, id});
        });
        logger.debug(existingSongsMap.size, existingSongsMap);

        var filesNames = fs.readdirSync(settings.songsFolderPath).map(f => path.join(settings.songsFolderPath, f));
        var newSongFiles = filesNames.filter(f => {
            var obj = existingSongsMap.get(f);
            if(obj) {
                existingSongsMap.set(f, {...obj, deleteFlag: false});
            }
            return !obj;
        });
        logger.debug("new song file: ", newSongFiles);

        existingSongsMap.forEach((value) => {
            if(value.deleteFlag) deleteSong(value.id)
        })

        var id = songs.latestSongId + 1;
        for(var newSong of newSongFiles) {
            var songBuffer = fs.readFileSync(newSong);
            var tags = NodeID3.read(songBuffer);
            var length = formatDuration(getMp3Duration(songBuffer));
            var fileTitle = path.basename(newSong, path.extname(newSong));
            songs[id] = {
                songId: id,
                title: tags.title || fileTitle,
                artist: tags.artist,
                album: tags.album,
                filePath: newSong,
                relativeFilePath: "\\songs\\" + fileTitle + path.extname(newSong),
                duration: length,
                dateAdded: new Date()
            }
            playlists[0].songIds.push(id);
            id++; 
        }
        songs.latestSongId = id-1;

        savePlaylists();
        fs.writeFile(SONGS_DATA_PATH, JSON.stringify(songs, null, 4), (err) => {
            if(err) logger.error("Error occurred while saving songs after sync", err);
        });

        return songs;
    } 
}