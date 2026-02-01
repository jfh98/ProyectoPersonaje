/**
 * Punto de entrada de la aplicación "Leyendas Olvidadas".
 * Su única responsabilidad es invocar la instancia única del Juego.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Obtenemos la única instancia del juego (Patrón Singleton)
        Juego partida = Juego.getInstancia();

        // 2. Iniciamos el motor del juego
        partida.iniciarJuego();

        // Cuando el método iniciarJuego() termina, el programa finaliza.
    }
}