package org.example;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Main extends JFrame {
    private int filas;
    private int columnas;
    private int numMinas;
    private char[][] tableroJugador1;
    private char[][] tableroJugador2;
    private boolean[][] reveladoJugador1;
    private boolean[][] reveladoJugador2;
    private int[][] contadorMinasJugador1;
    private int[][] contadorMinasJugador2;
    private JButton[][] botonesJugador1;
    private JButton[][] botonesJugador2;
    private JPanel panelJugador1;
    private JPanel panelJugador2;
    private JPanel panelPrincipal;
    private JLabel turnoLabel;
    private JLabel labelTiempo;
    private Timer timer;
    private int tiempo;
    private JLabel probabilityLabelJugador1;
    private JProgressBar probabilityBarJugador1;
    private JLabel probabilityLabelJugador2;
    private JProgressBar probabilityBarJugador2;
    private boolean turnoJugador1;

    public Main(int filas, int columnas, int numMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.numMinas = numMinas;

        setTitle("Buscaminas de Dos Jugadores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelJugador1 = new JPanel(new GridLayout(filas, columnas));
        panelJugador1.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelJugador1.setBackground(new Color(52, 152, 219));
        panelJugador1.setBorder(new TitledBorder(new LineBorder(new Color(236, 240, 241), 2), "Jugador 1", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));

        panelJugador2 = new JPanel(new GridLayout(filas, columnas));
        panelJugador2.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelJugador2.setBackground(new Color(231, 76, 60));
        panelJugador2.setBorder(new TitledBorder(new LineBorder(new Color(236, 240, 241), 2), "Jugador 2", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));

        tableroJugador1 = new char[filas][columnas];
        tableroJugador2 = new char[filas][columnas];
        reveladoJugador1 = new boolean[filas][columnas];
        reveladoJugador2 = new boolean[filas][columnas];
        contadorMinasJugador1 = new int[filas][columnas];
        contadorMinasJugador2 = new int[filas][columnas];
        botonesJugador1 = new JButton[filas][columnas];
        botonesJugador2 = new JButton[filas][columnas];

        inicializarTablero();
        colocarMinas();
        contarMinasCercanas();

        JPanel panelTurno0 = new JPanel(new FlowLayout());
        panelTurno0.setBackground(new Color(44, 62, 80));
        JPanel panelTurno = new JPanel(new FlowLayout());
        panelTurno.setBackground(new Color(44, 62, 80));

        turnoLabel = new JLabel("Turno: Jugador 1", SwingConstants.CENTER);
        turnoLabel.setForeground(new Color(255, 255, 255));
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panelTurno0.add(turnoLabel);

        labelTiempo = new JLabel("Tiempo: 0s");
        labelTiempo.setForeground(Color.WHITE);
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTurno0.add(labelTiempo);

        probabilityLabelJugador1 = new JLabel("Probabilidad de ganar Jugador 1: 0%");
        probabilityLabelJugador1.setForeground(Color.WHITE);
        probabilityLabelJugador1.setFont(new Font("Arial", Font.BOLD, 16));
        panelTurno.add(probabilityLabelJugador1);

        probabilityBarJugador1 = new JProgressBar(0, 100);
        probabilityBarJugador1.setValue(0);
        panelTurno.add(probabilityBarJugador1);

        probabilityLabelJugador2 = new JLabel("Probabilidad de ganar Jugador 2: 0%");
        probabilityLabelJugador2.setForeground(Color.WHITE);
        probabilityLabelJugador2.setFont(new Font("Arial", Font.BOLD, 16));
        panelTurno.add(probabilityLabelJugador2);

        probabilityBarJugador2 = new JProgressBar(0, 100);
        probabilityBarJugador2.setValue(0);
        panelTurno.add(probabilityBarJugador2);

        panelPrincipal.add(panelTurno0, BorderLayout.NORTH);
        panelPrincipal.add(panelTurno, BorderLayout.SOUTH);

        turnoJugador1 = true;

        crearBotones();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void crearBotones() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JButton botonJugador1 = new JButton();
                botonJugador1.setPreferredSize(new Dimension(50, 50));
                botonJugador1.setBackground(new Color(220, 220, 220));
                botonJugador1.setFont(new Font("Arial", Font.BOLD, 20));
                botonJugador1.setFocusable(false);

                JButton botonJugador2 = new JButton();
                botonJugador2.setPreferredSize(new Dimension(50, 50));
                botonJugador2.setBackground(new Color(220, 220, 220));
                botonJugador2.setFont(new Font("Arial", Font.BOLD, 20));
                botonJugador2.setFocusable(false);

                int fila = i;
                int columna = j;

                botonJugador1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (turnoJugador1 && SwingUtilities.isLeftMouseButton(e)) {
                            botonPresionado(fila, columna, true);
                        }else if (SwingUtilities.isRightMouseButton(e)) {
                            marcarMina(fila, columna, true);
                        }
                    }
                });

                botonJugador2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!turnoJugador1 && SwingUtilities.isLeftMouseButton(e)) {
                            botonPresionado(fila, columna, false);
                        }else if (SwingUtilities.isRightMouseButton(e)) {
                            marcarMina(fila, columna, false);
                        }
                    }
                });

                botonesJugador1[i][j] = botonJugador1;
                botonesJugador2[i][j] = botonJugador2;

                panelJugador1.add(botonJugador1);
                panelJugador2.add(botonJugador2);
            }
        }

        panelPrincipal.add(panelJugador1, BorderLayout.WEST);
        panelPrincipal.add(panelJugador2, BorderLayout.EAST);
        add(panelPrincipal, BorderLayout.CENTER);

    }

    private void inicializarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tableroJugador1[i][j] = '-';
                tableroJugador2[i][j] = '-';
                reveladoJugador1[i][j] = false;
                reveladoJugador2[i][j] = false;
                contadorMinasJugador1[i][j] = 0;
                contadorMinasJugador2[i][j] = 0;
            }
        }
    }

    private void colocarMinas() {
        int minasColocadas = 0;
        tiempo = 0;

        while (minasColocadas < numMinas) {
            int fila = (int) (Math.random() * filas);
            int columna = (int) (Math.random() * columnas);

            if (tableroJugador1[fila][columna] != '*') {
                tableroJugador1[fila][columna] = '*';
                minasColocadas++;
            }
        }

        minasColocadas = 0;

        while (minasColocadas < numMinas) {
            int fila = (int) (Math.random() * filas);
            int columna = (int) (Math.random() * columnas);

            if (tableroJugador2[fila][columna] != '*' && tableroJugador1[fila][columna] != '*') {
                tableroJugador2[fila][columna] = '*';
                minasColocadas++;
            }
        }
        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tiempo++;
                labelTiempo.setText("Tiempo: " + tiempo + "s");
                calcularProbabilidadDeGanar();
            }
        });

        timer.start();
    }

    private void contarMinasCercanas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int minasCercanasJugador1 = contarMinasCercanas(i, j, tableroJugador1);
                contadorMinasJugador1[i][j] = minasCercanasJugador1;

                int minasCercanasJugador2 = contarMinasCercanas(i, j, tableroJugador2);
                contadorMinasJugador2[i][j] = minasCercanasJugador2;
            }
        }
    }

    private int contarMinasCercanas(int fila, int columna, char[][] tablero) {
        int minasCercanas = 0;

        for (int k = fila - 1; k <= fila + 1; k++) {
            for (int l = columna - 1; l <= columna + 1; l++) {
                if (k >= 0 && k < filas && l >= 0 && l < columnas) {
                    if (tablero[k][l] == '*') {
                        minasCercanas++;
                    }
                }
            }
        }

        return minasCercanas;
    }

    private void botonPresionado(int fila, int columna, boolean jugador1) {
        if (jugador1 && !reveladoJugador1[fila][columna]) {
            turnoJugador1 = !turnoJugador1;
            turnoLabel.setText("Turno: Jugador " + (turnoJugador1 ? "1" : "2"));
            reveladoJugador1[fila][columna] = true;
            if (tableroJugador1[fila][columna] == '*') {
                gameOver("Jugador 1");
            } else if (contadorMinasJugador1[fila][columna] == 0) {
                expandirCasillas(fila, columna, tableroJugador1, reveladoJugador1);
            }
            actualizarBotones(true);
            if (verificarVictoria(true)) {
                victoria("Jugador 1");
            }
            turnoJugador1 = false;
        } else if (!jugador1 && !reveladoJugador2[fila][columna]) {
            turnoJugador1 = !turnoJugador1;
            turnoLabel.setText("Turno: Jugador " + (turnoJugador1 ? "1" : "2"));
            reveladoJugador2[fila][columna] = true;
            if (tableroJugador2[fila][columna] == '*') {
                gameOver("Jugador 2");
            } else if (contadorMinasJugador2[fila][columna] == 0) {
                expandirCasillas(fila, columna, tableroJugador2, reveladoJugador2);
            }
            actualizarBotones(false);
            if (verificarVictoria(false)) {
                victoria("Jugador 2");
            }
            turnoJugador1 = true;
        }
    }

    private void expandirCasillas(int fila, int columna, char[][] tablero, boolean[][] revelado) {
        for (int k = fila - 1; k <= fila + 1; k++) {
            for (int l = columna - 1; l <= columna + 1; l++) {
                if (k >= 0 && k < filas && l >= 0 && l < columnas) {
                    if (!revelado[k][l]) {
                        revelado[k][l] = true;
                        if (contadorMinasJugador1[k][l] == 0 && tablero == tableroJugador1) {
                            expandirCasillas(k, l, tableroJugador1, reveladoJugador1);
                        } else if (contadorMinasJugador2[k][l] == 0 && tablero == tableroJugador2) {
                            expandirCasillas(k, l, tableroJugador2, reveladoJugador2);
                        }
                    }
                }
            }
        }
    }

    private void actualizarBotones(boolean jugador1) {
        JButton[][] botones;
        boolean[][] revelado;
        int[][] contadorMinas;
        if (jugador1) {
            botones = botonesJugador1;
            revelado = reveladoJugador1;
            contadorMinas = contadorMinasJugador1;
        } else {
            botones = botonesJugador2;
            revelado = reveladoJugador2;
            contadorMinas = contadorMinasJugador2;
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JButton boton = botones[i][j];
                boton.setEnabled(!revelado[i][j]);

                if (revelado[i][j]) {
                    boton.setBackground(new Color(200, 200, 200));
                    int minasCercanas = contadorMinas[i][j];
                    if (minasCercanas > 0) {
                        boton.setText(Integer.toString(minasCercanas));
                    } else {
                        boton.setText("");
                    }
                }
            }
        }
    }

    private boolean verificarVictoria(boolean jugador1) {
        boolean[][] revelado;
        if (jugador1) {
            revelado = reveladoJugador1;
        } else {
            revelado = reveladoJugador2;
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!revelado[i][j] && tableroJugador1[i][j] != '*' && tableroJugador2[i][j] != '*') {
                    return false;
                }
            }
        }

        return true;
    }

    private void gameOver(String jugador) {
        JOptionPane.showMessageDialog(this, "¡Game Over! El jugador " + jugador + " ha perdido.", "Game Over", JOptionPane.INFORMATION_MESSAGE);

        reiniciarJuego();
    }

    private void victoria(String jugador) {
        JOptionPane.showMessageDialog(this, "¡Felicidades! El jugador " + jugador + " ha ganado.", "Victoria", JOptionPane.INFORMATION_MESSAGE);

        reiniciarJuego();
    }

    private void reiniciarJuego() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea jugar de nuevo?", "Reiniciar juego",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            new Main(filas, columnas, numMinas);
        } else {
            mostrarEstadisticas();

        }
    }

    private void calcularProbabilidadDeGanar() {
        int casillasJugadasJugador1 = 0;
        int casillasJugadasJugador2 = 0;
        int casillasTotales = filas * columnas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (reveladoJugador1[i][j]) {
                    casillasJugadasJugador1++;
                }
                if (reveladoJugador2[i][j]) {
                    casillasJugadasJugador2++;
                }
            }
        }

        int probabilidadJugador1 = (int) ((double) casillasJugadasJugador1 / casillasTotales * 100);
        int probabilidadJugador2 = (int) ((double) casillasJugadasJugador2 / casillasTotales * 100);

        probabilityLabelJugador1.setText("Probabilidad de ganar Jugador 1: " + probabilidadJugador1 + "%");
        probabilityBarJugador1.setValue(probabilidadJugador1);

        probabilityLabelJugador2.setText("Probabilidad de ganar Jugador 2: " + probabilidadJugador2 + "%");
        probabilityBarJugador2.setValue(probabilidadJugador2);
    }

    private void marcarMina(int fila, int columna, boolean jugador1) {
        JButton[][] botones;
        char[][] tablero;
        if (jugador1) {
            botones = botonesJugador1;
            tablero = tableroJugador1;
        } else {
            botones = botonesJugador2;
            tablero = tableroJugador2;
        }

        JButton boton = botones[fila][columna];
        if (!reveladoJugador1[fila][columna] || !reveladoJugador2[fila][columna]) {
            if (boton.getText().equals("")) {
                boton.setText("?");
                boton.setForeground(Color.RED);
                tablero[fila][columna] = '?';
            } else if (boton.getText().equals("?")) {
                boton.setText("");
                tablero[fila][columna] = '-';
            }
        }
    }
    private void mostrarEstadisticas() {
        // Calcular estadísticas
        double mediaJugador1 = calcularMedia(contadorMinasJugador1);
        double mediaJugador2 = calcularMedia(contadorMinasJugador2);
        double varianzaJugador1 = calcularVarianza(contadorMinasJugador1, mediaJugador1);
        double varianzaJugador2 = calcularVarianza(contadorMinasJugador2, mediaJugador2);
        double desviacionEstandarJugador1 = Math.sqrt(varianzaJugador1);
        double desviacionEstandarJugador2 = Math.sqrt(varianzaJugador2);

        // Crear ventana de estadísticas
        JFrame estadisticasFrame = new JFrame("Estadísticas");
        estadisticasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        estadisticasFrame.setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelEstadisticas = new JPanel(new GridLayout(5, 1));
        panelEstadisticas.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel mediaLabelJugador1 = new JLabel("Media aritmética Jugador 1: "+Double.toString(Math.round(mediaJugador1 * 100.0) / 100.0));

        JLabel varianzaLabelJugador1 = new JLabel("\nVarianza Jugador 1: "+Double.toString(Math.round(varianzaJugador1 * 100.0) / 100.0));

        JLabel desviacionLabelJugador1 = new JLabel("\nDesviación estándar Jugador 1: "+Double.toString(Math.round(desviacionEstandarJugador1 * 100.0) / 100.0));

        JLabel mediaLabelJugador2 = new JLabel("\nMedia aritmética Jugador 2: "+Double.toString(Math.round(mediaJugador2 * 100.0) / 100.0));

        JLabel varianzaLabelJugador2 = new JLabel("\nVarianza Jugador 2: "+Double.toString(Math.round(varianzaJugador2 * 100.0) / 100.0));

        JLabel desviacionLabelJugador2 = new JLabel("\nDesviación estándar Jugador 2: "+Double.toString(Math.round(desviacionEstandarJugador2 * 100.0) / 100.0));


        panelEstadisticas.add(mediaLabelJugador1);

        panelEstadisticas.add(varianzaLabelJugador1);

        panelEstadisticas.add(desviacionLabelJugador1);

        panelEstadisticas.add(mediaLabelJugador2);

        panelEstadisticas.add(varianzaLabelJugador2);

        panelEstadisticas.add(desviacionLabelJugador2);


        panelPrincipal.add(panelEstadisticas, BorderLayout.CENTER);
        estadisticasFrame.add(panelPrincipal);
        estadisticasFrame.pack();
        estadisticasFrame.setLocationRelativeTo(null);
        estadisticasFrame.setVisible(true);
    }

    private double calcularMedia(int[][] contadorMinas) {
        int suma = 0;
        int totalCasillas = filas * columnas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                suma += contadorMinas[i][j];
            }
        }

        return (double) suma / totalCasillas;
    }

    private double calcularVarianza(int[][] contadorMinas, double media) {
        int totalCasillas = filas * columnas;
        double sumaDiferencias = 0;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                double diferencia = contadorMinas[i][j] - media;
                sumaDiferencias += Math.pow(diferencia, 2);
            }
        }

        return sumaDiferencias / totalCasillas;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int filas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de filas y columnas:"));
            int columnas = filas;
            int numMinas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de minas:"));
            new Main(filas, columnas, numMinas);
        });
    }
}
