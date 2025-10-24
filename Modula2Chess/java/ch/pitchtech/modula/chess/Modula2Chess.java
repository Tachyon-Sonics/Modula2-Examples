package ch.pitchtech.modula.chess;

import ch.pitchtech.modula.chess.TDATablero.TipoColor;
import ch.pitchtech.modula.library.*;
import ch.pitchtech.modula.runtime.HaltException;
import ch.pitchtech.modula.runtime.Runtime;


public class Modula2Chess {

    // Imports
    private final Arguments arguments = Arguments.instance();
    private final IA iA = IA.instance();
    private final InOut inOut = InOut.instance();
    private final TDATablero tDATablero = TDATablero.instance();


    // CONST

    private static final boolean DEBUG = false;


    // VAR

    private int numarg;
    private String Argumento = "";
    private TDATablero.TipoTablero Tablero = new TDATablero.TipoTablero();
    private TDATablero.TipoMovimiento Movimiento = new TDATablero.TipoMovimiento();
    private boolean EnJuego;
    private int metodo;
    private boolean NADA;


    public int getNumarg() {
        return this.numarg;
    }

    public void setNumarg(int numarg) {
        this.numarg = numarg;
    }

    public String getArgumento() {
        return this.Argumento;
    }

    public void setArgumento(String Argumento) {
        this.Argumento = Argumento;
    }

    public TDATablero.TipoTablero getTablero() {
        return this.Tablero;
    }

    public void setTablero(TDATablero.TipoTablero Tablero) {
        this.Tablero = Tablero;
    }

    public TDATablero.TipoMovimiento getMovimiento() {
        return this.Movimiento;
    }

    public void setMovimiento(TDATablero.TipoMovimiento Movimiento) {
        this.Movimiento = Movimiento;
    }

    public boolean isEnJuego() {
        return this.EnJuego;
    }

    public void setEnJuego(boolean EnJuego) {
        this.EnJuego = EnJuego;
    }

    public int getMetodo() {
        return this.metodo;
    }

    public void setMetodo(int metodo) {
        this.metodo = metodo;
    }

    public boolean isNADA() {
        return this.NADA;
    }

    public void setNADA(boolean NADA) {
        this.NADA = NADA;
    }


    // PROCEDURE

    private boolean LeerArgumento(int numero, /* VAR */ Runtime.IRef<String> texto) {
        // CONST
        final int MaxCaracteres = 64;

        // VAR
        Runtime.Ref<Integer> numarg = new Runtime.Ref<>(0);
        int max = 0;
        Runtime.Ref<String[]> argumento = new Runtime.Ref<>(null);
        int n = 0;
        boolean error = false;

        error = false;
        max = numero;
        arguments.GetArgs(numarg, argumento);
        if ((numarg.get() - 1) < max) {
            error = true;
        } else {
            for (n = 0; n <= MaxCaracteres; n++) {
                Runtime.setChar(texto, n, Runtime.getChar(argumento.get()[numero], n));
            }
        }
        return error;
    }


    // Life Cycle

    private void begin() {
        InOut.instance().begin();
        TextIO.instance().begin();
        Storage.instance().begin();
        TDALista.instance().begin();
        Clock.instance().begin();
        IA.instance().begin();
        TDATablero.instance().begin();
        Arguments.instance().begin();

        EnJuego = true;
        /* Versión y autor */
        inOut.WriteString("*******AJEDREZ 1.0*******");
        inOut.WriteLn();
        inOut.WriteString("Por Javier Callón Álvarez");
        inOut.WriteLn();
        inOut.WriteString("*************************");
        inOut.WriteLn();
        /* Si no se han escrito argumentos */
        if (LeerArgumento(1, new Runtime.FieldRef<>(this::getArgumento, this::setArgumento))) {
            inOut.WriteString("Uso: ");
            inOut.WriteLn();
            inOut.WriteString("   ");
            EnJuego = LeerArgumento(0, new Runtime.FieldRef<>(this::getArgumento, this::setArgumento));
            inOut.WriteString(Argumento);
            EnJuego = LeerArgumento(1, new Runtime.FieldRef<>(this::getArgumento, this::setArgumento));
            inOut.WriteString(" Método Tablero");
            inOut.WriteLn();
            inOut.WriteString("Método: ");
            inOut.WriteLn();
            inOut.WriteString("   1: ALFA-BETA");
            inOut.WriteLn();
            inOut.WriteString("   2: MINIMAX ");
            inOut.WriteLn();
            inOut.WriteString("   3: MINIMAX y ALFA-BETA ");
            inOut.WriteLn();
            inOut.WriteString("Tablero: ");
            inOut.WriteLn();
            inOut.WriteString("   Archivo de tablero");
            inOut.WriteLn();
            inOut.WriteString("Tomando valores por defecto...");
            inOut.WriteLn();
            metodo = 1;
            if (tDATablero.LeerTablero(Tablero, "default.dat")) {
                inOut.WriteString("No se puede abrir el archivo default.dat");
                inOut.WriteLn();
                EnJuego = false;
            }
        } else {
            if (Runtime.getChar(Argumento, 0) == '1') {
                metodo = 1;
                inOut.WriteString("Método ALFA-BETA ");
                inOut.WriteLn();
            } else if (Runtime.getChar(Argumento, 0) == '2') {
                metodo = 2;
                inOut.WriteString("Método MINIMAX ");
                inOut.WriteLn();
            } else if (Runtime.getChar(Argumento, 0) == '3') {
                metodo = 3;
                inOut.WriteString("Método MINIMAX y ALFA-BETA ");
                inOut.WriteLn();
            } else {
                EnJuego = false;
                inOut.WriteString("Método no válido.");
                inOut.WriteLn();
            }
            if (EnJuego && !LeerArgumento(2, new Runtime.FieldRef<>(this::getArgumento, this::setArgumento))) {
                if (tDATablero.LeerTablero(Tablero, Argumento)) {
                    inOut.WriteString("No se puede abrir el archivo ");
                    inOut.WriteString(Argumento);
                    inOut.WriteLn();
                    EnJuego = false;
                }
            } else if (EnJuego) {
                if (tDATablero.LeerTablero(Tablero, "default.dat")) {
                    inOut.WriteString("No se puede abrir el archivo default.dat");
                    inOut.WriteLn();
                    EnJuego = false;
                }
            } else {
                EnJuego = false;
            }
        }
        if (EnJuego) {
            while (EnJuego) {
                tDATablero.ImprimirTablero(Tablero, DEBUG);
                switch (tDATablero.JaqueMate(Tablero, TipoColor.BLANCO)) {
                    case 1 -> {
                        inOut.WriteString("Jaque Mate!!! Las negras ganan!");
                        inOut.WriteLn();
                        EnJuego = false;
                    }
                    case 2 -> {
                        inOut.WriteString("Rey negro ahogado!");
                        inOut.WriteLn();
                        EnJuego = false;
                    }
                    default -> {
                    }
                }
                if (EnJuego) {
                    inOut.WriteString("Siguiente jugada?");
                    inOut.WriteLn();
                    tDATablero.LeerMovimiento(Movimiento);
                    if (tDATablero.MovimientoLegal(Movimiento.Origen, Movimiento.Destino, Tablero, DEBUG) && (Tablero.Casilla[Movimiento.Origen.x - 1][Movimiento.Origen.y - 1].Color == TipoColor.BLANCO) && !tDATablero.ReyEnJaque2(Movimiento.Origen, Movimiento.Destino, Tablero, Tablero.Casilla[Movimiento.Origen.x - 1][Movimiento.Origen.y - 1].Color)) {
                        tDATablero.MoverPieza(Movimiento.Origen, Movimiento.Destino, Tablero, true, false);
                        tDATablero.ImprimirTablero(Tablero, DEBUG);
                        inOut.WriteLn();
                        switch (tDATablero.JaqueMate(Tablero, TipoColor.NEGRO)) {
                            case 1 -> {
                                inOut.WriteString("Jaque Mate!!! Las blancas ganan!");
                                inOut.WriteLn();
                                EnJuego = false;
                            }
                            case 2 -> {
                                inOut.WriteString("Rey negro ahogado!");
                                inOut.WriteLn();
                                EnJuego = false;
                            }
                            default -> {
                            }
                        }
                        if (EnJuego) {
                            inOut.WriteString("Calculando jugada. Espere, por favor...");
                            inOut.WriteLn();
                            iA.Jugada(Tablero, TipoColor.NEGRO, metodo);
                        }
                    } else {
                        inOut.WriteString("Movimiento ilegal");
                        inOut.WriteLn();
                    }
                }
            }
        }
    }

    private void close() {
        Arguments.instance().close();
        TDATablero.instance().close();
        IA.instance().close();
        Clock.instance().close();
        TDALista.instance().close();
        Storage.instance().close();
        TextIO.instance().close();
        InOut.instance().close();
    }

    public static void main(String[] args) {
        Runtime.setArgs(args);
        Modula2Chess instance = new Modula2Chess();
        try {
            instance.begin();
        } catch (HaltException ex) {
            // Normal termination
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            instance.close();
        }
    }

}
