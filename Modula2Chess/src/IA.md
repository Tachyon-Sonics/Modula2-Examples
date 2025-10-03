(*
    Copyright 2003 Javier Callón Álvarez

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
DEFINITION MODULE IA;
FROM TDATablero IMPORT TipoTablero, TipoColor;
	
PROCEDURE JugadasPosibles(Tablero: TipoTablero; Color: TipoColor): BOOLEAN;
(******************************************************************************************************************
	Devuelve TRUE en caso de que el jugador del color especificado pueda hacer algún movimiento y FALSE en caso 
	contrario.
******************************************************************************************************************)

PROCEDURE Jugada(VAR Tablero: TipoTablero; Jugador: TipoColor; Metodo: INTEGER);
(******************************************************************************************************************
	El jugador Max hace un movimiento en el Tablero.
******************************************************************************************************************)

END IA.






