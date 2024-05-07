const fs = require("fs");
const path = require("path");
var { logLevel } = require("../environment");
var Logger = require("./logger");
const NodeID3 = require("node-id3");
const getMP3Duration = require("get-mp3-duration");
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

    return (mins < 10 ? "0": "") + mins + (sec < 10 ? "0": "") + sec;
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
            fs.writeFileSync(SONGS_DATA_PATH, JSON.stringify({}))
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
    saveSongFolderPath: function(path) {
        logger.debug("Saving song folder path: " + path);
        settings.songFolderPath = path;
        try {
            fs.writeFileSync(SETTINGS_DATA_PATH, JSON.stringify(settings));
        } catch(err) {
            logger.error("Error occurred while writing to settings data");
        }
    },
    syncSongData: function() {
        logger.info("Synchronizing song files");
        var existingSongs = new Map();
        Object.entries(songs).forEach(s => {
            existingSongs.set(s.filePath, {deleteFlag: true});
        });
        logger.debug(existingSongs.size, existingSongs);

        var filesNames = fs.readdirSync(settings.songFolderPath).map(f => path.join(settings.songFolderPath, f));
        var newSongFiles = filesNames.filter(f => {
            if(existingSongs.has(f)) {
                existingSongs.set(f, {deleteFlag: false});
            }
            return !existingSongs.has(f);
        });
        logger.debug("new song file: ", newSongFiles);

        var idx = existingSongs.size;
        for(var newSong of newSongFiles) {
            var songBuffer = fs.readFileSync(newSong);
            var tags = NodeID3.read(songBuffer);
            var length = formatDuration(getMp3Duration(songBuffer));
            songs[idx++] = {
                title: tags.title || path.basename(newSong, path.extname(newSong)),
                artist: tags.artist,
                album: tags.album,
                filePath: newSong,
                duration: length,
                dateAdded: new Date()
            } 
        }

        fs.writeFile(SONGS_DATA_PATH, JSON.stringify(songs), (err) => {
            if(err) logger.error("Error occurred while saving songs after sync", err);
        });

        return songs;
    } 
}