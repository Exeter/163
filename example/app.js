var app = require('http').createServer(handler),
    io = require('socket.io').listen(app),
    fs = require('fs'),
    people = {};

app.listen(8080);

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
  var handle;

  socket.on('register', function (data) {
    if (data.handle in people) {
      socket.emit('register', { success: false });
    }
    else {
      handle = data.handle;
      people[data.handle] = socket;
      socket.emit('register', { success: true });
      socket.broadcast.emit('message', { who: '<notice>', message: handle + ' logged in' });
    }
  });

  socket.on('disconnect', function (data) {
    delete people[handle];
    socket.broadcast.emit('message', { who: '<notice>', message: handle + ' logged out' });
  });

  socket.on('message', function (data) {
    people[data.who].emit('message', { who: handle, message: data.message });
  });
});
