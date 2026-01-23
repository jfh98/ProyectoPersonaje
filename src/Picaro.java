/**
 * Subclase de Personaje que utiliza Energía y agilidad.
 * Puede equipar dos armas y atacar con ambas en el mismo turno.
 *
 * @author Javier Fernández Gavino
 * @version 1.0
 */
public class Picaro extends Personaje {
    private int energia;
    private int energiaMaxima;
    private Arma arma1;
    private Arma arma2;

    /**
     * Crea un Pícaro.
     * @param nombre Nombre del personaje.
     * @param energiaMaxima Energía inicial.
     * @param arma1 Arma de mano derecha (puede ser null).
     * @param arma2 Arma de mano izquierda (puede ser null).
     */
    public Picaro(String nombre, int energiaMaxima, Arma arma1, Arma arma2) {
        super(nombre, 1, 100, 3); // Vida media, defensa media
        this.energiaMaxima = energiaMaxima;
        this.energia = energiaMaxima;
        this.arma1 = arma1;
        this.arma2 = arma2;
    }

    // --- Getters y Setters Específicos ---
    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia = Math.max(0, Math.min(energiaMaxima, energia));
    }

    public Arma getArma1() { return arma1; }
    public void setArma1(Arma arma1) { this.arma1 = arma1; }

    public Arma getArma2() { return arma2; }
    public void setArma2(Arma arma2) { this.arma2 = arma2; }

    // --- Lógica de Combate ---

    @Override
    public void atacar(Personaje objetivo) {
        if (!this.estaVivo()) return;

        int coste = 10;
        if (energia >= coste) {
            double d1 = (arma1 != null) ? arma1.getDanioExtra() : 0;
            double d2 = (arma2 != null) ? arma2.getDanioExtra() : 0;

            // Daño base del Pícaro + daño de armas
            double total = d1 + d2 + (5 * getNivel());

            System.out.println(getNombre() + " ataca velozmente desde las sombras.");
            objetivo.recibirDanio(total);

            setEnergia(energia - coste);
        } else {
            System.out.println(getNombre() + " está exhausto y no puede atacar.");
        }
    }

    @Override
    public void descansar() {
        super.descansar();
        setEnergia(this.energiaMaxima);
        System.out.println("   [Descanso]: Energía restaurada al máximo.");
    }

    @Override
    public void subirNivel() {
        super.subirNivel();
        setPuntosVidaMax(getPuntosVidaMax() + 15);
        setPuntosVida(getPuntosVidaMax());
        this.energiaMaxima += 15;
        this.energia = this.energiaMaxima;
        System.out.println("   [Pícaro]: Agilidad mejorada (+15 Vida, +15 Energía).");
    }
}