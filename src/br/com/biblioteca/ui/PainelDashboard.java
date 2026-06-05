package br.com.biblioteca.ui;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelDashboard extends JPanel {

    private final TelaPrincipal frame;

    //Services
    private final LivroService      livroSvc  = new LivroService();
    private final EmprestimoService empSvc    = new EmprestimoService();
    private final UsuarioService    usuarioSvc = new UsuarioService();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelDashboard(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        refresh();
    }

    public void refresh() {
        removeAll();

        //Acontece a busca dos dados do banco
        int totalLivros = 0, totalUsuarios = 0, empAtivos = 0, empAtrasados = 0;
        List<Livro>      livros      = List.of();
        List<Emprestimo> emprestimos = List.of();
        try { totalLivros   = livroSvc.contarTotal();        } catch (Exception ignored) {}
        try { totalUsuarios = usuarioSvc.contarAtivos();     } catch (Exception ignored) {}
        try { empAtivos     = empSvc.contarAtivos();         } catch (Exception ignored) {}
        try { empAtrasados  = empSvc.contarAtrasados();      } catch (Exception ignored) {}
        try { livros        = livroSvc.listarTodos();        } catch (Exception ignored) {}
        try { emprestimos   = empSvc.listarTodos();          } catch (Exception ignored) {}

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(18, 18, 18, 18));

        inner.add(buildStatGrid(totalLivros, totalUsuarios, empAtivos, empAtrasados));
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildMiddleRow(livros));
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildLoansCard(emprestimos));
        inner.add(Box.createVerticalStrut(18));

        add(inner, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    private JPanel buildStatGrid(int totalLivros, int totalUsuarios, int empAtivos, int empAtrasados) {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0)); p.setOpaque(false);
        p.add(Components.statCard(String.valueOf(totalLivros),   "Livros no acervo",
            Theme.STAT_AMBER_BG, Theme.ICON_AMBER, Theme.NUM_AMBER, new Color(133, 79, 11)));
        p.add(Components.statCard(String.valueOf(totalUsuarios), "Membros",
            Theme.STAT_GREEN_BG, Theme.ICON_GREEN, Theme.NUM_GREEN, new Color(59, 109, 17)));
        p.add(Components.statCard(String.valueOf(empAtivos),     "Emprestimos ativos",
            Theme.STAT_BLUE_BG,  Theme.ICON_BLUE,  Theme.NUM_BLUE,  Theme.ICON_BLUE));
        p.add(Components.statCard(String.valueOf(empAtrasados),  "Atrasos",
            Theme.STAT_TEAL_BG,  Theme.ICON_TEAL,  Theme.NUM_TEAL,  Theme.ICON_TEAL));
        return p;
    }

    private JPanel buildMiddleRow(List<Livro> livros) {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0)); row.setOpaque(false);
        row.add(buildBooksCard(livros)); row.add(buildQACard()); return row;
    }

    private Components.RoundedPanel buildBooksCard(List<Livro> livros) {
        Components.RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Ultimos livros adicionados"), BorderLayout.NORTH);
        JPanel list = new JPanel(); list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        int limit = Math.min(5, livros.size());
        for (int i = 0; i < limit; i++) {
            list.add(buildBookRow(livros.get(i), i));
            if (i < limit - 1) list.add(Components.sep());
        }
        card.add(list, BorderLayout.CENTER); return card;
    }

    private JPanel buildBookRow(Livro livro, int colorIndex) {
        JPanel p = new JPanel(new BorderLayout(10, 0)); p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        Color cov = Theme.COVERS[colorIndex % Theme.COVERS.length];
        p.add(new Components.BookCover(cov, livro.getTitulo(), 36, 50), BorderLayout.WEST);
        JPanel info = new JPanel(); info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(livro.getTitulo()); title.setFont(Theme.bold(12));
        title.setForeground(Theme.TEXT_PRIMARY);
        String autor = livro.getNomeAutor() != null ? livro.getNomeAutor() : "";
        String cat   = livro.getNomeCategoria() != null ? livro.getNomeCategoria() : "";
        JLabel sub = new JLabel(autor + (cat.isEmpty() ? "" : " · " + cat));
        sub.setFont(Theme.plain(11)); sub.setForeground(Theme.TEXT_SECONDARY);
        info.add(title); info.add(sub); p.add(info, BorderLayout.CENTER);
        boolean disponivel = "disponível".equalsIgnoreCase(livro.getStatus());
        p.add(disponivel ? Components.availBadge("Disponivel") : Components.loanBadge("Emprestado"), BorderLayout.EAST);
        return p;
    }

    private Components.RoundedPanel buildQACard() {
        Components.RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Acoes rapidas"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 2, 8, 8)); grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 10, 10, 10));
        grid.add(qaBtn("Cadastrar livro",  Theme.STAT_BLUE_BG,  Theme.ACCENT_DARK,     "livros"));
        grid.add(qaBtn("Novo usuario",     Theme.STAT_GREEN_BG, new Color(39, 80, 10), "usuarios"));
        grid.add(qaBtn("Novo emprestimo",  Theme.STAT_AMBER_BG, new Color(99, 56, 6),  "emprestimos"));
        grid.add(qaBtn("Devolucao",        Theme.STAT_TEAL_BG,  new Color(8, 80, 65),  "devolucoes"));
        grid.add(qaBtn("Ver estoque",      Theme.STAT_PURP_BG,  new Color(60, 52, 137),"estoque"));
        grid.add(qaBtn("Relatorios",       Theme.STAT_RED_BG,   new Color(121, 31, 31),"relatorios"));
        card.add(grid, BorderLayout.CENTER); return card;
    }

    private static final Color STAT_PURP_BG = new Color(238, 237, 254);
    private static final Color STAT_RED_BG  = new Color(252, 235, 235);

    private Components.StyledButton qaBtn(String text, Color bg, Color fg, String view) {
        Components.StyledButton b = Components.qaBtn(text, bg, fg);
        b.addActionListener(e -> frame.showView(view)); return b;
    }

    private Components.RoundedPanel buildLoansCard(List<Emprestimo> emprestimos) {
        Components.StyledButton viewAllBtn = new Components.StyledButton("Ver todos", Theme.BTN_INFO, Color.WHITE);
        viewAllBtn.setSmall(); viewAllBtn.addActionListener(e -> frame.showView("emprestimos"));
        Components.RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Emprestimos recentes", viewAllBtn), BorderLayout.NORTH);

        String[] cols = {"Livro", "Membro", "Data", "Devolucao", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        int limit = Math.min(5, emprestimos.size());
        for (int i = 0; i < limit; i++) {
            Emprestimo e = emprestimos.get(i);
            String dataEmp = e.getDataEmprestimo() != null ? e.getDataEmprestimo().format(FMT) : "-";
            String dataDev = e.getDataDevolucao()  != null ? e.getDataDevolucao().format(FMT)  : "-";
            model.addRow(new Object[]{e.getTituloLivro(), e.getNomeUsuario(), dataEmp, dataDev, e.getStatus()});
        }
        JTable table = Components.buildTable(model);
        table.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            String val = v == null ? "" : v.toString();
            p.add(val.equals("atrasado") ? Components.overBadge(val) : Components.availBadge(val));
            return p;
        });
        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER); card.setPreferredSize(new Dimension(0, 210)); return card;
    }
}