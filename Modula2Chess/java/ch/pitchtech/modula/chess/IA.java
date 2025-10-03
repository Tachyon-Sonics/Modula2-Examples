package ch.pitchtech.modula.chess;

import ch.pitchtech.modula.chess.TDATablero.TipoColor;
import ch.pitchtech.modula.chess.TDATablero.TipoPieza;
import ch.pitchtech.modula.library.Clock;
import ch.pitchtech.modula.library.InOut;
import ch.pitchtech.modula.library.Storage;
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

        Nodo = new TDATablero.TipoDatos();
        tDATablero.CopiarTablero(Tablero, Nodo.Tablero);
        Nodo.Predecesor = null;
        Nodo.Nivel = 0;
        return Nodo;
    }

    private void EliminarNodo(TDATablero.TipoDatos Nodo) {
        Nodo = null;
    }

    private void CrearHijo(TDATablero.TipoDatos PtrTablero1, /* VAR */ Runtime.IRef<TDATablero.TipoDatos> PtrTablero2) {
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

        NumeroJugadas.set(0);
        tDATablero.PosicionRey(Tablero, Color, Posicion);
        Nodo = CrearNodo(Tablero);
        tDALista.InicializarLista(Lista);
        Expandir(Nodo, Lista, Color, NumeroJugadas);
        while (!tDALista.Vacia(Lista)) {
            tDALista.Primero(Lista, NodoTemp);
            tDALista.Resto(Lista);
            if (tDATablero.ReyEnJaque(NodoTemp.get().Tablero, Color))
                NumeroJugadas.set(NumeroJugadas.get() - 1);
        }
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

        Origen.x = i;
        Origen.y = j;
        switch (PtrTablero.Tablero.Casilla[i - 1][j - 1].Pieza) {
            case REY -> {
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                    Destino.x = Destino.x - 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                    Destino.x = Destino.x + 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (tDATablero.PiezaAmenazada(Destino, PtrTableroTemp.get().Tablero)) {
                        EliminarNodo(PtrTableroTemp.get());
                    } else {
                        tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                        TotalJugadas.set(TotalJugadas.get() + 1);
                    }
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
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
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.y = Destino.y - 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x - 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x + 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y - 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y - 1;
                }
            }
            case TORRE -> {
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.y = Destino.y - 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x - 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x + 1;
                }
            }
            case ALFIL -> {
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x - 1;
                    Destino.y = Destino.y - 1;
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                    Destino.x = Destino.x + 1;
                    Destino.y = Destino.y - 1;
                }
            }
            case CABALLO -> {
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 2;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x - 2;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x - 2;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x + 2;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
                Destino.x = Origen.x + 2;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                    TotalJugadas.set(TotalJugadas.get() + 1);
                }
            }
            case PEON -> {
                Destino.x = Origen.x;
                Destino.y = Origen.y + 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                    Destino.y = Destino.y + 1;
                }
                Destino.x = Origen.x;
                Destino.y = Origen.y - 1;
                while (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                    Destino.y = Destino.y - 1;
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y + 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
                Destino.x = Origen.x - 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    }
                }
                Destino.x = Origen.x + 1;
                Destino.y = Origen.y - 1;
                if (tDATablero.MovimientoLegal(Origen, Destino, PtrTablero.Tablero, false)) {
                    CrearHijo(PtrTablero, PtrTableroTemp);
                    tDATablero.MoverPieza(Origen, Destino, PtrTableroTemp.get().Tablero, false, false);
                    if (PtrTableroTemp.get().Tablero.Casilla[Destino.x - 1][Destino.y - 1].Color == TipoColor.BLANCO) {
                        if (Destino.y == PtrTableroTemp.get().Tablero.Alto) {
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
                            tDALista.AnadirFIFO(PtrTableroTemp.get(), Lista);
                            TotalJugadas.set(TotalJugadas.get() + 1);
                        }
                    } else {
                        if (Destino.y == 1) {
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

    private void Expandir(TDATablero.TipoDatos PtrTablero, /* VAR+WRT */ TDALista.TipoLista Lista, TipoColor Color, /* VAR+WRT */ Runtime.IRef<Integer> TotalJugadas) {
        // VAR
        int i = 0;
        int j = 0;

        for (j = 1; j <= PtrTablero.Tablero.Alto; j++) {
            for (i = 1; i <= PtrTablero.Tablero.Ancho; i++) {
                if (PtrTablero.Tablero.Casilla[i - 1][j - 1].Color == Color)
                    MovimientosPieza(PtrTablero, Lista, i, j, TotalJugadas);
            }
        }
    }

    private int NivelNodo(TDATablero.TipoDatos Nodo) {
        return Nodo.Nivel;
    }

    private void ImprimirJugadas(TDATablero.TipoDatos PtrTablero, boolean Debug) {
        if (PtrTablero != null) {
            ImprimirJugadas(PtrTablero.Predecesor, Debug);
            tDATablero.ImprimirTablero(PtrTablero.Tablero, Debug);
            inOut.WriteString("Nivel ");
            inOut.WriteInt(PtrTablero.Nivel, 1);
            inOut.WriteLn();
        }
    }

    private TDATablero.TipoDatos alfa_beta_max(TDATablero.TipoDatos nodo1, TDATablero.TipoDatos nodo2) {
        if (nodo1.Alfa < nodo2.Beta) {
            nodo2.Alfa = nodo2.Beta;
            nodo2.Beta = nodo1.Beta;
            return nodo2;
        } else {
            return nodo1;
        }
    }

    private TDATablero.TipoDatos alfa_beta_min(TDATablero.TipoDatos nodo1, TDATablero.TipoDatos nodo2) {
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

        J.Alfa = alfa;
        J.Beta = beta;
        if ((NivelNodo(J) > MaxNivel) || tDATablero.EsMate(J.Tablero)) {
            J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
            if ((NivelNodo(J) % 2) == 0)
                J.Alfa = J.Evaluacion;
            else
                J.Beta = J.Evaluacion;
            return J;
        } else {
            tDALista.InicializarLista(Compleciones);
            if ((NivelNodo(J) % 2) == 0) {
                Expandir(J, Compleciones, JugadorMax, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                if (tDALista.Vacia(Compleciones)) {
                    J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
                    return J;
                } else {
                    do {
                        tDALista.Primero(Compleciones, Jk);
                        tDALista.Resto(Compleciones);
                        J = alfa_beta_max(J, alfa_beta(Jk.get(), J.Alfa, J.Beta));
                        if (J.Alfa >= J.Beta) {
                            J.Alfa = J.Beta;
                            return J;
                        }
                    } while (!tDALista.Vacia(Compleciones));
                    return J;
                }
            } else {
                Expandir(J, Compleciones, JugadorMin, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                if (tDALista.Vacia(Compleciones)) {
                    J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
                    return J;
                } else {
                    do {
                        tDALista.Primero(Compleciones, Jk);
                        tDALista.Resto(Compleciones);
                        J = alfa_beta_min(J, alfa_beta(Jk.get(), J.Alfa, J.Beta));
                        if (J.Alfa >= J.Beta) {
                            J.Beta = J.Alfa;
                            return J;
                        }
                    } while (!tDALista.Vacia(Compleciones));
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

        tDALista.InicializarLista(Compleciones);
        if ((NivelNodo(J) > MaxNivel) || tDATablero.EsMate(J.Tablero)) {
            if ((NivelNodo(J) % 2) == 0)
                J.Evaluacion = tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
            else
                J.Evaluacion = -tDATablero.PuntuacionTablero(J.Tablero, JugadorMax);
            return J;
        } else {
            if ((NivelNodo(J) % 2) == 0) {
                Expandir(J, Compleciones, JugadorMax, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                if (tDALista.Vacia(Compleciones)) {
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
                Expandir(J, Compleciones, JugadorMin, new Runtime.FieldRef<>(this::getJugadasGeneradas, this::setJugadasGeneradas));
                if (tDALista.Vacia(Compleciones)) {
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

    public void Jugada(/* VAR+WRT */ TDATablero.TipoTablero Tablero, TipoColor Jugador, int Metodo) {
        // VAR
        TDATablero.TipoDatos Nodo = null;
        TDATablero.TipoDatos NodoTemp = null;
        int index = 0;

        if (Jugador == TipoColor.BLANCO) {
            JugadorMin = TipoColor.NEGRO;
            JugadorMax = TipoColor.BLANCO;
        } else {
            JugadorMin = TipoColor.BLANCO;
            JugadorMax = TipoColor.NEGRO;
        }
        if (Metodo != 2) {
            JugadasGeneradas = 0;
            NodoTemp = CrearNodo(Tablero);
            inOut.WriteString("*** Se va a ejecutar poda alfa-beta ***");
            inOut.WriteLn();
            inOut.WriteString("Generando jugada...");
            inOut.WriteLn();
            clock.ResetClock();
            Nodo = alfa_beta(NodoTemp, -100000, 100000);
            Tiempo = (clock.UserTime() / 6);
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
            JugadasGeneradas = 0;
            NodoTemp = CrearNodo(Tablero);
            inOut.WriteString("*** Se va a ejecutar MINIMAX ***");
            inOut.WriteLn();
            inOut.WriteString("Generando jugada...");
            inOut.WriteLn();
            clock.ResetClock();
            Nodo = minimax(NodoTemp);
            Tiempo = (clock.UserTime() / 6);
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
        EliminarNodo(NodoTemp);
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
