var io = require("socket.io")(7000);
var mysql = require("mysql");

var usuarios = new Array();

var connection = mysql.createConnection({
	host: "localhost",
	user: "root",
	password: "",
	database: "damasInglesas",
	port: 3306
});

var tablero = new Array(8);
for (var i = 0; i < 8; i++) {
	tablero[i] = new Array(8);
}
for (var i = 0; i < 8; i++) {
	for (var j = 0; j < 8; j++) {
		tablero[i][j] = 0;
	}
}

console.log("Servidor esperando...");
io.on("connection", function(socket) {
    console.log("Cliente conectado");


function ejeX (posicion) {
	return parseInt(posicion.charAt(0));
}

function ejeY (posicion) {
	return parseInt(posicion.charAt(2));
}

socket.on("fichasTablero", function (posiciones){
	tablero[ejeX(posiciones.ficha1)][ejeY(posiciones.ficha1)] = 1;
	tablero[ejeX(posiciones.ficha2)][ejeY(posiciones.ficha2)] = 1;
	tablero[ejeX(posiciones.ficha3)][ejeY(posiciones.ficha3)] = 1;
	tablero[ejeX(posiciones.ficha4)][ejeY(posiciones.ficha4)] = 1;
	tablero[ejeX(posiciones.ficha5)][ejeY(posiciones.ficha5)] = 1;
	tablero[ejeX(posiciones.ficha6)][ejeY(posiciones.ficha6)] = 1;
	tablero[ejeX(posiciones.ficha7)][ejeY(posiciones.ficha7)] = 1;
	tablero[ejeX(posiciones.ficha8)][ejeY(posiciones.ficha8)] = 1;
	tablero[ejeX(posiciones.ficha9)][ejeY(posiciones.ficha9)] = 1;
	tablero[ejeX(posiciones.ficha10)][ejeY(posiciones.ficha10)] = 1;
	tablero[ejeX(posiciones.ficha11)][ejeY(posiciones.ficha11)] = 1;
	tablero[ejeX(posiciones.ficha12)][ejeY(posiciones.ficha12)] = 1;
	tablero[ejeX(posiciones.ficha13)][ejeY(posiciones.ficha13)] = 1;
	tablero[ejeX(posiciones.ficha14)][ejeY(posiciones.ficha14)] = 1;
	tablero[ejeX(posiciones.ficha15)][ejeY(posiciones.ficha15)] = 1;
	tablero[ejeX(posiciones.ficha16)][ejeY(posiciones.ficha16)] = 1;
	tablero[ejeX(posiciones.ficha17)][ejeY(posiciones.ficha17)] = 1;
	tablero[ejeX(posiciones.ficha18)][ejeY(posiciones.ficha18)] = 1;
	tablero[ejeX(posiciones.ficha19)][ejeY(posiciones.ficha19)] = 1;
	tablero[ejeX(posiciones.ficha20)][ejeY(posiciones.ficha20)] = 1;
	tablero[ejeX(posiciones.ficha21)][ejeY(posiciones.ficha21)] = 1;
	tablero[ejeX(posiciones.ficha22)][ejeY(posiciones.ficha22)] = 1;
	tablero[ejeX(posiciones.ficha23)][ejeY(posiciones.ficha23)] = 1;
	tablero[ejeX(posiciones.ficha24)][ejeY(posiciones.ficha24)] = 1;
});


socket.on ("tiros", function(tiro) {
	if (tablero[tiro.originalY][tiro.originalX] === 1) {
		tablero[tiro.originalY][tiro.originalX] = 0;
		if (tablero[tiro.movX][tiro.movY] === 0) {
			tablero[tiro.movX][tiro.movY] = 1;
			io.sockets.in(tiro.retador).emit('ganaste', true);
			io.sockets.in(tiro.contrincante).emit('ganaste', false);
		} else {
			console.log("no se puede mover aquí");
			io.sockets.in(tiro.retador).emit('comer', true);
			io.sockets.in(tiro.contrincante).emit('mover', tiro);
		}
	} else {
		console.log("Se jodio en el false");
		io.sockets.in(tiro.retador).emit('comer', false);
		io.sockets.in(tiro.contrincante).emit('comer', true);
		io.sockets.in(tiro.contrincante).emit('mover', tiro);
	}
});


    socket.broadcast.emit("avisoConectado", "Nuevo usuario conectado");

    socket.on("nombreUsuarioServidor",function(mensaje){
    	console.log(mensaje);
    	socket.emit("nombreUsuarioCliente", mensaje);
    	socket.broadcast.emit("nombreUsuarioCliente", mensaje);
    }).on("desconectado", function(usuarioDesconectado) {
    	console.log(usuarioDesconectado);
    });
});

