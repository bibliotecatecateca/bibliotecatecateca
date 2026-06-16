package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.FuncionarioDAO;
import model.ConexaoBD;
import model.Funcionario;
import model.LoginFuncionario;
import model.TipoAtualizaBD;

public class TelaAdministrador extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextField tfUsuario;
	private JTextField tfSenha;

	/**
	 * Create the frame.
	 */
	public TelaAdministrador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 450);
		//Centralizar na tela
        setLocationRelativeTo(null);
        //Não permite o úsuario dar tela cheia
        setResizable(false);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel("Cadastrar Login do Funcionário");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitulo.setBounds(226, 66, 313, 29);
		contentPane.add(lblTitulo);
		
		JButton btnFuncExiste = new JButton("Funcionários");
		btnFuncExiste.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Abre a tela FuncionariosAtivos
		        FuncionariosAtivos tela = new FuncionariosAtivos();
		        tela.setVisible(true);
			}
		});

		
		btnFuncExiste.setFont(new Font("Arial", Font.BOLD, 10));
		btnFuncExiste.setBounds(670, 10, 106, 20);
		contentPane.add(btnFuncExiste);
		
		JLabel lblNomeFunc = new JLabel("Nome");
		lblNomeFunc.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNomeFunc.setHorizontalAlignment(SwingConstants.CENTER);
		lblNomeFunc.setBounds(300, 152, 44, 20);
		contentPane.add(lblNomeFunc);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
		lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsuario.setBounds(300, 203, 44, 20);
		contentPane.add(lblUsuario);
		
		JLabel lblNewLabel_3 = new JLabel("Senha");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(300, 251, 44, 20);
		contentPane.add(lblNewLabel_3);
		
		tfNome = new JTextField();
		tfNome.setBounds(354, 153, 96, 18);
		contentPane.add(tfNome);
		tfNome.setColumns(10);
		
		tfUsuario = new JTextField();
		tfUsuario.setBounds(354, 204, 96, 18);
		contentPane.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		tfSenha = new JTextField();
		tfSenha.setBounds(354, 252, 96, 18);
		contentPane.add(tfSenha);
		tfSenha.setColumns(10);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Pega os campos digitados
				String nome = tfNome.getText();
				String usuario = tfUsuario.getText();
				String senha = tfSenha.getText();
				
		        //Confirmar que nenhum ficou vazio e se ficou printar mensagem na tela
		        if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
		            return;
		        }
		        
		        //Cria um objeto login com usuario e senha definidos pelo campo digitado
		        LoginFuncionario login = new LoginFuncionario(usuario, senha);
		        
		        //Passa o login criado como parametro de um novo objeto funcionario
		        Funcionario func = new Funcionario(login);
		        
		        //Muda o nome do funcionario para o digitado no campo
		        func.setNome(nome);
		        
		        ConexaoBD bd = new ConexaoBD();
		        
		        if(bd.connect()) {
		            FuncionarioDAO funcionarioDao = new FuncionarioDAO(bd, func);
		            
		            //Cadastra o funcionario no banco
		            String mensagem = funcionarioDao.atualizar(TipoAtualizaBD.Criar);

		            //Printa a mensagem de sucesso
		            JOptionPane.showMessageDialog(null, mensagem);
		            
		            bd.close();
		        } else {
		            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
		        }
		        
			}
		});
		
		
		btnCadastrar.setFont(new Font("Arial", Font.BOLD, 10));
		btnCadastrar.setBounds(339, 339, 84, 20);
		contentPane.add(btnCadastrar);

	}

}
