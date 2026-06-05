package br.com.biblioteca.ui;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.service.LivroService;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

// Tabela de dados vindos da tabela livro
public class PainelEstoque extends JPanel {

    private final TelaPrincipal frame;
    private final LivroService  service = new LivroService();
    private DefaultTableModel   tableModel;
    private List<Livro>         livros  = List.of();

    public PainelEstoque(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        RoundedPanel card = Components.card();
        card.add(Components.cardHeader("Controle de Estoque"), BorderLayout.NORTH);

        String[] cols = {"Livro", "Total", "Disponíveis", "Emprestadas", "Ocupação"};
        tableModel = new DefaultTableModel(cols, 0) {
           @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = Components.buildTable(tableModel);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setPreferredWidth(230);
        table.getColumnModel().getColumn(1).setPreferredWidth(65);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
    

        table.getColumnModel().getColumn(0).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            Color cov = Theme.COVERS[r % Theme.COVERS.length];
            p.add(new BookCover(cov, v.toString(), 22, 30));
            JLabel lbl = new JLabel(v.toString());
            lbl.setFont(Theme.FONT_BODY); lbl.setForeground(Theme.TEXT_PRIMARY);
            p.add(lbl); return p;
        });

        //Disponíveis green
        table.getColumnModel().getColumn(2).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            p.add(Components.availBadge(v.toString())); return p;
        });

        //Emprestados amber
        table.getColumnModel().getColumn(3).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            p.add(Components.loanBadge(v.toString())); return p;
        });

        //Progress bar
        table.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, foc, r, c) -> {
            int pct = v instanceof Integer ? (Integer) v : 0;
            JPanel wrapper = Components.progressBar(pct);
            wrapper.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            return wrapper;
        });

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        try {
            livros = service.listarTodos();
            tableModel.setRowCount(0);
            for (Livro l : livros) {
                int total    = 1;
                int avail    = "disponível".equalsIgnoreCase(l.getStatus()) ? 1 : 0;
                int borrowed = total - avail;
                int pct      = borrowed * 100;  // 0% ou 100%
                tableModel.addRow(new Object[]{l.getTitulo(), total, avail, borrowed, pct });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar estoque:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}
