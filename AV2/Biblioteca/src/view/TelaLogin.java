package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.FuncionarioDAO;
import model.ConexaoBD;
import model.Funcionario;
import model.LoginFuncionario;

public class TelaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfUsuario;
	private JPasswordField pfSenha;
	
	public TelaLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		//Centralizar na tela
        setLocationRelativeTo(null);
        //Não permite o úsuario dar tela cheia
        setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(118, 27, 200, 30);
		lblLogin.setFont(new Font("Arial", Font.BOLD, 18));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblLogin);
		
		JLabel lblUsuario = new JLabel("Usuário");
		lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
		lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsuario.setToolTipText("");
		lblUsuario.setBounds(113, 87, 80, 25);
		contentPane.add(lblUsuario);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Arial", Font.PLAIN, 12));
		lblSenha.setHorizontalAlignment(SwingConstants.CENTER);
		lblSenha.setBounds(113, 125, 80, 25);
		contentPane.add(lblSenha);
		
		tfUsuario = new JTextField();
		tfUsuario.setHorizontalAlignment(SwingConstants.LEFT);
		tfUsuario.setBounds(203, 90, 96, 18);
		contentPane.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		pfSenha = new JPasswordField();
		pfSenha.setBounds(203, 128, 96, 18);
		contentPane.add(pfSenha);
		
		JButton btnEntrar = new JButton("Entrar");
		
		btnEntrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Pega os campos digitados
				String usuario = tfUsuario.getText();
				String senha = String.valueOf(pfSenha.getPassword());
				
				//Cria um objeto login para passar de parametro na criação do objeto funcionario
				LoginFuncionario login = new LoginFuncionario(usuario, senha);
				Funcionario func = new Funcionario(login);
				
				ConexaoBD bd = new ConexaoBD();
	         	if (bd.connect()) {
	         		FuncionarioDAO loginDao = new FuncionarioDAO(bd, func);
	         		
	         		//Busca o funcionário no banco de dados retorna true se encontrar e false se não encontrar
	         		boolean estado = loginDao.buscar();
	         		bd.close();
	         		
	         		//Se retornou true
	         		if(estado) {
	         			//Valida se o usuário e a senha estão corretos
	         			if (login.validarLogin(usuario,senha)) {
	         				JOptionPane.showMessageDialog(null, "Login realizado!");
	         				
	         				//Se o ID do funcionário for 1, abre a tela
	         				if(func.getIdFunc() == 1) {
	         					TelaAdministrador telaADM = new TelaAdministrador();
	         					telaADM.setVisible(true);
	         				
	         				} else {
	         					
	         					//Se for outro ID abre a TelaPrincipal
		         				TelaPrincipal tela = new TelaPrincipal();
		         				tela.setVisible(true);
	         					
	         				}
	         				// Fecha a tela de login
	         				dispose();
	         			} else {
	         				JOptionPane.showMessageDialog( null, "Login ou Senha incorretas!", "Informação", JOptionPane.ERROR_MESSAGE );
	         			}
                		} else {
                			JOptionPane.showMessageDialog( null, "Login ou Senha incorretas!", "Informação", JOptionPane.ERROR_MESSAGE );
                		}
	         	}	
            }
        });
		
		btnEntrar.setFont(new Font("Arial", Font.BOLD, 11));
		btnEntrar.setBounds(167, 182, 96, 25);
		contentPane.add(btnEntrar);

	}
}
