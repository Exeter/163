var app = require('http').createServer(handler),
    net = require('net'),
    io = require('socket.io').listen(app),
    fs = require('fs'),
    people = [];

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
  var id = people.length;
  socket.on('client_to_server', function(data) { console.log(data, people[data.who]); people[data.who](id + ": " + data.what); });
  socket.emit("id", {data: id});
  people.push(function(string) { console.log("writing " + string); socket.emit("server_to_client", { data: string }); });
});
