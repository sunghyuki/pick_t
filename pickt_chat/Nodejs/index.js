const express = require('express')
const path = require('path')
var app = express();
const connect=require('./schemas');
var http = require('http').Server(app);
var io = require('socket.io')(http);
const Room = require('./schemas/room');
const Chat = require('./schemas/chat');

const room=io.of('/room');
const chat=io.of('/chat');
connect();
room.on('connection',function(socket){
  var json=new Object;
  console.log('room 네임스페이스에 접속');
  socket.on('newRoom',function(room){
    console.log('새로운 ROOM');

    try {
    const newRoom = Room.create({
      roomNumber: room['roomnumber'],
      owner: room['user'],
    });
    json.exist="false"
    socket.emit('existedRoom',json);
  } catch (error) {
    console.error(error);
    next(error);
  }}

  )
  socket.on('disconnect',()=>{
      console.log('room 네임스페이스 접속 해제')
  });
});
app.set('port', (process.env.PORT || 3001));

app.use(express.json());
app.use(express.static(__dirname + '/public'));

app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');

console.log("outside io");

chat.on('connection',function(socket){
  console.log('chat 네임스페이스 접속');
  console.log('User Conncetion');

  socket.on('connect user', function(user){
    console.log("Connected user ");
    console.log(user);
    
    const roomName=user['roomnumber'];
    
    console.log(roomName);
    console.log(typeof(user));
    console.log(typeof(roomName));
    socket.join(`${roomName}`);
    chat.to(`${roomName}`).emit('connect user', user);
  });

  socket.on('on typing', function(typing){
    console.log("Typing.... ");
    chat.emit('on typing', typing);
  });

  socket.on('chat message', function(msg){
    const roomName=msg['roomnumber'];
    console.log("Message " + msg['message']);
    chat.to(roomName).emit('chat message', msg);
  });
  
})

http.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});
