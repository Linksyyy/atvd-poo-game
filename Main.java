
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
        setPreferredSize(new Dimension(200, 280));
        setBackground(base);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel nome = new JLabel(c.getNome(), SwingConstants.CENTER);
        nome.setForeground(Color.WHITE);

        vida = new JProgressBar(0, c.getPvMax());
        vida.setForeground(Color.GREEN.darker());
        vida.setValue(c.getPv());
        vida.setStringPainted(true);

        String imagePath = "assets/" + c.getClass().getSimpleName() + ".png";
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(180, 240, java.awt.Image.SCALE_SMOOTH);
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
        double porcentagem = (double) c.getPv()/c.getPvMax();


        if(porcentagem <= 0.6 && porcentagem > 0.3) {
            vida.setForeground(new Color(200, 170, 0));
        } else if(porcentagem <= 0.3) {
            vida.setForeground(new Color(150, 0, 0));

        }


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

    private JFrame f;
    private JButton atacar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        this.f = new JFrame("Torneio de Cartas");
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

        this.atacar = new JButton("Atacar");
        atacar.setBackground(Color.BLACK);
        atacar.setForeground(Color.WHITE);

        JPanel menu = new JPanel();
        menu.setBackground(Color.BLACK);
        menu.setForeground(Color.WHITE);
        menu.setLayout(new BorderLayout());

        atacar.addActionListener(e -> {
            if (atacanteSelecionado != null && alvoSelecionado != null) {
                if (atacanteSelecionado.getCombatente().vivo() && alvoSelecionado.getCombatente().vivo()) {
                    atacar.setEnabled(false); 

                    animarAtaque(atacanteSelecionado, alvoSelecionado, () -> {
                        turno++;
                        turnos.setText("Turnos: " + String.valueOf(turno));

                        Combatente a = atacanteSelecionado.getCombatente();
                        Combatente b = alvoSelecionado.getCombatente();
                        
                        int dano = a.atacar();
                        mostrarDanoAnimado(alvoSelecionado, dano);

                        b.receberDano(dano);
                        atacanteSelecionado.atualizar();
                        alvoSelecionado.atualizar();
                        
                        new javax.swing.Timer(1000, ev -> {
                            ((javax.swing.Timer) ev.getSource()).stop();
                            executarTurnoInimigo(() -> {
                                atacar.setEnabled(true);
                            });
                        }).start();
                    });
                } else {
                    atacar.setEnabled(true);
                }
            }
        });
        menu.add(turnos, BorderLayout.WEST);
        menu.add(atacar, BorderLayout.CENTER);

        f.add(jogador, BorderLayout.SOUTH);
        f.add(inimigo, BorderLayout.NORTH);
        f.add(menu, BorderLayout.CENTER);

        f.setSize(1000, 700);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    
    private void mostrarDanoAnimado(Component alvo, int dano) {
        JLabel danoLabel = new JLabel("-" + dano);
        danoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        danoLabel.setForeground(Color.RED);
        danoLabel.setSize(danoLabel.getPreferredSize());

        Point pontoInicial = SwingUtilities.convertPoint(alvo.getParent(), alvo.getLocation(), f.getLayeredPane());
        int x = pontoInicial.x + (alvo.getWidth() / 2) - (danoLabel.getWidth() / 2);
        int y = pontoInicial.y + (alvo.getHeight() / 2) - (danoLabel.getHeight() / 2);
        danoLabel.setLocation(x, y);

        f.getLayeredPane().add(danoLabel, JLayeredPane.POPUP_LAYER);

        int duracao = 800;
        int quadros = 30;
        int intervalo = duracao / quadros;
        int pixelsParaSubir = 60;

        javax.swing.Timer timerDano = new javax.swing.Timer(intervalo, null);
        final int[] passoAtual = {0};

        timerDano.addActionListener(e -> {
            passoAtual[0]++;
            
            danoLabel.setLocation(x, y - (pixelsParaSubir * passoAtual[0] / quadros));

            if (passoAtual[0] >= quadros) {
                timerDano.stop();
                f.getLayeredPane().remove(danoLabel);
                f.getLayeredPane().repaint(danoLabel.getBounds());
            }
        });

        timerDano.start();
    }

    private void animarAtaque(CartaCombatente atacante, CartaCombatente alvo, Runnable onAnimationEnd) {
        Container parentOriginal = atacante.getParent();
        int indiceOriginal = -1;
        for (int i = 0; i < parentOriginal.getComponentCount(); i++) {
            if (parentOriginal.getComponent(i) == atacante) {
                indiceOriginal = i;
                break;
            }
        }
        final int indiceFinal = indiceOriginal;

        Point pontoInicial = SwingUtilities.convertPoint(parentOriginal, atacante.getLocation(), f.getLayeredPane());
        Point pontoFinal = SwingUtilities.convertPoint(alvo.getParent(), alvo.getLocation(), f.getLayeredPane());

        parentOriginal.remove(atacante);
        parentOriginal.revalidate();
        parentOriginal.repaint();

        atacante.setBounds(pontoInicial.x, pontoInicial.y, atacante.getWidth(), atacante.getHeight());
        f.getLayeredPane().add(atacante, JLayeredPane.DRAG_LAYER);

        int duracao = 200;
        int quadros = 20;
        int intervalo = duracao / quadros;

        javax.swing.Timer timerIda = new javax.swing.Timer(intervalo, null);
        final int[] passoAtual = {0};

        timerIda.addActionListener(e -> {
            passoAtual[0]++;
            float progresso = (float) passoAtual[0] / quadros;
            int x = Math.round(pontoInicial.x + (pontoFinal.x - pontoInicial.x) * progresso);
            int y = Math.round(pontoInicial.y + (pontoFinal.y - pontoInicial.y) * progresso);
            atacante.setLocation(x, y);

            if (passoAtual[0] >= quadros) {
                timerIda.stop();

                javax.swing.Timer timerVolta = new javax.swing.Timer(intervalo, null);
                final int[] passoVolta = {0};

                timerVolta.addActionListener(ev -> {
                    passoVolta[0]++;
                    float progressoVolta = (float) passoVolta[0] / quadros;
                    int xVolta = Math.round(pontoFinal.x + (pontoInicial.x - pontoFinal.x) * progressoVolta);
                    int yVolta = Math.round(pontoFinal.y + (pontoInicial.y - pontoFinal.y) * progressoVolta);
                    atacante.setLocation(xVolta, yVolta);

                    if (passoVolta[0] >= quadros) {
                        timerVolta.stop();
                        f.getLayeredPane().remove(atacante);
                        if (indiceFinal != -1) {
                            parentOriginal.add(atacante, indiceFinal);
                        } else {
                            parentOriginal.add(atacante);
                        }
                        parentOriginal.revalidate();
                        parentOriginal.repaint();
                        onAnimationEnd.run();
                    }
                });
                timerVolta.start();
            }
        });
        timerIda.start();
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

    private void executarTurnoInimigo(Runnable onTurnoInimigoEnd) {
        Random random = new Random();

        List<CartaCombatente> inimigosVivos = cartasInimigo.stream().filter(c -> c.getCombatente().vivo()).toList();
        List<CartaCombatente> jogadoresVivos = cartasJogador.stream().filter(c -> c.getCombatente().vivo()).toList();
        if (inimigosVivos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    f, "🏆 Você venceu!\nTodos os inimigos foram derrotados.", "Vitória", JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        if (jogadoresVivos.isEmpty()) {
            JOptionPane.showMessageDialog(f, "💀 GAME OVER", "Derrota", JOptionPane.ERROR_MESSAGE);
            atacar.setEnabled(false);
            return;
        }

        CartaCombatente inimigoAtacante = inimigosVivos.get(random.nextInt(inimigosVivos.size()));
        CartaCombatente jogadorAlvo = jogadoresVivos.get(random.nextInt(jogadoresVivos.size()));

        animarAtaque(inimigoAtacante, jogadorAlvo, () -> {
            Combatente a = inimigoAtacante.getCombatente();
            Combatente b = jogadorAlvo.getCombatente();
            
            int dano = a.atacar();
            mostrarDanoAnimado(jogadorAlvo, dano);

            b.receberDano(dano);
            jogadorAlvo.atualizar();
            inimigoAtacante.atualizar();


            if (cartasJogador.stream().filter(c -> c.getCombatente().vivo()).toList().isEmpty()) {
                JOptionPane.showMessageDialog(
                        f, "💀 GAME OVER\nVocê foi derrotado...", "Derrota", JOptionPane.ERROR_MESSAGE
                );
            } else if (onTurnoInimigoEnd != null) {
                onTurnoInimigoEnd.run();
            }
        });
    }
}
