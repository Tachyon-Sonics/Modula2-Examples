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
DEFINITION MODULE TDALista;
FROM TDATablero IMPORT TipoNodo;

TYPE
		  TipoElemento = TipoNodo;
(* Definición del tipo de datos abstracto *)
        TipoLista = RECORD
			elemento: ARRAY [1..100000] OF TipoElemento;
			ultimo: INTEGER;
			END;

PROCEDURE RecolectorDeBasura();
(* Elimina la memoria que ha sido usada y ya no es necesaria *)

PROCEDURE CantidadBasura();
(* Muestra la cantidad de basura que hay *)

PROCEDURE Insertar(VAR L: TipoLista; x : TipoElemento;p: INTEGER);
(* Inserta un elemento, x, en una posicion p de L, pasando los elementos
 de la posicion p y siguientes a la posicion inmediatamente posterior *)

PROCEDURE Recuperar(VAR L: TipoLista; VAR x : TipoElemento;p: INTEGER;
VAR encontrado:BOOLEAN);
(* Encuentra el elemento x que esta en la posicion p, si la posicion p es
mayor que el numero de elementos de L, devuelve a encontrado FALSE *)

PROCEDURE Suprimir(VAR L: TipoLista; p: INTEGER);
(* Elimina de L el elemento de la posicion p *)

PROCEDURE Anula(VAR L: TipoLista);
(* Vacía L *)

PROCEDURE Primero(VAR L: TipoLista; VAR x: TipoElemento);
(* Devuelve el primer elemento de L *)

PROCEDURE Ultimo(VAR L: TipoLista; VAR x: TipoElemento);
(* Devuelve el ultimo elemento de L *)

PROCEDURE InicializarLista(VAR L: TipoLista);
(* Función que "Devuelve una lista vacía") *)

PROCEDURE AnadirLIFO(x: TipoElemento; VAR L: TipoLista);
(* Añade el elemento x al principio de la lista L (pila) *)

PROCEDURE AnadirFIFO(x: TipoElemento; VAR L: TipoLista);
(* Añade el elemento x al final de la lista L (cola) *)

PROCEDURE Resto(VAR L: TipoLista);
(* Elimina el primer elemento de la lista *)

PROCEDURE Vacia(VAR L: TipoLista): BOOLEAN;
(* Comprueba que en la lista hay algún elemento *)

PROCEDURE Elemento(p: INTEGER; VAR L: TipoLista; VAR e: TipoElemento);
(* Consulta el elemento de la posición p de la lista sin modificarla *)


END TDALista.





