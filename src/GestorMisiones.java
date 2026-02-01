/**
 * Clase GestorMisiones.
 * Gestiona el progreso, estado y recompensas de la misión.
 *
 * @author Juan María Alanís Rodríguez
 * @version 1.2
 */
public class GestorMisiones {

    // Atributos
    /**
     * Personaje al que se le asignan las misiones.
     */
    private Personaje jugador;
    /**
     * Misión actualmente asignada al personaje.
     */
    private Mision misionActual;

    // Constructores
    /**
     * Crea un GestorMisiones para un personaje dado.
     *
     * @param jugador Personaje al que se le asignarán las misiones.
     */
    public GestorMisiones(Personaje jugador) {
        this.jugador = jugador;
    }

    // Getters
    public Personaje getJugador() {
        return this.jugador;
    }
    public Mision getMisionActual() {
        return this.misionActual;
    }

    // Setters
    public void setJugador(Personaje jugador) {
        this.jugador = jugador;
    }
    public void setMisionActual(Mision misionActual) {
        this.misionActual = misionActual;
    }

    // Métodos
    //
    /**
     * Comprueba si la misión actual es nula (no hay misión asignada).
     *
     * @return true si no hay misión asignada; false en caso contrario.
     */
    private boolean comprobarSiMisionEsNula() {
        return this.misionActual == null;
    }

    // ------- Métodos de uso -------
    /**
     * Asigna una nueva misión al personaje.
     * Si ya tiene una misión activa, no se asigna la nueva y devuelve false.
     *
     * @param nuevaMision Misión a asignar.
     * @return true si la misión se asignó correctamente; false si ya había una misión activa.
     */
    public boolean asignarMision(Mision nuevaMision) {
        boolean res = true;
        if (comprobarSiMisionEsNula()) {
            this.misionActual = nuevaMision;
            this.misionActual.aceptar(jugador);
        }
        else
            res = false;
        return res;
    }
    /**
     * Actualiza el progreso de la misión actual basado en un evento del juego.
     * (Es posible notificar cualquier tipo de evento; la misión decidirá si es relevante).
     * Devuelve false si no hay misión asignada.
     * -
     * Requiere de la función procesarEvento en la clase Misión.
     *
     * @param evento Evento que ocurrió en el juego.
     * @return true si el evento fue procesado; false si no hay misión asignada.
     */
    public boolean notificarEvento(Object evento) {
        boolean res = true;
        if (!comprobarSiMisionEsNula())
            misionActual.procesarEvento(evento);
        else
            res = false;
        return res;
    }
    /**
     * Finaliza la misión actual si está completa y entrega la recompensa.
     * Si la misión no está completa, permite abandonarla sin recompensa.
     * -
     * Solo usar para salir de la mision actual independientemente de su estado.
     *
     * @return true si la misión se completó y se entregó la recompensa; false si se abandonó o no había misión.
     */
    // EN GESTORMISIONES.JAVA

    public boolean finalizarMision() {
        boolean res = false;
        // Solo si existe y está lista para entregar, la cobramos y borramos
        if (!comprobarSiMisionEsNula() && misionActual.estaListaParaEntregar()) {
            misionActual.entregarRecompensa(jugador);
            misionActual = null;
            res = true;
        }
        return res;
    }
    /**
     * Muestra el estado de la misión actual.
     */
    public void comprobarMisionActual() {
        if (!comprobarSiMisionEsNula()) {
            System.out.println("\n[MISIÓN ACTUAL: " + misionActual.getTitulo() + "]");
            System.out.println("   Descripción: " + misionActual.getDescripcion());
            System.out.println("   Progreso: " + misionActual.getProgresoActual() + "/" + misionActual.getObjetivoCantidad());
            System.out.println("   Estado: " + misionActual.getEstado() + "\n");
        }
        else
            System.out.println("\n[!] No tienes ninguna misión activa en este momento.");
    }
}
