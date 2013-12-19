# Testing purposes only
handler = (req, res) ->
  fs.readFile __dirname + '/index.html', (err, data) ->
    if err
      res.writeHead 500
      return res.end 'Error loading index.html'
    
    res.writeHead 200
    res.end data

app = require('http').createServer(handler)
net = require 'net'
io = require('socket.io').listen 8081
fs = require 'fs'
emitters = {}
current_id = 0
binding = (id, string) ->

app.listen 8080

# Listen on a local port for the Java communication
socket_server = net.createServer (conn) ->
  current_binding = binding
  binding = (id, string) ->
    # Pass all socket.io responses on to the client
    conn.write id + ' ' + string
    current_binding(id, string)
  conn.on 'data', (data) ->
    parsed = JSON.parse(data.toString())
    emitters[parsed.id](parsed.message)

socket_server.listen 9001

# Listen on port (currently 8080) for the socket.io connection
io.sockets.on 'connection', (socket) ->
  id = null

  socket.on 'identify', (data) ->
    if id of emitters
      socket.emit 'identify', error: 'I.D. conflict'
    else
      id = data.id
      emitters[id] = (message) ->
        socket.emit 'message', message: message
      console.log JSON.stringify emitters
      socket.emit 'identify', success: true

  socket.on 'message', (data) ->
    if id?
      binding id, data.message
      socket.emit 'confirm', success: true
    else
      socket.emit 'confirm', error: 'No given I.D.'
