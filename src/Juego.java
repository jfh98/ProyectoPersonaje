import java.util.Scanner;

/**
 * Clase principal que actúa como motor del videojuego.
 *
 * Esta clase implementa el patrón de diseño SINGLETON para garantizar
 * que solo exista una única instancia del juego en ejecución.
 *
 * Responsabilidades principales:
 * - Gestionar el flujo de la partida (Bucle principal).
 * - Controlar la creación del personaje.
 * - Administrar los menús y la interacción con el usuario.
 * - Coordinar el combate y la exploración.
 *
 * @author Carlos Fernández Gavino
 * @version 2.1 (Final)
 */
public class Juego {

    // --- PATRÓN SINGLETON ---

    /**
     * Variable estática que almacena la única instancia posible de la clase Juego.
     * Se inicializa en null y se crea solo cuando se llama a getInstancia().
     */
    private static Juego instancia = null;

    // --- ATRIBUTOS DE LA CLASE ---

    /** Jugador principal controlado por el usuario. */
    private Personaje jugador;

    /** Scanner para leer la entrada del teclado. */
    private Scanner sc;

    /** Gestor que controla las misiones y objetivos. */
    private GestorMisiones gestorMisiones;

    /** Evento actual que está ocurriendo en la sala. */
    private Evento eventoActual;

    /** Sala en la que se encuentra el jugador actualmente. */
    private Sala salaActual;

    /**
     * Constructor PRIVADO.
     * Es privado para evitar que se pueda instanciar la clase con 'new Juego()' desde fuera.
     * Inicializa el escáner para la entrada de datos.
     */
    private Juego() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Método de acceso global a la instancia del juego.
     * Si la instancia no existe, la crea. Si ya existe, devuelve la que hay.
     *
     * @return La única instancia activa de la clase Juego.
     */
    public static Juego getInstancia() {
        if (instancia == null) {
            instancia = new Juego();
        }
        return instancia;
    }

    // --- LÓGICA DEL JUEGO ---

    /**
     * Método principal que inicia el bucle del juego.
     * Muestra la bienvenida, crea el personaje y mantiene el juego activo
     * mientras el jugador siga vivo y no haya terminado.
     */
    public void iniciarJuego() {
        // Pantalla de título decorativa
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("||                                                       ||");
        System.out.println("||     ⚔️   L E Y E N D A S   O L V I D A D A S   ⚔️    ||");
        System.out.println("||                                                       ||");
        System.out.println("===========================================================");
        System.out.println("      >> El destino del reino está en tus manos <<         ");
        System.out.println("\n");

        // Paso 1: Configuración del Héroe
        crearPersonaje();

        System.out.println("\n===========================================================");
        System.out.println("  Bienvenido, " + jugador.getNombre().toUpperCase() + ".");
        System.out.println("  Tu leyenda comienza con " + (int)jugador.getPuntosVida() + " PV y un alma inquebrantable.");
        System.out.println("===========================================================\n");

        // Inicializamos componentes lógicos
        this.gestorMisiones = new GestorMisiones(jugador);
        int nivelActual = 1;

        // Cargamos el primer nivel
        cargarNivel(nivelActual);

        boolean jugando = true;

        // --- BUCLE PRINCIPAL DEL JUEGO ---
        while (jugando && jugador.estaVivo()) {
            // Mostramos el menú y guardamos si el jugador quiere seguir jugando
            jugando = mostrarMenuPrincipal();

            // Verificamos si la misión actual se ha completado
            if (gestorMisiones.finalizarMision()) {
                System.out.println("\n#################################################");
                System.out.println("#      ✨ ¡SALA " + nivelActual + " COMPLETADA CON ÉXITO! ✨      #");
                System.out.println("#################################################");
                System.out.println("El ambiente se calma. Recoges tus cosas y avanzas hacia la oscuridad...");

                nivelActual++; // Avanzamos de nivel

                // Control de progreso: Niveles normales vs Jefe Final
                if (nivelActual <= 3) {
                    cargarNivel(nivelActual);
                } else {
                    jefeFinal();     // Evento especial de fin de juego
                    jugando = false; // Rompemos el bucle tras el jefe
                }
            }
        }
        System.out.println("\n>>> FIN DE LA PARTIDA <<<");
    }

    /**
     * Gestiona la creación del personaje pidiendo datos al usuario.
     * Permite elegir entre tres clases (Guerrero, Mago, Pícaro) mediante un menú interactivo.
     * Valida que la entrada sea correcta.
     */
    private void crearPersonaje() {
        System.out.println("¿Cuál es tu nombre, viajero?");
        System.out.print("> ");
        String nombre = sc.nextLine();
        boolean opcionValida = false;

        // Bucle para asegurar que el usuario elija una opción válida
        do {
            System.out.println("\nSelecciona tu destino:");
            System.out.println(" [1] GUERRERO :: Un coloso de acero. (Alta Vida, Daño medio)");
            System.out.println(" [2] MAGO     :: Maestro de lo arcano. (Vida baja, Daño masivo)");
            System.out.println(" [3] PÍCARO   :: Sombra letal. (Equilibrado, Críticos rápidos)");
            System.out.print("Elige tu clase [1-3]: ");

            try {
                int opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        this.jugador = new Guerrero(nombre, new Arma("Espada Bastarda", 5));
                        opcionValida = true;
                        break;
                    case 2:
                        this.jugador = new Mago(nombre);
                        opcionValida = true;
                        break;
                    case 3:
                        this.jugador = new Picaro(nombre, new Arma("Daga Oxidada", 4), new Arma("Daga Oxidada", 4));
                        opcionValida = true;
                        break;
                    default:
                        System.out.println("(!) Esa clase no existe en este reino.");
                }
            } catch (NumberFormatException e) {
                System.out.println("(!) Por favor, introduce un número válido.");
            }
        } while (!opcionValida);
    }

    /**
     * Muestra las opciones principales del menú y gestiona la elección del jugador.
     *
     * @return true si el jugador decide continuar jugando; false si elige salir.
     */
    private boolean mostrarMenuPrincipal() {
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│              MENÚ PRINCIPAL              │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. 🕯️  Explorar la zona                 │");
        System.out.println("│  2. 📜  Ver Estado y Misión Actual       │");
        System.out.println("│  3. 🎒  Abrir Inventario                 │");
        System.out.println("│  4. 🚪  Salir del juego                  │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("¿Qué deseas hacer?: ");

        try {
            String input = sc.nextLine();
            if (input.isEmpty()) return true;
            int opcion = Integer.parseInt(input);

            switch (opcion) {
                case 1:
                    explorar();
                    break;
                case 2:
                    System.out.println("\n--- 👤 ESTADO DEL HÉROE ---");
                    System.out.println("Vida: " + (int)jugador.getPuntosVida() + " / " + (int)jugador.getPuntosVidaMax());
                    System.out.println("Nivel: " + jugador.getNivel());
                    gestorMisiones.comprobarMisionActual();
                    break;
                case 3:
                    gestionarInventarioMenu();
                    break;
                case 4:
                    System.out.println("Guardando recuerdos... Hasta pronto.");
                    return false;
                default:
                    System.out.println("(!) Opción desconocida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("(!) Introduce un número.");
        }
        return true;
    }

    /**
     * Método auxiliar para gestionar el uso del inventario desde el menú principal.
     */
    private void gestionarInventarioMenu() {
        jugador.mostrarInventario();
        System.out.println("\n¿Qué objeto quieres usar o equipar?");
        System.out.println("(Escribe el número del objeto o pulsa ENTER para cerrar la mochila)");
        System.out.print("> ");

        String entrada = sc.nextLine();

        if (entrada.isEmpty()) {
            System.out.println("Cierras la mochila.");
        } else {
            try {
                int indice = Integer.parseInt(entrada);
                jugador.usarObjetoDeMochila(indice);
            } catch (NumberFormatException e) {
                System.out.println("(!) Eso no es un número válido.");
            }
        }
    }

    /**
     * Lógica de exploración de una sala.
     * Aplica los efectos pasivos de la sala y determina aleatoriamente si aparece un enemigo.
     */
    private void explorar() {
        System.out.println("\n🌑 Te adentras en las sombras, antorcha en mano...");

        salaActual.aplicarEfecto(jugador);

        if (jugador.estaVivo()) {
            Enemigo enemigo = eventoActual.avanzar(gestorMisiones);

            if (enemigo != null) {
                combatir(enemigo);
            } else {
                System.out.println("No hay enemigos a la vista... por ahora.");
            }
        }
    }

    /**
     * Sistema de combate por turnos entre el jugador y un enemigo.
     * El combate continúa hasta que uno de los dos personajes se queda sin vida.
     * Incluye opciones para atacar, usar objetos o descansar.
     *
     * @param enemigo El objeto Personaje contra el que se va a luchar.
     */
    private void combatir(Personaje enemigo) {
        System.out.println("\n⚔️ ¡ALERTA! Un " + enemigo.getNombre().toUpperCase() + " bloquea tu camino. ⚔️");
        System.out.println("    >>> COMIENZA EL COMBATE <<<");

        while (jugador.estaVivo() && enemigo.estaVivo()) {
            boolean turnoTerminado = false;

            // --- TURNO DEL JUGADOR ---
            while (!turnoTerminado) {
                System.out.println("\n--- TU TURNO (PV: " + (int)jugador.getPuntosVida() + ") ---");
                System.out.println(" [1] ⚔️ Atacar");
                System.out.println(" [2] 🧪 Usar Objeto");
                System.out.println(" [3] 💤 Defender y Descansar");
                System.out.print("Orden: ");

                try {
                    String input = sc.nextLine();
                    if(input.isEmpty()) continue;
                    int opcion = Integer.parseInt(input);

                    switch (opcion) {
                        case 1:
                            System.out.println("¡Te lanzas al ataque!");
                            jugador.atacar(enemigo);
                            turnoTerminado = true;
                            break;
                        case 2:
                            jugador.mostrarInventario();
                            System.out.println("Elige objeto (ENTER para cancelar):");
                            String entradaCombate = sc.nextLine();

                            if (entradaCombate.isEmpty()) {
                                System.out.println("Vuelves a empuñar tu arma.");
                            } else {
                                try {
                                    int idx = Integer.parseInt(entradaCombate);
                                    jugador.usarObjetoDeMochila(idx);
                                    turnoTerminado = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("(!) Opción inválida.");
                                }
                            }
                            break;
                        case 3:
                            System.out.println(jugador.getNombre() + " toma una posición defensiva para recuperar aliento.");
                            jugador.descansar();
                            turnoTerminado = true;
                            break;
                        default:
                            System.out.println("(!) No puedes hacer eso en combate.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("(!) Elige una opción válida.");
                }
            }

            // --- COMPROBACIÓN POST-ATAQUE JUGADOR ---
            if (!enemigo.estaVivo()) {
                System.out.println("\n⭐⭐⭐ ¡VICTORIA! El enemigo ha caído. ⭐⭐⭐");

                if (enemigo instanceof Enemigo) {
                    Enemigo e = (Enemigo) enemigo;
                    jugador.ganarExperiencia((int) e.getExperienciaRecompensa());
                    gestorMisiones.notificarEvento(enemigo);

                    Item botin = e.soltarBotin(jugador);
                    if (botin != null) {
                        System.out.println("🎁 ¡Has encontrado: " + botin.getNombre() + "!");
                        jugador.recogerItem(botin);
                    }
                }
                return;
            }

            // --- TURNO DEL ENEMIGO ---
            System.out.println("\n🔻 Turno de " + enemigo.getNombre() + "...");

            if (enemigo.getPuntosVida() <= (enemigo.getPuntosVidaMax() * 0.3) && Math.random() < 0.15) {
                System.out.println(enemigo.getNombre() + " jadea y trata de curarse sus heridas.");
                enemigo.descansar();
            } else {
                enemigo.atacar(jugador);
            }

            if (!jugador.estaVivo()) {
                System.out.println("\n💀💀💀 -------------------------- 💀💀💀");
                System.out.println("      HAS MUERTO. Tu alma se desvanece.");
                System.out.println("💀💀💀 -------------------------- 💀💀💀");
            }
        }
    }

    /**
     * Configura el nivel actual. Crea la sala, instancia los enemigos y asigna las misiones correspondientes.
     *
     * @param nivel El número del nivel a cargar (1, 2 o 3).
     */
    private void cargarNivel(int nivel) {
        System.out.println("\n🌀 ------------------------------------------- 🌀");
        System.out.println("      ENTRANDO EN EL NIVEL " + nivel);
        System.out.println("🌀 ------------------------------------------- 🌀");

        this.salaActual = new Sala(nivel, jugador.getNivel());
        this.eventoActual = null;

        switch (nivel) {
            case 1:
                System.out.println(salaActual.getDescripcion());
                Enemigo obj1 = new Enemigo("Orco Raso", 1);
                MisionCaza m1 = new MisionCaza("Limpieza de Túneles", "Acaba con la plaga de 2 Orcos.",
                        2, obj1, 50, new Pocion("Poción Vital", 50, TipoPocion.VIDA), salaActual);
                gestorMisiones.asignarMision(m1);
                eventoActual = new Evento(salaActual, m1, jugador);
                break;

            case 2:
                System.out.println(salaActual.getDescripcion());
                Enemigo obj2 = new Enemigo("Espectro Doliente", 2);
                MisionCaza m2 = new MisionCaza("Exorcismo", "Libera las almas de 2 Espectros.",
                        2, obj2, 100, new Pocion("Elixir Mayor", 100, TipoPocion.VIDA), salaActual);
                gestorMisiones.asignarMision(m2);
                eventoActual = new Evento(salaActual, m2, jugador);
                break;

            case 3:
                System.out.println(salaActual.getDescripcion());
                Enemigo obj3 = new Enemigo("Caballero Corrupto", 3);
                MisionCaza m3 = new MisionCaza("La Guardia Real", "Derrota a 3 Caballeros Corruptos.",
                        3, obj3, 200, new Arma("Hoja de Luz", 15), salaActual);
                gestorMisiones.asignarMision(m3);
                eventoActual = new Evento(salaActual, m3, jugador);
                break;
        }
        System.out.println("📜 Nueva Misión Inscrita: " + gestorMisiones.getMisionActual().getTitulo());
    }

    /**
     * Evento especial del Jefe Final.
     * Instancia un enemigo de tipo 'Jefe' (subclase de Enemigo) con estadísticas aumentadas.
     */
    private void jefeFinal() {
        System.out.println("\n\n");
        System.out.println("🔥⚡🔥 --------------------------------------------------- 🔥⚡🔥");
        System.out.println("   Las puertas del Salón del Trono se abren con un estruendo.");
        System.out.println("   El aire se congela. Sentado en el trono de huesos,");
        System.out.println("   te aguarda una figura imponente envuelta en sombras.");
        System.out.println("\n   LORD MALACOR: '¿Crees que un mortal puede desafiarme?'");
        System.out.println("   El Usurpador se levanta, empuñando una maza que supura oscuridad.");
        System.out.println("🔥⚡🔥 --------------------------------------------------- 🔥⚡🔥");

        // Instanciamos al JEFE
        Jefe boss = new Jefe("Lord Malacor", 8);

        combatir(boss);

        if (jugador.estaVivo()) {
            System.out.println("\n🏆🏆🏆 ******************************************** 🏆🏆🏆");
            System.out.println("   Con un último rugido, Lord Malacor se desintegra en cenizas.");
            System.out.println("   La oscuridad se disipa. El reino vuelve a ver la luz.");
            System.out.println("\n         --- ¡HAS COMPLETADO EL JUEGO! ---");
            System.out.println("           Eres el nuevo héroe de la leyenda.");
            System.out.println("🏆🏆🏆 ******************************************** 🏆🏆🏆");
        }
    }
}