package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class TelaPrincipal extends JFrame {
	
	//Classe que cria abas
	private JTabbedPane abas;

    public TelaPrincipal() {
    	//Comportamento do X(fechar) da tela
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 563);
		//Centralizar na tela
        setLocationRelativeTo(null);
        //Não permite o úsuario dar tela cheia
        setResizable(false);

        setTitle("Sistema Biblioteca");

        //Cria abas
        abas = new JTabbedPane();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(abas, java.awt.BorderLayout.CENTER);
        
        AbaLivros abaLivros = new AbaLivros();
        //Adiciona a aba "Livros"
        abas.addTab("Livros", abaLivros);
        //Adiciona a aba "Empréstimos"
        abas.addTab("Empréstimos", new AbaEmprestimo());


        setVisible(true);
    }
}



