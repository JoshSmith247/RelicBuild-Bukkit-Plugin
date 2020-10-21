var http = require('http');
var url = require('url');
var fs = require('fs');

var assets = [];

fs.readdir([__dirname, "files"].join("/"), function (err, files) {
    if (err) {
        console.log("Directory Error.");
        console.log(err);
    } else {
        files.forEach(function (file) {
            assets.push("/files/" + file);
        });
    }
});

http.createServer(function (req, res) {
  var path = url.parse(req.url).pathname;
  var fspath = __dirname + path;
    switch (path) {
      case '/texturepack.zip'://TO DO: Link to homepage.html
          fs.readFile(path, function(err, data) {
            res.writeHead(200, {'Content-Type': 'application/zip'});
            res.write(data);
            res.end();
          });
        break;
      case '/playerlookup'://TO DO: Link to homepage.html
          fs.readFile(path, function(err, data) {
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(data);
            res.end();
          });
        break;
      default:
        if (assets.includes(path)) {
                fs.readFile(fspath, function (err, data) {
                    res.writeHead(200, {
                        'Content-Type': 'text/plain'
                    });
                    res.write(data);
                    res.end();
                });
            } else {
            res.writeHead(200, {'Content-Type': 'text/html'});
                res.write("Oops! Page not found.");
                res.end();
            break;
            }
    }
}).listen(8080);
