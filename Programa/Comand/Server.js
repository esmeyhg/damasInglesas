var io = require("socket.io")(7000);

io.on("connection", function(socket) {
    console.log("Cliente conectado");
    socket.broadcast.emit("avisoConectado","Nuevo cliente conectado");

    socket.on("saludoServidor",function(mensaje){
    	console.log(mensaje);
    	socket.emit("saludoCliente", mensaje);
    	socket.broadcast.emit("saludoUsuario", mensaje);
    });
});


