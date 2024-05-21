var Logger = require("./logger");
var { logLevel } = require("../src/environment");

var http = require('http');
const fileUtils = require("./music-player-file-utils");

const PORT = 8080;
const logger = new Logger(logLevel, "music-player-server");

fileUtils.initFiles();
fileUtils.loadData();

logger.info("Starting server...");
var server = http.createServer((req, res) => {
    logger.info(req.method, req.url);

    if(req.method === "HEAD") return;
    res.setHeader("Access-Control-Allow-Origin", "*")

    if(req.method === "OPTIONS") {
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS");
        res.end();
    }

    if(req.url === "/") {
        logger.info("Health check - OK");
        res.writeHead(200);
        res.end();
    }

    else if(req.url === "/playlists") {
        if(req.method === "GET") {
            try {
                var playlistData = fileUtils.getPlaylists();
                res.writeHead(200);
                res.end(JSON.stringify(playlistData));
            } catch(err) {
                logger.error(err);
            }
        }

        if(req.method === "PUT") {
            req.on("data", (chunk) => {
                var data = JSON.parse(chunk.toString());
                logger.debug("data ", data);
                var result = null;
                try {
                    result = fileUtils.createPlaylist(data);
                    res.writeHead(200);

                } catch(err) {
                    
                }
                res.end(JSON.stringify(result));
            })
        }

        if(req.method === "POST") {
            req.on("data", (chunk) => {
                var data = JSON.parse(chunk.toString());
                logger.debug("data ", data);
                var result = null;
                try {
                    switch(data.action) {
                        case "ADD_SONGS":
                            result = fileUtils.addSongsToPlaylist(data.playlistId, data.songIds);
                            break;
                        default: 
                            logger.error("Unknown action type");
                            res.writeHead(400);
                    }
                } catch(err) {
                    logger.error("Error occurred while modifying playlist");
                    res.writeHead(500);
                }
                res.end(JSON.stringify(result));
            })
        }
    }

    else if(req.url === "/songs") {
        if(req.method === "GET") {
            try {
                var songsData = fileUtils.getSongs();
                res.writeHead(200);
                res.end(JSON.stringify(songsData));
            } catch(err) {
                logger.error(err);
            }
        }
    }

    else if(req.url === "/settings") {
        if(req.method === "GET") {
            try {
                var settingsData = fileUtils.getSettings();
                res.writeHead(200);
                res.end(JSON.stringify(settingsData));
            } catch(err) {
                logger.error(err);
            }
        }

        if(req.method === "POST") {
            req.on("data", chunk => {
                var data = JSON.parse(chunk.toString());
                logger.info("data", data);
                var newData = fileUtils.saveSettings(data);
                if(newData) {
                    res.writeHead(210);
                    res.end(JSON.stringify(newData));
                } else {
                    res.writeHead(200);
                }
            })
        }
    }
})
server.listen(PORT, () => {
    logger.info("MusicPlayer server listening on port " + PORT);
})