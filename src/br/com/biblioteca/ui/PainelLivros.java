package br.com.biblioteca.ui;

import br.com.biblioteca.model.Autor;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Editora;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.service.AutorService;
import br.com.biblioteca.service.CategoriaService;
import br.com.biblioteca.service.EditoraService;
import br.com.biblioteca.service.LivroService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class PainelLivros extends JPanel {

    private final TelaPrincipal  frame;
    private final LivroService   service     = new LivroService();
    private final AutorService   autorSvc    = new AutorService();
    private final CategoriaService catSvc    = new CategoriaService();
    private final EditoraService editoraSvc  = new EditoraService();
    private DefaultTableModel    tableModel;
    private JTable               table;

    private static final DateTimeFormatter FORMATO_BR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String[] COLS = {"#", "Titulo", "ISBN", "Ano", "Status", "Categoria", "Autor", "Editora", "Acoes"};

    public PainelLivros(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        Components.RoundedPanel card = Components.card();
        Components.StyledButton addBtn = new Components.StyledButton("+ Novo Livro", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchRow.setBackground(Color.WHITE);
        searchRow.setBorder(new MatteBorder(0, 0, 1, 0, Theme.CARD_BORDER));
        JTextField searchF = Components.styledField(""); searchF.setPreferredSize(new Dimension(280, 30));
        Components.StyledButton searchBtn = new Components.StyledButton("Buscar", Theme.BTN_INFO, Color.WHITE);
        searchBtn.setSmall();
        searchBtn.addActionListener(e -> filterTable(searchF.getText()));
        searchF.addActionListener(e -> filterTable(searchF.getText()));
        searchRow.add(new JLabel("Pesquisar:")); searchRow.add(searchF); searchRow.add(searchBtn);

        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8; }
            @Override public Class<?> getColumnClass(int c) { return Object.class; }
        };

        
        table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);   // #
        table.getColumnModel().getColumn(1).setPreferredWidth(250);  // Titulo
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // ISBN
        table.getColumnModel().getColumn(3).setPreferredWidth(70);   // Ano
        table.getColumnModel().getColumn(4).setPreferredWidth(120);  // Status
        table.getColumnModel().getColumn(5).setPreferredWidth(120);  // Categoria
        table.getColumnModel().getColumn(6).setPreferredWidth(180);  // Autor
        table.getColumnModel().getColumn(7).setPreferredWidth(200);  // Editora
        table.getColumnModel().getColumn(8).setPreferredWidth(180);  // Ações
        table.getColumnModel().getColumn(4).setCellRenderer(statusRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ActionsEditor(this));

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        JPanel body = new JPanel(new BorderLayout()); body.setBackground(Color.WHITE);
        body.add(searchRow, BorderLayout.NORTH); body.add(sp, BorderLayout.CENTER);
        card.add(body, BorderLayout.CENTER);

        refresh();
        card.add(Components.cardHeader("Acervo de Livros (" + tableModel.getRowCount() + ")", addBtn), BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }

    public void refresh() {
        try {
            List<Livro> livros = service.listarTodos();
            populateTable(livros);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar livros:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTable(List<Livro> livros) {
        tableModel.setRowCount(0);
        for (Livro l : livros) {
            String autor = l.getNomeAutor() != null ? l.getNomeAutor() : "";
            String cat   = l.getNomeCategoria() != null ? l.getNomeCategoria() : "";
            String ano   = l.getAnoPublicacao() != null ? String.valueOf(l.getAnoPublicacao().getYear()) : "";
            tableModel.addRow(new Object[]{l.getId(), l.getTitulo(), l.getIsbn(), ano, l.getStatus(), cat, autor, l.getNomeEditora(), "actions"});
        }
    }

    private void filterTable(String q) {
        try {
            List<Livro> livros = q.isBlank() ? service.listarTodos() : service.buscar(q);
            populateTable(livros);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro na busca:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private TableCellRenderer statusRenderer() {
        return (t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            String val = v == null ? "" : v.toString();
            p.add("emprestado".equalsIgnoreCase(val) ? Components.loanBadge(val)
                : "atrasado".equalsIgnoreCase(val)  ? Components.overBadge(val)
                : Components.availBadge(val));
            return p;
        };
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
        Livro livroEdit = null;
        if (isEdit) {
            int id = (int) tableModel.getValueAt(row, 0);
            try { livroEdit = service.listarTodos().stream().filter(l -> l.getId() == id).findFirst().orElse(null); }
            catch (Exception ignored) {}
        }
        final Livro le = livroEdit;

        JDialog dlg = new JDialog(frame, isEdit ? "Editar Livro" : "Novo Livro", true);
        dlg.setSize(460, 470); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; gbc.insets = new Insets(3, 0, 3, 0);

        JTextField fTitulo = Components.styledField("");
        JTextField fIsbn   = Components.styledField("");
        JTextField fAno = Components.styledField("DD/MM/AAAA");

        //Chaves FK de (autor, categoria, editora)
        List<Autor>     autores    = List.of();
        List<Categoria> categorias = List.of();
        List<Editora>   editoras   = List.of();
        try { autores    = autorSvc.listarTodos();   } catch (Exception ignored) {}
        try { categorias = catSvc.listarTodos();     } catch (Exception ignored) {}
        try { editoras   = editoraSvc.listarTodos(); } catch (Exception ignored) {}

        String[] autorItems = autores.stream().map(a -> a.getId() + " - " + a.getNome()).toArray(String[]::new);
        String[] catItems   = categorias.stream().map(c -> c.getId() + " - " + c.getNome()).toArray(String[]::new);
        String[] edItems    = editoras.stream().map(e -> e.getId() + " - " + e.getNome()).toArray(String[]::new);
        JComboBox<String> cAutor = Components.styledCombo(autorItems.length > 0 ? autorItems : new String[]{"(nenhum)"});
        JComboBox<String> cCat   = Components.styledCombo(catItems.length   > 0 ? catItems   : new String[]{"(nenhum)"});
        JComboBox<String> cEdit  = Components.styledCombo(edItems.length    > 0 ? edItems    : new String[]{"(nenhum)"});
        JComboBox<String> cStatus = Components.styledCombo(new String[]{"disponível", "emprestado"});
        cStatus.setEnabled(false);

        if (isEdit && le != null) {
            fTitulo.setText(le.getTitulo());
            fIsbn.setText(String.valueOf(le.getIsbn()));
            fAno.setText(le.getAnoPublicacao() != null ? le.getAnoPublicacao().format(FORMATO_BR) : "");
            cStatus.setSelectedItem(le.getStatus());
            for (int i = 0; i < autores.size(); i++) if (autores.get(i).getId() == le.getIdAutor()) { cAutor.setSelectedIndex(i); break; }
            for (int i = 0; i < categorias.size(); i++) if (categorias.get(i).getId() == le.getIdCategoria()) { cCat.setSelectedIndex(i); break; }
            for (int i = 0; i < editoras.size(); i++) if (editoras.get(i).getId() == le.getIdEditora()) { cEdit.setSelectedIndex(i); break; }
        }

        String[] labels = {"Titulo *", "ISBN", "Ano publicacao (DD/MM/AAAA)", "Status", "Categoria", "Autor *", "Editora"};
        java.awt.Component[] flds = {fTitulo, fIsbn, fAno, cStatus, cCat, cAutor, cEdit};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;     form.add(Components.formLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1; form.add(flds[i], gbc);
        }
        gbc.gridy = labels.length * 2 + 1; gbc.insets = new Insets(14, 0, 0, 0);
        Components.StyledButton saveBtn = new Components.StyledButton(isEdit ? "Atualizar" : "Salvar", Theme.BTN_PRIMARY, Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));

        final List<Autor>     autoresFinal    = autores;
        final List<Categoria> categoriasFinal = categorias;
        final List<Editora>   editorasFinal   = editoras;

        saveBtn.addActionListener(e -> {
            if (fTitulo.getText().isBlank()) {
                JOptionPane.showMessageDialog(dlg, "Preencha os campos obrigatorios.", "Atencao", JOptionPane.WARNING_MESSAGE); return;
            }
            try {
                Livro l = (isEdit && le != null) ? le : new Livro();
                l.setTitulo(fTitulo.getText().trim());
                l.setIsbn(fIsbn.getText().trim());
                l.setStatus((String) cStatus.getSelectedItem());
                DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                l.setAnoPublicacao(fAno.getText().isBlank() ? null : LocalDate.parse(fAno.getText().trim(), formatoBR));
                if (!autoresFinal.isEmpty())    l.setIdAutor    (autoresFinal.get(cAutor.getSelectedIndex()).getId());
                if (!categoriasFinal.isEmpty()) l.setIdCategoria(categoriasFinal.get(cCat.getSelectedIndex()).getId());
                if (!editorasFinal.isEmpty())   l.setIdEditora  (editorasFinal.get(cEdit.getSelectedIndex()).getId());
                if (isEdit) service.atualizar(l); else service.cadastrar(l);
                refresh();
                frame.painelEstoque.refresh();
                frame.painelDashboard.refresh();
                frame.painelRelatorios.refresh();
                JOptionPane.showMessageDialog(dlg, isEdit ? "Livro atualizado!" : "Livro cadastrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(saveBtn, gbc); dlg.add(form); dlg.setVisible(true);
    }

    public void confirmDelete(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String titulo = tableModel.getValueAt(row, 1).toString();
        int opt = JOptionPane.showConfirmDialog(frame, "Excluir \"" + titulo + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                service.excluir(id);
                refresh();
                frame.painelEstoque.refresh();
                frame.painelDashboard.refresh();
                frame.painelRelatorios.refresh();
                JOptionPane.showMessageDialog(frame, "Excluido!", "OK", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao excluir:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelLivros owner; private int currentRow;
        ActionsEditor(PainelLivros owner) {
            super(new JCheckBox()); this.owner = owner; setClickCountToStart(1);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4)); panel.setBackground(Theme.ROW_HOVER);
            Components.StyledButton edit = new Components.StyledButton("Editar",  Theme.BTN_WARNING, Color.WHITE); edit.setSmall();
            Components.StyledButton del  = new Components.StyledButton("Excluir", Theme.BTN_DANGER,  Color.WHITE); del.setSmall();
            edit.addActionListener(e -> { fireEditingStopped(); owner.openForm(currentRow); });
            del .addActionListener(e -> { fireEditingStopped(); owner.confirmDelete(currentRow); });
            panel.add(edit); panel.add(del);
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int r, int c) { currentRow = r; return panel; }
        @Override public Object getCellEditorValue() { return "actions"; }
    }
}