package br.com.biblioteca.ui;

import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.service.CategoriaService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

//Tabela de categorias
public class PainelCategoria extends JPanel {

    private final TelaPrincipal    frame;
    private final CategoriaService service = new CategoriaService();
    private DefaultTableModel      tableModel;

    public PainelCategoria(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        Components.RoundedPanel card = Components.card();
        Components.StyledButton addBtn = new Components.StyledButton("+ Nova Categoria", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));

        String[] cols = {"#", "Nome", "Descricao", "Acoes"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 3; }
        };

        JTable table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);

        table.getColumnModel().getColumn(1).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            Color dotColor = Theme.COVERS[r % Theme.COVERS.length];
            JPanel dot = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(dotColor); g2.fillOval(0, 2, 10, 10); g2.dispose();
                }
            };
            dot.setOpaque(false); dot.setPreferredSize(new Dimension(10, 14));
            JLabel lbl = new JLabel(v == null ? "" : v.toString()); lbl.setFont(Theme.FONT_BODY);
            lbl.setForeground(Theme.TEXT_PRIMARY); p.add(dot); p.add(lbl); return p;
        });
        table.getColumnModel().getColumn(3).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ActionsEditor(this));

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);

        refresh();
        card.add(Components.cardHeader("Categorias (" + tableModel.getRowCount() + ")", addBtn), BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    public void refresh() {
        try {
            List<Categoria> cats = service.listarTodos();
            tableModel.setRowCount(0);
            for (Categoria c : cats)
                tableModel.addRow(new Object[]{c.getId(), c.getNome(), c.getDescricao(), "actions"});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar categorias:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private TableCellRenderer actionsRenderer() {
        return (t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            Components.StyledButton edit = new Components.StyledButton("Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            Components.StyledButton del  = new Components.StyledButton("Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            p.add(edit); p.add(del); return p;
        };
    }

    public void openForm(int row) {
        boolean isEdit = row >= 0;
        JDialog dlg = new JDialog(frame, isEdit ? "Editar Categoria" : "Nova Categoria", true);
        dlg.setSize(360, 240); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; gbc.insets = new Insets(3, 0, 3, 0);
        JTextField fNome = Components.styledField(""); JTextField fDesc = Components.styledField("");
        if (isEdit) { fNome.setText(tableModel.getValueAt(row, 1).toString()); Object d = tableModel.getValueAt(row, 2); fDesc.setText(d != null ? d.toString() : ""); }
        gbc.gridy = 0; form.add(Components.formLabel("Nome da categoria *"), gbc);
        gbc.gridy = 1; form.add(fNome, gbc);
        gbc.gridy = 2; form.add(Components.formLabel("Descricao"), gbc);
        gbc.gridy = 3; form.add(fDesc, gbc);
        gbc.gridy = 5; gbc.insets = new Insets(14, 0, 0, 0);
        Components.StyledButton save = new Components.StyledButton(isEdit ? "Atualizar" : "Salvar", Theme.BTN_PRIMARY, Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));
        save.addActionListener(e -> {
            if (fNome.getText().isBlank()) { JOptionPane.showMessageDialog(dlg, "Nome obrigatorio.", "Atencao", JOptionPane.WARNING_MESSAGE); return; }
            try {
                Categoria c = new Categoria();
                if (isEdit) c.setId((int) tableModel.getValueAt(row, 0));
                c.setNome(fNome.getText().trim());
                c.setDescricao(fDesc.getText().trim());
                if (isEdit) service.atualizar(c); else service.cadastrar(c);
                JOptionPane.showMessageDialog(dlg, "Categoria salva!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); refresh();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
        });
        form.add(save, gbc); dlg.add(form); dlg.setVisible(true);
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelCategoria owner; private int row;
        ActionsEditor(PainelCategoria owner) {
            super(new JCheckBox()); this.owner = owner; setClickCountToStart(1);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4)); panel.setBackground(Theme.ROW_HOVER);
            Components.StyledButton edit = new Components.StyledButton("Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            Components.StyledButton del  = new Components.StyledButton("Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            edit.addActionListener(e -> { fireEditingStopped(); owner.openForm(row); });
            del .addActionListener(e -> { fireEditingStopped();
                if (JOptionPane.showConfirmDialog(null, "Excluir categoria?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int id = (int) owner.tableModel.getValueAt(row, 0);
                    try { owner.service.excluir(id); owner.refresh(); }
                    catch (Exception ex) { JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage()); }
                }
            });
            panel.add(edit); panel.add(del);
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int r, int c) { row = r; return panel; }
        @Override public Object getCellEditorValue() { return "actions"; }
    }
}