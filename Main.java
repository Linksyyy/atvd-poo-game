
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

class CartaCombatente extends JPanel {

    private final Combatente c;
    private final JProgressBar vida;

    public CartaCombatente(Combatente c, Color base) {
        this.c = c;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 280)); // Increased card size
        setBackground(base);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel nome = new JLabel(c.getNome(), SwingConstants.CENTER);
        nome.setForeground(Color.WHITE);

        vida = new JProgressBar(0, c.getPvMax());
        vida.setValue(c.getPv());
        vida.setStringPainted(true);

        String imagePath = "assets/" + c.getClass().getSimpleName() + ".png";
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(180, 240, java.awt.Image.SCALE_SMOOTH); // Increased image size
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);

        add(nome, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(vida, BorderLayout.SOUTH);
    }

    public Combatente getCombatente() {
        return c;
    }

    public void atualizar() {
        vida.setValue(c.getPv());
        if (!c.vivo()) {
            setBackground(Color.DARK_GRAY);
        }
    }

    public void selecionar(boolean s) {
        setBorder(BorderFactory.createLineBorder(
                s ? Color.YELLOW : Color.BLACK, 3));
    }
}

public class Main {

    private int turno = 0;

    private CartaCombatente atacanteSelecionado;
    private CartaCombatente alvoSelecionado;

    private List<CartaCombatente> cartasJogador;
    private List<CartaCombatente> cartasInimigo;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        JFrame f = new JFrame("Torneio de Cartas");
        f.setLayout(new BorderLayout());

        JPanel jogador = new JPanel();
        JPanel inimigo = new JPanel();

        jogador.setBackground(new Color(80, 0, 120));
        inimigo.setBackground(new Color(90, 0, 0));

        cartasJogador = criarTime(
                jogador,
                new Color(120, 0, 180),
                List.of(
                        new Guardiao("Guardião"),
                        new Arcanista("Mago"),
                        new Cacador("Caçador")
                ),
                true
        );
        cartasInimigo = criarTime(
                inimigo,
                new Color(140, 20, 20),
                List.of(
                        new Guardiao("Inimigo A"),
                        new Arcanista("Inimigo B"),
                        new Cacador("Inimigo C")
                ),
                false
        );

        JLabel turnos = new JLabel("Turnos: " + String.valueOf(turno));
        turnos.setBackground(Color.BLACK);
        turnos.setForeground(Color.WHITE);
        turnos.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton atacar = new JButton("Atacar");
        atacar.setBackground(Color.BLACK);
        atacar.setForeground(Color.WHITE);

        JPanel menu = new JPanel();
        menu.setBackground(Color.BLACK);
        menu.setForeground(Color.WHITE);
        menu.setLayout(new BorderLayout());

        atacar.addActionListener(e -> {
            if (atacanteSelecionado != null && alvoSelecionado != null) {
                turno++;
                turnos.setText("Turnos: " + String.valueOf(turno));

                Combatente a = atacanteSelecionado.getCombatente();
                Combatente b = alvoSelecionado.getCombatente();
                if (a.vivo() && b.vivo()) {
                    b.receberDano(a.atacar());
                    atacanteSelecionado.atualizar();
                    alvoSelecionado.atualizar();

                    atacar.setEnabled(false);
                    new javax.swing.Timer(1000 , ev -> { 
                        executarTurnoInimigo();
                        atacar.setEnabled(true);

                        ((javax.swing.Timer)ev.getSource()).stop();
                    }).start();
                }
            }
        });

        menu.add(turnos, BorderLayout.WEST);
        menu.add(atacar, BorderLayout.CENTER);

        f.add(jogador, BorderLayout.SOUTH);
        f.add(inimigo, BorderLayout.NORTH);
        f.add(menu, BorderLayout.CENTER);

        f.setSize(1000, 700); // Increased main frame size
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private List<CartaCombatente> criarTime(
            JPanel painel,
            Color cor,
            List<Combatente> combatentes,
            boolean jogador
    ) {
        List<CartaCombatente> cartas = new ArrayList<>();

        for (Combatente c : combatentes) {
            CartaCombatente carta = new CartaCombatente(c, cor);
            cartas.add(carta);

            carta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!c.vivo()) {
                        return;
                    }

                    if (jogador) {
                        if (atacanteSelecionado != null) {
                            atacanteSelecionado.selecionar(false);
                        }
                        atacanteSelecionado = carta;
                        carta.selecionar(true);
                    } else {
                        if (alvoSelecionado != null) {
                            alvoSelecionado.selecionar(false);
                        }
                        alvoSelecionado = carta;
                        carta.selecionar(true);
                    }
                }
            });

            painel.add(carta);
        }
        return cartas;
    }
    private void executarTurnoInimigo() {
        Random random = new Random();

        List<CartaCombatente> inimigosVivos = cartasInimigo.stream().filter(c -> c.getCombatente().vivo()).toList();
        List<CartaCombatente> jogadoresVivos = cartasJogador.stream().filter(c -> c.getCombatente().vivo()).toList();
        if(inimigosVivos.isEmpty() || jogadoresVivos.isEmpty() ) {
            System.out.println("Fim de jogo");
            return;
        }
        CartaCombatente inimigoAtacante = inimigosVivos.get(random.nextInt(inimigosVivos.size()));
        CartaCombatente jogadorAlvo = jogadoresVivos.get(random.nextInt(jogadoresVivos.size()));

        Combatente a = inimigoAtacante.getCombatente(); 
        Combatente b = jogadorAlvo.getCombatente(); 

        b.receberDano(a.atacar());
        jogadorAlvo.atualizar();
    }
}
