package br.com.biblioteca.ui;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.UsuarioService;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

// Tabela de empréstimos 
public class PainelEmprestimos extends JPanel {

    private final TelaPrincipal     frame;
    private final EmprestimoService service    = new EmprestimoService();
    private final LivroService      livroSvc   = new LivroService();
    private final UsuarioService    usuarioSvc = new UsuarioService();
    private DefaultTableModel       tableModel;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelEmprestimos(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 18, 18, 18));

        RoundedPanel card = Components.card();
        StyledButton addBtn = new StyledButton("+ Novo Empréstimo", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm());
        card.add(Components.cardHeader("Empréstimos", addBtn), BorderLayout.NORTH);

        String[] cols = {"#", "Livro", "Membro", "Data Empréstimo", "Data Devolução", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
            

        JTable table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(5).setPreferredWidth(85);

        //Status 
        table.getColumnModel().getColumn(5).setCellRenderer((t, v, sel, foc, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            p.setBackground(sel ? Theme.ROW_HOVER : (r % 2 == 0 ? Color.WHITE : Theme.ROW_ALT));
            String val = v == null ? "" : v.toString();
            p.add(val.equals("atrasado") ? Components.overBadge(val) : Components.availBadge(val)); return p;
        });

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        try {
            List<Emprestimo> lista = service.listarTodos();
            tableModel.setRowCount(0);
            for (Emprestimo e : lista) {
                String dataEmp = e.getDataEmprestimo() != null ? e.getDataEmprestimo().format(FMT) : "-";
                String dataDev = e.getDataPrevistaDevolucao() != null ? e.getDataPrevistaDevolucao().format(FMT) : "-";
                tableModel.addRow(new Object[]{e.getId(), e.getTituloLivro(), e.getNomeUsuario(), dataEmp, dataDev, e.getStatus() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar empréstimos:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openForm() {
        JDialog dlg = new JDialog(frame, "Novo Empréstimo", true);
        dlg.setSize(440, 310); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1; gbc.insets = new Insets(3, 0, 3, 0);

        //Carrega os livros disponíveis e usuários ativos 
        List<Livro>    livrosDisp = List.of();
        List<Usuario>  usuAtivos  = List.of();
        try { livrosDisp = livroSvc.listarTodos().stream()
                .filter(l -> "disponível".equalsIgnoreCase(l.getStatus())).collect(Collectors.toList()); } catch (Exception ignored) {}
        try { usuAtivos  = usuarioSvc.listarTodos().stream()
                .filter(u -> "ativo".equalsIgnoreCase(u.getStatus())).collect(Collectors.toList()); } catch (Exception ignored) {}

        String[] livroItems   = livrosDisp.stream().map(l -> l.getId() + " — " + l.getTitulo()).toArray(String[]::new);
        String[] usuarioItems = usuAtivos.stream() .map(u -> u.getId() + " — " + u.getNome()).toArray(String[]::new);

        JComboBox<String> fLivro  = Components.styledCombo(livroItems.length   > 0 ? livroItems   : new String[]{"Nenhum livro disponível"});
        JComboBox<String> fMembro = Components.styledCombo(usuarioItems.length > 0 ? usuarioItems : new String[]{"Nenhum usuário ativo"});
        JTextField fDev = Components.styledField("DD/MM/AAAA");

        String[] labels = {"Livro *", "Membro *", "Data de Devolução Prevista *"};
        java.awt.Component[] fields = {fLivro, fMembro, fDev};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;     form.add(Components.formLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1; form.add(fields[i], gbc);
        }
        gbc.gridy = 7; gbc.insets = new Insets(14, 0, 0, 0);
        StyledButton save = new StyledButton("Registrar Empréstimo", Theme.BTN_PRIMARY, Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));

        final List<Livro>   livrosFinal  = livrosDisp;
        final List<Usuario> usuFinal     = usuAtivos;

        save.addActionListener(e -> {
            if (fDev.getText().isBlank() || livrosFinal.isEmpty() || usuFinal.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Preencha todos os campos obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            try {
                LocalDate dataDev = LocalDate.parse(fDev.getText().trim(), FMT);
                Emprestimo emp = new Emprestimo();
                emp.setLivroId  (livrosFinal.get(fLivro.getSelectedIndex()).getId());
                emp.setUsuarioId(usuFinal.get(fMembro.getSelectedIndex()).getId());
                emp.setDataPrevistaDevolucao(dataDev);
                service.realizarEmprestimo(emp);
                refresh();
                frame.painelLivros.refresh();
                frame.painelEstoque.refresh();
                frame.painelDashboard.refresh();
                frame.painelRelatorios.refresh();
                JOptionPane.showMessageDialog(dlg, "Empréstimo registrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); 
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dlg, "Data inválida. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(save, gbc); dlg.add(form); dlg.setVisible(true);
    }

}