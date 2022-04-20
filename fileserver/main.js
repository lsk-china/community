let http = require('http')
let url = require('url')
let util = require('util')
let fs = require('fs')

function write404 (reqUrl, resp) {
    resp.writeHead(404, {'Content-Type': 'text/html', 'Access-Control-Allow-Origin': '*'})
    resp.end('<h1>404 Not Found</h1><h3>' + reqUrl + '</h3>')
}

let handlers = {
    all: function (location, resp) {
        let content = fs.readFileSync(location).toString()
        resp.writeHead(200, {'Content-Type': 'text/plain', 'Access-Control-Allow-Origin': '*'})
        resp.end(content + '\r\n')
    },
//    markdown: function (location, resp) {
//        let content = fs.readFileSync(location).toString()
//        let rendered = converter.makeHtml(content)
//        let html = '<!DOCTYPE html><html><head><meta charset="utf-8"/></head><body>' + rendered + '</body></html>'
//        resp.writeHead(200, {'Content-Type': 'text/html'})
//       resp.end(html)
//    },
    jpg: function (location, resp) {
        let content = fs.readFileSync(location)
        resp.writeHead(200, {'Content-Type': 'image/jpeg', 'Access-Control-Allow-Origin': '*'})
        resp.end(content)
    }
}

console.log("Starting server at 10002...")
http.createServer((req, resp) => {
    try {
        let reqUrl = url.parse(req.url, true).path
        let location = 'serverRoot' + reqUrl
        console.log(location)
        if (!fs.existsSync(location)) {
            write404(reqUrl, resp)
            return
        }
        let type = reqUrl.split('.')[1]
        let handler = handlers[type]
        if (handler === undefined) {
            handler = handlers.all
        }
        handler(location, resp)
    } catch (e) {
        console.error(e)
        resp.writeHead(500, {'Content-Type': 'text/plain', 'Access-Control-Allow-Origin': '*'})
        resp.end('Server error.\n')
    }
}).listen(10002)

