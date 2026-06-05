package br.com.biblioteca.ui;

import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.service.UsuarioService;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

//Tabela + status + CRUD
public class PainelUsuarios extends JPanel {

    final TelaPrincipal  frame;
    private final UsuarioService  service = new UsuarioService();
    private DefaultTableModel     tableModel;

    public PainelUsuarios(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        RoundedPanel card = Components.card();
        StyledButton addBtn = new StyledButton("+ Novo Usuário", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));

        String[] cols = {"#", "Nome", "E-mail", "Telefone", "Status", "Ações"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 5; }
        };

        JTable table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        // Nome com avatar
        table.getColumnModel().getColumn(1).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            p.add(new Avatar(v == null ? "?" : v.toString(), Theme.ACCENT, 26));
            JLabel lbl = new JLabel(v == null ? "" : v.toString());
            lbl.setFont(Theme.FONT_BODY); lbl.setForeground(Theme.TEXT_PRIMARY);
            p.add(lbl); return p;
        });

        //Status 
        table.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            p.add("ativo".equals(v) ? Components.availBadge("ativo") : Components.overBadge("inativo")); return p;
        });

        table.getColumnModel().getColumn(5).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ActionsEditor(this));

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);

        refresh();
        card.add(Components.cardHeader("Usuários (" + tableModel.getRowCount() + ")", addBtn), BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    public void refresh() {
        try {
            List<Usuario> usuarios = service.listarTodos();
            tableModel.setRowCount(0);
            for (Usuario u : usuarios)
                tableModel.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getStatus(), "actions"});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar usuários:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private TableCellRenderer actionsRenderer() {
        return (t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            StyledButton edit = new StyledButton("✎ Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            StyledButton del  = new StyledButton("🗑 Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            p.add(edit); p.add(del); return p;
        };
    }

    public void openForm(int row) {
        boolean isEdit = row >= 0;
        JDialog dlg = new JDialog(frame, isEdit ? "Editar Usuário" : "Novo Usuário", true);
        dlg.setSize(420, 360); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; gbc.insets = new Insets(3, 0, 3, 0);

        JTextField  fNome  = Components.styledField("");
        JTextField  fEmail = Components.styledField("");
        JTextField  fTel   = Components.styledField("");
        JPasswordField fSenha = Components.styledPassword();

        if (isEdit) {
            fNome.setText (tableModel.getValueAt(row, 1).toString());
            fEmail.setText(tableModel.getValueAt(row, 2).toString());
            Object tel = tableModel.getValueAt(row, 3);
            fTel.setText(tel != null ? tel.toString() : "");
        }

        String[] labels = {"Nome completo *", "E-mail *", "Telefone", isEdit ? "Nova senha (deixe em branco para manter)" : "Senha *"};
        java.awt.Component[] fields = {fNome, fEmail, fTel, fSenha};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;     form.add(Components.formLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1; form.add(fields[i], gbc);
        }
        gbc.gridy = 9; gbc.insets = new Insets(14, 0, 0, 0);
        StyledButton save = new StyledButton(isEdit ? "Atualizar" : "Salvar Usuário", Theme.BTN_PRIMARY, Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));
        save.addActionListener(e -> {
            if (fNome.getText().isBlank() || fEmail.getText().isBlank()) {
                JOptionPane.showMessageDialog(dlg, "Preencha nome e e-mail.", "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            String senha = new String(fSenha.getPassword()).trim();
            if (!isEdit && senha.isBlank()) {
                JOptionPane.showMessageDialog(dlg, "Senha obrigatória.", "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            try {
                Usuario u = new Usuario();
                if (isEdit) u.setId((int) tableModel.getValueAt(row, 0));
                u.setNome(fNome.getText().trim());
                u.setEmail(fEmail.getText().trim());
                u.setTelefone(fTel.getText().trim());
                u.setStatus("ativo");
                if (!senha.isBlank()) u.setSenha(senha);
                if (isEdit) service.atualizar(u); else service.cadastrar(u);
                JOptionPane.showMessageDialog(dlg, "Usuário salvo!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); refresh();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(save, gbc); dlg.add(form); dlg.setVisible(true);
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelUsuarios owner; private int row;
        ActionsEditor(PainelUsuarios owner) {
            super(new JCheckBox()); this.owner = owner; setClickCountToStart(1);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4)); panel.setBackground(Theme.ROW_HOVER);
            StyledButton edit = new StyledButton("✎ Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            StyledButton del  = new StyledButton("🗑 Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            edit.addActionListener(e -> { fireEditingStopped(); owner.openForm(row); });
            del .addActionListener(e -> { fireEditingStopped();
                if (JOptionPane.showConfirmDialog(owner.frame, "Excluir usuário?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
