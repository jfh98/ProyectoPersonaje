/**
 * Item consumible que restaura estadísticas del personaje.
 * Utiliza un Enum para definir si afecta a la Vida o al Maná.
 *
 * @author Javier Fernández Gavino
 * @version 1.1
 */
public class Pocion extends Item {
    private double cantidad;
    private TipoPocion tipo; // VIDA o MANA

    /**
     * Crea una poción.
     * @param nombre Nombre (ej: "Poción Menor").
     * @param cantidad Puntos que restaura.
     * @param tipo El tipo de estadística que restaura (TipoPocion.VIDA o TipoPocion.MANA).
     */
    public Pocion(String nombre, double cantidad, TipoPocion tipo) {
        super(nombre);
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    /**
     * Usa la poción sobre un personaje.
     * @return true si la poción tuvo efecto (y debe gastarse), false si no.
     */
    @Override
    public boolean usarItem(Personaje objetivo) {
        switch (tipo) {
            case VIDA:
                double vidaAntes = objetivo.getPuntosVida();
                objetivo.setPuntosVida(vidaAntes + cantidad);
                System.out.println(objetivo.getNombre() + " bebe " + getNombre() + " y se cura.");
                return true; // ÉXITO

            case MANA:
                if (objetivo instanceof Mago) {
                    Mago m = (Mago) objetivo;
                    m.recuperarMana(cantidad);
                    System.out.println(objetivo.getNombre() + " bebe una poción de maná.");
                    return true; // ÉXITO
                } else {
                    System.out.println(objetivo.getNombre() + " intenta beber el maná pero no tiene efecto (No es mago).");
                    return false; // FALLO
                }

            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return getNombre() + " (+" + (int) cantidad + " " + tipo + ")";
    }
}