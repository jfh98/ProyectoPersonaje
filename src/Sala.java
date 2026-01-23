/**
 * Clase que representa una sala en el juego con diferentes efectos y dificultades.
 * Integra la generación de enemigos.
 *
 * @author Rafa Navarro
 * @since 2026.01.23
 */
public class Sala {

    /**
     * Tipo de efecto ambiental de la sala (ESCARCHA, NIEBLA, BENDICION, NORMAL).
     */
    private String tipo;
    /**
     * Descripción del efecto ambiental.
     */
    private String descripcion;
    /**
     * Dificultad de la sala (FÁCIL, MEDIA, DIFÍCIL).
     */
    private String dificultad;
    /**
     * Nivel sugerido para el jugador en esta sala.
     */
    private int nivelSugerido;

    /**
     * Constructor de la sala.
     * Asigna dificultad y efecto ambiental aleatorio.
     *
     * @param numeroSala   Número identificativo de la sala (1, 2, 3).
     * @param nivelJugador Nivel actual del jugador para ajustar la dificultad.
     */
    public Sala(int numeroSala, int nivelJugador) {
        /**
         * Asignación de Dificultad y Nivel Sugerido.
         */
        if (numeroSala == 1) {
            this.dificultad = "FÁCIL";
            this.nivelSugerido = Math.max(1, nivelJugador - 2);
        } else if (numeroSala == 2) {
            this.dificultad = "MEDIA";
            this.nivelSugerido = nivelJugador;
        } else {
            this.dificultad = "DIFÍCIL";
            this.nivelSugerido = nivelJugador + 2;
        }

        /**
         * Asignación de Efecto Ambiental Aleatorio.
         */
        int azar = (int) (Math.random() * 4);
        switch (azar) {
            case 0:
                this.tipo = "ESCARCHA";
                this.descripcion = "Un frío sepulcral que congela tus reservas.";
                break;
            case 1:
                this.tipo = "NIEBLA";
                this.descripcion = "Vapores venenosos que corroen tu piel.";
                break;
            case 2:
                this.tipo = "BENDICION";
                this.descripcion = "Un rayo de luz celestial que purifica tus heridas.";
                break;
            default:
                this.tipo = "NORMAL";
                this.descripcion = "Una estancia de piedra fría sin peligros aparentes.";
                break;
        }
    }

    /**
     * Generador de enemigos.
     * Ajusta las probabilidades de aparición según la dificultad de la sala.
     */
    public Enemigo generarEnemigo() {
        double probabilidad = Math.random();

        if (this.dificultad.equals("FÁCIL")) {
            /**
             * 80% Orco, 15% Espectro, 5% Jefe
             */
            if (probabilidad < 0.05) return new Enemigo("Comandante Orco (Mini-Jefe)", nivelSugerido + 2);
            if (probabilidad < 0.20) return new Enemigo("Espectro Débil", nivelSugerido + 1);
            return new Enemigo("Orco Raso", nivelSugerido);

        } else if (this.dificultad.equals("MEDIA")) {
            /**
             * 50% Orco, 35% Espectro, 15% Jefe
             */
            if (probabilidad < 0.15) return new Enemigo("Caballero Espectral (Mini-Jefe)", nivelSugerido + 2);
            if (probabilidad < 0.50) return new Enemigo("Espectro Acechador", nivelSugerido + 1);
            return new Enemigo("Orco Guerrero", nivelSugerido);

        } else {
            /**
             * 20% Orco, 50% Espectro, 30% Jefe
             */
            if (probabilidad < 0.30) return new Enemigo("SEÑOR DE LAS SOMBRAS (JEFE)", nivelSugerido + 3);
            if (probabilidad < 0.80) return new Enemigo("Espectro de Élite", nivelSugerido + 2);
            return new Enemigo("Orco Berserker", nivelSugerido + 1);
        }
    }

    /**
     * Aplica el efecto ambiental de la sala al personaje.
     *
     * @param p Personaje que entra en la sala.
     */
    public void aplicarEfecto(Personaje p) {
        System.out.println("\n--- [SALA " + dificultad + "]: " + descripcion + " ---");
        switch (this.tipo) {
            case "ESCARCHA":
                if (p instanceof Mago) {
                    Mago m = (Mago) p;
                    m.setPuntosMana(m.getPuntosMana() - 15);
                    System.out.println("   * El frío drena 15 de Maná.");
                } else if (p instanceof Picaro) {
                    ((Picaro) p).setEnergia(((Picaro) p).getEnergia() - 15);
                    System.out.println("   * El frío drena 15 de Energía.");
                }
                break;
            case "NIEBLA":
                p.recibirDanio(10);
                break;
            case "BENDICION":
                p.setPuntosVida(p.getPuntosVida() + 15);
                System.out.println("   * La luz restaura 15 PV.");
                break;
        }
    }

    /**
     * Getters de la sala.
     *
     * @return tipo, descripcion, dificultad, nivelSugerido
     */
    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public int getNivelSugerido() {
        return nivelSugerido;
    }


    /**
     * Representación en cadena de la sala.
     *
     * @return Detalles de la sala.
     */
    @Override
    public String toString() {
        return "Sala{" +
                "tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", dificultad='" + dificultad + '\'' +
                ", nivelSugerido=" + nivelSugerido +
                '}';
    }
}
