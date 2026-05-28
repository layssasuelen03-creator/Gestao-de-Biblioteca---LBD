package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


public class PainelLogin extends JPanel {

    private final TelaPrincipal frame;
    private boolean showingLogin = true;

    private static final Color VERDE    = new Color(99, 153, 34);
    private static final Color ABA_AZUL = new Color(24, 95, 165);
    private static final Color BORDA    = new Color(200, 208, 220);

    //referências às abas para trocar estilo
    private JLabel  abaEntrar, abaCadastrar;
    private JPanel  wrapEntrar, wrapCadastrar;
    private JPanel  card; //card principal

    public PainelLogin(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        rebuild();
    }

    private void rebuild() {
        removeAll();

        //Container centralizado do card
        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(Theme.CONTENT_BG);
        bg.setPreferredSize(new Dimension(900, 700));

        card = buildCard();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(card, gbc);

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
        c.setPreferredSize(new Dimension(520, 760));
        c.setMinimumSize(new Dimension(520, 760));
        c.setMaximumSize(new Dimension(520, 760));
        c.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDA, 1, true),
            new EmptyBorder(0, 0, 20, 0)
        ));

        //Ícone 
        c.add(Box.createVerticalStrut(18));

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

        //Título 
        c.add(Box.createVerticalStrut(10));
        JLabel titulo = new JLabel("Biblioteca", SwingConstants.CENTER);
        titulo.setFont(Theme.bold(22));
        titulo.setForeground(Theme.TEXT_PRIMARY);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        c.add(titulo);

        c.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Sistema de Gestao de Acervo", SwingConstants.CENTER);
        sub.setFont(Theme.plain(18));
        sub.setForeground(Theme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        c.add(sub);

        c.add(Box.createVerticalStrut(18));

        //Linha
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDA);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        c.add(sep);

        //Abas 
        JPanel abaRow = new JPanel(new GridLayout(1, 2, 0, 0));
        abaRow.setBackground(Color.WHITE);
        abaRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        abaEntrar    = abaLbl("Entrar",    true);
        abaCadastrar = abaLbl("Cadastrar", false);
        wrapEntrar   = abaWrap(abaEntrar,    true);
        wrapCadastrar= abaWrap(abaCadastrar, false);

        wrapEntrar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { mudarAba(true); }
        });
        wrapCadastrar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { mudarAba(false); }
        });

        abaRow.add(wrapEntrar);
        abaRow.add(wrapCadastrar);
        c.add(abaRow);

        c.add(formAtual());

        return c;
    }

    private void mudarAba(boolean login) {
        if (showingLogin == login) return;
        showingLogin = login;

        //Atualiza estilo das abas
        abaEntrar.setFont(login ? Theme.bold(13) : Theme.plain(13));
        abaEntrar.setForeground(login ? ABA_AZUL : Theme.TEXT_SECONDARY);

        abaEntrar.setHorizontalAlignment(SwingConstants.CENTER);
        abaEntrar.setPreferredSize(new Dimension(200, 60));

        abaCadastrar.setFont(!login ? Theme.bold(13) : Theme.plain(13));
        abaCadastrar.setForeground(!login ? ABA_AZUL : Theme.TEXT_SECONDARY);

        abaCadastrar.setHorizontalAlignment(SwingConstants.CENTER);
        abaCadastrar.setPreferredSize(new Dimension(200, 60));

        wrapEntrar.setBorder(login
            ? new MatteBorder(0, 0, 2, 0, ABA_AZUL)
            : new EmptyBorder(0, 0, 2, 0));
        wrapCadastrar.setBorder(!login
            ? new MatteBorder(0, 0, 2, 0, ABA_AZUL)
            : new EmptyBorder(0, 0, 2, 0));

        //Troca o último componente do card
        card.remove(card.getComponentCount() - 1);
        card.add(formAtual());
        card.revalidate();
        card.repaint();
    }

    private JPanel formAtual() {
        return showingLogin ? formEntrar() : formCadastrar();
    }

    //FORM ENTRAR 
    private JPanel formEntrar() {

    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(Color.WHITE);
    form.setBorder(new EmptyBorder(30, 40, 20, 40));

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(6, 0, 6, 0);

    JTextField usuario = (JTextField) campo("seu usuario", false);
    JComponent senha = campo("", true);

    // Usuário
    gbc.gridy = 0;
    form.add(rotulo("Usuario"), gbc);

    gbc.gridy = 1;
    form.add(usuario, gbc);

    // Senha
    gbc.gridy = 2;
    form.add(rotulo("Senha"), gbc);

    gbc.gridy = 3;
    form.add(senha, gbc);

    // Botão
    gbc.gridy = 4;
    gbc.insets = new Insets(20, 0, 10, 0);

    form.add(botaoVerde("Acessar", ev -> {

        JOptionPane.showMessageDialog(
            frame,
            "Login realizado!",
            "Acesso",
            JOptionPane.INFORMATION_MESSAGE
        );

        frame.showView("dashboard");

    }), gbc);

    // Texto
    gbc.gridy = 5;
    gbc.insets = new Insets(8, 0, 8, 0);

    JLabel ou = new JLabel("ou entre com", SwingConstants.CENTER);

    ou.setFont(Theme.plain(11));

    ou.setForeground(Theme.TEXT_MUTED);

    form.add(ou, gbc);

    // Botões sociais
    gbc.gridy = 6;

    JPanel soc = new JPanel(new GridLayout(1, 3, 8, 0));

    soc.setOpaque(false);

    soc.add(socBtn("Google"));
    soc.add(socBtn("Facebook"));
    soc.add(socBtn("E-mail"));

    form.add(soc, gbc);

    return form;
   }

    //FORM CADASTRAR 
    private JPanel formCadastrar() {

    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(Color.WHITE);
    form.setBorder(new EmptyBorder(18, 40, 20, 40));

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(6, 0, 6, 0);

    JTextField usuario = (JTextField) campo("nome de usuario", false);
    JTextField email = (JTextField) campo("seu@email.com", false);

    JComponent senha = campo("", true);

    JTextField confirmacao =
        (JTextField) campo("repita a senha", false);

    // Usuário
    gbc.gridy = 0;
    form.add(rotulo("Usuario"), gbc);

    gbc.gridy = 1;
    form.add(usuario, gbc);

    // Email
    gbc.gridy = 2;
    form.add(rotulo("E-mail"), gbc);

    gbc.gridy = 3;
    form.add(email, gbc);

    // Senha
    gbc.gridy = 4;
    form.add(rotulo("Senha"), gbc);

    gbc.gridy = 5;
    form.add(senha, gbc);

    // Confirmação
    gbc.gridy = 6;
    form.add(rotulo("Confirmacao"), gbc);

    gbc.gridy = 7;
    form.add(confirmacao, gbc);

    // Botão
    gbc.gridy = 8;
    gbc.insets = new Insets(20, 0, 0, 0);

    form.add(botaoVerde("Cadastrar", ev -> {

        JOptionPane.showMessageDialog(
            frame,
            "Conta criada com sucesso!",
            "Cadastro",
            JOptionPane.INFORMATION_MESSAGE
        );

    }), gbc);

    return form;
    }

    // Helpers 

    private JLabel abaLbl(String txt, boolean ativo) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(ativo ? Theme.bold(13) : Theme.plain(13));
        l.setForeground(ativo ? ABA_AZUL : Theme.TEXT_SECONDARY);
        return l;
    }

    private JPanel abaWrap(JLabel lbl, boolean ativo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.add(lbl, BorderLayout.CENTER);
        p.setBorder(ativo
            ? new MatteBorder(0, 0, 2, 0, ABA_AZUL)
            : new EmptyBorder(0, 0, 2, 0));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return p;
    }

    private JLabel rotulo(String txt) {

    JLabel l = new JLabel(txt);

    l.setFont(Theme.plain(18));
    l.setForeground(Theme.TEXT_SECONDARY);
    l.setAlignmentX(Component.LEFT_ALIGNMENT);
    l.setHorizontalAlignment(SwingConstants.LEFT);
    l.setBorder(new EmptyBorder(0, 2, 4, 0));

    return l;
    }

    private JComponent campo(String placeholder, boolean senha) {

    JTextField f = senha ? new JPasswordField() : new JTextField();

    f.setFont(Theme.plain(18));

    f.setPreferredSize(new Dimension(340, 48));
    f.setMaximumSize(new Dimension(340, 48));
    f.setMinimumSize(new Dimension(340, 48));

    f.setBorder(new CompoundBorder(
        new LineBorder(BORDA, 1, true),
        new EmptyBorder(9, 16, 9, 16)
    ));

    f.setBackground(Color.WHITE);

    f.setAlignmentX(Component.LEFT_ALIGNMENT);

    if (!senha && !placeholder.isEmpty()) {

        f.setText(placeholder);

        f.setForeground(new Color(160, 170, 185));

        f.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {

                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(Theme.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

                if (f.getText().isBlank()) {
                    f.setText(placeholder);
                    f.setForeground(new Color(160, 170, 185));
                }
            }
        });
    }

    return f;
    }

    private JPanel botaoVerde(String txt, ActionListener acao) {

    JPanel w = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    w.setOpaque(false);

    w.setPreferredSize(new Dimension(340, 52));
    w.setMaximumSize(new Dimension(340, 52));
    w.setMinimumSize(new Dimension(340, 52));

    JButton btn = new JButton(txt) {

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                getModel().isRollover()
                    ? VERDE.darker()
                    : VERDE
            );

            g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                10,
                10
            );

            g2.dispose();

            super.paintComponent(g);
        }
    };

    btn.setPreferredSize(new Dimension(340, 52));
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setContentAreaFilled(false);
    btn.setOpaque(false);

    btn.setForeground(Color.WHITE);
    btn.setFont(Theme.bold(18));

    btn.setCursor(
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    );

    btn.addActionListener(acao);
    w.setAlignmentX(Component.CENTER_ALIGNMENT);
    w.add(btn);

    return w;
    }

    private JButton socBtn(String txt) {

    JButton b = new JButton(txt);

    b.setPreferredSize(new Dimension(110, 38));
    b.setMaximumSize(new Dimension(110, 38));
    b.setMinimumSize(new Dimension(110, 38));

    b.setFont(Theme.plain(12));
    b.setForeground(Theme.TEXT_SECONDARY);
    b.setBackground(Color.WHITE);
    b.setBorder(new LineBorder(BORDA, 1, true));
    b.setFocusPainted(false);
    b.setCursor(
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    );

    return b;
    }
}