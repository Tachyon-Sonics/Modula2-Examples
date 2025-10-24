package ch.pitchtech.modula.chess;

import ch.pitchtech.modula.library.InOut;
import ch.pitchtech.modula.library.Storage;
import ch.pitchtech.modula.library.TextIO;
import ch.pitchtech.modula.runtime.Runtime;


public class TDATablero {

    // Imports
    private final IA iA;
    private final InOut inOut;
    private final TextIO textIO;


    private TDATablero() {
        instance = this; // Set early to handle circular dependencies
        iA = IA.instance();
        inOut = InOut.instance();
        textIO = TextIO.instance();
    }


    // CONST

    public static final int MAXALTO = 8;
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★*/
    public static final int MAXANCHO = 8;
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★    Funciones generales para el manejo de tableros     **************************/
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★*/
    public static final int DespX = 0;
    public static final int DespY = 60;
    public static final int TamanhoCasilla = 60;


    // TYPE

    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    	Determina si el jugador especificado por Color está en jaque mate.
    	Devuelve:
    		0: Si no hay jaque mate ni el rey está ahogado;
    		1: Si es jaque mate;
    		2: Si el rey está ahogado;
    ******************************************************************************************************************/
    public static class TipoPosicion { // RECORD

        public int x;
        public int y;


        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }


        public void copyFrom(TipoPosicion other) {
            this.x = other.x;
            this.y = other.y;
        }

        public TipoPosicion newCopy() {
            TipoPosicion copy = new TipoPosicion();
            copy.copyFrom(this);
            return copy;
        }

    }

    public static enum TipoColor {
        NEGRO,
        BLANCO;
    }

    /* Si se puede hacer alguna jugada */
    public static enum TipoPieza {
        VACIA,
        PEON,
        CABALLO,
        ALFIL,
        TORRE,
        DAMA,
        REY;
    }

    public static class TipoCasilla { // RECORD

        public TipoColor Color;
        public TipoPieza Pieza;


        public TipoColor getColor() {
            return this.Color;
        }

        public void setColor(TipoColor Color) {
            this.Color = Color;
        }

        public TipoPieza getPieza() {
            return this.Pieza;
        }

        public void setPieza(TipoPieza Pieza) {
            this.Pieza = Pieza;
        }


        public void copyFrom(TipoCasilla other) {
            this.Color = other.Color;
            this.Pieza = other.Pieza;
        }

        public TipoCasilla newCopy() {
            TipoCasilla copy = new TipoCasilla();
            copy.copyFrom(this);
            return copy;
        }

    }

    /* Si no se pueden hacer jugadas */
    /* No se puede hacer ningún movimiento pero el rey no está en jaque */
    public static class TipoMovimiento { // RECORD

        public TipoPosicion Origen = new TipoPosicion();
        public TipoPosicion Destino = new TipoPosicion();


        public TipoPosicion getOrigen() {
            return this.Origen;
        }

        public void setOrigen(TipoPosicion Origen) {
            this.Origen = Origen;
        }

        public TipoPosicion getDestino() {
            return this.Destino;
        }

        public void setDestino(TipoPosicion Destino) {
            this.Destino = Destino;
        }


        public void copyFrom(TipoMovimiento other) {
            this.Origen.copyFrom(other.Origen);
            this.Destino.copyFrom(other.Destino);
        }

        public TipoMovimiento newCopy() {
            TipoMovimiento copy = new TipoMovimiento();
            copy.copyFrom(this);
            return copy;
        }

    }

    public static class TipoTablero { // RECORD

        public int Alto;
        public int Ancho;
        public TipoCasilla[][] Casilla = Runtime.initArray(new TipoCasilla[MAXALTO][MAXANCHO]);
        public TipoMovimiento UltimoMovimiento = new TipoMovimiento();
        public boolean ReyBlancoMovido;
        public boolean TorreIBlancaMovida;
        public boolean TorreDBlancaMovida;
        public boolean ReyNegroMovido;
        public boolean TorreINegraMovida;
        public boolean TorreDNegraMovida;
        public TipoCasilla UltimaCaptura = new TipoCasilla();


        public int getAlto() {
            return this.Alto;
        }

        public void setAlto(int Alto) {
            this.Alto = Alto;
        }

        public int getAncho() {
            return this.Ancho;
        }

        public void setAncho(int Ancho) {
            this.Ancho = Ancho;
        }

        public TipoCasilla[][] getCasilla() {
            return this.Casilla;
        }

        public void setCasilla(TipoCasilla[][] Casilla) {
            this.Casilla = Casilla;
        }

        public TipoMovimiento getUltimoMovimiento() {
            return this.UltimoMovimiento;
        }

        public void setUltimoMovimiento(TipoMovimiento UltimoMovimiento) {
            this.UltimoMovimiento = UltimoMovimiento;
        }

        public boolean isReyBlancoMovido() {
            return this.ReyBlancoMovido;
        }

        public void setReyBlancoMovido(boolean ReyBlancoMovido) {
            this.ReyBlancoMovido = ReyBlancoMovido;
        }

        public boolean isTorreIBlancaMovida() {
            return this.TorreIBlancaMovida;
        }

        public void setTorreIBlancaMovida(boolean TorreIBlancaMovida) {
            this.TorreIBlancaMovida = TorreIBlancaMovida;
        }

        public boolean isTorreDBlancaMovida() {
            return this.TorreDBlancaMovida;
        }

        public void setTorreDBlancaMovida(boolean TorreDBlancaMovida) {
            this.TorreDBlancaMovida = TorreDBlancaMovida;
        }

        public boolean isReyNegroMovido() {
            return this.ReyNegroMovido;
        }

        public void setReyNegroMovido(boolean ReyNegroMovido) {
            this.ReyNegroMovido = ReyNegroMovido;
        }

        public boolean isTorreINegraMovida() {
            return this.TorreINegraMovida;
        }

        public void setTorreINegraMovida(boolean TorreINegraMovida) {
            this.TorreINegraMovida = TorreINegraMovida;
        }

        public boolean isTorreDNegraMovida() {
            return this.TorreDNegraMovida;
        }

        public void setTorreDNegraMovida(boolean TorreDNegraMovida) {
            this.TorreDNegraMovida = TorreDNegraMovida;
        }

        public TipoCasilla getUltimaCaptura() {
            return this.UltimaCaptura;
        }

        public void setUltimaCaptura(TipoCasilla UltimaCaptura) {
            this.UltimaCaptura = UltimaCaptura;
        }


        public void copyFrom(TipoTablero other) {
            this.Alto = other.Alto;
            this.Ancho = other.Ancho;
            this.Casilla = Runtime.copyOf(true, other.Casilla);
            this.UltimoMovimiento.copyFrom(other.UltimoMovimiento);
            this.ReyBlancoMovido = other.ReyBlancoMovido;
            this.TorreIBlancaMovida = other.TorreIBlancaMovida;
            this.TorreDBlancaMovida = other.TorreDBlancaMovida;
            this.ReyNegroMovido = other.ReyNegroMovido;
            this.TorreINegraMovida = other.TorreINegraMovida;
            this.TorreDNegraMovida = other.TorreDNegraMovida;
            this.UltimaCaptura.copyFrom(other.UltimaCaptura);
        }

        public TipoTablero newCopy() {
            TipoTablero copy = new TipoTablero();
            copy.copyFrom(this);
            return copy;
        }

    }

    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    	Devuelve TRUE en caso de haberse comido un rey y FALSE en caso contrario.
    ******************************************************************************************************************/

    public static class TipoDatos { // RECORD

        public TipoTablero Tablero = new TipoTablero();
        public TipoDatos Predecesor /* POINTER */;
        public int Nivel;
        public int Alfa;
        public int Beta;
        public int Evaluacion;


        public TipoTablero getTablero() {
            return this.Tablero;
        }

        public void setTablero(TipoTablero Tablero) {
            this.Tablero = Tablero;
        }

        public TipoDatos getPredecesor() {
            return this.Predecesor;
        }

        public void setPredecesor(TipoDatos Predecesor) {
            this.Predecesor = Predecesor;
        }

        public int getNivel() {
            return this.Nivel;
        }

        public void setNivel(int Nivel) {
            this.Nivel = Nivel;
        }

        public int getAlfa() {
            return this.Alfa;
        }

        public void setAlfa(int Alfa) {
            this.Alfa = Alfa;
        }

        public int getBeta() {
            return this.Beta;
        }

        public void setBeta(int Beta) {
            this.Beta = Beta;
        }

        public int getEvaluacion() {
            return this.Evaluacion;
        }

        public void setEvaluacion(int Evaluacion) {
            this.Evaluacion = Evaluacion;
        }


        public void copyFrom(TipoDatos other) {
            this.Tablero.copyFrom(other.Tablero);
            this.Predecesor = other.Predecesor;
            this.Nivel = other.Nivel;
            this.Alfa = other.Alfa;
            this.Beta = other.Beta;
            this.Evaluacion = other.Evaluacion;
        }

        public TipoDatos newCopy() {
            TipoDatos copy = new TipoDatos();
            copy.copyFrom(this);
            return copy;
        }

    }


    // PROCEDURE

    public int JaqueMate(TipoTablero Tablero, TipoColor Color) {
        if (iA.JugadasPosibles(Tablero, Color)) {
            return 0;
        } else {
            if (ReyEnJaque(Tablero, Color)) {
                return 1;
            } else {
                inOut.WriteString("Rey ahogado");
                inOut.WriteLn();
                return 2;
            }
        }
    }

    public boolean EsMate(TipoTablero Tablero) {
        if (Tablero.UltimaCaptura.Pieza == TipoPieza.REY)
            return true;
        else
            return false;
    }

    public void CopiarTablero(TipoTablero Tablero1, /* VAR */ TipoTablero Tablero2) {
        // VAR
        int i = 0;
        int j = 0;

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Copia el contenido del primer tablero al segundo. 
           Los dos tableros deben tener las mismas dimensiones. (Precondición)
        ******************************************************************************************************************/
        Tablero2.Alto = Tablero1.Alto;
        Tablero2.Ancho = Tablero1.Ancho;
        for (j = 1; j <= Tablero1.Alto; j++) {
            for (i = 1; i <= Tablero1.Ancho; i++) {
                Tablero2.Casilla[i - 1][j - 1].Pieza = Tablero1.Casilla[i - 1][j - 1].Pieza;
                Tablero2.Casilla[i - 1][j - 1].Color = Tablero1.Casilla[i - 1][j - 1].Color;
            }
        }
        Tablero2.UltimoMovimiento.Origen.x = Tablero1.UltimoMovimiento.Origen.x;
        Tablero2.UltimoMovimiento.Origen.y = Tablero1.UltimoMovimiento.Origen.y;
        Tablero2.UltimoMovimiento.Destino.x = Tablero1.UltimoMovimiento.Destino.x;
        Tablero2.UltimoMovimiento.Destino.y = Tablero1.UltimoMovimiento.Destino.y;
        Tablero2.ReyBlancoMovido = Tablero1.ReyBlancoMovido;
        Tablero2.TorreIBlancaMovida = Tablero1.TorreIBlancaMovida;
        Tablero2.TorreDBlancaMovida = Tablero1.TorreDBlancaMovida;
        Tablero2.ReyNegroMovido = Tablero1.ReyNegroMovido;
        Tablero2.TorreINegraMovida = Tablero1.TorreINegraMovida;
        Tablero2.TorreDNegraMovida = Tablero1.TorreDNegraMovida;
        Tablero2.UltimaCaptura.copyFrom(Tablero1.UltimaCaptura);
    }

    public boolean LeerTablero(/* VAR */ TipoTablero Tablero, /* WRT */ String _Archivo) {
        Runtime.Ref<String> Archivo = new Runtime.Ref<>(_Archivo);

        // VAR
        Runtime.Ref<TextIO.File> F = new Runtime.Ref<>(null);
        int i = 0;
        int j = 0;
        Runtime.Ref<Character> Temp = new Runtime.Ref<>((char) 0);
        Runtime.Ref<Integer> Temp2 = new Runtime.Ref<>(0);

        /* Índices para la matriz */
        /* Almacenamiento temporal para el tipo de pieza */
        /* Comprobar si existe el archivo y se puede escribir en él */
        if (!textIO.Accessible(Archivo, false))
            return true;
        /* Abrir archivo */
        textIO.OpenInput(F, Archivo.get());
        /* Leer alto y ancho */
        textIO.GetInt(F.get(), new Runtime.FieldExprRef<>(Tablero, TipoTablero::getAlto, TipoTablero::setAlto));
        /* Comprueba que el alto no excede el tamaño máximo */
        if (Tablero.Alto > MAXALTO)
            return true;
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        textIO.GetInt(F.get(), new Runtime.FieldExprRef<>(Tablero, TipoTablero::getAncho, TipoTablero::setAncho));
        /* Comprueba que el ancho no excede el tamaño máximo */
        if (Tablero.Alto > MAXANCHO)
            return true;
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Leer casillas */
        for (j = (Tablero.Alto); j >= 1; j -= 1) {
            for (i = 1; i <= Tablero.Ancho; i++) {
                textIO.GetChar(F.get(), Temp);
                switch (Temp.get()) {
                    case 'R' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.REY;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.NEGRO;
                    }
                    case 'D' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.DAMA;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.NEGRO;
                    }
                    case 'T' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.TORRE;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.NEGRO;
                    }
                    case 'A' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.ALFIL;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.NEGRO;
                    }
                    case 'C' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.CABALLO;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.NEGRO;
                    }
                    case 'P' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.PEON;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.NEGRO;
                    }
                    case 'r' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.REY;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    case 'd' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.DAMA;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    case 't' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.TORRE;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    case 'a' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.ALFIL;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    case 'c' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.CABALLO;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    case 'p' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.PEON;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    case 'V' -> {
                        Tablero.Casilla[i - 1][j - 1].Pieza = TipoPieza.VACIA;
                        Tablero.Casilla[i - 1][j - 1].Color = TipoColor.BLANCO;
                    }
                    default -> {
                        return true;
                    }
                }
            }
            textIO.GetChar(F.get(), Temp);
        }
        /* Lee el salto de linea */
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Leer coordenadas del último movimiento */
        textIO.GetInt(F.get(), new Runtime.FieldExprRef<>(Tablero.UltimoMovimiento.Origen, TipoPosicion::getX, TipoPosicion::setX));
        textIO.GetInt(F.get(), new Runtime.FieldExprRef<>(Tablero.UltimoMovimiento.Origen, TipoPosicion::getY, TipoPosicion::setY));
        textIO.GetInt(F.get(), new Runtime.FieldExprRef<>(Tablero.UltimoMovimiento.Destino, TipoPosicion::getX, TipoPosicion::setX));
        textIO.GetInt(F.get(), new Runtime.FieldExprRef<>(Tablero.UltimoMovimiento.Destino, TipoPosicion::getY, TipoPosicion::setY));
        if ((Tablero.UltimoMovimiento.Origen.x > MAXANCHO) || (Tablero.UltimoMovimiento.Origen.y > MAXALTO) || (Tablero.UltimoMovimiento.Destino.x > MAXANCHO) || (Tablero.UltimoMovimiento.Destino.y > MAXALTO) || (Tablero.UltimoMovimiento.Origen.x < 1) || (Tablero.UltimoMovimiento.Origen.y < 1) || (Tablero.UltimoMovimiento.Destino.x < 1) || (Tablero.UltimoMovimiento.Destino.y < 1))
            return true;
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Leer si los reyes y las torres han sido movidos */
        textIO.GetInt(F.get(), Temp2);
        if (Temp2.get() == 1)
            Tablero.ReyBlancoMovido = true;
        else if (Temp2.get() == 0)
            Tablero.ReyBlancoMovido = false;
        else
            return true;
        textIO.GetInt(F.get(), Temp2);
        if (Temp2.get() == 1)
            Tablero.TorreIBlancaMovida = true;
        else if (Temp2.get() == 0)
            Tablero.TorreIBlancaMovida = false;
        else
            return true;
        textIO.GetInt(F.get(), Temp2);
        if (Temp2.get() == 1)
            Tablero.TorreDBlancaMovida = true;
        else if (Temp2.get() == 0)
            Tablero.TorreDBlancaMovida = false;
        else
            return true;
        textIO.GetInt(F.get(), Temp2);
        if (Temp2.get() == 1)
            Tablero.ReyNegroMovido = true;
        else if (Temp2.get() == 0)
            Tablero.ReyNegroMovido = false;
        else
            return true;
        textIO.GetInt(F.get(), Temp2);
        if (Temp2.get() == 1)
            Tablero.TorreINegraMovida = true;
        else if (Temp2.get() == 0)
            Tablero.TorreINegraMovida = false;
        else
            return true;
        textIO.GetInt(F.get(), Temp2);
        if (Temp2.get() == 1)
            Tablero.TorreDNegraMovida = true;
        else if (Temp2.get() == 0)
            Tablero.TorreDNegraMovida = false;
        else
            return true;
        /* Lee el salto de linea */
        textIO.GetChar(F.get(), Temp);
        /* Se cierra el archivo */
        textIO.Close(F.get());
        /* Se comprueba que haya dos reyes (uno de cada color) */
        /* POR HACER */
        /* Se añaden los valores predeterminados para la última captura */
        Tablero.UltimaCaptura.Pieza = TipoPieza.VACIA;
        Tablero.UltimaCaptura.Color = TipoColor.BLANCO;
        /* Si se ha llegado a este punto, no ha habido error */
        return false;
    }

    public void MoverPieza(TipoPosicion Posicion1, TipoPosicion Posicion2, /* VAR */ TipoTablero Tablero, boolean Humano, boolean SDL) {
        // VAR
        TipoPosicion OrigenTorre = new TipoPosicion();
        TipoPosicion DestinoTorre = new TipoPosicion();
        boolean PromocionPeon = false;

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
          Mueve la pieza de la casilla Posicion1 a la casilla Posicion2 sin hacer comprobaciones.
          Las comprobaciones deben hacerse anteriormente (precondición)
        *******************************************************************************************************************/
        /* Guardar UltimaCaptura si procede*/
        /* Si la posición de destino no está vacía, se trata de una captura y se guarda*/
        if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza != TipoPieza.VACIA) {
            Tablero.UltimaCaptura.Pieza = Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza;
            Tablero.UltimaCaptura.Color = Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Color;
        }
        /* Si se va a mover el rey o una torre */
        PromocionPeon = false;
        if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza == TipoPieza.REY) {
            if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color == TipoColor.BLANCO) {
                Tablero.ReyBlancoMovido = true;
                if ((Posicion1.x + 2) == Posicion2.x) {
                    OrigenTorre.x = Tablero.Ancho;
                    OrigenTorre.y = 1;
                    DestinoTorre.x = Posicion2.x - 1;
                    DestinoTorre.y = 1;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Pieza = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Color = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza = TipoPieza.VACIA;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color = TipoColor.BLANCO;
                    Tablero.TorreDBlancaMovida = true;
                } else if (((Posicion1.x - 2) == Posicion2.x)) {
                    OrigenTorre.x = 1;
                    OrigenTorre.y = 1;
                    DestinoTorre.x = Posicion2.x + 1;
                    DestinoTorre.y = 1;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Pieza = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Color = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza = TipoPieza.VACIA;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color = TipoColor.BLANCO;
                    Tablero.TorreIBlancaMovida = true;
                }
            } else {
                Tablero.ReyNegroMovido = true;
                if ((Posicion1.x + 2) == Posicion2.x) {
                    OrigenTorre.x = Tablero.Ancho;
                    OrigenTorre.y = Tablero.Alto;
                    DestinoTorre.x = Posicion2.x - 1;
                    DestinoTorre.y = Tablero.Alto;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Pieza = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Color = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza = TipoPieza.VACIA;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color = TipoColor.NEGRO;
                    Tablero.TorreDNegraMovida = true;
                } else if (((Posicion1.x - 2) == Posicion2.x)) {
                    OrigenTorre.x = 1;
                    OrigenTorre.y = Tablero.Alto;
                    DestinoTorre.x = Posicion2.x + 1;
                    DestinoTorre.y = Tablero.Alto;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Pieza = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza;
                    Tablero.Casilla[DestinoTorre.x - 1][DestinoTorre.y - 1].Color = Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Pieza = TipoPieza.VACIA;
                    Tablero.Casilla[OrigenTorre.x - 1][OrigenTorre.y - 1].Color = TipoColor.NEGRO;
                    Tablero.TorreINegraMovida = true;
                }
            }
        } else if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza == TipoPieza.TORRE) {
            if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color == TipoColor.BLANCO) {
                if ((Posicion1.x == 1) && (Posicion1.y == 1))
                    Tablero.TorreIBlancaMovida = true;
                else if ((Posicion1.x == Tablero.Ancho) && (Posicion1.y == 1))
                    Tablero.TorreDBlancaMovida = true;
            } else {
                if ((Posicion1.x == 1) && (Posicion1.y == Tablero.Alto))
                    Tablero.TorreINegraMovida = true;
                else if ((Posicion1.x == Tablero.Ancho) && (Posicion1.y == Tablero.Alto))
                    Tablero.TorreDNegraMovida = true;
            }
        } else if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza == TipoPieza.PEON) {
            if ((Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza == TipoPieza.VACIA) && (Posicion1.x != Posicion2.x)) {
                Tablero.Casilla[Posicion2.x - 1][Posicion1.y - 1].Pieza = TipoPieza.VACIA;
                Tablero.Casilla[Posicion2.x - 1][Posicion1.y - 1].Color = TipoColor.BLANCO;
            }
            if (((Posicion2.y == Tablero.Alto) && (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color == TipoColor.BLANCO)) || ((Posicion2.y == 1) && (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color == TipoColor.NEGRO)))
                PromocionPeon = true;
        }
        /* Promoción del peón */
        /* Copiar la casilla de origen a la casilla de destino */
        if (PromocionPeon) {
            if (Humano)
                ElegirPieza(new Runtime.FieldExprRef<>(Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1], TipoCasilla::getPieza, TipoCasilla::setPieza));
            Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Color = Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color;
        } else {
            Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza = Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza;
            Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Color = Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color;
        }
        /* Juega la máquina */
        /* Vaciar la casilla de origen */
        Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza = TipoPieza.VACIA;
        Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color = TipoColor.BLANCO;
        /* Guardar movimiento */
        Tablero.UltimoMovimiento.Origen.x = Posicion1.x;
        Tablero.UltimoMovimiento.Origen.y = Posicion1.y;
        Tablero.UltimoMovimiento.Destino.x = Posicion2.x;
        Tablero.UltimoMovimiento.Destino.y = Posicion2.y;
    }

    public boolean PiezaAmenazada(TipoPosicion Posicion, TipoTablero Tablero) {
        // VAR
        TipoPosicion Actual = new TipoPosicion();

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Devuelve el valor TRUE si alguna pieza está amenazando la posición suministrada como parámetro y FALSE en 
           caso contrario.
        ******************************************************************************************************************/
        /* Movimiento a la izquierda */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.x = Actual.x - 1;
        } while (!((Actual.x <= 1) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento arriba a la izquierda */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.x = Actual.x - 1;
            Actual.y = Actual.y + 1;
        } while (!((Actual.x <= 1) || (Actual.y >= Tablero.Alto) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento arriba */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.y = Actual.y + 1;
        } while (!((Actual.y >= Tablero.Alto) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento arriba a la derecha */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.x = Actual.x + 1;
            Actual.y = Actual.y + 1;
        } while (!((Actual.x >= Tablero.Ancho) || (Actual.y >= Tablero.Alto) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento a la derecha */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.x = Actual.x + 1;
        } while (!((Actual.x >= Tablero.Ancho) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento abajo a la derecha */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.x = Actual.x + 1;
            Actual.y = Actual.y - 1;
        } while (!((Actual.x >= Tablero.Ancho) || (Actual.y <= 1) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento abajo*/
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.y = Actual.y - 1;
        } while (!((Actual.y <= 1) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimiento abajo a la derecha */
        /* Se inicializa la posición a comprobar */
        Actual.x = Posicion.x;
        Actual.y = Posicion.y;
        do {
            Actual.x = Actual.x - 1;
            Actual.y = Actual.y - 1;
        } while (!((Actual.x <= 1) || (Actual.y <= 1) || (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)));
        /* Se mueve una posición */
        if (MovimientoLegal(Actual, Posicion, Tablero, false) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA))
            return true;
        /* Si la última posición comprobada tiene una pieza y esta pieza puede acceder a la posición de origen */
        /* Movimientos de caballo */
        if ((Posicion.y + 2) <= Tablero.Alto) {
            Actual.y = Posicion.y + 2;
            if (Posicion.x < Tablero.Ancho) {
                Actual.x = Posicion.x + 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
            if (Posicion.x > 1) {
                Actual.x = Posicion.x - 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
        }
        /* Arriba-Arriba-Izquierda */
        if ((Posicion.y - 2) >= 1) {
            Actual.y = Posicion.y - 2;
            if (Posicion.x < Tablero.Ancho) {
                Actual.x = Posicion.x + 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
            if (Posicion.x > 1) {
                Actual.x = Posicion.x - 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
        }
        /* Abajo-Abajo-Izquierda */
        if ((Posicion.x - 2) >= 1) {
            Actual.x = Posicion.x - 2;
            if (Posicion.y < Tablero.Alto) {
                Actual.y = Posicion.y + 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
            if (Posicion.y > 1) {
                Actual.y = Posicion.y - 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
        }
        /* Izquierda-Izquierda-Abajo */
        if ((Posicion.x + 2) <= Tablero.Ancho) {
            Actual.x = Posicion.x + 2;
            if (Posicion.y < Tablero.Alto) {
                Actual.y = Posicion.y + 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
            if (Posicion.y > 1) {
                Actual.y = Posicion.y - 1;
                if ((Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza == TipoPieza.CABALLO) && (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Color != Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color))
                    return true;
            }
        }
        /* El llegar hasta este punto sin haber encontrado ninguna pieza que amenace la posición original supone
        	   que la pieza no está amenazada */
        return false;
    }

    private boolean PiezaAmenazada2(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero) {
        // VAR
        TipoTablero TableroTemp = new TipoTablero(); /* WRT */

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        	Devuelve el valor TRUE si está amenazada la pieza en la posición 1 al moverla y FALSE caso contrario.
        ******************************************************************************************************************/
        CopiarTablero(Tablero, TableroTemp);
        MoverPieza(Posicion1, Posicion2, TableroTemp, false, false);
        if (PiezaAmenazada(Posicion2, TableroTemp))
            return true;
        else
            return false;
    }

    private boolean JugadaEnTablero(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero) {
        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Devuelve el valor TRUE si las posiciones de la jugada no exceden los límites del tablero y
           devuelve FALSE en caso contrario.
        ******************************************************************************************************************/
        if ((Posicion1.x >= 1) && (Posicion1.x <= Tablero.Ancho) && (Posicion1.y >= 1) && (Posicion1.y <= Tablero.Alto) && (Posicion2.x >= 1) && (Posicion2.x <= Tablero.Ancho) && (Posicion2.y >= 1) && (Posicion2.y <= Tablero.Alto))
            return true;
        else
            return false;
    }

    /* Las posiciones están dentro de los límites del tablero */
    private boolean PosicionesIguales(TipoPosicion Posicion1, TipoPosicion Posicion2) {
        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        Si las posiciones son iguales, devuelve TRUE, en caso contrario devuelve FALSE
        
        Se requieren las comprobaciones anteriores de (precondiciones):
        -Comprobar que las direcciones no exceden los límites del tablero.
        ******************************************************************************************************************/
        if ((Posicion1.x == Posicion2.x) && (Posicion1.y == Posicion2.y))
            return true;
        else
            return false;
    }

    private boolean DireccionVacia(TipoPosicion Posicion, TipoTablero Tablero) {
        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Devuelve TRUE si no hay una pieza en la posicion, en caso contrario devuelve FALSE.
        Se requieren las comprobaciones anteriores de (precondiciones):
        -Comprobar que la dirección no excede los límites del tablero.
        ******************************************************************************************************************/
        if (Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Pieza == TipoPieza.VACIA)
            return true;
        else
            return false;
    }

    private boolean MismoColor(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero) {
        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Función que comprueba si dos piezas son del mismo color.
           Devuelve TRUE en caso afirmativo y FALSE en caso contrario.
        
        Se requieren las comprobaciones anteriores de (precondiciones):
        -Comprobar que las direcciones no exceden los límites del tablero.
        ******************************************************************************************************************/
        if (!DireccionVacia(Posicion2, Tablero) && (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color == Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Color))
            return true;
        else
            return false;
    }

    private boolean CapturaAlPaso(TipoTablero Tablero, TipoPosicion Origen, TipoPosicion Destino) {
        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
          Función que comprueba si un peon puede comer al paso.
          Devuelve TRUE en caso afirmativo y FALSE en caso negativo.
          Origen y Destino son las posiciones de origen y destino respectivamente del peón que realiza la captura.
        ******************************************************************************************************************/
        if (Tablero.Casilla[Destino.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) {
            if ((Tablero.UltimoMovimiento.Destino.x == Destino.x) && (Tablero.UltimoMovimiento.Destino.y == Origen.y) && (Tablero.UltimoMovimiento.Origen.x == Destino.x) && (Tablero.UltimoMovimiento.Origen.y == Destino.y))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    /* No se puede capturar al paso */
    private boolean Enroque(TipoTablero Tablero, TipoPosicion Posicion, int Tipo) {
        // VAR
        TipoPosicion PosicionTorreBlancaI = new TipoPosicion();
        TipoPosicion PosicionTorreBlancaD = new TipoPosicion();
        TipoPosicion PosicionTorreNegraI = new TipoPosicion();
        TipoPosicion PosicionTorreNegraD = new TipoPosicion();

        PosicionTorreBlancaI.x = 1;
        PosicionTorreBlancaI.y = 1;
        PosicionTorreBlancaD.x = Tablero.Ancho;
        PosicionTorreBlancaD.y = 1;
        PosicionTorreNegraI.x = 1;
        PosicionTorreNegraI.y = Tablero.Alto;
        PosicionTorreNegraD.x = Tablero.Ancho;
        PosicionTorreNegraD.y = Tablero.Alto;
        if (Tablero.Casilla[Posicion.x - 1][Posicion.y - 1].Color == TipoColor.BLANCO) {
            if (!Tablero.ReyBlancoMovido && !ReyEnJaque(Tablero, TipoColor.BLANCO)) {
                if ((Tipo == 1) && !Tablero.TorreIBlancaMovida && !PiezaAmenazada(PosicionTorreBlancaI, Tablero))
                    return true;
                else if ((Tipo == 2) && !Tablero.TorreDBlancaMovida && !PiezaAmenazada(PosicionTorreBlancaD, Tablero))
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        } else {
            if (!Tablero.ReyNegroMovido && !ReyEnJaque(Tablero, TipoColor.NEGRO)) {
                if ((Tipo == 1) && !Tablero.TorreINegraMovida && !PiezaAmenazada(PosicionTorreNegraI, Tablero))
                    return true;
                else if ((Tipo == 2) && !Tablero.TorreDNegraMovida && !PiezaAmenazada(PosicionTorreNegraD, Tablero))
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        }
    }

    /* Movimiento ilegal */
    private boolean DireccionValida(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero) {
        switch (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza) {
            case PEON -> {
                if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Color == TipoColor.BLANCO) {
                    if (Posicion1.x == Posicion2.x) {
                        if ((Posicion1.y + 1) == Posicion2.y) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza == TipoPieza.VACIA)
                                return true;
                            else
                                return false;
                        } else if (((Posicion1.y + 2) == Posicion2.y) && (Posicion1.y == 2)) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza == TipoPieza.VACIA)
                                return true;
                            else
                                return false;
                        } else {
                            return false;
                        }
                    } else if (((Posicion1.y + 1) == Posicion2.y)) {
                        if ((Posicion1.x + 1) == Posicion2.x) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza != TipoPieza.VACIA)
                                return true;
                            else if (CapturaAlPaso(Tablero, Posicion1, Posicion2))
                                return true;
                            else
                                return false;
                        } else if (((Posicion1.x - 1) == Posicion2.x)) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza != TipoPieza.VACIA)
                                return true;
                            else if (CapturaAlPaso(Tablero, Posicion1, Posicion2))
                                return true;
                            else
                                return false;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    if (Posicion1.x == Posicion2.x) {
                        if ((Posicion1.y - 1) == Posicion2.y) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza == TipoPieza.VACIA)
                                return true;
                            else
                                return false;
                        } else if (((Posicion1.y - 2) == Posicion2.y) && (Posicion1.y == (Tablero.Alto - 1))) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza == TipoPieza.VACIA)
                                return true;
                            else
                                return false;
                        } else {
                            return false;
                        }
                    } else if (((Posicion1.y - 1) == Posicion2.y)) {
                        if ((Posicion1.x + 1) == Posicion2.x) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza != TipoPieza.VACIA)
                                return true;
                            else if (CapturaAlPaso(Tablero, Posicion1, Posicion2))
                                return true;
                            else
                                return false;
                        } else if (((Posicion1.x - 1) == Posicion2.x)) {
                            if (Tablero.Casilla[Posicion2.x - 1][Posicion2.y - 1].Pieza != TipoPieza.VACIA)
                                return true;
                            else if (CapturaAlPaso(Tablero, Posicion1, Posicion2))
                                return true;
                            else
                                return false;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            case TORRE -> {
                if (Posicion1.x == Posicion2.x)
                    return true;
                else if (Posicion1.y == Posicion2.y)
                    return true;
                else
                    return false;
            }
            case ALFIL -> {
                if (((Posicion2.x - Posicion1.x) == (Posicion2.y - Posicion1.y)) || ((Posicion2.x - Posicion1.x) == (-(Posicion2.y - Posicion1.y))))
                    return true;
                else
                    return false;
            }
            case CABALLO -> {
                if ((Posicion1.x + 2) == Posicion2.x) {
                    if ((Posicion1.y + 1) == Posicion2.y)
                        return true;
                    else if ((Posicion1.y - 1) == Posicion2.y)
                        return true;
                    else
                        return false;
                } else if ((Posicion1.x - 2) == Posicion2.x) {
                    if ((Posicion1.y + 1) == Posicion2.y)
                        return true;
                    else if ((Posicion1.y - 1) == Posicion2.y)
                        return true;
                    else
                        return false;
                } else if ((Posicion1.y + 2) == Posicion2.y) {
                    if ((Posicion1.x + 1) == Posicion2.x)
                        return true;
                    else if ((Posicion1.x - 1) == Posicion2.x)
                        return true;
                    else
                        return false;
                } else if ((Posicion1.y - 2) == Posicion2.y) {
                    if ((Posicion1.x + 1) == Posicion2.x)
                        return true;
                    else if ((Posicion1.x - 1) == Posicion2.x)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            }
            case DAMA -> {
                if (Posicion1.x == Posicion2.x)
                    return true;
                else if (Posicion1.y == Posicion2.y)
                    return true;
                else if (((Posicion2.x - Posicion1.x) == (Posicion2.y - Posicion1.y)) || ((Posicion2.x - Posicion1.x) == (-(Posicion2.y - Posicion1.y))))
                    return true;
                else
                    return false;
            }
            case REY -> {
                if ((((Posicion2.x - Posicion1.x) <= 1) && ((Posicion2.x - Posicion1.x) >= -1)) && (((Posicion2.y - Posicion1.y) <= 1) && ((Posicion2.y - Posicion1.y) >= -1))) {
                    return true;
                } else if (((Posicion1.x - 2) == Posicion2.x) && (Posicion1.y == Posicion2.y)) {
                    if (Enroque(Tablero, Posicion1, 1))
                        return true;
                    else
                        return false;
                } else if (((Posicion1.x + 2) == Posicion2.x) && (Posicion1.y == Posicion2.y)) {
                    if (Enroque(Tablero, Posicion1, 2))
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }

    /* Movimiento ilegal */
    private boolean CaminoDespejado(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero) {
        // VAR
        TipoPosicion Actual = new TipoPosicion();
        int Indice = 0;

        if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza != TipoPieza.CABALLO) {
            if (Tablero.Casilla[Posicion1.x - 1][Posicion1.y - 1].Pieza != TipoPieza.REY) {
                Actual.x = Posicion1.x;
                Actual.y = Posicion1.y;
                if (Actual.y < Posicion2.y) {
                    if (Actual.x < Posicion2.x) {
                        Actual.x = Actual.x + 1;
                        Actual.y = Actual.y + 1;
                        while ((Actual.x < Posicion2.x) && (Actual.y < Posicion2.y)) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.x = Actual.x + 1;
                            Actual.y = Actual.y + 1;
                        }
                        return true;
                    } else if (Actual.x > Posicion2.x) {
                        Actual.x = Actual.x - 1;
                        Actual.y = Actual.y + 1;
                        while ((Actual.x > Posicion2.x) && (Actual.y < Posicion2.y)) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.x = Actual.x - 1;
                            Actual.y = Actual.y + 1;
                        }
                        return true;
                    } else {
                        Actual.y = Actual.y + 1;
                        while (Actual.y < Posicion2.y) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.y = Actual.y + 1;
                        }
                        return true;
                    }
                } else if (Actual.y > Posicion2.y) {
                    if (Actual.x < Posicion2.x) {
                        Actual.x = Actual.x + 1;
                        Actual.y = Actual.y - 1;
                        while ((Actual.x < Posicion2.x) && (Actual.y > Posicion2.y)) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.x = Actual.x + 1;
                            Actual.y = Actual.y - 1;
                        }
                        return true;
                    } else if (Actual.x > Posicion2.x) {
                        Actual.x = Actual.x - 1;
                        Actual.y = Actual.y - 1;
                        while ((Actual.x > Posicion2.x) && (Actual.y > Posicion2.y)) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.x = Actual.x - 1;
                            Actual.y = Actual.y - 1;
                        }
                        return true;
                    } else {
                        Actual.y = Actual.y - 1;
                        while (Actual.y > Posicion2.y) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.y = Actual.y - 1;
                        }
                        return true;
                    }
                } else {
                    if (Actual.x < Posicion2.x) {
                        Actual.x = Actual.x + 1;
                        while (Actual.x < Posicion2.x) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.x = Actual.x + 1;
                        }
                        return true;
                    } else {
                        Actual.x = Actual.x - 1;
                        while (Actual.x > Posicion2.x) {
                            if (Tablero.Casilla[Actual.x - 1][Actual.y - 1].Pieza != TipoPieza.VACIA)
                                return false;
                            Actual.x = Actual.x - 1;
                        }
                        return true;
                    }
                }
            } else if (Posicion1.y == Posicion2.y) {
                if ((Posicion1.x + 2) == Posicion2.x) {
                    for (Indice = (Posicion1.x + 1); Indice <= (Tablero.Ancho - 1); Indice++) {
                        if (Tablero.Casilla[Indice - 1][Posicion1.y - 1].Pieza != TipoPieza.VACIA)
                            return false;
                    }
                    return true;
                } else if ((Posicion1.x - 2) == Posicion2.x) {
                    for (Indice = (Posicion1.x - 1); Indice >= 2; Indice -= 1) {
                        if (Tablero.Casilla[Indice - 1][Posicion1.y - 1].Pieza != TipoPieza.VACIA)
                            return false;
                    }
                    return true;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /* La pieza es el rey y como sólo se mueve una posición no puede haber obstáculos */
    /* Si la pieza es un caballo, no hace falta comprobación */
    public void PosicionRey(TipoTablero Tablero, TipoColor Color, /* VAR */ TipoPosicion Posicion) {
        // VAR
        int i = 0;
        int j = 0;
        boolean Encontrado = false;

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        	Devuelve la posición del rey del color elegido.
        ******************************************************************************************************************/
        i = 1;
        Encontrado = false;
        while ((i <= Tablero.Ancho) && (!Encontrado)) {
            j = 1;
            while ((j <= Tablero.Alto) && (!Encontrado)) {
                if ((Tablero.Casilla[i - 1][j - 1].Pieza == TipoPieza.REY) && (Tablero.Casilla[i - 1][j - 1].Color == Color)) {
                    Posicion.x = i;
                    Posicion.y = j;
                    Encontrado = true;
                }
                j = j + 1;
            }
            i = i + 1;
        }
    }

    public boolean ReyEnJaque(TipoTablero Tablero, TipoColor Color) {
        // VAR
        TipoPosicion Posicion = new TipoPosicion(); /* WRT */

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        	Devuelve TRUE en caso de que el rey elegido esté en jaque y FALSE en caso contrario.
        ******************************************************************************************************************/
        PosicionRey(Tablero, Color, Posicion);
        if (PiezaAmenazada(Posicion, Tablero))
            return true;
        else
            return false;
    }

    public boolean ReyEnJaque2(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero, TipoColor Color) {
        // VAR
        TipoTablero TableroTemp = new TipoTablero(); /* WRT */

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        	Devuelve TRUE si tras la jugada de posición 1 a posición 2 el rey está enjaque y FALSE en caso contrario.
        ******************************************************************************************************************/
        CopiarTablero(Tablero, TableroTemp);
        MoverPieza(Posicion1, Posicion2, TableroTemp, false, false);
        if (ReyEnJaque(TableroTemp, Color))
            return true;
        else
            return false;
    }

    public boolean MovimientoLegal(TipoPosicion Posicion1, TipoPosicion Posicion2, TipoTablero Tablero, boolean Debug) {
        if (!Debug) {
            if (JugadaEnTablero(Posicion1, Posicion2, Tablero) && !PosicionesIguales(Posicion1, Posicion2) && !DireccionVacia(Posicion1, Tablero) && !MismoColor(Posicion1, Posicion2, Tablero) && DireccionValida(Posicion1, Posicion2, Tablero) && CaminoDespejado(Posicion1, Posicion2, Tablero))
                return true;
            else
                return false;
        } else {
            if (JugadaEnTablero(Posicion1, Posicion2, Tablero)) {
                inOut.WriteString("Jugada en tablero");
                inOut.WriteLn();
                if (!PosicionesIguales(Posicion1, Posicion2)) {
                    inOut.WriteString("Posiciones de origen y destino distintas");
                    inOut.WriteLn();
                    if (!DireccionVacia(Posicion1, Tablero)) {
                        inOut.WriteString("Dirección vacía");
                        inOut.WriteLn();
                        if (!MismoColor(Posicion1, Posicion2, Tablero)) {
                            inOut.WriteString("El origen y destino no contienen piezas del mismo color");
                            inOut.WriteLn();
                            if (DireccionValida(Posicion1, Posicion2, Tablero)) {
                                inOut.WriteString("Dirección válida");
                                inOut.WriteLn();
                                if (CaminoDespejado(Posicion1, Posicion2, Tablero)) {
                                    inOut.WriteString("Camino despejado");
                                    inOut.WriteLn();
                                    inOut.WriteString("Movimiento legal");
                                    inOut.WriteLn();
                                    return true;
                                } else {
                                    inOut.WriteString("Camino bloqueado");
                                    inOut.WriteLn();
                                    return false;
                                }
                            } else {
                                inOut.WriteString("Dirección no válida");
                                inOut.WriteLn();
                                return false;
                            }
                        } else {
                            inOut.WriteString("El origen y destino contienen piezas del mismo color");
                            inOut.WriteLn();
                            return false;
                        }
                    } else {
                        inOut.WriteString("La dirección de origen tiene una pieza");
                        inOut.WriteLn();
                        return false;
                    }
                } else {
                    inOut.WriteString("Posiciones de origen y destino iguales");
                    inOut.WriteLn();
                    return false;
                }
            } else {
                inOut.WriteString("Jugada fuera del tablero");
                inOut.WriteLn();
                return false;
            }
        }
    }

    private int PuntuacionTablero_Auxiliar1(int num, TipoTablero Tablero) {
        /* 
        		Devuelve números de forma ascendente hasta la mitad (Ancho) y descendentes a partir de la mitad. P.ej.:
        		1 2 3 4 4 3 2 1 ...
        	*/
        if (num > (Tablero.Ancho / 2))
            return (Tablero.Ancho + 1 - num);
        else
            return num;
    }

    private int PuntuacionTablero_Auxiliar2(int num, TipoTablero Tablero) {
        /*
        		Devuelve números de forma ascendente hasta la mitad (Alto) y descendentes a partir de la mitad. P.ej.:
        		1 2 3 4 4 3 2 1 ...
        	*/
        if (num > (Tablero.Alto / 2))
            return (Tablero.Alto + 1 - num);
        else
            return num;
    }

    private int PuntuacionTablero_Centrado(int x, int y, TipoTablero Tablero) {
        // VAR
        int valx = 0;
        int valy = 0;

        valx = PuntuacionTablero_Auxiliar1(x, Tablero);
        valy = PuntuacionTablero_Auxiliar1(y, Tablero);
        if (valx < valy)
            return valx - 1;
        else
            return valy - 1;
    }

    public int PuntuacionTablero(TipoTablero Tablero, TipoColor Jugador) {
        // VAR
        boolean reyblanco = false;
        boolean reynegro = false;
        int i = 0;
        int j = 0;
        int Puntuacion = 0;
        TipoPosicion Origen = new TipoPosicion();
        TipoPosicion Destino = new TipoPosicion();

        Puntuacion = 0;
        reyblanco = false;
        reynegro = false;
        for (j = 1; j <= Tablero.Alto; j++) {
            for (i = 1; i <= Tablero.Ancho; i++) {
                if (Tablero.Casilla[i - 1][j - 1].Color == Jugador) {
                    switch (Tablero.Casilla[i - 1][j - 1].Pieza) {
                        case PEON -> {
                            Puntuacion = Puntuacion + 100;
                            if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.BLANCO) {
                                Puntuacion = Puntuacion + (j * (PuntuacionTablero_Auxiliar1(i, Tablero))) - 2;
                                Origen.x = i;
                                Origen.y = j;
                                do {
                                    Origen.y = Origen.y + 1;
                                } while (!((Origen.y == Tablero.Alto) || ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.BLANCO))));
                                if ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.BLANCO))
                                    Puntuacion = Puntuacion - 20;
                            } else {
                                Puntuacion = Puntuacion + ((Tablero.Alto + 1 - j) * (PuntuacionTablero_Auxiliar1(i, Tablero))) - 2;
                                Origen.x = i;
                                Origen.y = j;
                                do {
                                    Origen.y = Origen.y - 1;
                                } while (!((Origen.y == 1) || ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.NEGRO))));
                                if ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.NEGRO))
                                    Puntuacion = Puntuacion - 20;
                            }
                        }
                        case TORRE -> {
                            Puntuacion = Puntuacion + 500;
                            Origen.x = i;
                            Origen.y = j;
                            Destino.x = Origen.x;
                            Destino.y = Origen.y + 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.y = Destino.y + 1;
                            }
                            Destino.x = Origen.x;
                            Destino.y = Origen.y - 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.y = Destino.y - 1;
                            }
                            Destino.x = Origen.x - 1;
                            Destino.y = Origen.y;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.x = Destino.x - 1;
                            }
                            Destino.x = Origen.x + 1;
                            Destino.y = Origen.y;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.x = Destino.x + 1;
                            }
                        }
                        case ALFIL -> {
                            Puntuacion = Puntuacion + 330;
                            Origen.x = i;
                            Origen.y = j;
                            Destino.x = Origen.x - 1;
                            Destino.y = Origen.y + 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.x = Destino.x - 1;
                                Destino.y = Destino.y + 1;
                            }
                            Destino.x = Origen.x + 1;
                            Destino.y = Origen.y + 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.x = Destino.x + 1;
                                Destino.y = Destino.y + 1;
                            }
                            Destino.x = Origen.x - 1;
                            Destino.y = Origen.y - 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.x = Destino.x - 1;
                                Destino.y = Destino.y - 1;
                            }
                            Destino.x = Origen.x + 1;
                            Destino.y = Origen.y - 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion + 1;
                                Destino.x = Destino.x + 1;
                                Destino.y = Destino.y - 1;
                            }
                        }
                        case CABALLO -> {
                            Puntuacion = Puntuacion + 315;
                            Puntuacion = Puntuacion + (3 * PuntuacionTablero_Centrado(i, j, Tablero));
                        }
                        case DAMA -> {
                            Puntuacion = Puntuacion + 940;
                            Puntuacion = Puntuacion + (3 * PuntuacionTablero_Centrado(i, j, Tablero));
                        }
                        case REY -> {
                            if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.BLANCO)
                                reyblanco = true;
                            else
                                reynegro = true;
                        }
                        default -> {
                        }
                    }
                } else {
                    switch (Tablero.Casilla[i - 1][j - 1].Pieza) {
                        case PEON -> {
                            Puntuacion = Puntuacion - 100;
                            if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.BLANCO) {
                                Puntuacion = Puntuacion - ((j * (PuntuacionTablero_Auxiliar1(i, Tablero)) - 2));
                                Origen.x = i;
                                Origen.y = j;
                                do {
                                    Origen.y = Origen.y + 1;
                                } while (!((Origen.y == Tablero.Alto) || ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.BLANCO))));
                                if ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.BLANCO))
                                    Puntuacion = Puntuacion + 20;
                            } else {
                                Puntuacion = Puntuacion - (((Tablero.Alto + 1 - j) * (PuntuacionTablero_Auxiliar1(i, Tablero))) - 2);
                                Origen.x = i;
                                Origen.y = j;
                                do {
                                    Origen.y = Origen.y - 1;
                                } while (!((Origen.y == 1) || ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.NEGRO))));
                                if ((Tablero.Casilla[Origen.x - 1][Origen.y - 1].Pieza == TipoPieza.PEON) && (Tablero.Casilla[Origen.x - 1][Origen.y - 1].Color == TipoColor.NEGRO))
                                    Puntuacion = Puntuacion + 20;
                            }
                        }
                        case TORRE -> {
                            Puntuacion = Puntuacion - 500;
                            Origen.x = i;
                            Origen.y = j;
                            Destino.x = Origen.x;
                            Destino.y = Origen.y + 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.y = Destino.y + 1;
                            }
                            Destino.x = Origen.x;
                            Destino.y = Origen.y - 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.y = Destino.y - 1;
                            }
                            Destino.x = Origen.x - 1;
                            Destino.y = Origen.y;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.x = Destino.x - 1;
                            }
                            Destino.x = Origen.x + 1;
                            Destino.y = Origen.y;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.x = Destino.x + 1;
                            }
                        }
                        case ALFIL -> {
                            Puntuacion = Puntuacion - 330;
                            Origen.x = i;
                            Origen.y = j;
                            Destino.x = Origen.x - 1;
                            Destino.y = Origen.y + 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.x = Destino.x - 1;
                                Destino.y = Destino.y + 1;
                            }
                            Destino.x = Origen.x + 1;
                            Destino.y = Origen.y + 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.x = Destino.x + 1;
                                Destino.y = Destino.y + 1;
                            }
                            Destino.x = Origen.x - 1;
                            Destino.y = Origen.y - 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.x = Destino.x - 1;
                                Destino.y = Destino.y - 1;
                            }
                            Destino.x = Origen.x + 1;
                            Destino.y = Origen.y - 1;
                            while (MovimientoLegal(Origen, Destino, Tablero, false)) {
                                Puntuacion = Puntuacion - 1;
                                Destino.x = Destino.x + 1;
                                Destino.y = Destino.y - 1;
                            }
                        }
                        case CABALLO -> {
                            Puntuacion = Puntuacion - 315;
                            Puntuacion = Puntuacion - (3 * PuntuacionTablero_Centrado(i, j, Tablero));
                        }
                        case DAMA -> {
                            Puntuacion = Puntuacion - 940;
                            Puntuacion = Puntuacion - (3 * PuntuacionTablero_Centrado(i, j, Tablero));
                        }
                        case REY -> {
                            if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.BLANCO)
                                reyblanco = true;
                            else
                                reynegro = true;
                        }
                        default -> {
                        }
                    }
                }
            }
        }
        /* Casilla vacía */
        if (!reyblanco) {
            if (Jugador == TipoColor.NEGRO)
                Puntuacion = Short.MAX_VALUE /* MAX(INTEGER) */;
            else
                Puntuacion = Short.MIN_VALUE /* MIN(INTEGER) */;
        } else {
            if (!reynegro) {
                if (Jugador == TipoColor.BLANCO)
                    Puntuacion = Short.MAX_VALUE /* MAX(INTEGER) */;
                else
                    Puntuacion = Short.MIN_VALUE /* MIN(INTEGER) */;
            }
        }
        return Puntuacion;
    }

    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★*/
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★*/
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★*/
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★    Funciones para representación en modo texto      ****************************/
    /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★*/
    public void ImprimirTablero(TipoTablero Tablero, boolean Debug) {
        // CONST
        final boolean Numeracion = true;

        // VAR
        int i = 0;
        int j = 0;
        char Temp = (char) 0;

        /* Leer casillas */
        /* Si numeración es TRUE, se muestran las coordenadas */
        /* Índices para la matriz */
        /* Almacenamiento temporal para el tipo de pieza */
        inOut.WriteLn();
        for (j = (Tablero.Alto); j >= 1; j -= 1) {
            if (Numeracion)
                inOut.WriteInt(j, 2);
            inOut.WriteString("   ");
            for (i = 1; i <= Tablero.Ancho; i++) {
                switch (Tablero.Casilla[i - 1][j - 1].Pieza) {
                    case REY -> {
                        if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.NEGRO)
                            inOut.Write('R');
                        else
                            inOut.Write('r');
                    }
                    case DAMA -> {
                        if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.NEGRO)
                            inOut.Write('D');
                        else
                            inOut.Write('d');
                    }
                    case TORRE -> {
                        if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.NEGRO)
                            inOut.Write('T');
                        else
                            inOut.Write('t');
                    }
                    case ALFIL -> {
                        if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.NEGRO)
                            inOut.Write('A');
                        else
                            inOut.Write('a');
                    }
                    case CABALLO -> {
                        if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.NEGRO)
                            inOut.Write('C');
                        else
                            inOut.Write('c');
                    }
                    case PEON -> {
                        if (Tablero.Casilla[i - 1][j - 1].Color == TipoColor.NEGRO)
                            inOut.Write('P');
                        else
                            inOut.Write('p');
                    }
                    default -> {
                        inOut.Write('V');
                    }
                }
            }
            inOut.WriteLn();
        }
        /* Casilla vacía */
        if (Numeracion) {
            inOut.WriteLn();
            inOut.WriteString("     ");
            for (i = 1; i <= Tablero.Ancho; i++) {
                inOut.WriteInt(i, 1);
            }
        }
        /* Escribe la numeración inferior */
        inOut.WriteLn();
        inOut.WriteLn();
        if (Debug) {
            inOut.WriteString("Alto: ");
            inOut.WriteInt(Tablero.Alto, 4);
            inOut.WriteLn();
            inOut.WriteString("Ancho: ");
            inOut.WriteInt(Tablero.Ancho, 3);
            inOut.WriteLn();
            inOut.WriteString("La última jugada fue: ");
            inOut.WriteInt(Tablero.UltimoMovimiento.Origen.x, 1);
            inOut.WriteInt(Tablero.UltimoMovimiento.Origen.y, 1);
            inOut.WriteInt(Tablero.UltimoMovimiento.Destino.x, 1);
            inOut.WriteInt(Tablero.UltimoMovimiento.Destino.y, 1);
            inOut.WriteLn();
            inOut.WriteString("Puntuación del tablero para el último jugador");
            inOut.WriteLn();
            inOut.WriteString("Piezas relevantes movidas: ");
            inOut.WriteLn();
            inOut.WriteString("ReyBlanco  TorreBlancaIzquierda  TorreBlancaDerecha");
            inOut.WriteLn();
            if (Tablero.ReyBlancoMovido)
                inOut.WriteInt(1, 4);
            else
                inOut.WriteInt(0, 4);
            if (Tablero.TorreIBlancaMovida)
                inOut.WriteInt(1, 17);
            else
                inOut.WriteInt(0, 17);
            if (Tablero.TorreDBlancaMovida)
                inOut.WriteInt(1, 20);
            else
                inOut.WriteInt(0, 20);
            inOut.WriteLn();
            inOut.WriteString("ReyNegro   TorreNegraIzquierda   TorreNegraDerecha");
            inOut.WriteLn();
            if (Tablero.ReyNegroMovido)
                inOut.WriteInt(1, 4);
            else
                inOut.WriteInt(0, 4);
            if (Tablero.TorreINegraMovida)
                inOut.WriteInt(1, 17);
            else
                inOut.WriteInt(0, 17);
            if (Tablero.TorreDNegraMovida)
                inOut.WriteInt(1, 20);
            else
                inOut.WriteInt(0, 20);
            inOut.WriteLn();
            inOut.WriteLn();
            inOut.WriteLn();
        }
    }

    private int TextoANumero(String Texto, int NumDigitos) {
        // VAR
        int i = 0;
        int numero = 0;
        int total = 0;

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Lee un movimiento de la entrada estandar, para hacer esto se leen cuatro números,
           cada uno corresponde a una coordenada, dos de origen y dos de destino.
           No se hacen comprobaciones.
        ******************************************************************************************************************/
        total = 0;
        for (i = 0; i <= NumDigitos; i++) {
            if (('0' < Runtime.getChar(Texto, i)) && (Runtime.getChar(Texto, i) < '9')) {
                numero = Runtime.getChar(Texto, i) - '0';
                total = numero + (total * 10);
            }
        }
        return total;
    }

    public void LeerMovimiento(/* var */ TipoMovimiento Movimiento) {
        // CONST
        final int MAXBUFFER = 4;

        // VAR
        Runtime.Ref<String> Buffer = new Runtime.Ref<>("");

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Lee un movimiento de la entrada estandar, para hacer esto se leen cuatro números,
           cada uno corresponde a una coordenada, dos de origen y dos de destino.
           No se hacen comprobaciones.
        ******************************************************************************************************************/
        inOut.ReadString(Buffer);
        Movimiento.Origen.x = TextoANumero(Buffer.get(), MAXBUFFER);
        inOut.ReadString(Buffer);
        Movimiento.Origen.y = TextoANumero(Buffer.get(), MAXBUFFER);
        inOut.ReadString(Buffer);
        Movimiento.Destino.x = TextoANumero(Buffer.get(), MAXBUFFER);
        inOut.ReadString(Buffer);
        Movimiento.Destino.y = TextoANumero(Buffer.get(), MAXBUFFER);
    }

    public void ElegirPieza(/* VAR+WRT */ Runtime.IRef<TipoPieza> Pieza) {
        // VAR
        Runtime.Ref<Character> Letra = new Runtime.Ref<>((char) 0);

        /*★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
           Muestra el menú de selección de pieza
        ******************************************************************************************************************/
        inOut.WriteString("Promoción del peón");
        inOut.WriteLn();
        /* Mostrar piezas */
        inOut.WriteString("Elige una pieza entre las siguientes: D A C T");
        inOut.WriteLn();
        inOut.Read(Letra);
        /* Lee el último salto de linea */
        inOut.Read(Letra);
        switch (Letra.get()) {
            case 'D' -> {
                Pieza.set(TipoPieza.DAMA);
            }
            case 'A' -> {
                Pieza.set(TipoPieza.ALFIL);
            }
            case 'C' -> {
                Pieza.set(TipoPieza.CABALLO);
            }
            case 'T' -> {
                Pieza.set(TipoPieza.TORRE);
            }
            case 'd' -> {
                Pieza.set(TipoPieza.DAMA);
            }
            case 'a' -> {
                Pieza.set(TipoPieza.ALFIL);
            }
            case 'c' -> {
                Pieza.set(TipoPieza.CABALLO);
            }
            case 't' -> {
                Pieza.set(TipoPieza.TORRE);
            }
            default -> {
                inOut.WriteString("Elige una letra válida");
                inOut.WriteLn();
                ElegirPieza(Pieza);
            }
        }
    }


    // Support

    private static TDATablero instance;

    public static TDATablero instance() {
        if (instance == null)
            new TDATablero(); // will set 'instance'
        return instance;
    }

    // Life-cycle

    public void begin() {
    }

    public void close() {
    }

}
