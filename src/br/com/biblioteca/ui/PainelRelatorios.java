package br.com.biblioteca.ui;

import br.com.biblioteca.service.RelatorioService;
import br.com.biblioteca.ui.Components.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelRelatorios extends JPanel {

    private final RelatorioService service = new RelatorioService();

    private int titulosUnicos     = 0;
    private int copiasTotais      = 0;
    private int emprestimosAtivos = 0;
    private int emprestimosAtraso = 0;

    //Dados do gráficos 
    private List<Object[]> livrosPorCategoria = List.of();
    private List<Object[]> resumoMembros      = List.of();

    public PainelRelatorios(TelaPrincipal frame) {
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        carregarDados();

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        inner.add(buildStatRow());
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildChartsRow());
        inner.add(Box.createVerticalStrut(18));

        add(inner, BorderLayout.NORTH);
    }

    private void carregarDados() {
        try {
            titulosUnicos      = service.contarTitulosUnicos();
            copiasTotais       = service.contarCopiasTotais();
            emprestimosAtivos  = service.contarEmprestimosAtivos();
            emprestimosAtraso  = service.contarEmprestimosAtrasados();
            livrosPorCategoria = service.livrosPorCategoria();
            resumoMembros      = service.resumoMembros();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Cards 
    private JPanel buildStatRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0));
        p.setOpaque(false);
        p.add(Components.statCard(
                String.valueOf(titulosUnicos), "Títulos únicos",
                Theme.STAT_AMBER_BG, Theme.ICON_AMBER, Theme.NUM_AMBER, new Color(133, 79, 11)));
        p.add(Components.statCard(
                String.valueOf(copiasTotais), "Cópias totais",
                Theme.STAT_GREEN_BG, Theme.ICON_GREEN, Theme.NUM_GREEN, new Color(59, 109, 17)));
        p.add(Components.statCard(
                String.valueOf(emprestimosAtivos), "Empréstimos ativos",
                Theme.STAT_BLUE_BG, Theme.ICON_BLUE, Theme.NUM_BLUE, Theme.ICON_BLUE));
        p.add(Components.statCard(
                String.valueOf(emprestimosAtraso), "Em atraso",
                Theme.STAT_TEAL_BG, Theme.ICON_TEAL, Theme.NUM_TEAL, Theme.ICON_TEAL));
        return p;
    }

    //Linha do gráficos
    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);
        row.add(buildGenreChart());
        row.add(buildMembersCard());
        return row;
    }

    //Gráfico de barras por categoria
    private static final Color[] BAR_COLORS = {
        new Color(0x185FA5), new Color(0xEF9F27), new Color(0xE24B4A),
        new Color(0x639922), new Color(0x7F77DD), new Color(0x2AA198),
        new Color(0xD33682), new Color(0x6C71C4)
    };

    private RoundedPanel buildGenreChart() {
        RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Livros por Categoria"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(14, 16, 14, 16));

        if (livrosPorCategoria.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma categoria cadastrada.");
            vazio.setFont(Theme.plain(12));
            vazio.setForeground(Theme.TEXT_SECONDARY);
            body.add(vazio);
        } else {
            int max = livrosPorCategoria.stream()
                          .mapToInt(r -> (int) r[1])
                          .max().orElse(1);
            max = Math.max(max, 1);

            for (int i = 0; i < livrosPorCategoria.size(); i++) {
                Object[] row  = livrosPorCategoria.get(i);
                String   nome = (String) row[0];
                int      tot  = (int)    row[1];
                Color    cor  = BAR_COLORS[i % BAR_COLORS.length];
                body.add(buildBarRow(nome, tot, max, cor));
                body.add(Box.createVerticalStrut(10));
            }
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildBarRow(String label, int count, int max, Color color) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(110, 0));
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.plain(12));
        lbl.setForeground(Theme.TEXT_PRIMARY);
        JLabel cnt = new JLabel(count + " livro" + (count != 1 ? "s" : ""));
        cnt.setFont(Theme.plain(10));
        cnt.setForeground(Theme.TEXT_SECONDARY);
        left.add(lbl, BorderLayout.NORTH);
        left.add(cnt, BorderLayout.SOUTH);
        row.add(left, BorderLayout.WEST);

        final int   pct      = (count * 100) / max;
        final Color barColor = color;
        JPanel track = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CONTENT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                int w = (int) (getWidth() * pct / 100.0);
                if (w > 0) {
                    g2.setColor(barColor);
                    g2.fillRoundRect(0, 0, w, getHeight(), 6, 6);
                }
                g2.dispose();
            }
        };
        track.setOpaque(false);
        track.setPreferredSize(new Dimension(0, 10));
        row.add(track, BorderLayout.CENTER);
        return row;
    }

    //Resumo de membros 
    private RoundedPanel buildMembersCard() {
        RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Resumo de Membros"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(10, 12, 12, 12));

        if (resumoMembros.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum usuário cadastrado.");
            vazio.setFont(Theme.plain(12));
            vazio.setForeground(Theme.TEXT_SECONDARY);
            body.add(vazio);
        } else {
            for (Object[] m : resumoMembros) {
                body.add(buildMemberRow(m));
                body.add(Box.createVerticalStrut(6));
            }
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildMemberRow(Object[] m) {
        String nome   = (String) m[0];
        int    loans  = (int)    m[1];
        String status = (String) m[2];

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Theme.CONTENT_BG);
        row.setBorder(new CompoundBorder(
            new LineBorder(Theme.CARD_BORDER, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        row.add(new Avatar(nome, Theme.ACCENT, 30), BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nameLbl = new JLabel(nome);
        nameLbl.setFont(Theme.bold(12));
        nameLbl.setForeground(Theme.TEXT_PRIMARY);
        JLabel sub = new JLabel(loans + " empréstimo" + (loans != 1 ? "s ativos" : " ativo"));
        sub.setFont(Theme.plain(11));
        sub.setForeground(Theme.TEXT_SECONDARY);
        info.add(nameLbl);
        info.add(sub);
        row.add(info, BorderLayout.CENTER);

        boolean ativo = "ativo".equalsIgnoreCase(status);
        row.add(ativo
                ? Components.availBadge("ativo")
                : Components.overBadge("inativo"),
                BorderLayout.EAST);
        return row;
    }
}