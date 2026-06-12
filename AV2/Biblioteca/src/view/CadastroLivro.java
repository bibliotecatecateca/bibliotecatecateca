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

import dao.LivroDAO;
import model.ConexaoBD;
import model.Livro;
import model.TipoAtualizaBD;

public class CadastroLivro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfGenero;
	private JTextField tfAutor;
	private JTextField tfTitulo;
	private JTextField tfEditora;

	/**
	 * Create the frame.
	 */
	public CadastroLivro(AbaLivros abaLivros) {
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		//Centralizar na tela
        setLocationRelativeTo(null);
        //Não permite o úsuario dar tela cheia
        setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCadastraLivro = new JLabel("Cadastrar Livro");
		lblCadastraLivro.setBounds(142, 29, 138, 22);
		lblCadastraLivro.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(lblCadastraLivro);
		
		JLabel lblTitulo = new JLabel("Título");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.PLAIN, 13));
		lblTitulo.setBounds(81, 60, 57, 22);
		contentPane.add(lblTitulo);
		
		JLabel lblAutor = new JLabel("Autor");
		lblAutor.setHorizontalAlignment(SwingConstants.CENTER);
		lblAutor.setFont(new Font("Arial", Font.PLAIN, 13));
		lblAutor.setBounds(81, 126, 57, 22);
		contentPane.add(lblAutor);
		
		tfGenero = new JTextField();
		tfGenero.setBounds(243, 92, 170, 18);
		contentPane.add(tfGenero);
		tfGenero.setColumns(10);
		
		tfAutor = new JTextField();
		tfAutor.setBounds(30, 158, 170, 18);
		contentPane.add(tfAutor);
		tfAutor.setColumns(10);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Pegar tudo digitado em cada textfield
		        String titulo = tfTitulo.getText();
		        String autor = tfAutor.getText();
		        String genero = tfGenero.getText();
		        String editora = tfEditora.getText();
		        
		        //Confirmar que nenhum ficou vazio e se ficou printar mensagem na tela
		        if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || editora.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
		            return;
		        }
		        
		        //Criar um objeto da classe livro
		        Livro livro = new Livro();

		        //Definir os atributos do objeto livro com os dados preenchidos no tf
		        livro.setTitulo(titulo);
		        livro.setAutor(autor);
		        livro.setGenero(genero);
		        livro.setEditora(editora);
		        
		 
		        
		        //Criar um objeto para poder estabelecer a conexão
		        ConexaoBD bd = new ConexaoBD();

		        
		        if (bd.connect()) {
		            LivroDAO livroDao = new LivroDAO(bd, livro);

		            String mensagem = livroDao.atualizar(TipoAtualizaBD.Criar);

		            JOptionPane.showMessageDialog(null, mensagem);

		            bd.close();
		            
		            if (abaLivros != null) {
		                abaLivros.buscarLivros();
		            }

		            dispose();
		            
		        } else {
		            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
		        }
			
				
			}
		});
		
		btnCadastrar.setBounds(103, 216, 95, 22);
		contentPane.add(btnCadastrar);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		btnVoltar.setBounds(238, 216, 95, 22);
		contentPane.add(btnVoltar);
		
		JLabel lblGenero = new JLabel("Gênero");
		lblGenero.setHorizontalAlignment(SwingConstants.CENTER);
		lblGenero.setFont(new Font("Arial", Font.PLAIN, 13));
		lblGenero.setBounds(302, 65, 44, 12);
		contentPane.add(lblGenero);
		
		tfTitulo = new JTextField();
		tfTitulo.setBounds(30, 92, 170, 18);
		contentPane.add(tfTitulo);
		tfTitulo.setColumns(10);
		
		JLabel lblEditora = new JLabel("Editora");
		lblEditora.setFont(new Font("Arial", Font.PLAIN, 13));
		lblEditora.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditora.setBounds(302, 131, 44, 12);
		contentPane.add(lblEditora);
		
		tfEditora = new JTextField();
		tfEditora.setBounds(243, 158, 170, 18);
		contentPane.add(tfEditora);
		tfEditora.setColumns(10);

	}

}
