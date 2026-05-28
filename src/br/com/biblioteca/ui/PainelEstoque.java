package br.com.biblioteca.ui;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;


//Tabela com barra de ocupação po livros
public class PainelEstoque extends JPanel {

    private final TelaPrincipal frame;

    private static final Object[][] DATA = {
        {"Money Is Good Just Like Food", 3, 2, Theme.COVERS[0]},
        {"Learn Java in One Minute",     5, 4, Theme.COVERS[1]},
        {"Think Inside The Box",         2, 0, Theme.COVERS[2]},
        {"What To Learn",                4, 3, Theme.COVERS[3]},
        {"HTML CSS JS",                  6, 5, Theme.COVERS[4]},
        {"Clean Code",                   3, 1, Theme.COVERS[5]},
        {"O Poder do Hábito",            4, 4, Theme.COVERS[6]},
        {"Design Patterns",              2, 2, Theme.COVERS[7]},
    };

    public PainelEstoque(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Controle de Estoque"), BorderLayout.NORTH);

        String[] cols = {"Livro","Total","Disponíveis","Emprestadas","Ocupação","Ação"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 5; }
        };

        for (Object[] row : DATA) {
            int total   = (int) row[1];
            int avail   = (int) row[2];
            int borrowed = total - avail;
            int pct     = total == 0 ? 0 : (borrowed * 100) / total;
            model.addRow(new Object[]{row[0], total, avail, borrowed, pct, "actions"});
        }

        JTable table = Components.buildTable(model);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setPreferredWidth(230);
        table.getColumnModel().getColumn(1).setPreferredWidth(65);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        //Capa mini + título
        table.getColumnModel().getColumn(0).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            Color cov = r < DATA.length ? (Color) DATA[r][3] : Color.GRAY;
            p.add(new BookCover(cov, v.toString(), 22, 30));
            JLabel lbl = new JLabel(v.toString());
            lbl.setFont(Theme.FONT_BODY); lbl.setForeground(Theme.TEXT_PRIMARY);
            p.add(lbl); return p;
        });

        //Disponíveis badge green
        table.getColumnModel().getColumn(2).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,7));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            p.add(Components.availBadge(v.toString())); return p;
        });

        //Emprestados badge amber
        table.getColumnModel().getColumn(3).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,7));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            p.add(Components.loanBadge(v.toString())); return p;
        });

        //Progress bar
        table.getColumnModel().getColumn(4).setCellRenderer((t,v,sel,foc,r,c)->{
            int pct = v instanceof Integer ? (Integer) v : 0;
            JPanel wrapper = Components.progressBar(pct);
            wrapper.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            return wrapper;
        });

        //button
        table.getColumnModel().getColumn(5).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,4,6));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            StyledButton b=new StyledButton("✎ Ajustar",Theme.BTN_WARNING,Color.WHITE); b.setSmall();
            p.add(b); return p;
        });
        table.getColumnModel().getColumn(5).setCellEditor(new AjusteEditor(this, model));

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    void openAjuste(int row, DefaultTableModel model) {
        String titulo = model.getValueAt(row, 0).toString();
        String novoTot = JOptionPane.showInputDialog(frame,
            "Ajustar total de cópias para:\n\"" + titulo + "\"\n\nNovo total:",
            "Ajuste de Estoque", JOptionPane.PLAIN_MESSAGE);
        if (novoTot != null && !novoTot.isBlank()) {
            try {
                int total = Integer.parseInt(novoTot.trim());
                if (total < 1) throw new NumberFormatException();
                JOptionPane.showMessageDialog(frame, "Estoque atualizado para " + total + " cópia(s).", "Atualizado", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Informe um número inteiro válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class AjusteEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelEstoque owner; private final DefaultTableModel model; private int row;
        AjusteEditor(PainelEstoque owner, DefaultTableModel model){
            super(new JCheckBox()); this.owner=owner; this.model=model; setClickCountToStart(1);
            panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,6)); panel.setBackground(Theme.ROW_HOVER);
            StyledButton b=new StyledButton("✎ Ajustar",Theme.BTN_WARNING,Color.WHITE); b.setSmall();
            b.addActionListener(e->{ fireEditingStopped(); owner.openAjuste(row, model); });
            panel.add(b);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean sel,int r,int c){row=r;return panel;}
        @Override public Object getCellEditorValue(){return"actions";}
    }
}
