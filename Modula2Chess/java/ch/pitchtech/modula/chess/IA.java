/*
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
 */
package ch.pitchtech.modula.chess;

import ch.pitchtech.modula.chess.TDATablero.TipoColor;
import ch.pitchtech.modula.chess.TDATablero.TipoPieza;
import ch.pitchtech.modula.library.mocka.Clock;
import ch.pitchtech.modula.library.mocka.InOut;
import ch.pitchtech.modula.library.mocka.Storage;
import ch.pitchtech.modula.runtime.Runtime;


public class IA {

    // Imports
    private final Clock clock;
    private final InOut inOut;
    private final TDALista tDALista;
    private final TDATablero tDATablero;


    private IA() {
        instance = this; // Set early to handle circular dependencies
        clock = Clock.instance();
        inOut = InOut.instance();
        tDALista = TDALista.instance();
        tDATablero = TDATablero.instance();
    }


    /*
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
     */
    // CONST

    private static final boolean DEBUG = true;
    private static final int MaxNivel = 2;


    // VAR

    private TipoColor JugadorMax;
    private TipoColor JugadorMin;
    private TDATablero.TipoPosicion PosicionDePrueba = new TDATablero.TipoPosicion();
    private int index;
    private TDATablero.TipoTablero Tablero = new TDATablero.TipoTablero();
    private TDATablero.TipoDatos Nodo /* POINTER */;
    private TDATablero.TipoDatos Nodo1 /* POINTER */;
    private TDATablero.TipoDatos Nodo2 /* POINTER */;
    private int JugadasGeneradas;
    private int Tiempo;
    private int NodosEliminados;


    public TipoColor getJugadorMax() {
        return this.JugadorMax;
    }

    public void setJugadorMax(TipoColor JugadorMax) {
        this.JugadorMax = JugadorMax;
    }

    public TipoColor getJugadorMin() {
        return this.JugadorMin;
    }

    public void setJugadorMin(TipoColor JugadorMin) {
        this.JugadorMin = JugadorMin;
    }

    public TDATablero.TipoPosicion getPosicionDePrueba() {
        return this.PosicionDePrueba;
    }

    public void setPosicionDePrueba(TDATablero.TipoPosicion PosicionDePrueba) {
        this.PosicionDePrueba = PosicionDePrueba;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TDATablero.TipoTablero getTablero() {
        return this.Tablero;
    }

    public void setTablero(TDATablero.TipoTablero Tablero) {
        this.Tablero = Tablero;
    }

    public TDATablero.TipoDatos getNodo() {
        return this.Nodo;
    }

    public void setNodo(TDATablero.TipoDatos Nodo) {
        this.Nodo = Nodo;
    }

    public TDATablero.TipoDatos getNodo1() {
        return this.Nodo1;
    }

    public void setNodo1(TDATablero.TipoDatos Nodo1) {
        this.Nodo1 = Nodo1;
    }

    public TDATablero.TipoDatos getNodo2() {
        return this.Nodo2;
    }

    public void setNodo2(TDATablero.TipoDatos Nodo2) {
        this.Nodo2 = Nodo2;
    }

    public int getJugadasGeneradas() {
        return this.JugadasGeneradas;
    }

    public void setJugadasGeneradas(int JugadasGeneradas) {
        this.JugadasGeneradas = JugadasGeneradas;
    }

    public int getTiempo() {
        return this.Tiempo;
    }

    public void setTiempo(int Tiempo) {
        this.Tiempo = Tiempo;
    }

    public int getNodosEliminados() {
        return this.NodosEliminados;
    }

    public void setNodosEliminados(int NodosEliminados) {
        this.NodosEliminados = NodosEliminados;
    }


    // PROCEDURE

    private TDATablero.TipoDatos CrearNodo(TDATablero.TipoTablero Tablero) {
        // VAR
        TDATablero.TipoDatos Nodo = null;

        /* ****************************************************************************************************************
           Crea un nodo a partir de la posición inicial.
        ******************************************************************************************************************/
        Nodo = new TDATablero.TipoDatos();
        tDATablero.CopiarTablero(Tablero, Nodo.Tablero);
        Nodo.Predecesor = null;
        Nodo.Nivel = 0;
        return Nodo;
    }

    private void EliminarNodo(TDATablero.TipoDatos Nodo) {
        /* ****************************************************************************************************************
           Elimina un nodo.
        ******************************************************************************************************************/
        Nodo = null;
    }

    private void CrearHijo(TDATablero.TipoDatos PtrTablero1, /* VAR */ Runtime.IRef<TDATablero.TipoDatos> PtrTablero2) {
        /* ****************************************************************************************************************
           Crea un puntero a una copia del tablero al que apunta PtrTablero1 y hace apuntar el campo Predecesor de la copia
           al tablero original.
           Los dos tableros deben tener las mismas dimensiones. (Precondición)
        ******************************************************************************************************************/
        PtrTablero2.set(new TDATablero.TipoDatos());
        tDATablero.CopiarTablero(PtrTablero1.Tablero, PtrTablero2.get().Tablero);
        PtrTablero2.get().Nivel = PtrTablero1.Nivel + 1;
        PtrTablero2.get().Predecesor = PtrTablero1;
    }

    public boolean JugadasPosibles(TDATablero.TipoTablero Tablero, TipoColor Color) {
        // VAR
        TDALista.TipoLista Lista = new TDALista.TipoLista(); /* WRT */
        TDATablero.TipoPosicion Posicion = new TDATablero.TipoPosicion(); /* WRT */
        TDATablero.TipoDatos Nodo = null;
        Runtime.Ref<TDATablero.TipoDatos> NodoTemp = new Runtime.Ref<>(null);
        Runtime.Ref<Integer> NumeroJugadas = new Runtime.Ref<>(0);

        /* ****************************************************************************************************************
        	Devuelve TRUE en caso de que el jugador del color especificado pueda hacer algún movimiento y FALSE en caso 
        	contrario.
        ******************************************************************************************************************/
        NumeroJugadas.set(0);
        tDATablero.PosicionRey(Tablero, Color, Posicion);
        Nodo = CrearNodo(Tablero);
        tDALista.InicializarLista(Lista);
        Expandir(Nodo, Lista, Color, NumeroJugadas);
        /* Comprueba que las jugadas posibles no son jaque */
        while (!tDALista.Vacia(Lista)) {
            tDALista.Primero(Lista, NodoTemp);
            tDALista.Resto(Lista);
            if (tDATablero.ReyEnJaque(NodoTemp.get().Tablero, Color))
                NumeroJugadas.set(NumeroJugadas.get() - 1);
        }
        /* Libera la memoria */
        tDALista.RecolectorDeBasura();
        EliminarNodo(Nodo);
        if (NumeroJugadas.get() == 0)
            return false;
        else
            return true;
    }

    private void MovimientosPieza(TDATablero.TipoDatos PtrTablero, /* VAR+WRT */ TDALista.TipoLista Lista, int i, int j, /* VAR */ Runtime.IRef<Integer> TotalJugadas) {
        // VAR
        TDATablero.TipoPosicion Origen = new TDATablero.TipoPosicion();
        TDATablero.TipoPosicion Destino = new TDATablero.TipoPosicion();
        Runtime.Ref<TDATablero.TipoDatos> PtrTableroTemp = new Runtime.Ref<>(null);

        /* ****************************************************************************************************************
           Se expande el nodo, generando todas las compleciones para la situación actual. Se hacen las jugadas del jugador 
           especificado por el parámetro Color. La lista de jugadas se introduce en la lista pasada como parámetro.
        ******************************************************************************************************************/
        Origen.x = i;
        Origen.y = j;
        switch (PtrTablero.Tablero.Casilla[i - 1][j - 1].Pieza) {
            case REY -> {
                /* Para cada movimiento posible */
                /* No se añaden las compleciones en las que el rey está amenazado */
                /* Arriba */
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                /* ********/
                /* Abajo */
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                /* ********/
                /* Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                    Destino.x = Destino.x - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                    Destino.x = Destino.x + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Arriba-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                /* ********/
                /* Arriba-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                /* ********/
                /* Abajo-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                /* ********/
                /* Abajo-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
            }
            case DAMA -> {
                /* ********/
                /* Para cada movimiento posible */
                /* Arriba */
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo */
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.y = Destino.y - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Arriba-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Arriba-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y - 1;
                }
            }
            case TORRE -> {
                /* Probar la siguiente posición */
                /* ********/
                /* Para cada movimiento posible */
                /* Arriba */
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo */
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.y = Destino.y - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x + 1;
                }
            }
            case ALFIL -> {
                /* Probar la siguiente posición */
                /* ********/
                /* Para cada movimiento posible */
                /* Arriba-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Arriba-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    /* Añadir la jugada a la lista */
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y - 1;
                }
            }
            case CABALLO -> {
                /* Probar la siguiente posición */
                /* ********/
                /* Para cada movimiento posible */
                /* Arriba-Arriba-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Arriba-Arriba-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Abajo-Abajo-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Abajo-Abajo-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Izquierda-Izquierda-Arriba */
                Destino.x = Origen.x - 2;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Izquierda-Izquierda-Abajo */
                Destino.x = Origen.x - 2;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Derecha-Derecha-Arriba */
                Destino.x = Origen.x + 2;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                /* ********/
                /* Derecha-Derecha-Abajo */
                Destino.x = Origen.x + 2;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
            }
            case PEON -> {
                /* ********/
                /* Para cada movimiento posible */
                /* Arriba */
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
                            /* Promoción del peón blanco */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
                            /* Promoción del peón negro */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                    /* Añadir la jugada a la lista */
                    Destino.y = Destino.y + 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Abajo */
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
                            /* Promoción del peón blanco */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
                            /* Promoción del peón negro */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                    /* Añadir la jugada a la lista */
                    Destino.y = Destino.y - 1;
                }
                /* Probar la siguiente posición */
                /* ********/
                /* Arriba-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
                            /* Promoción del peón blanco */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
                            /* Promoción del peón negro */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
                /* ********/
                /* Arriba-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
                            /* Promoción del peón blanco */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
                            /* Promoción del peón negro */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
                /* ********/
                /* Abajo-Izquierda */
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
                            /* Promoción del peón blanco */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
                            /* Promoción del peón negro */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
                /* ********/
                /* Abajo-Derecha */
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    /* Mientras la jugada no sea ilegal */
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    /* Se hace una copia del tablero tras el movimiento */
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
                            /* Promoción del peón blanco */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
                            /* Promoción del peón negro */
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.DAMA;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.CABALLO;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.ALFIL;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                            PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Pieza = TipoPieza.TORRE;
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        } else {
                            /* Movimiento normal */
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
            }
            default -> {
            }
        }
    }

    /* ********/
    private void Expandir(TDATablero.TipoDatos PtrTablero, /* VAR+WRT */ TDALista.TipoLista Lista, TipoColor Color, /* VAR+WRT */ Runtime.IRef<Integer> TotalJugadas) {
        // VAR
        int i = 0;
        int j = 0;

        /* ****************************************************************************************************************
           Se expande el nodo, generando todas las compleciones para la situación actual. Se hacen las jugadas del jugador 
           especificado por el parámetro Color. La lista de jugadas se introduce en la lista pasada como parámetro.
        ******************************************************************************************************************/
        for (j = 1; j <= PtrTablero.Tablero.Alto; j++) {
            for (i = 1; i <= PtrTablero.Tablero.Ancho; i++) {
                if (PtrTablero.Tablero.Casilla[i - 1][j - 1].Color == Color)
                    MovimientosPieza(PtrTablero, Lista, i, j, TotalJugadas);
            }
        }
    }

    private int NivelNodo(TDATablero.TipoDatos Nodo) {
        /*	IF (Nodo^.Predecesor = NIL) THEN
        		RETURN 0;
        	ELSE
        		RETURN (NivelNodo (Nodo^.Predecesor) + 1);
        	END;
        */
        return Nodo.Nivel;
    }

    private void ImprimirJugadas(TDATablero.TipoDatos PtrTablero, boolean Debug) {
        /* ****************************************************************************************************************
           Se imprimen en la salida estandar las jugadas hechas desde el nodo raiz del árbol de jugadas, hasta la situación 
           final.
        ******************************************************************************************************************/
        if (PtrTablero != null) {
            ImprimirJugadas(PtrTablero.Predecesor, Debug);
            tDATablero.ImprimirTablero(PtrTablero.Tablero, Debug);
            inOut.WriteString("Nivel ");
            inOut.WriteInt(PtrTablero.Nivel, 1);
            inOut.WriteLn();
        }
    }

    /* ****************************************************************************************************************
    	Devuelve el nodo meta al aplicar una poda alfa-beta sobre minimax.
    ******************************************************************************************************************/
    private TDATablero.TipoDatos alfa_beta_max(TDATablero.TipoDatos nodo1, TDATablero.TipoDatos nodo2) {
        /*
        		Develve el nodo1 si el valor alfa de este es mayor que el valor beta del
        		nodo2
        	*/
        if (nodo1.Alfa < nodo2.Beta) {
            nodo2.Alfa = nodo2.Beta;
            nodo2.Beta = nodo1.Beta;
            return nodo2;
        } else {
            return nodo1;
        }
    }

    private TDATablero.TipoDatos alfa_beta_min(TDATablero.TipoDatos nodo1, TDATablero.TipoDatos nodo2) {
        /*
        		Develve el nodo1 si el valor beta de este es menor que el valor alfa del
        		nodo2
        	*/
        if (nodo1.Beta > nodo2.Alfa) {
            nodo2.Beta = nodo2.Alfa;
            nodo2.Alfa = nodo1.Alfa;
            return nodo2;
        } else {
            return nodo1;
        }
    }

    private TDATablero.TipoDatos alfa_beta(TDATablero.TipoDatos J, int alfa, int beta) {
        // VAR
        TDALista.TipoLista Compleciones = new TDALista.TipoLista(); /* WRT */
        Runtime.Ref<TDATablero.TipoDatos> Jk = new Runtime.Ref<>(null);

        /* Nota: los nodos se crean en principio con los valores alfa y beta de su padre */
        J.Alfa = alfa;
        J.Beta = beta;
        /* Si J es terminal, devolver J */
        if ((NivelNodo(J) > MaxNivel) || tDATablero.EsMate(J.Tablero)) {
            /* Se evalua la posición */
            J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
            if ((NivelNodo(J) % 2) == 0)
                /* Para un nodo max, se actualiza el valor de alfa */
                J.Alfa = J.Evaluacion;
            else
                /* Para un nodo min, se actualiza el valor de beta */
                J.Beta = J.Evaluacion;
            return J;
        } else {
            tDALista.InicializarLista(Compleciones);
            if ((NivelNodo(J) % 2) == 0) {
                /* Juega MAX */
                /* WriteString("Juega max"); WriteLn; */
                Expandir(J, Compleciones, JugadorMax, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                /* Se generan las jugadas posibles para el jugador MAX a partir de esa posición */
                if (tDALista.Vacia(Compleciones)) {
                    /* J es terminal */
                    J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
                    return J;
                } else {
                    do {
                        /* Se toma el primer hijo Jk */
                        tDALista.Primero(Compleciones, Jk);
                        tDALista.Resto(Compleciones);
                        /* Se devuelve el hijo que tenga un mayor valor de alfa */
                        J = alfa_beta_max(J, alfa_beta(Jk.get(), J.Alfa, J.Beta));
                        /* Si en algún momento alfa es mayor o igual que beta, se poda el árbol */
                        if (J.Alfa >= J.Beta) {
                            J.Alfa = J.Beta;
                            return J;
                        }
                    } while (!tDALista.Vacia(Compleciones));
                    /* Si no hay más hijos, devolver el nodo (alfa en Evaluacion) */
                    return J;
                }
            } else {
                /* Juega MIN */
                /* WriteString("Juega min"); WriteLn; */
                Expandir(J, Compleciones, JugadorMin, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                /* Se generan las jugadas posibles para el jugador MAX a partir de esa posición */
                if (tDALista.Vacia(Compleciones)) {
                    /* J es terminal */
                    J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
                    return J;
                } else {
                    do {
                        /* Se toma el primer hijo Jk */
                        tDALista.Primero(Compleciones, Jk);
                        tDALista.Resto(Compleciones);
                        /* Se devuelve el hijo que tenga un mayor valor de alfa */
                        J = alfa_beta_min(J, alfa_beta(Jk.get(), J.Alfa, J.Beta));
                        /* Si en algún momento alfa es mayor o igual que beta, se poda el árbol */
                        if (J.Alfa >= J.Beta) {
                            J.Beta = J.Alfa;
                            return J;
                        }
                    } while (!tDALista.Vacia(Compleciones));
                    /* Si no hay más hijos, devolver el nodo (beta en Evaluacion) */
                    return J;
                }
            }
        }
    }

    private TDATablero.TipoDatos minimax(TDATablero.TipoDatos J) {
        // VAR
        TDALista.TipoLista Compleciones = new TDALista.TipoLista(); /* WRT */
        TDATablero.TipoDatos Temp = null;
        Runtime.Ref<TDATablero.TipoDatos> n = new Runtime.Ref<>(null);
        int mejor = 0;

        /* ****************************************************************************************************************
        	Devuelve la mejor jugada por el método minimax (Etiquetado MMvalor)
        ******************************************************************************************************************/
        tDALista.InicializarLista(Compleciones);
        /* Si J es terminal, devolver J */
        if ((NivelNodo(J) > MaxNivel) || tDATablero.EsMate(J.Tablero)) {
            /* Se evalua la posición */
            if ((NivelNodo(J) % 2) == 0)
                /* Para un nodo max se devuelve la función de evaluación */
                J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
            else
                /* Para un nodo min se devuelve el valor negativo de la función de evaluación */
                J.Evaluacion = -tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
            return J;
        } else {
            if ((NivelNodo(J) % 2) == 0) {
                /* Juega MAX */
                Expandir(J, Compleciones, JugadorMax, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                if (tDALista.Vacia(Compleciones)) {
                    /* J es terminal */
                    J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
                    return J;
                } else {
                    mejor = Short.MIN_VALUE /* MIN(INTEGER) */;
                    while (!tDALista.Vacia(Compleciones)) {
                        tDALista.Primero(Compleciones, n);
                        Temp = minimax(n.get());
                        tDALista.Resto(Compleciones);
                        if ((-Temp.Evaluacion) > mejor) {
                            mejor = -Temp.Evaluacion;
                            J = Temp;
                            J.Evaluacion = mejor;
                        }
                    }
                    return J;
                }
            } else {
                /* Juega MIN */
                Expandir(J, Compleciones, JugadorMin, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                if (tDALista.Vacia(Compleciones)) {
                    /* J es terminal */
                    J.Evaluacion = -tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
                    return J;
                } else {
                    mejor = Short.MIN_VALUE /* MIN(INTEGER) */;
                    while (!tDALista.Vacia(Compleciones)) {
                        tDALista.Primero(Compleciones, n);
                        Temp = minimax(n.get());
                        tDALista.Resto(Compleciones);
                        if ((-Temp.Evaluacion) > mejor) {
                            mejor = -Temp.Evaluacion;
                            J = Temp;
                            J.Evaluacion = mejor;
                        }
                    }
                    return J;
                }
            }
        }
    }

    /* ****************************************************************************************************************
    	Devuelve TRUE en caso de que el jugador del color especificado pueda hacer algún movimiento y FALSE en caso 
    	contrario.
    ******************************************************************************************************************/
    public void Jugada(/* VAR+WRT */ TDATablero.TipoTablero Tablero, TipoColor Jugador, int Metodo) {
        // VAR
        TDATablero.TipoDatos Nodo = null;
        TDATablero.TipoDatos NodoTemp = null;
        int index = 0;

        /* ****************************************************************************************************************
        	El jugador Max hace un movimiento en el Tablero.
        	Movimiento:
        		1: minimax
        		2: alfa-beta
        		3: alfa-beta y minimax
        ******************************************************************************************************************/
        if (Jugador == TipoColor.BLANCO) {
            JugadorMin = TipoColor.NEGRO;
            JugadorMax = TipoColor.BLANCO;
        } else {
            JugadorMin = TipoColor.BLANCO;
            JugadorMax = TipoColor.NEGRO;
        }
        if (Metodo != 2) {
            /* Método de minimax */
            JugadasGeneradas = 0;
            NodoTemp = CrearNodo(Tablero);
            inOut.WriteString("*** Se va a ejecutar poda alfa-beta ***");
            inOut.WriteLn();
            inOut.WriteString("Generando jugada...");
            inOut.WriteLn();
            clock.ResetClock();
            Nodo = alfa_beta(NodoTemp, -100000, 100000);
            Tiempo = (clock.UserTime() / 6);
            /* Se muestra el número de jugadas generadas */
            inOut.WriteString("Se han generado ");
            inOut.WriteInt(JugadasGeneradas, 1);
            inOut.WriteString(" jugadas generadas por el método de poda alfa-beta.");
            inOut.WriteLn();
            inOut.WriteString("Tiempo de generación por alfa-beta: ");
            inOut.WriteInt(Tiempo, 1);
            inOut.WriteString(" décimas de segundo.");
            inOut.WriteLn();
        }
        if (Metodo != 1) {
            /* Método de poda alfa-beta */
            JugadasGeneradas = 0;
            NodoTemp = CrearNodo(Tablero);
            inOut.WriteString("*** Se va a ejecutar MINIMAX ***");
            inOut.WriteLn();
            inOut.WriteString("Generando jugada...");
            inOut.WriteLn();
            clock.ResetClock();
            Nodo = minimax(NodoTemp);
            Tiempo = (clock.UserTime() / 6);
            /* Se muestra el número de jugadas generadas */
            inOut.WriteString("Se han generado ");
            inOut.WriteInt(JugadasGeneradas, 1);
            inOut.WriteString(" jugadas generadas por el método de MINIMAX.");
            inOut.WriteLn();
            inOut.WriteString("Tiempo de generación por MINIMAX: ");
            inOut.WriteInt(Tiempo, 1);
            inOut.WriteString(" décimas de segundo.");
            inOut.WriteLn();
        }
        while (Nodo.Nivel > 1) {
            Nodo = Nodo.Predecesor;
        }
        tDATablero.CopiarTablero(Nodo.Tablero, Tablero);
        /* Se libera el nodo raiz */
        EliminarNodo(NodoTemp);
        /* Se libera la memoria */
        tDALista.RecolectorDeBasura();
    }


    // Support

    private static IA instance;

    public static IA instance() {
        if (instance == null)
            new IA(); // will set 'instance'
        return instance;
    }

    // Life-cycle

    public void begin() {
    }

    public void close() {
    }

}
