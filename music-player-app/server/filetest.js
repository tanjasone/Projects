const fs = require("fs");
const NodeID3 = require("node-id3");
const getMP3Duration = require("get-mp3-duration");
const path = require("path");

const FOLDER_PATH = "C:/Users/bugle/Documents/projects/react-app/songs";

fs.readdirSync(FOLDER_PATH).forEach(file => {
    var filePath = path.join(FOLDER_PATH, file);
    console.log("Reading tags from " + filePath);
    var buf = fs.readFileSync(filePath);
    var tags = NodeID3.read(buf);
    console.log(tags);
    var sec = getMP3Duration(buf) / 1000;
    console.log("duration - " + Math.floor(sec / 60) + ":" + Math.floor(sec % 60));
    console.log("===================")
})