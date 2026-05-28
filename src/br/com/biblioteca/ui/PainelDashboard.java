package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class PainelDashboard extends JPanel {

    private final TelaPrincipal frame;

    // Livros atualizados com os títulos solicitados
    static final Object[][] BOOKS_DATA = {
        {"Dom Quixote",                    "Miguel de Cervantes",          "Literatura",   1605, 3, 2, Theme.COVERS[0]},
        {"Pai Rico, Pai Pobre",            "Robert Kiyosaki",              "Financas",     1997, 5, 4, Theme.COVERS[1]},
        {"A Estrategia do Oceano Azul",    "W. Chan Kim e R. Mauborgne",   "Negocios",     2005, 2, 0, Theme.COVERS[2]},
        {"Didatica",                       "Ilma Passos Alencastro",       "Educacao",     2008, 4, 3, Theme.COVERS[3]},
        {"Entendendo Algoritmos",          "Aditya Y. Bhargava",           "Tecnologia",   2016, 6, 5, Theme.COVERS[4]},
        {"Clean Code",                     "Robert C. Martin",             "Tecnologia",   2020, 3, 1, Theme.COVERS[5]},
        {"O Poder do Habito",              "Charles Duhigg",               "Autoajuda",    2019, 4, 4, Theme.COVERS[6]},
        {"Design Patterns",                "Gang of Four",                 "Tecnologia",   2018, 2, 2, Theme.COVERS[7]},
    };

    static final Object[][] LOANS_DATA = {
        {"Pai Rico, Pai Pobre",         "Joao Silva",   "10/05/2026","24/05/2026","ativo"},
        {"A Estrategia do Oceano Azul", "Maria Santos", "15/05/2026","29/05/2026","ativo"},
        {"Clean Code",                  "Joao Silva",   "01/05/2026","15/05/2026","atrasado"},
        {"O Poder do Habito",           "Ana Costa",    "20/05/2026","03/06/2026","ativo"},
    };

    public PainelDashboard(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(18,18,18,18));

        inner.add(buildStatGrid());
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildMiddleRow());
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildLoansCard());
        inner.add(Box.createVerticalStrut(18));

        add(inner, BorderLayout.NORTH);
    }

    private JPanel buildStatGrid() {
        JPanel p = new JPanel(new GridLayout(1,4,12,0)); p.setOpaque(false);
        p.add(Components.statCard("8","Livros no acervo",
            Theme.STAT_AMBER_BG, Theme.ICON_AMBER, Theme.NUM_AMBER, new Color(133,79,11)));
        p.add(Components.statCard("4","Membros",
            Theme.STAT_GREEN_BG, Theme.ICON_GREEN, Theme.NUM_GREEN, new Color(59,109,17)));
        p.add(Components.statCard("3","Emprestimos ativos",
            Theme.STAT_BLUE_BG,  Theme.ICON_BLUE,  Theme.NUM_BLUE,  Theme.ICON_BLUE));
        p.add(Components.statCard("1","Atrasos",
            Theme.STAT_TEAL_BG,  Theme.ICON_TEAL,  Theme.NUM_TEAL,  Theme.ICON_TEAL));
        return p;
    }

    private JPanel buildMiddleRow() {
        JPanel row = new JPanel(new GridLayout(1,2,14,0)); row.setOpaque(false);
        row.add(buildBooksCard()); row.add(buildQACard()); return row;
    }

    private Components.RoundedPanel buildBooksCard() {
        Components.RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Ultimos livros adicionados"), BorderLayout.NORTH);
        JPanel list = new JPanel(); list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        for (int i=0; i<Math.min(5, BOOKS_DATA.length); i++) {
            list.add(buildBookRow(BOOKS_DATA[i]));
            if (i<4) list.add(Components.sep());
        }
        card.add(list, BorderLayout.CENTER); return card;
    }

    private JPanel buildBookRow(Object[] row) {
        JPanel p = new JPanel(new BorderLayout(10,0)); p.setOpaque(false);
        p.setBorder(new EmptyBorder(8,12,8,12));
        p.add(new Components.BookCover((Color)row[6], row[0].toString(), 36,50), BorderLayout.WEST);
        JPanel info = new JPanel(); info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(row[0].toString()); title.setFont(Theme.bold(12));
        title.setForeground(Theme.TEXT_PRIMARY);
        JLabel sub = new JLabel(row[1]+" · "+row[2]); sub.setFont(Theme.plain(11));
        sub.setForeground(Theme.TEXT_SECONDARY);
        info.add(title); info.add(sub); p.add(info, BorderLayout.CENTER);
        int avail=(int)row[5];
        p.add(avail>0 ? Components.availBadge("Disponivel") : Components.loanBadge("Emprestado"), BorderLayout.EAST);
        return p;
    }

    private Components.RoundedPanel buildQACard() {
        Components.RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Acoes rapidas"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3,2,8,8)); grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10,10,10,10));
        grid.add(qaBtn("Cadastrar livro",  Theme.STAT_BLUE_BG,  Theme.ACCENT_DARK, "livros"));
        grid.add(qaBtn("Novo usuario",     Theme.STAT_GREEN_BG, new Color(39,80,10),"usuarios"));
        grid.add(qaBtn("Novo emprestimo",  Theme.STAT_AMBER_BG, new Color(99,56,6), "emprestimos"));
        grid.add(qaBtn("Devolucao",        Theme.STAT_TEAL_BG,  new Color(8,80,65), "devolucoes"));
        grid.add(qaBtn("Ver estoque",      Theme.STAT_PURP_BG,  new Color(60,52,137),"estoque"));
        grid.add(qaBtn("Relatorios",       Theme.STAT_RED_BG,   new Color(121,31,31),"relatorios"));
        card.add(grid, BorderLayout.CENTER); return card;
    }

    private static final Color STAT_PURP_BG = new Color(238,237,254);
    private static final Color STAT_RED_BG  = new Color(252,235,235);

    private Components.StyledButton qaBtn(String text, Color bg, Color fg, String view) {
        Components.StyledButton b = Components.qaBtn(text,bg,fg);
        b.addActionListener(e -> frame.showView(view)); return b;
    }

    private Components.RoundedPanel buildLoansCard() {
        Components.StyledButton viewAllBtn = new Components.StyledButton("Ver todos", Theme.BTN_INFO, Color.WHITE);
        viewAllBtn.setSmall(); viewAllBtn.addActionListener(e -> frame.showView("emprestimos"));
        Components.RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Emprestimos recentes", viewAllBtn), BorderLayout.NORTH);

        String[] cols={"Livro","Membro","Data","Devolucao","Status"};
        DefaultTableModel model=new DefaultTableModel(cols,0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        for(Object[] row:LOANS_DATA) model.addRow(row);
        JTable table = Components.buildTable(model);
        table.getColumnModel().getColumn(4).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            String val=v==null?"":v.toString();
            p.add(val.equals("atrasado")?Components.overBadge(val):Components.availBadge(val));
            return p;
        });
        JScrollPane sp=new JScrollPane(table); sp.setBorder(null);
        card.add(sp,BorderLayout.CENTER); card.setPreferredSize(new Dimension(0,210)); return card;
    }
}
