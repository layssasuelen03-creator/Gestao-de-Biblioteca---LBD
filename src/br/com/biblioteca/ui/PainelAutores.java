package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class PainelAutores extends JPanel {

    private final TelaPrincipal frame;
    private DefaultTableModel tableModel;

    private static final Object[][] DATA = {
        {1,"Miguel de Cervantes",       "Literatura",  "Dom Quixote"},
        {2,"Robert Kiyosaki",           "Financas",    "Pai Rico, Pai Pobre"},
        {3,"W. Chan Kim / R. Mauborgne","Negocios",    "A Estrategia do Oceano Azul"},
        {4,"Ilma Passos Alencastro",    "Educacao",    "Didatica"},
        {5,"Aditya Y. Bhargava",        "Tecnologia",  "Entendendo Algoritmos"},
        {6,"Robert C. Martin",          "Tecnologia",  "Clean Code"},
        {7,"Charles Duhigg",            "Autoajuda",   "O Poder do Habito"},
        {8,"Gang of Four",              "Tecnologia",  "Design Patterns"},
    };

    public PainelAutores(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18,18,18,18));

        Components.RoundedPanel card = Components.card();
        Components.StyledButton addBtn = new Components.StyledButton("+ Novo Autor", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));
        card.add(Components.cardHeader("Autores ("+DATA.length+")", addBtn), BorderLayout.NORTH);

        String[] cols={"#","Nome","Especialidade","Livros cadastrados","Acoes"};
        tableModel=new DefaultTableModel(cols,0){
            @Override public boolean isCellEditable(int r,int c){return c==4;}
        };
        for(Object[] row:DATA) tableModel.addRow(new Object[]{row[0],row[1],row[2],row[3],"actions"});

        JTable table=Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(240);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ActionsEditor(this));

        JScrollPane sp=new JScrollPane(table); sp.setBorder(null);
        card.add(sp,BorderLayout.CENTER); add(card,BorderLayout.CENTER);
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
        JDialog dlg=new JDialog(frame,isEdit?"Editar Autor":"Novo Autor",true);
        dlg.setSize(400,260); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form=new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1; gbc.insets=new Insets(3,0,3,0);
        JTextField fNome=Components.styledField(""); JTextField fEsp=Components.styledField("");
        if(isEdit){fNome.setText(tableModel.getValueAt(row,1).toString());fEsp.setText(tableModel.getValueAt(row,2).toString());}
        gbc.gridy=0; form.add(Components.formLabel("Nome *"),gbc);
        gbc.gridy=1; form.add(fNome,gbc);
        gbc.gridy=2; form.add(Components.formLabel("Especialidade"),gbc);
        gbc.gridy=3; form.add(fEsp,gbc);
        gbc.gridy=5; gbc.insets=new Insets(14,0,0,0);
        Components.StyledButton save=new Components.StyledButton(isEdit?"Atualizar":"Salvar",Theme.BTN_PRIMARY,Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE,36));
        save.addActionListener(e->{
            if(fNome.getText().isBlank()){JOptionPane.showMessageDialog(dlg,"Nome obrigatorio.","Atencao",JOptionPane.WARNING_MESSAGE);return;}
            JOptionPane.showMessageDialog(dlg,"Autor salvo!","Sucesso",JOptionPane.INFORMATION_MESSAGE); dlg.dispose();
        });
        form.add(save,gbc); dlg.add(form); dlg.setVisible(true);
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelAutores owner; private int row;
        ActionsEditor(PainelAutores owner){
            super(new JCheckBox()); this.owner=owner; setClickCountToStart(1);
            panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4)); panel.setBackground(Theme.ROW_HOVER);
            Components.StyledButton edit=new Components.StyledButton("Editar",Theme.BTN_WARNING,Color.WHITE); edit.setSmall();
            Components.StyledButton del=new Components.StyledButton("Excluir",Theme.BTN_DANGER,Color.WHITE); del.setSmall();
            edit.addActionListener(e->{fireEditingStopped(); owner.openForm(row);});
            del.addActionListener(e->{fireEditingStopped();
                if(JOptionPane.showConfirmDialog(null,"Excluir autor?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                    owner.tableModel.removeRow(row);
            });
            panel.add(edit); panel.add(del);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean sel,int r,int c){row=r;return panel;}
        @Override public Object getCellEditorValue(){return"actions";}
    }
}
