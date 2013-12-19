var app = require('http').createServer(handler)
  , io = require('socket.io').listen(app)
  , fs = require('fs')

  app.listen(1300);

  function handler (req, res) {
    fs.readFile(__dirname + '/index.html',
    function (err, data) {
      if (err) {
        res.writeHead(500);
        return res.end('Error loading index.html');
      }

      res.writeHead(200);
      res.end(data);
    });
  }
  
  io.sockets.on('connection', function (socket) {
    socket.emit('new', [1,2,3,4,5,6]);
    socket.on('submit', function (data) {
      console.log(data);
    });
  });
