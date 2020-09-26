var http = require('http');
var fs = require('fs');
http.createServer(function (req, res) {
  var path = url.parse(request.url).pathname;
    switch (path) {
      case '/texturepack.zip'://TO DO: Link to homepage.html
          fs.readFile(path, function(err, data) {
            res.writeHead(200, {'Content-Type': 'zip'});
            res.write(data);
            return res.end();
          });
        break;
      case '/playerlookup'://TO DO: Link to homepage.html
          fs.readFile(path, function(err, data) {
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(data);
            return res.end();
          });
        break;
      default:
        res.writeHead(200, {'Content-Type': 'text/html'});
            res.write("Oops! Page not found.");
            return res.end();
        break;
    }
}).listen(8080);
