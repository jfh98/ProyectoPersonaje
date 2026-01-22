/**
 * Representa a los oponentes controlados por el juego.
 * Tienen estadísticas basadas en el nivel y pueden soltar botín al ser derrotados.
 *
 * @author Javier Fernández Gavino
 * @version 1.1
 */
public class Enemigo extends Personaje {
    private double experienciaRecompensa;
    private double danioBase;

    /**
     * Crea un enemigo escalado según el nivel proporcionado.
     * @param nombre Nombre del enemigo.
     * @param nivel Nivel de dificultad (afecta vida, defensa y daño).
     */
    public Enemigo(String nombre, int nivel) {
        super(nombre, nivel, 40 + (25 * nivel), 2 + (1 * nivel));
        this.danioBase = 6 + (4 * nivel);
        this.experienciaRecompensa = 20 * nivel;
    }

    public double getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    /**
     * Calcula si el enemigo suelta un objeto al ser derrotado.
     * Existe un 40% de probabilidad de soltar botín.
     * El botín se adapta a la clase del jugador (Pociones de maná para Magos, etc.).
     * * @param jugador El personaje que derrotó al enemigo.
     * @return Un objeto Item si hay suerte, o null si no suelta nada.
     */
    public Item soltarBotin(Personaje jugador) {
        double probabilidad = Math.random();

        if (probabilidad < 0.40) {
            if (jugador instanceof Mago) {
                double cura = 30 + (10 * getNivel());
                return new Pocion("Poción de Maná", cura, TipoPocion.MANA);
            } else {
                if (Math.random() < 0.5) {
                    double danioArma = 6 + (3 * getNivel());
                    String nombreArma = (jugador instanceof Guerrero) ? "Espada Bastarda" : "Daga Envenenada";
                    return new Arma(nombreArma, danioArma);
                } else {

                    return new Pocion("Poción de Salud", 40, TipoPocion.VIDA);
                }
            }
        }
        return null;
    }

    @Override
    public void atacar(Personaje objetivo) {
        if (Math.random() < 0.85) {
            double factor = 0.9 + (Math.random() * 0.2); // Variación de daño +/- 10%
            double danioFinal = danioBase * factor;
            System.out.println(getNombre() + " ataca ferozmente a " + objetivo.getNombre() + "!");
            objetivo.recibirDanio((int) danioFinal);
        } else {
            System.out.println(getNombre() + " falla el ataque.");
        }
    }
}