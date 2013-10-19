var app = require('http').createServer(handler),
    io = require('socket.io').listen(app),
    fs = require('fs')

app.listen(80);

function handler(req, res) {
  fs.readFile(__dirname + "/index.html",
    function(err, data) {
      if (err) {
        res.writeHead(500);
        return res.end('Error loading index.html');
      }

      res.writeHead(200);
      res.end(data);
    }
  );
}

io.sockets.on('connection', function (socket) {
  socket.on('client_to_server', function(data) {
    console.log(data.data);
  });
  process.stdin.resume();
  process.stdin.setEncoding('utf8');
  process.stdin.on('data', function(string) {
    socket.emit("server_to_client", { data: string });
  });
});
