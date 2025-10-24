package ch.pitchtech.modula.chess;

import ch.pitchtech.modula.library.InOut;
import ch.pitchtech.modula.library.Storage;
import ch.pitchtech.modula.runtime.Runtime;


public class TDALista {

    // Imports
    private final InOut inOut;


    private TDALista() {
        instance = this; // Set early to handle circular dependencies
        inOut = InOut.instance();
    }


    // TYPE

    public static class TipoLista { // RECORD

        public TDATablero.TipoDatos[] elemento = new TDATablero.TipoDatos[100000];
        public int ultimo;


        public TDATablero.TipoDatos[] getElemento() {
            return this.elemento;
        }

        public void setElemento(TDATablero.TipoDatos[] elemento) {
            this.elemento = elemento;
        }

        public int getUltimo() {
            return this.ultimo;
        }

        public void setUltimo(int ultimo) {
            this.ultimo = ultimo;
        }


        public void copyFrom(TipoLista other) {
            this.elemento = Runtime.copyOf(false, other.elemento);
            this.ultimo = other.ultimo;
        }

        public TipoLista newCopy() {
            TipoLista copy = new TipoLista();
            copy.copyFrom(this);
            return copy;
        }

    }


    // TYPE

    private static class TipoBasura { // RECORD

        private TDATablero.TipoDatos[] elemento = new TDATablero.TipoDatos[1000000];
        private int ultimo;


        public TDATablero.TipoDatos[] getElemento() {
            return this.elemento;
        }

        public void setElemento(TDATablero.TipoDatos[] elemento) {
            this.elemento = elemento;
        }

        public int getUltimo() {
            return this.ultimo;
        }

        public void setUltimo(int ultimo) {
            this.ultimo = ultimo;
        }


        public void copyFrom(TipoBasura other) {
            this.elemento = Runtime.copyOf(false, other.elemento);
            this.ultimo = other.ultimo;
        }

        public TipoBasura newCopy() {
            TipoBasura copy = new TipoBasura();
            copy.copyFrom(this);
            return copy;
        }

    }


    // VAR

    /* Aquí se guardarán todos los nodos que se vayan referenciando para liberarlos al terminar */
    /* BASURA será un dato de tipo encapsulado */
    private TipoBasura BASURA = new TipoBasura();


    public TipoBasura getBASURA() {
        return this.BASURA;
    }

    public void setBASURA(TipoBasura BASURA) {
        this.BASURA = BASURA;
    }


    // PROCEDURE

    /* Procedimientos para recoger la basura */
    private void InicializarBasura() {
        /* Función que "Devuelve una basura vacía") */
        BASURA.ultimo = 0;
    }

    private void AnadirBasura(TDATablero.TipoDatos x) {
        /* Añade el elemento a la basura */
        BASURA.elemento[BASURA.ultimo + 1 - 1] = x;
        BASURA.ultimo = BASURA.ultimo + 1;
    }

    private void SuprimirBasura() {
        // VAR
        int i = 0;

        /* Elimina un elemento de la basura */
        BASURA.elemento[BASURA.ultimo - 1] = null;
        BASURA.ultimo = BASURA.ultimo - 1;
    }

    private boolean VaciaBasura() {
        /* Comprueba que en la lista hay algún elemento */
        if (BASURA.ultimo == 0)
            return true;
        else
            return false;
    }

    public void RecolectorDeBasura() {
        while (!VaciaBasura()) {
            SuprimirBasura();
        }
    }

    public void CantidadBasura() {
        inOut.WriteString("La cantidad de basura es ");
        inOut.WriteInt(BASURA.ultimo, 1);
        inOut.WriteLn();
    }

    /* Procedimientos generales */
    public void Insertar(/* VAR */ TipoLista L, TDATablero.TipoDatos x, int p) {
        // VAR
        int i = 0;

        /* Inserta un elemento, x, en una posicion p de L, pasando los elementos
         de la posicion p y siguientes a la posicion inmediatamente posterior */
        for (i = L.ultimo + 1; i >= p + 1; i -= 1) {
            L.elemento[i - 1] = L.elemento[i - 1 - 1];
        }
        L.elemento[p - 1] = x;
        L.ultimo = L.ultimo + 1;
        AnadirBasura(x);
    }

    public void Recuperar(/* var */ TipoLista L, /* VAR */ Runtime.IRef<TDATablero.TipoDatos> x, int p, /* VAR */ Runtime.IRef<Boolean> encontrado) {
        // VAR
        int i = 0;

        /* Encuentra el elemento x que esta en la posicion p, si la posicion p es
        mayor que el numero de elementos de L, devuelve a encontrado FALSE */
        if (p > L.ultimo) {
            encontrado.set(false);
        } else {
            x.set(L.elemento[p - 1]);
            encontrado.set(true);
        }
    }

    public void Suprimir(/* VAR */ TipoLista L, int p) {
        // VAR
        int i = 0;

        /* Elimina de L el elemento de la posicion p */
        for (i = p; i <= L.ultimo; i++) {
            L.elemento[i - 1] = L.elemento[i + 1 - 1];
        }
        L.ultimo = L.ultimo - 1;
    }

    public void Anula(/* VAR */ TipoLista L) {
        /* Vacía L */
        L.ultimo = 0;
    }

    public void Primero(/* var */ TipoLista L, /* VAR */ Runtime.IRef<TDATablero.TipoDatos> x) {
        /* De vuelve el primer elemento de L */
        x.set(L.elemento[0]);
    }

    public void Ultimo(/* var */ TipoLista L, /* VAR */ Runtime.IRef<TDATablero.TipoDatos> x) {
        /* Devuelve el ultimo elemento de L */
        x.set(L.elemento[L.ultimo - 1]);
    }

    public void InicializarLista(/* VAR */ TipoLista L) {
        /* Función que "Devuelve una lista vacía") */
        L.ultimo = 0;
    }

    public void AnadirFIFO(TDATablero.TipoDatos x, /* VAR+WRT */ TipoLista L) {
        /* Añade el elemento x al final de la lista L (cola) */
        Insertar(L, x, L.ultimo + 1);
    }

    public void AnadirLIFO(TDATablero.TipoDatos x, /* VAR+WRT */ TipoLista L) {
        /* Añade el elemento x al principio de la lista L (pila) */
        Insertar(L, x, 1);
    }

    public void Resto(/* VAR+WRT */ TipoLista L) {
        /* Elimina el primer elemento de la lista */
        Suprimir(L, 1);
    }

    public boolean Vacia(/* var */ TipoLista L) {
        /* Comprueba que en la lista hay algún elemento */
        if (L.ultimo == 0)
            return true;
        else
            return false;
    }

    public void Elemento(int p, /* VAR+WRT */ TipoLista L, /* VAR */ Runtime.IRef<TDATablero.TipoDatos> e) {
        // VAR
        Runtime.Ref<TDATablero.TipoDatos> x = new Runtime.Ref<>(null);
        Runtime.Ref<Boolean> encontrado = new Runtime.Ref<>(false);

        /* Consulta el elemento de la posición p de la lista sin modificarla */
        Recuperar(L, x, p, encontrado);
        if (encontrado.get() == true)
            e.set(x.get());
    }


    // Support

    private static TDALista instance;

    public static TDALista instance() {
        if (instance == null)
            new TDALista(); // will set 'instance'
        return instance;
    }

    // Life-cycle

    public void begin() {
        InicializarBasura();
    }

    public void close() {
    }

}
