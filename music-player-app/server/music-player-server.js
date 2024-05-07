var Logger = require("./logger");
var { logLevel } = require("../environment");

var http = require('http');
const fileUtils = require("./music-player-file-utils");

const PORT = 8080;
const logger = new Logger(logLevel, "music-player-server");

fileUtils.initFiles();
fileUtils.loadData();

logger.info("Starting server...");
var server = http.createServer((req, res) => {
    logger.info(req.method, req.url);

    if(req.url === "/") {
        logger.info("Health check - OK");
        res.writeHead(200);
    }

    else if(req.url === "/playlists") {
        if(req.method === "GET") {
            
        }

        if(req.method === "POST") {
            req.on("data", (chunk) => {
                console.log("data", chunk.toString());
            })
        }
    }
})
server.listen(PORT, () => {
    logger.info("MusicPlayer server listening on port " + PORT);
})