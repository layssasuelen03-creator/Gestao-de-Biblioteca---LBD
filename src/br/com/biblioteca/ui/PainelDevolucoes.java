package br.com.biblioteca.ui;

import br.com.biblioteca.ui.Components.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PainelDevolucoes extends JPanel {

    private final TelaPrincipal frame;

    public PainelDevolucoes(TelaPrincipal frame) {
        this.frame = frame;
        setBackground(Theme.CONTENT_BG);
        setLayout(new GridBagLayout()); // centraliza o card

        RoundedPanel card = Components.card();
        card.setPreferredSize(new Dimension(500, 380));
        card.setLayout(new BorderLayout());
        card.add(Components.cardHeader("Registrar Devolução"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(4, 0, 4, 0);

        // Fields
        JTextField fId     = Components.styledField("Ex: 3");
        JTextField fLivro  = Components.styledField("Título do livro");
        JTextField fMembro = Components.styledField("Nome do membro");
        JComboBox<String> fCondicao = Components.styledCombo(new String[]{
            "Bom estado", "Com avarias leves", "Danificado"
        });

        String[] labels = {"ID do Empréstimo *", "Livro", "Membro", "Condição de devolução"};
        java.awt.Component[] fields = {fId, fLivro, fMembro, fCondicao};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;
            form.add(Components.formLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            form.add(fields[i], gbc);
        }

        //Botão
        gbc.gridy = 9;
        gbc.insets = new Insets(16, 0, 0, 0);
        StyledButton btn = new StyledButton("✓  Confirmar Devolução", Theme.BTN_SUCCESS, Color.WHITE);
        btn.setFont(Theme.bold(13));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.addActionListener(e -> {
            if (fId.getText().isBlank()) {
                JOptionPane.showMessageDialog(frame,
                    "Informe o ID do empréstimo.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(frame,
                "Devolução registrada com sucesso!\nCondição: " + fCondicao.getSelectedItem(),
                "Devolução Confirmada", JOptionPane.INFORMATION_MESSAGE);
            fId.setText(""); fLivro.setText(""); fMembro.setText("");
            fCondicao.setSelectedIndex(0);
        });
        form.add(btn, gbc);

        card.add(form, BorderLayout.CENTER);
        add(card);
    }
}
