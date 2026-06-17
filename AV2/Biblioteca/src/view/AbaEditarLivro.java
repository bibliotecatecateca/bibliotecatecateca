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

public class AbaEditarLivro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfTitulo;
	private JTextField tfAutor;
	private JTextField tfGenero;
	private JTextField tfEditora;


	//Recebe um objeto Livro que tem os dados do livro que serão editados
	//e tela AbaLivros, para atualizar a lista após a edição
	public AbaEditarLivro(Livro livro, AbaLivros abaLivros) {
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
		
		JLabel lblEditarLivro = new JLabel("Editar Livro");
		lblEditarLivro.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditarLivro.setBounds(150, 29, 138, 22);
		lblEditarLivro.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(lblEditarLivro);
		
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
		
		JLabel lblGenero = new JLabel("Gênero");
		lblGenero.setHorizontalAlignment(SwingConstants.CENTER);
		lblGenero.setFont(new Font("Arial", Font.PLAIN, 13));
		lblGenero.setBounds(302, 65, 44, 12);
		contentPane.add(lblGenero);
		
		
		JLabel lblEditora = new JLabel("Editora");
		lblEditora.setFont(new Font("Arial", Font.PLAIN, 13));
		lblEditora.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditora.setBounds(302, 131, 44, 12);
		contentPane.add(lblEditora);
		
		tfTitulo = new JTextField();
		tfTitulo.setBounds(29, 83, 170, 18);
		contentPane.add(tfTitulo);
		tfTitulo.setColumns(10);
		
		tfAutor = new JTextField();
		tfAutor.setBounds(29, 158, 170, 18);
		contentPane.add(tfAutor);
		tfAutor.setColumns(10);
		
		tfGenero = new JTextField();
		tfGenero.setBounds(243, 87, 170, 18);
		contentPane.add(tfGenero);
		tfGenero.setColumns(10);
		
		tfEditora = new JTextField();
		tfEditora.setBounds(243, 158, 170, 18);
		contentPane.add(tfEditora);
		tfEditora.setColumns(10);
		
		
		//Preenche os campos da tela com os dados atuais do livro selecionado para edição
		tfTitulo.setText(livro.getTitulo());
		tfAutor.setText(livro.getAutor());
		tfGenero.setText(livro.getGenero());
		tfEditora.setText(livro.getEditora());
		
		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {

		    	
		    	//Pega o que foi digitado e remove os espaços extras com o .trim()
		        String titulo = tfTitulo.getText().trim();
		        String autor = tfAutor.getText().trim();
		        String genero = tfGenero.getText().trim();
		        String editora = tfEditora.getText().trim();

		        
		        //Verifica se todos os campos estão digitados e se não printa avisando
		        if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || editora.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
		            return;
		        }

		        //Atualiza o objeto com os dados de cada campo
		        livro.setTitulo(titulo);
		        livro.setAutor(autor);
		        livro.setGenero(genero);
		        livro.setEditora(editora);

		        ConexaoBD bd = new ConexaoBD();

		        if (bd.connect()) {
		            LivroDAO livroDAO = new LivroDAO(bd, livro);

		            //Atualiza o livro no banco
		            String mensagem = livroDAO.atualizar(TipoAtualizaBD.Alterar);

		            JOptionPane.showMessageDialog(null, mensagem);

		            bd.close();

		            //Atualiza a página usando o para buscar os livros novamente
		            abaLivros.buscarLivros();

		            dispose();

		        } else {
		            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco.");
		        }
		    }
		});
		btnAtualizar.setFont(new Font("Arial", Font.PLAIN, 10));
		btnAtualizar.setBounds(172, 205, 84, 20);
		contentPane.add(btnAtualizar);

	}


}
