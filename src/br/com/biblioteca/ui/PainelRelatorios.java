package br.com.biblioteca.ui;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;


public class PainelRelatorios extends JPanel {

    public PainelRelatorios(TelaPrincipal frame) {
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        inner.add(buildStatRow());
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildChartsRow());
        inner.add(Box.createVerticalStrut(18));

        add(inner, BorderLayout.NORTH);
    }

    //4 stat cards 
    private JPanel buildStatRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0));
        p.setOpaque(false);
        p.add(Components.statCard("8","Títulos únicos",
            Theme.STAT_AMBER_BG, Theme.ICON_AMBER, Theme.NUM_AMBER, new Color(133,79,11)));
        p.add(Components.statCard("29","Cópias totais",
            Theme.STAT_GREEN_BG, Theme.ICON_GREEN, Theme.NUM_GREEN, new Color(59,109,17)));
        p.add(Components.statCard("3","Empréstimos ativos",
            Theme.STAT_BLUE_BG,  Theme.ICON_BLUE,  Theme.NUM_BLUE,  Theme.ICON_BLUE));
        p.add(Components.statCard("1","Em atraso",
            Theme.STAT_TEAL_BG,  Theme.ICON_TEAL,  Theme.NUM_TEAL,  Theme.ICON_TEAL));
        return p;
    }

    //Charts row 
    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);
        row.add(buildGenreChart());
        row.add(buildMembersCard());
        return row;
    }

    //gráfico de barras por gênero
    private RoundedPanel buildGenreChart() {
        RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Livros por Gênero"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(14, 16, 14, 16));

        String[][] genres = {
            {"Tecnologia","4","#185FA5"},
            {"Finanças",  "1","#EF9F27"},
            {"Negócios",  "1","#E24B4A"},
            {"Educação",  "1","#639922"},
            {"Autoajuda", "1","#7F77DD"},
        };
        int max = 4;
        for (String[] g : genres) {
            body.add(buildBarRow(g[0], Integer.parseInt(g[1]), max, Color.decode(g[2])));
            body.add(Box.createVerticalStrut(10));
        }
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildBarRow(String label, int count, int max, Color color) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        //label + count
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(110, 0));
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.plain(12)); lbl.setForeground(Theme.TEXT_PRIMARY);
        JLabel cnt = new JLabel(count + " livro" + (count != 1 ? "s" : ""));
        cnt.setFont(Theme.plain(10)); cnt.setForeground(Theme.TEXT_SECONDARY);
        left.add(lbl, BorderLayout.NORTH);
        left.add(cnt, BorderLayout.SOUTH);
        row.add(left, BorderLayout.WEST);

        //bar track
        final int pct = max == 0 ? 0 : (count * 100) / max;
        final Color barColor = color;
        JPanel track = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CONTENT_BG); g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                int w = (int)(getWidth() * pct / 100.0);
                if (w > 0) { g2.setColor(barColor); g2.fillRoundRect(0,0,w,getHeight(),6,6); }
                g2.dispose();
            }
        };
        track.setOpaque(false);
        track.setPreferredSize(new Dimension(0, 10));
        row.add(track, BorderLayout.CENTER);
        return row;
    }

    //resumo de membros
    private RoundedPanel buildMembersCard() {
        RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Resumo de Membros"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(10, 12, 12, 12));

        Object[][] members = {
            {"João Silva",   3, "ativo"},
            {"Maria Santos", 1, "ativo"},
            {"Pedro Lima",   0, "inativo"},
            {"Ana Costa",    2, "ativo"},
        };

        for (Object[] m : members) {
            body.add(buildMemberRow(m));
            body.add(Box.createVerticalStrut(6));
        }
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildMemberRow(Object[] m) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Theme.CONTENT_BG);
        row.setBorder(new CompoundBorder(
            new LineBorder(Theme.CARD_BORDER, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        row.add(new Avatar(m[0].toString(), Theme.ACCENT, 30), BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name  = new JLabel(m[0].toString());
        name.setFont(Theme.bold(12)); name.setForeground(Theme.TEXT_PRIMARY);
        int loans = (int) m[1];
        JLabel sub = new JLabel(loans + " empréstimo" + (loans != 1 ? "s ativos" : " ativo"));
        sub.setFont(Theme.plain(11)); sub.setForeground(Theme.TEXT_SECONDARY);
        info.add(name); info.add(sub);
        row.add(info, BorderLayout.CENTER);

        boolean ativo = "ativo".equals(m[2]);
        row.add(ativo ? Components.availBadge("ativo") : Components.overBadge("inativo"), BorderLayout.EAST);
        return row;
    }
}
