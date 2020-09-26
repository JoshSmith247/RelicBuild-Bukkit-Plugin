var http = require('http');
var url = require('url');
var fs = require('fs');
http.createServer(function (req, res) {
  var path = url.parse(req.url).pathname;
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
        res.writeHead(200, {'Content-Type': 'text/html'});
            res.write("Oops! Page not found.");
            res.end();
        break;
    }
}).listen(8080);
