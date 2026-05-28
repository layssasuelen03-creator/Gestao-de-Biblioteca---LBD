package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class PainelLivros extends JPanel {

    private final TelaPrincipal frame;
    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLS = {"#","Titulo","Autor","Genero","Ano","Copias","Disponiveis","Status","Acoes"};

    private static final Object[][] SOURCE = {
        {1,"Dom Quixote",                 "Miguel de Cervantes",        "Literatura", 1605,3,2,"Disponivel", Theme.COVERS[0]},
        {2,"Pai Rico, Pai Pobre",          "Robert Kiyosaki",            "Financas",   1997,5,4,"Disponivel", Theme.COVERS[1]},
        {3,"A Estrategia do Oceano Azul", "W. Chan Kim / R. Mauborgne", "Negocios",   2005,2,0,"Emprestado", Theme.COVERS[2]},
        {4,"Didatica",                    "Ilma Passos Alencastro",     "Educacao",   2008,4,3,"Disponivel", Theme.COVERS[3]},
        {5,"Entendendo Algoritmos",       "Aditya Y. Bhargava",         "Tecnologia", 2016,6,5,"Disponivel", Theme.COVERS[4]},
        {6,"Clean Code",                  "Robert C. Martin",           "Tecnologia", 2020,3,1,"Disponivel", Theme.COVERS[5]},
        {7,"O Poder do Habito",           "Charles Duhigg",             "Autoajuda",  2019,4,4,"Disponivel", Theme.COVERS[6]},
        {8,"Design Patterns",             "Gang of Four",               "Tecnologia", 2018,2,2,"Disponivel", Theme.COVERS[7]},
    };

    public PainelLivros(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18,18,18,18));

        Components.RoundedPanel card = Components.card();
        Components.StyledButton addBtn = new Components.StyledButton("+ Novo Livro", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));
        card.add(Components.cardHeader("Acervo de Livros ("+SOURCE.length+")", addBtn), BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT,10,8));
        searchRow.setBackground(Color.WHITE);
        searchRow.setBorder(new MatteBorder(0,0,1,0,Theme.CARD_BORDER));
        JTextField searchF = Components.styledField(""); searchF.setPreferredSize(new Dimension(280,30));
        Components.StyledButton searchBtn = new Components.StyledButton("Buscar", Theme.BTN_INFO, Color.WHITE);
        searchBtn.setSmall();
        searchBtn.addActionListener(e -> filterTable(searchF.getText()));
        searchF.addActionListener(e -> filterTable(searchF.getText()));
        searchRow.add(new JLabel("Pesquisar:")); searchRow.add(searchF); searchRow.add(searchBtn);

        tableModel = new DefaultTableModel(COLS,0){
            @Override public boolean isCellEditable(int r,int c){return c==8;}
            @Override public Class<?> getColumnClass(int c){return Object.class;}
        };
        populateTable(SOURCE);

        table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(230);
        table.getColumnModel().getColumn(7).setPreferredWidth(90);
        table.getColumnModel().getColumn(8).setPreferredWidth(130);
        table.getColumnModel().getColumn(7).setCellRenderer(statusRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ActionsEditor(this));

        JScrollPane sp = new JScrollPane(table); sp.setBorder(null);
        JPanel body = new JPanel(new BorderLayout()); body.setBackground(Color.WHITE);
        body.add(searchRow, BorderLayout.NORTH); body.add(sp, BorderLayout.CENTER);
        card.add(body, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void populateTable(Object[][] data) {
        tableModel.setRowCount(0);
        for(Object[] row:data)
            tableModel.addRow(new Object[]{row[0],row[1],row[2],row[3],row[4],row[5],row[6],row[7],"actions"});
    }

    private void filterTable(String q) {
        tableModel.setRowCount(0);
        for(Object[] row:SOURCE){
            if(q.isBlank()||row[1].toString().toLowerCase().contains(q.toLowerCase())
                ||row[2].toString().toLowerCase().contains(q.toLowerCase())
                ||row[3].toString().toLowerCase().contains(q.toLowerCase()))
                tableModel.addRow(new Object[]{row[0],row[1],row[2],row[3],row[4],row[5],row[6],row[7],"actions"});
        }
    }

    private TableCellRenderer statusRenderer(){
        return (t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            String val=v==null?"":v.toString();
            p.add(val.equals("Emprestado")?Components.loanBadge(val):val.equals("atrasado")?Components.overBadge(val):Components.availBadge(val));
            return p;
        };
    }

    private TableCellRenderer actionsRenderer(){
        return (t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            Components.StyledButton edit=new Components.StyledButton("Editar",Theme.BTN_WARNING,Color.WHITE); edit.setSmall();
            Components.StyledButton del=new Components.StyledButton("Excluir",Theme.BTN_DANGER,Color.WHITE); del.setSmall();
            p.add(edit); p.add(del); return p;
        };
    }

    public void openForm(int row){
        boolean isEdit=row>=0;
        JDialog dlg=new JDialog(frame,isEdit?"Editar Livro":"Novo Livro",true);
        dlg.setSize(440,400); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form=new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1; gbc.insets=new Insets(3,0,3,0);
        JTextField fTitulo=Components.styledField(""); JTextField fAutor=Components.styledField("");
        JTextField fGenero=Components.styledField(""); JTextField fAno=Components.styledField("");
        JTextField fCopias=Components.styledField("");
        if(isEdit){fTitulo.setText(tableModel.getValueAt(row,1).toString());fAutor.setText(tableModel.getValueAt(row,2).toString());fGenero.setText(tableModel.getValueAt(row,3).toString());fAno.setText(tableModel.getValueAt(row,4).toString());fCopias.setText(tableModel.getValueAt(row,5).toString());}
        String[] labels={"Titulo *","Autor *","Genero","Ano","Total de copias *"};
        JTextField[] fields={fTitulo,fAutor,fGenero,fAno,fCopias};
        for(int i=0;i<labels.length;i++){gbc.gridy=i*2;form.add(Components.formLabel(labels[i]),gbc);gbc.gridy=i*2+1;form.add(fields[i],gbc);}
        gbc.gridy=labels.length*2+1; gbc.insets=new Insets(14,0,0,0);
        Components.StyledButton saveBtn=new Components.StyledButton(isEdit?"Atualizar":"Salvar",Theme.BTN_PRIMARY,Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE,36));
        saveBtn.addActionListener(e->{
            if(fTitulo.getText().isBlank()||fAutor.getText().isBlank()||fCopias.getText().isBlank()){JOptionPane.showMessageDialog(dlg,"Preencha os campos obrigatorios.","Atencao",JOptionPane.WARNING_MESSAGE);return;}
            JOptionPane.showMessageDialog(dlg,isEdit?"Livro atualizado!":"Livro cadastrado!","Sucesso",JOptionPane.INFORMATION_MESSAGE); dlg.dispose();
        });
        form.add(saveBtn,gbc); dlg.add(form); dlg.setVisible(true);
    }

    public void confirmDelete(int row){
        String titulo=tableModel.getValueAt(row,1).toString();
        int opt=JOptionPane.showConfirmDialog(frame,"Excluir \""+titulo+"\"?","Confirmar",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(opt==JOptionPane.YES_OPTION){tableModel.removeRow(row);JOptionPane.showMessageDialog(frame,"Excluido!","OK",JOptionPane.INFORMATION_MESSAGE);}
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelLivros owner; private int currentRow;
        ActionsEditor(PainelLivros owner){
            super(new JCheckBox()); this.owner=owner; setClickCountToStart(1);
            panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4)); panel.setBackground(Theme.ROW_HOVER);
            Components.StyledButton edit=new Components.StyledButton("Editar",Theme.BTN_WARNING,Color.WHITE); edit.setSmall();
            Components.StyledButton del=new Components.StyledButton("Excluir",Theme.BTN_DANGER,Color.WHITE); del.setSmall();
            edit.addActionListener(e->{fireEditingStopped();owner.openForm(currentRow);});
            del.addActionListener(e->{fireEditingStopped();owner.confirmDelete(currentRow);});
            panel.add(edit); panel.add(del);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean sel,int r,int c){currentRow=r;return panel;}
        @Override public Object getCellEditorValue(){return"actions";}
    }
}
