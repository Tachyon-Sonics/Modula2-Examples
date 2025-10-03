(*
    Copyright 2003 Javier Call�n �lvarez

    This file is part of Modula2Chess.

    Modula2Chess is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    any later version.

    Modula2Chess is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Modula2Chess; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*)
DEFINITION MODULE TDATablero;
FROM InOut IMPORT Write, WriteString, WriteInt, WriteLn, ReadInt, Read;
FROM TextIO IMPORT Accessible, File, OpenInput, Close, GetInt, GetChar;
FROM Storage IMPORT ALLOCATE, DEALLOCATE;

CONST
	MAXALTO = 8;
	MAXANCHO = 8;
(* Constantes para representaci�n con SDL *)
	DespX=0;
	DespY=60;
	TamanhoCasilla=60;

TYPE 

TipoPosicion = RECORD
	x,y: INTEGER;
	END;

TipoColor = (NEGRO, BLANCO);

TipoPieza = (VACIA, PEON, CABALLO, ALFIL, TORRE, DAMA, REY);

TipoCasilla = RECORD
	Color: TipoColor;
	Pieza: TipoPieza;
	END;

TipoMovimiento = RECORD
	Origen, Destino: TipoPosicion;
	END;

TipoTablero = RECORD
	Alto: INTEGER;
	Ancho: INTEGER;
	Casilla: ARRAY [1..MAXALTO],[1..MAXANCHO] OF TipoCasilla;
	UltimoMovimiento: TipoMovimiento;
	ReyBlancoMovido, TorreIBlancaMovida, TorreDBlancaMovida: BOOLEAN; (* Necesario para enroques *)
	ReyNegroMovido, TorreINegraMovida, TorreDNegraMovida: BOOLEAN;
	UltimaCaptura: TipoCasilla;
	END;

TipoNodo = POINTER TO TipoDatos;

TipoDatos = RECORD
   Tablero: TipoTablero;
   Predecesor: TipoNodo;
   Nivel: INTEGER;
   Alfa, Beta: INTEGER;
   Evaluacion: INTEGER;
   END;

(**************************************************************************************************************)
(*****************************    Funciones generales para el manejo de tableros     **************************)
(**************************************************************************************************************)

PROCEDURE JaqueMate(Tablero: TipoTablero; Color: TipoColor):INTEGER;
(******************************************************************************************************************
	Determina si el jugador especificado por Color est� en jaque mate.
	Devuelve:
		0: Si no hay jaque mate ni el rey est� ahogado;
		1: Si es jaque mate;
		2: Si el rey est� ahogado;
******************************************************************************************************************)

PROCEDURE EsMate(Tablero: TipoTablero):BOOLEAN;
(******************************************************************************************************************
   Devuelve TRUE en caso de haberse comido un rey y FALSE en caso contrario.
******************************************************************************************************************)

PROCEDURE CopiarTablero(Tablero1: TipoTablero; VAR Tablero2: TipoTablero);
(******************************************************************************************************************
   Copia el contenido del primer tablero al segundo. 
   Los dos tableros deben tener las mismas dimensiones. (Precondici�n)
******************************************************************************************************************)

PROCEDURE LeerTablero (VAR Tablero: TipoTablero; Archivo: ARRAY OF CHAR):BOOLEAN;
(******************************************************************************************************************
Lee un tablero guardado en un archivo con el siguiente formato:
-Alto
-Ancho
-Matriz de piezas:
	R -> REY
	D -> DAMA
	T -> TORRE
	A -> ALFIL
	C -> CABALLO
	P -> PEON
	V -> Casilla vac�a.
  * Se representan con may�scula las piezas negras y con min�scula las piezas blancas.
-Coordenadas del �ltimo movimiento, por ejemplo:
	4 2 6 4
 representar�a el movimiento de la pieza que est� en 4,2 a la posici�n 6,4
-Valores booleanos que representan si se han movido alguna vez las siguientes piezas por este orden:
 Rey blanco, torre blanca izquierda, torre blanca derecha, rey negro, torre negra izquierda, 
 torre negra derecha. Por ejemplo:
 0 0 1 1 1 0

Devuelve TRUE en caso de error y FALSE en caso de no existir error en la lectura.
******************************************************************************************************************)

PROCEDURE MoverPieza (Posicion1, Posicion2: TipoPosicion; VAR Tablero: TipoTablero; Humano: BOOLEAN; SDL: BOOLEAN);
(******************************************************************************************************************
  Mueve la pieza de la casilla Posicion1 a la casilla Posicion2 sin hacer comprobaciones.
  Las comprobaciones deben hacerse anteriormente (precondici�n)
*******************************************************************************************************************)

PROCEDURE PiezaAmenazada (Posicion: TipoPosicion; Tablero: TipoTablero):BOOLEAN;
(******************************************************************************************************************
   Devuelve el valor TRUE si alguna pieza est� amenazando la posici�n suministrada como par�metro y FALSE en 
   caso contrario.
******************************************************************************************************************)

PROCEDURE PosicionRey(Tablero: TipoTablero; Color: TipoColor; VAR Posicion: TipoPosicion);
(******************************************************************************************************************
	Devuelve la posici�n del rey del color elegido.
******************************************************************************************************************)

PROCEDURE ReyEnJaque(Tablero: TipoTablero; Color: TipoColor):BOOLEAN;
(******************************************************************************************************************
   Devuelve TRUE en caso de que el rey elegido est� en jaque y FALSE en caso contrario.
******************************************************************************************************************)

PROCEDURE ReyEnJaque2(Posicion1, Posicion2: TipoPosicion; Tablero: TipoTablero; Color: TipoColor):BOOLEAN;
(******************************************************************************************************************
	Devuelve TRUE si tras la jugada de posici�n 1 a posici�n 2 el rey est� en jaque y FALSE en caso contrario.
******************************************************************************************************************)

PROCEDURE MovimientoLegal (Posicion1, Posicion2: TipoPosicion; Tablero: TipoTablero; Debug: BOOLEAN):BOOLEAN;
(******************************************************************************************************************
   Devuelve TRUE en caso de que el movimiento sea legal y False en caso contrario.

Se hacen las siguientes comprobaciones:
-Comprobar que las direcciones no exceden los l�mites del tablero.
-Comprobar que las direcciones de origen y destino no son iguales.
-Comprobar que la direcci�n de origen no est� vac�a.
-Comprobar que la direcci�n de destino no contiene una pieza del mismo color.
-Comprobar que la direcci�n del movimiento es posible.
-Comprobar que no existen obst�culos para realizar la jugada.
** La comprobaci�n de que el rey no est� amenazado se hace en "ReyEnJaque2"
** La poda por estar el rey en jaque se hace en el procedimiento de generaci�n de jugadas
******************************************************************************************************************)

PROCEDURE PuntuacionTablero(Tablero: TipoTablero; Jugador: TipoColor):INTEGER;
(******************************************************************************************************************
   Funci�n que estima la bondad de una situaci�n del tablero para un Jugador.

100 por cada peon,
315 por cada caballo, 
330 por cada alfil, 
500 por cada torre, 
940 por cada dama,
m�ximo por el rey,
1 punto por cada casilla a la que se puedan mover los alfiles, 
1 punto por cada casilla a la que se puedan mover las torres, 
Da mayor puntuaci�n el mover los peones centrales (casillas avanzadas * posici�n(1 lateral a 4 central)), 
De 0 hasta 9 gradualmente por la posicion de cada caballo (desde un rincon del tablero hasta el centro), 
De 0 a 9 gradualmente por la posicion de la dama (desde un rincon hasta el centro), 
-20 por peon doblado, 
bonus gradual para la posicion del rey, en la apertura mientras m�s cerca del centro es m�s malo y en los finales de partida (poco material) al rev�s. 
******************************************************************************************************************)

(**************************************************************************************************************)
(**************************************************************************************************************)


(**************************************************************************************************************)
(*****************************    Funciones para representaci�n en modo texto      ****************************)
(**************************************************************************************************************)

PROCEDURE ImprimirTablero (Tablero: TipoTablero; Debug: BOOLEAN);
(******************************************************************************************************************
   Imprime un tablero en la salida estandar.
   Si Debug es TRUE, muestra informaci�n adicional:
	alto, ancho, �ltima jugada y piezas (reyes y torres) movidas.
******************************************************************************************************************)

PROCEDURE LeerMovimiento (VAR Movimiento: TipoMovimiento);
(******************************************************************************************************************
   Lee un movimiento de la entrada estandar, para hacer esto se leen cuatro n�meros,
   cada uno corresponde a una coordenada, dos de origen y dos de destino.
   No se hacen comprobaciones.
******************************************************************************************************************)

PROCEDURE ElegirPieza (VAR Pieza: TipoPieza);
(******************************************************************************************************************
   Muestra el men� de selecci�n de pieza
******************************************************************************************************************)

(**************************************************************************************************************)
(**************************************************************************************************************)


END TDATablero.

