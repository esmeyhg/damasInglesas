var io = require("socket.io")(7000);


io.on('connection', function(socket) {
    console.log("¡Jugador conectado!");
    socket.emit('retador', {id: socket.id});
    socket.emit('contrincante', {id: socket.id});
    socket.broadcast.emit('contrincante', {id: socket.id});
    socket.on('disconnect', function(){
    	console.log("Jugador desconectado :(");
    });
});

