package br.com.biblioteca.ui;

import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PainelLogin extends JPanel {

    private final TelaPrincipal  frame;
    private final UsuarioService service = new UsuarioService();

    private static final Color VERDE    = new Color(99, 153, 34);
    private static final Color ABA_AZUL = new Color(24, 95, 165);
    private static final Color BORDA    = new Color(200, 208, 220);

    public PainelLogin(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        rebuild();
    }

    private void rebuild() {
        removeAll();

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(Theme.CONTENT_BG);
        bg.setPreferredSize(new Dimension(900, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(buildCard(), gbc);

        JScrollPane sp = new JScrollPane(bg);
        sp.setBorder(null);
        sp.getViewport().setBackground(Theme.CONTENT_BG);
        sp.getVerticalScrollBar().setUnitIncrement(10);

        add(sp, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel buildCard() {
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(520, 520));
        c.setMinimumSize(new Dimension(520, 520));
        c.setMaximumSize(new Dimension(520, 520));
        c.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDA, 1, true),
            new EmptyBorder(0, 0, 30, 0)
        ));

        c.add(Box.createVerticalStrut(30));

        //Ícone
        JPanel circleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        circleRow.setOpaque(false);
        circleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        JPanel circle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(228, 238, 252));
                g2.fillOval(0, 0, 70, 70);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(70, 70));
        circle.setLayout(new BorderLayout());
        JLabel icoB = new JLabel("B", SwingConstants.CENTER);
        icoB.setFont(Theme.bold(34));
        icoB.setForeground(ABA_AZUL);
        circle.add(icoB);
        circleRow.add(circle);
        c.add(circleRow);

        c.add(Box.createVerticalStrut(14));

        JLabel titulo = new JLabel("Biblioteca", SwingConstants.CENTER);
        titulo.setFont(Theme.bold(22));
        titulo.setForeground(Theme.TEXT_PRIMARY);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        c.add(titulo);

        c.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Sistema de Gestão de Acervo", SwingConstants.CENTER);
        sub.setFont(Theme.plain(14));
        sub.setForeground(Theme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        c.add(sub);

        c.add(Box.createVerticalStrut(28));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDA);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        c.add(sep);

        c.add(formEntrar());

        return c;
    }

    //FORM ENTRAR 
    private JPanel formEntrar() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(30, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 6, 0);

        JTextField   fEmail = (JTextField) campo("seu@email.com");
        JPasswordField fSenha = new JPasswordField();
        estilizarCampo(fSenha);

        gbc.gridy = 0; form.add(rotulo("E-mail"), gbc);
        gbc.gridy = 1; form.add(fEmail, gbc);
        gbc.gridy = 2; form.add(rotulo("Senha"), gbc);
        gbc.gridy = 3; form.add(fSenha, gbc);

        gbc.gridy  = 4;
        gbc.insets = new Insets(24, 0, 0, 0);
        form.add(botaoVerde("Acessar", ev -> {
            String email = fEmail.getText().trim();
            String senha = new String(fSenha.getPassword());

            if (email.isBlank() || email.equals("seu@email.com")) {
                JOptionPane.showMessageDialog(frame, "Preencha o e-mail.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (senha.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Preencha a senha.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //Autentica no banco se as informações estiverem corretas — tabela usuario 
            try {
                Usuario u = service.autenticar(email, senha);
                if (u == null) {
                    JOptionPane.showMessageDialog(frame,
                        "E-mail ou senha incorretos, ou usuário inativo.",
                        "Acesso negado", JOptionPane.ERROR_MESSAGE);
                } else {
                    frame.showView("dashboard");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                    "Erro ao conectar com o banco:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }), gbc);

        return form;
    }


    private JLabel rotulo(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(Theme.plain(13));
        l.setForeground(Theme.TEXT_SECONDARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 2, 2, 0));
        return l;
    }

    private JComponent campo(String placeholder) {
        JTextField f = new JTextField();
        estilizarCampo(f);
        f.setText(placeholder);
        f.setForeground(new Color(160, 170, 185));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(Theme.TEXT_PRIMARY);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isBlank()) {
                    f.setText(placeholder);
                    f.setForeground(new Color(160, 170, 185));
                }
            }
        });
        return f;
    }

    private void estilizarCampo(JTextField f) {
        f.setFont(Theme.plain(14));
        f.setPreferredSize(new Dimension(340, 44));
        f.setMaximumSize(new Dimension(340, 44));
        f.setMinimumSize(new Dimension(340, 44));
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDA, 1, true),
            new EmptyBorder(8, 14, 8, 14)
        ));
        f.setBackground(Color.WHITE);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JPanel botaoVerde(String txt, ActionListener acao) {
        JPanel w = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        w.setOpaque(false);
        w.setPreferredSize(new Dimension(340, 48));
        w.setMaximumSize(new Dimension(340, 48));
        w.setMinimumSize(new Dimension(340, 48));

        JButton btn = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE.darker() : VERDE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(340, 48));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.bold(14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(acao);
        w.add(btn);
        return w;
    }
}