package br.com.biblioteca.ui;

import br.com.biblioteca.model.Editora;
import br.com.biblioteca.service.EditoraService;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

//Tabela de Editoras
public class PainelEditoras extends JPanel {

    private final TelaPrincipal  frame;
    private final EditoraService service = new EditoraService();
    private DefaultTableModel    tableModel;

    public PainelEditoras(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        RoundedPanel card = Components.card();
        StyledButton addBtn = new StyledButton("+ Nova Editora", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));

        String[] cols = {"#", "Nome", "Cidade", "País", "Ações"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
        };

        JTable table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ActionsEditor(this));

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);

        refresh();
        card.add(Components.cardHeader("Editoras (" + tableModel.getRowCount() + ")", addBtn), BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    public void refresh() {
        try {
            List<Editora> editoras = service.listarTodos();
            tableModel.setRowCount(0);
            for (Editora e : editoras)
                tableModel.addRow(new Object[]{e.getId(), e.getNome(), e.getCidade(), e.getPais(), "actions"});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar editoras:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private TableCellRenderer actionsRenderer() {
        return (t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            StyledButton edit = new StyledButton("Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            StyledButton del  = new StyledButton("Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            p.add(edit); p.add(del); return p;
        };
    }

    public void openForm(int row) {
        boolean isEdit = row >= 0;
        JDialog dlg = new JDialog(frame, isEdit ? "Editar Editora" : "Nova Editora", true);
        dlg.setSize(400, 300); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; gbc.insets = new Insets(3, 0, 3, 0);

        JTextField fNome   = Components.styledField("");
        JTextField fCidade = Components.styledField("");
        JTextField fPais   = Components.styledField("");
        if (isEdit) {
            fNome.setText  (tableModel.getValueAt(row, 1).toString());
            Object cid = tableModel.getValueAt(row, 2); fCidade.setText(cid != null ? cid.toString() : "");
            Object pai = tableModel.getValueAt(row, 3); fPais.setText  (pai != null ? pai.toString() : "");
        }

        String[] labels = {"Nome *", "Cidade", "País"};
        JTextField[] flds = {fNome, fCidade, fPais};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;     form.add(Components.formLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1; form.add(flds[i], gbc);
        }
        gbc.gridy = 7; gbc.insets = new Insets(14, 0, 0, 0);
        StyledButton save = new StyledButton(isEdit ? "Atualizar" : "Salvar", Theme.BTN_PRIMARY, Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));
        save.addActionListener(e -> {
            if (fNome.getText().isBlank()) { JOptionPane.showMessageDialog(dlg, "Nome obrigatório.", "Atenção", JOptionPane.WARNING_MESSAGE); return; }
            try {
                Editora ed = new Editora();
                if (isEdit) ed.setId((int) tableModel.getValueAt(row, 0));
                ed.setNome  (fNome.getText().trim());
                ed.setCidade(fCidade.getText().trim());
                ed.setPais  (fPais.getText().trim());
                if (isEdit) service.atualizar(ed); else service.cadastrar(ed);
                JOptionPane.showMessageDialog(dlg, "Editora salva!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); refresh();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
        });
        form.add(save, gbc); dlg.add(form); dlg.setVisible(true);
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelEditoras owner; private int row;
        ActionsEditor(PainelEditoras owner) {
            super(new JCheckBox()); this.owner = owner; setClickCountToStart(1);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4)); panel.setBackground(Theme.ROW_HOVER);
            StyledButton edit = new StyledButton("Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            StyledButton del  = new StyledButton("Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            edit.addActionListener(e -> { fireEditingStopped(); owner.openForm(row); });
            del .addActionListener(e -> { fireEditingStopped();
                if (JOptionPane.showConfirmDialog(null, "Excluir editora?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
