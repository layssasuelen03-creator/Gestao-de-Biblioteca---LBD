package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TelaPrincipal extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private JPanel contentArea;
    private JLabel pgTitle, pgSub;
    private NavItem activeNav;

    public PainelDashboard   painelDashboard;
    public PainelLivros      painelLivros;
    public PainelAutores     painelAutores;
    public PainelCategoria   painelCategoria;
    public PainelEditoras    painelEditoras;
    public PainelUsuarios    painelUsuarios;
    public PainelEmprestimos painelEmprestimos;
    public PainelDevolucoes  painelDevolucoes;
    public PainelEstoque     painelEstoque;
    public PainelRelatorios  painelRelatorios;
    public PainelLogin       painelLogin;

    public TelaPrincipal() {
        setTitle("Biblioteca — Sistema de Gestao");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(980, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.SIDEBAR_BG);
        add(buildSidebar(),  BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);
        initPanels();
    }

   
    //SIDEBAR
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.SIDEBAR_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);

        //Logo 
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        logoRow.setOpaque(false);
        logoRow.setBorder(new MatteBorder(0, 0, 1, 0, Theme.SIDEBAR_BORDER));
        logoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        Components.RoundedPanel logoIco = new Components.RoundedPanel(9);
        logoIco.setBackground(Theme.ACCENT);
        logoIco.setPreferredSize(new Dimension(36, 36));
        logoIco.setLayout(new BorderLayout());
        JLabel logoChar = new JLabel("B", SwingConstants.CENTER);
        logoChar.setFont(Theme.bold(18));
        logoChar.setForeground(Color.WHITE);
        logoIco.add(logoChar);

        JLabel logoTxt = new JLabel("Biblioteca");
        logoTxt.setFont(Theme.FONT_LOGO);
        logoTxt.setForeground(Color.WHITE);
        logoRow.add(logoIco);
        logoRow.add(logoTxt);
        sidebar.add(logoRow);

        //Nav items 
        addSection(sidebar, "Principal");
        NavItem navDash = addNav(sidebar, "  Dashboard",   "dashboard");

        addSection(sidebar, "Acervo");
        addNav(sidebar, "  Livros",       "livros");
        addNav(sidebar, "  Autores",      "autores");
        addNav(sidebar, "  Categorias",   "categorias");
        addNav(sidebar, "  Editoras",     "editoras");


        addSection(sidebar, "Membros");
        addNav(sidebar, "  Usuarios",     "usuarios");
        addNav(sidebar, "  Emprestimos",  "emprestimos");
        addNav(sidebar, "  Devolucoes",   "devolucoes");

        addSection(sidebar, "Gestao");
        addNav(sidebar, "  Estoque",      "estoque");
        addNav(sidebar, "  Relatorios",   "relatorios");
 
    sidebar.add(Box.createVerticalGlue());

//Botão SAIR 
JPanel sairRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
sairRow.setOpaque(false);
sairRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
sairRow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

JLabel sairLbl = new JLabel("     Sair do sistema");
sairLbl.setFont(Theme.plain(13));
sairLbl.setForeground(new Color(226, 75, 74));

sairRow.add(sairLbl);

sairRow.addMouseListener(new MouseAdapter() {

    @Override
    public void mouseClicked(MouseEvent e) {

        int opt = JOptionPane.showConfirmDialog(
            TelaPrincipal.this,
            "Deseja sair do sistema?",
            "Confirmar saida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (opt == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        sairLbl.setForeground(Color.WHITE);
        sairRow.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        sairLbl.setForeground(new Color(226, 75, 74));
        sairRow.repaint();
    }
});

sidebar.add(sairRow);

//Footer: avatar + nome/login 
JPanel footer = new JPanel();
footer.setOpaque(false);
footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
footer.setBorder(new EmptyBorder(10, 12, 10, 12));
footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

//linha superior: avatar + nome/login
JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
userRow.setOpaque(false);

userRow.add(new Components.Avatar("Administrador", Theme.ACCENT, 32));

JPanel names = new JPanel();
names.setOpaque(false);
names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));

JLabel nameL = new JLabel("Administrador");
nameL.setFont(Theme.bold(12));
nameL.setForeground(Color.WHITE);

JLabel roleL = new JLabel("Login");
roleL.setFont(Theme.plain(11));
roleL.setForeground(Theme.SIDEBAR_TEXT);

roleL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

roleL.addMouseListener(new MouseAdapter() {

    @Override
    public void mouseClicked(MouseEvent e) {

        if (activeNav != null) {
            activeNav.setActive(false);
        }

        showView("login");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        roleL.setForeground(Color.WHITE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        roleL.setForeground(Theme.SIDEBAR_TEXT);
    }
});

names.add(nameL);
names.add(roleL);

userRow.add(names);

footer.add(userRow);

sidebar.add(footer);

activeNav = navDash;
navDash.setActive(true);

return sidebar;
    }

    private void addSection(JPanel parent, String text) {
    JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 22, 0));
    wrap.setOpaque(false);
    wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
    JLabel l = Components.sectionLabel(text);
    wrap.add(l);
    parent.add(Box.createVerticalStrut(14));
    parent.add(wrap);
    }

    private NavItem addNav(JPanel parent, String label, String view) {
        NavItem item = new NavItem(label, view, this);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        parent.add(item);
        return item;
    }

    
    //MAIN AREA
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.CONTENT_BG);

        // Topbar — mesma cor do sidebar
        JPanel topbar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.TOPBAR_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topbar.setPreferredSize(new Dimension(0, 54));
        topbar.setBorder(new EmptyBorder(0, 20, 0, 16));
        topbar.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(9, 0, 9, 0));
        pgTitle = new JLabel("Dashboard");
        pgTitle.setFont(Theme.bold(15));
        pgTitle.setForeground(Theme.TOPBAR_TEXT);
        pgSub = new JLabel("Bem-vindo(a) de volta!");
        pgSub.setFont(Theme.plain(11));
        pgSub.setForeground(Theme.TOPBAR_SUB);
        left.add(pgTitle);
        left.add(pgSub);
        topbar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 11));
        right.setOpaque(false);

        //barra de busca
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 38));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(new Color(255, 255, 255, 55));
                g2.setStroke(new BasicStroke(0.5f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 8, 8));
                g2.dispose();
            }
        };
        searchBar.setOpaque(false);
        searchBar.setPreferredSize(new Dimension(230, 32));
        JLabel searchIco = new JLabel("Pesquisar...");
        searchIco.setFont(Theme.plain(12));
        searchIco.setForeground(new Color(255, 255, 255, 160));
        searchBar.add(searchIco);
        right.add(searchBar);
        topbar.add(right, BorderLayout.EAST);
        main.add(topbar, BorderLayout.NORTH);

        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.CONTENT_BG);
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        main.add(scroll, BorderLayout.CENTER);
        return main;
    }

   
    //PANELS
    private void initPanels() {
        painelDashboard   = new PainelDashboard(this);
        painelLivros      = new PainelLivros(this);
        painelAutores     = new PainelAutores(this);
        painelCategoria   = new PainelCategoria(this);
        painelEditoras    = new PainelEditoras(this);
        painelUsuarios    = new PainelUsuarios(this);
        painelEmprestimos = new PainelEmprestimos(this);
        painelDevolucoes  = new PainelDevolucoes(this);
        painelEstoque     = new PainelEstoque(this);
        painelRelatorios  = new PainelRelatorios(this);
        painelLogin       = new PainelLogin(this);

        contentArea.add(painelDashboard,   "dashboard");
        contentArea.add(painelLivros,      "livros");
        contentArea.add(painelAutores,     "autores");
        contentArea.add(painelCategoria,   "categorias");
        contentArea.add(painelEditoras,    "editoras");
        contentArea.add(painelUsuarios,    "usuarios");
        contentArea.add(painelEmprestimos, "emprestimos");
        contentArea.add(painelDevolucoes,  "devolucoes");
        contentArea.add(painelEstoque,     "estoque");
        contentArea.add(painelRelatorios,  "relatorios");
        contentArea.add(painelLogin,       "login");

        showView("dashboard");
    }

  
    //NAVIGATION API
    public void showView(String view) {
        cardLayout.show(contentArea, view);
        switch (view) {
            case "dashboard":   pgTitle.setText("Dashboard");    pgSub.setText("Bem-vindo(a) de volta!"); break;
            case "livros":      pgTitle.setText("Livros");       pgSub.setText("Gerencie o acervo de livros"); break;
            case "autores":     pgTitle.setText("Autores");      pgSub.setText("Cadastro de autores"); break;
            case "categorias":  pgTitle.setText("Categorias");   pgSub.setText("Categorias do acervo"); break;
            case "editoras":    pgTitle.setText("Editoras");     pgSub.setText("Cadastro de editoras"); break;
            case "usuarios":    pgTitle.setText("Usuarios");     pgSub.setText("Cadastro de usuarios"); break;
            case "emprestimos": pgTitle.setText("Emprestimos");  pgSub.setText("Controle de emprestimos"); break;
            case "devolucoes":  pgTitle.setText("Devolucoes");   pgSub.setText("Registrar devolucoes"); break;
            case "estoque":     pgTitle.setText("Estoque");      pgSub.setText("Controle de estoque"); break;
            case "relatorios":  pgTitle.setText("Relatorios");   pgSub.setText("Relatorios e estatisticas"); pgSub.setText("Relatorios e estatisticas"); break;
            case "login":       pgTitle.setText("Acesso");       pgSub.setText("Entre na sua conta"); break;
        }
    }

    public void activateNav(NavItem item) {
        if (activeNav != null) activeNav.setActive(false);
        activeNav = item;
        item.setActive(true);
    }

   
//NAV ITEM
public static class NavItem extends JPanel {

    private boolean active;

    private final String view;
    private final TelaPrincipal frame;

    private final JLabel iconLabel;
    private final JLabel textLabel;

    public NavItem(String text, String view, TelaPrincipal frame) {

        this.view = view;
        this.frame = frame;

        setOpaque(false);

        setLayout(new FlowLayout(FlowLayout.LEFT, 14, 8));

        setBorder(new EmptyBorder(0, 12, 0, 0));

        //ÍCONES
        String icon;

        switch (view) {

            case "dashboard":
                icon = "⌘";
                break;

            case "livros":
                icon = "📖";
                break;

            case "autores":
                icon = "👤";
                break;

            case "categorias":
                icon = "🏷";
                break;
            
            case "editoras":
                icon = "🏢";
                break;

            case "usuarios":
                icon = "👥";
                break;

            case "emprestimos":
                icon = "⇄";
                break;

            case "devolucoes":
                icon = "↩";
                break;

            case "estoque":
                icon = "📦";
                break;

            case "relatorios":
                icon = "📊";
                break;

            default:
                icon = "•";
                break;
        }

        iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        iconLabel.setForeground(Theme.SIDEBAR_TEXT);

        textLabel = new JLabel(text.replace("  ", ""));
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        textLabel.setForeground(Theme.SIDEBAR_TEXT);

        add(iconLabel);
        add(textLabel);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                frame.activateNav(NavItem.this);

                frame.showView(view);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

                if (!active) {

                    iconLabel.setForeground(Color.WHITE);
                    textLabel.setForeground(Color.WHITE);

                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                if (!active) {

                    iconLabel.setForeground(Theme.SIDEBAR_TEXT);
                    textLabel.setForeground(Theme.SIDEBAR_TEXT);

                    repaint();
                }
            }
        });
    }

    public void setActive(boolean a) {

        active = a;

        textLabel.setFont(
            new Font(
                "SansSerif",
                a ? Font.BOLD : Font.PLAIN,
                15
            )
        );

        iconLabel.setForeground(
            a ? Color.WHITE : Theme.SIDEBAR_TEXT
        );

        textLabel.setForeground(
            a ? Color.WHITE : Theme.SIDEBAR_TEXT
        );

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (active)
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Theme.SIDEBAR_ACTIVE_BG);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Theme.ACCENT);
            g2.fillRect(getWidth() - 2, 0, 2, getHeight());
            g2.dispose();
        }

        super.paintComponent(g);
    }
}}

