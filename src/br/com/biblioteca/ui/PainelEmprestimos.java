package br.com.biblioteca.ui;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;


//Tabela de empréstimos com badge de status + botão Devolver
public class PainelEmprestimos extends JPanel {

    private final TelaPrincipal frame;
    private DefaultTableModel tableModel;

    private static final Object[][] DATA = {
        {1,"Learn Java in One Minute",  "João Silva",   "10/05/2026","24/05/2026","ativo"},
        {2,"Think Inside The Box",      "Maria Santos", "15/05/2026","29/05/2026","ativo"},
        {3,"Clean Code",                "João Silva",   "01/05/2026","15/05/2026","atrasado"},
        {4,"O Poder do Hábito",         "Ana Costa",    "20/05/2026","03/06/2026","ativo"},
    };

    public PainelEmprestimos(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18,18,18,18));

        RoundedPanel card = Components.card();
        StyledButton addBtn = new StyledButton("+ Novo Empréstimo", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm());
        card.add(Components.cardHeader("Empréstimos", addBtn), BorderLayout.NORTH);

        String[] cols={"#","Livro","Membro","Data Empréstimo","Data Devolução","Status","Ações"};
        tableModel = new DefaultTableModel(cols,0){
            @Override public boolean isCellEditable(int r,int c){return c==6;}
        };
        for(Object[] row:DATA) tableModel.addRow(new Object[]{row[0],row[1],row[2],row[3],row[4],row[5],"actions"});

        JTable table = Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(5).setPreferredWidth(85);
        table.getColumnModel().getColumn(6).setPreferredWidth(115);

        //Status badge
        table.getColumnModel().getColumn(5).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            String val=v==null?"":v.toString();
            p.add(val.equals("atrasado")?Components.overBadge(val):Components.availBadge(val)); return p;
        });

        //Status actions 
        table.getColumnModel().getColumn(6).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            StyledButton dev=new StyledButton("✓ Devolver",Theme.BTN_SUCCESS,Color.WHITE); dev.setSmall(); p.add(dev); return p;
        });
        table.getColumnModel().getColumn(6).setCellEditor(new DevoluçãoEditor(this));

        JScrollPane sp=new JScrollPane(table); sp.setBorder(null);
        card.add(sp,BorderLayout.CENTER);
        add(card,BorderLayout.CENTER);
    }

    private void openForm() {
        JDialog dlg=new JDialog(frame,"Novo Empréstimo",true);
        dlg.setSize(440,310); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form=new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1; gbc.insets=new Insets(3,0,3,0);

        JTextField fLivro=Components.styledField("Título do livro");
        JTextField fMembro=Components.styledField("Nome do membro");
        JTextField fDev=Components.styledField("DD/MM/AAAA");

        String[] labels={"Livro *","Membro *","Data de Devolução Prevista *"};
        JTextField[] fields={fLivro,fMembro,fDev};
        for(int i=0;i<labels.length;i++){
            gbc.gridy=i*2; form.add(Components.formLabel(labels[i]),gbc);
            gbc.gridy=i*2+1; form.add(fields[i],gbc);
        }
        gbc.gridy=7; gbc.insets=new Insets(14,0,0,0);
        StyledButton save=new StyledButton("Registrar Empréstimo",Theme.BTN_PRIMARY,Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE,36));
        save.addActionListener(e->{
            if(fLivro.getText().isBlank()||fMembro.getText().isBlank()||fDev.getText().isBlank()){
                JOptionPane.showMessageDialog(dlg,"Preencha todos os campos obrigatórios.","Atenção",JOptionPane.WARNING_MESSAGE);return;}
            JOptionPane.showMessageDialog(dlg,"Empréstimo registrado!","Sucesso",JOptionPane.INFORMATION_MESSAGE); dlg.dispose();
        });
        form.add(save,gbc); dlg.add(form); dlg.setVisible(true);
    }

    public void devolverRow(int row){
        String livro = tableModel.getValueAt(row,1).toString();
        int opt=JOptionPane.showConfirmDialog(frame,
            "Confirmar devolução do livro:\n\""+livro+"\"?","Confirmar Devolução",JOptionPane.YES_NO_OPTION);
        if(opt==JOptionPane.YES_OPTION){
            tableModel.setValueAt("devolvido",row,5);
            JOptionPane.showMessageDialog(frame,"Devolução registrada com sucesso!","Sucesso",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static class DevoluçãoEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelEmprestimos owner; private int row;
        DevoluçãoEditor(PainelEmprestimos owner){
            super(new JCheckBox()); this.owner=owner; setClickCountToStart(1);
            panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4)); panel.setBackground(Theme.ROW_HOVER);
            StyledButton dev=new StyledButton("✓ Devolver",Theme.BTN_SUCCESS,Color.WHITE); dev.setSmall();
            dev.addActionListener(e->{ fireEditingStopped(); owner.devolverRow(row); });
            panel.add(dev);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean sel,int r,int c){row=r;return panel;}
        @Override public Object getCellEditorValue(){return"actions";}
    }
}
