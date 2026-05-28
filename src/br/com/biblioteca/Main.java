package br.com.biblioteca;

import br.com.biblioteca.ui.TelaPrincipal;
import javax.swing.*;


//TelaPrincipal do Java Swing, executa a aplicção
public class Main {
    public static void main(String[] args) {
        // Habilitar anti-aliasing para texto
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        //Aparência do sistema como base
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        //Inicia a janela na EDT
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal app = new TelaPrincipal();
            app.setVisible(true);
        });
    }
}
