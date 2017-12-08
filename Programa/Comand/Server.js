var io = require("socket.io")(7000);


io.on("connection", function(socket) {
    console.log("Cliente conectado");
    socket.broadcast.emit("avisoConectado", "Nuevo usuario conectado");

    socket.on("nombreUsuarioServidor",function(mensaje){
    	console.log(mensaje);
    	socket.emit("nombreUsuarioCliente", mensaje);
    	socket.broadcast.emit("nombreUsuarioCliente", mensaje);
    }).on("desconectado", function(usuarioDesconectado) {
    	console.log(usuarioDesconectado);
    });
});

