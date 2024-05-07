class Logger {
    moduleName = "";
    logLevel = 5;
    _TRACE = 1;
    _DEBUG = 2
    _INFO = 3;
    _WARN = 4;
    _ERROR = 5;
    _NONE = 6;

    constructor(logLevel, moduleName="") {
        switch(logLevel.toLowerCase()) {
            case "trace":
                this.logLevel = this._TRACE;
                break;
            case "debug":
                this.logLevel = this._DEBUG;
                break;
            case "info":
                this.logLevel = this._INFO;
                break;
            case "warn":
                this.logLevel = this._WARN;
                break;
            case "error":
                this.logLevel = this._ERROR;
                break;
            case "none":
                this.logLevel = this._NONE;
                break;
            default:
                this.logLevel = 5;
        }
        console.log(moduleName);
        this.moduleName = moduleName;
    }

    trace(...obj) {
        if(this.logLevel <= this._TRACE) {
            console.log("[" + new Date().toLocaleString() + "] |TRACE|" + this.moduleName + "> ", ...obj);
        }
    }
    
    debug(...obj) {
        if(this.logLevel <= this._DEBUG) {
            console.log("[" + new Date().toLocaleString() + "] |DEBUG|" + this.moduleName + "> ",...obj);
        }
    }

    info(...obj) {
        if(this.logLevel <= this._INFO) {
            console.log("[" + new Date().toLocaleString() + "] |INFO |" + this.moduleName + "> ",...obj);
        }
    }
    warn(...obj) {
        if(this.logLevel <= this._WARN) {
            console.log("[" + new Date().toLocaleString() + "] |WARN |" + this.moduleName + "> ",...obj);
        }
    }
    error(...obj) {
        if(this.logLevel <= this._ERROR) 
            console.log("[" + new Date().toLocaleString() + "] |ERROR|" + this.moduleName + "> ",...obj);
    }
};

module.exports = Logger;