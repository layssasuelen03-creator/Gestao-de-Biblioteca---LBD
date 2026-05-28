package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class PainelGeneros extends JPanel {

    private final TelaPrincipal frame;
    private DefaultTableModel tableModel;

    private static final Object[][] DATA = {
        {1,"Literatura",  1, Theme.COVERS[0]},
        {2,"Financas",    1, Theme.COVERS[1]},
        {3,"Negocios",    1, Theme.COVERS[2]},
        {4,"Educacao",    1, Theme.COVERS[3]},
        {5,"Tecnologia",  3, Theme.COVERS[4]},
        {6,"Autoajuda",   1, Theme.COVERS[6]},
    };

    public PainelGeneros(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18,18,18,18));

        Components.RoundedPanel card = Components.card();
        Components.StyledButton addBtn = new Components.StyledButton("+ Novo Genero", Theme.BTN_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openForm(-1));
        card.add(Components.cardHeader("Generos ("+DATA.length+")", addBtn), BorderLayout.NORTH);

        String[] cols={"#","Genero","Livros","Acoes"};
        tableModel=new DefaultTableModel(cols,0){
            @Override public boolean isCellEditable(int r,int c){return c==3;}
        };
        for(Object[] row:DATA) tableModel.addRow(new Object[]{row[0],row[1],row[2],"actions"});

        JTable table=Components.buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);

        // Colored dot + name
        table.getColumnModel().getColumn(1).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,8));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            Color dotColor = r<DATA.length ? (Color)DATA[r][3] : Color.GRAY;
            JPanel dot=new JPanel(){
                @Override protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(dotColor); g2.fillOval(0,2,10,10); g2.dispose();
                }
            };
            dot.setOpaque(false); dot.setPreferredSize(new Dimension(10,14));
            JLabel lbl=new JLabel(v==null?"":v.toString()); lbl.setFont(Theme.FONT_BODY);
            lbl.setForeground(Theme.TEXT_PRIMARY); p.add(dot); p.add(lbl); return p;
        });
        table.getColumnModel().getColumn(2).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
            p.setBackground(sel?Theme.ROW_HOVER:(r%2==0?Color.WHITE:Theme.ROW_ALT));
            p.add(Components.availBadge(v==null?"0":v.toString())); return p;
        });
        table.getColumnModel().getColumn(3).setCellRenderer(actionsRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ActionsEditor(this));

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
        JDialog dlg=new JDialog(frame,isEdit?"Editar Genero":"Novo Genero",true);
        dlg.setSize(360,200); dlg.setLocationRelativeTo(frame);
        dlg.getContentPane().setBackground(Color.WHITE);
        JPanel form=new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1; gbc.insets=new Insets(3,0,3,0);
        JTextField fNome=Components.styledField("");
        if(isEdit) fNome.setText(tableModel.getValueAt(row,1).toString());
        gbc.gridy=0; form.add(Components.formLabel("Nome do genero *"),gbc);
        gbc.gridy=1; form.add(fNome,gbc);
        gbc.gridy=3; gbc.insets=new Insets(14,0,0,0);
        Components.StyledButton save=new Components.StyledButton(isEdit?"Atualizar":"Salvar",Theme.BTN_PRIMARY,Color.WHITE);
        save.setPreferredSize(new Dimension(Integer.MAX_VALUE,36));
        save.addActionListener(e->{
            if(fNome.getText().isBlank()){JOptionPane.showMessageDialog(dlg,"Nome obrigatorio.","Atencao",JOptionPane.WARNING_MESSAGE);return;}
            JOptionPane.showMessageDialog(dlg,"Genero salvo!","Sucesso",JOptionPane.INFORMATION_MESSAGE); dlg.dispose();
        });
        form.add(save,gbc); dlg.add(form); dlg.setVisible(true);
    }

    static class ActionsEditor extends DefaultCellEditor {
        private JPanel panel; private final PainelGeneros owner; private int row;
        ActionsEditor(PainelGeneros owner){
            super(new JCheckBox()); this.owner=owner; setClickCountToStart(1);
            panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4)); panel.setBackground(Theme.ROW_HOVER);
            Components.StyledButton edit=new Components.StyledButton("Editar",Theme.BTN_WARNING,Color.WHITE); edit.setSmall();
            Components.StyledButton del=new Components.StyledButton("Excluir",Theme.BTN_DANGER,Color.WHITE); del.setSmall();
            edit.addActionListener(e->{fireEditingStopped(); owner.openForm(row);});
            del.addActionListener(e->{fireEditingStopped();
                if(JOptionPane.showConfirmDialog(null,"Excluir genero?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                    owner.tableModel.removeRow(row);
            });
            panel.add(edit); panel.add(del);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean sel,int r,int c){row=r;return panel;}
        @Override public Object getCellEditorValue(){return"actions";}
    }
}
