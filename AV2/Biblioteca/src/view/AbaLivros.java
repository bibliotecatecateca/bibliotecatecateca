package view;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.LivroDAO;
import model.ConexaoBD;
import model.Livro;

public class AbaLivros extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField tfPesquisar;
	private JLabel lblFoto;
	private JTable tabelaLivros;
	private JScrollPane scrollLivros;
	private DefaultTableModel modeloTabela;
	
	public void buscarLivros() {
	    String titulo = tfPesquisar.getText();

	    if (titulo.isEmpty()) {
	        return;
	    }

	    Livro livro = new Livro();
	    livro.setTitulo(titulo);

	    ConexaoBD bd = new ConexaoBD();

	    if (bd.connect()) {
	        LivroDAO livroDao = new LivroDAO(bd, livro);

	        boolean estado = livroDao.buscar();

	        if (estado) {
	            List<Livro> listaLivros = livroDao.getLivros();

	            modeloTabela.setRowCount(0);

	            for (Livro l : listaLivros) {
	                modeloTabela.addRow(new Object[] {
	                    l.getIdLivro(),
	                    l.getTitulo(),
	                    l.getGenero(),
	                    l.getEditora(),
	                    l.getAutor(),
	                    l.getDisponivel() ? "Sim" : "Não",
	                    "Editar",
	                    "Excluir"
	                });
	            }

	            lblFoto.setVisible(false);
	            scrollLivros.setVisible(true);
	            revalidate();
	            repaint();
	        }

	        bd.close();
	    }
	}

	/**
	 * Create the panel.
	 */
	public AbaLivros() {
		
		JLabel lblNewLabel = new JLabel("Livros");
		lblNewLabel.setBounds(10, 10, 66, 20);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblNewLabel);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBounds(893, 10, 84, 20);
		btnCadastrar.setFont(new Font("Arial", Font.BOLD, 10));
		btnCadastrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CadastroLivro cadastrar = new CadastroLivro(AbaLivros.this);
				cadastrar.setVisible(true);
			}
		});
		
		tfPesquisar = new JTextField();
		tfPesquisar.setBounds(275, 29, 418, 19);
		tfPesquisar.setColumns(10);
		
		JLabel lblPesquisar = new JLabel("Pesquisar");
		lblPesquisar.setBounds(434, 10, 100, 12);
		lblPesquisar.setHorizontalAlignment(SwingConstants.CENTER);
		lblPesquisar.setFont(new Font("Arial", Font.BOLD, 12));
		
		ImageIcon iconOriginal = new ImageIcon(
			    "C:\\Users\\franc\\Downloads\\Apis-melifera_1.png"
			);

		
			Image imagemRedimensionada = iconOriginal.getImage().getScaledInstance(250, 350, Image.SCALE_SMOOTH);
			lblFoto = new JLabel();
			lblFoto.setBounds(10, 60, 967, 505);
			lblFoto.setIcon(new ImageIcon(imagemRedimensionada));
			lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
			
			modeloTabela = new DefaultTableModel();

			modeloTabela.addColumn("ID");
			modeloTabela.addColumn("Título");
			modeloTabela.addColumn("Gênero");
			modeloTabela.addColumn("Editora");
			modeloTabela.addColumn("Autor");
			modeloTabela.addColumn("Disponível");modeloTabela.addColumn("Editar");
			modeloTabela.addColumn("Excluir");

			tabelaLivros = new JTable(modeloTabela);
			//Alterar o tamanho das colunas 
			tabelaLivros.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
			tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(300);  // Título
			tabelaLivros.getColumnModel().getColumn(2).setPreferredWidth(100);  // Gênero
			tabelaLivros.getColumnModel().getColumn(3).setPreferredWidth(120);  // Editora
			tabelaLivros.getColumnModel().getColumn(4).setPreferredWidth(120);  // Autor
			tabelaLivros.getColumnModel().getColumn(5).setPreferredWidth(80);   // Disponível
			tabelaLivros.getColumnModel().getColumn(6).setPreferredWidth(80);   // Editar
			tabelaLivros.getColumnModel().getColumn(7).setPreferredWidth(80);   // Excluir
			

			
			
			tabelaLivros.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			        int linha = tabelaLivros.getSelectedRow();
			        int coluna = tabelaLivros.getSelectedColumn();

			        if (linha == -1) {
			            return;
			        }

			        int idLivro = Integer.parseInt(tabelaLivros.getValueAt(linha, 0).toString());
			        String titulo = tabelaLivros.getValueAt(linha, 1).toString();
			        String genero = tabelaLivros.getValueAt(linha, 2).toString();
			        String editora = tabelaLivros.getValueAt(linha, 3).toString();
			        String autor = tabelaLivros.getValueAt(linha, 4).toString();
			        String disponivelTexto = tabelaLivros.getValueAt(linha, 5).toString();

			        boolean disponivel = disponivelTexto.equalsIgnoreCase("Sim");

			        if (coluna == 6) {
			            JOptionPane.showMessageDialog(null, "Editar livro: " + titulo);

			            Livro livro = new Livro();
			            livro.setIdLivro(idLivro);
			            livro.setTitulo(titulo);
			            livro.setGenero(genero);
			            livro.setEditora(editora);
			            livro.setAutor(autor);
			            livro.setDisponivel(disponivel);

			            AbaEditarLivro editar = new AbaEditarLivro(livro, AbaLivros.this);
			            editar.setVisible(true);
			            
			            
			            
			            
			        }

			        if (coluna == 7) {
			            int opcao = JOptionPane.showConfirmDialog(
			                null,
			                "Deseja excluir o livro \"" + titulo + "\"?",
			                "Confirmar exclusão",
			                JOptionPane.YES_NO_OPTION
			            );

			            if (opcao == JOptionPane.YES_OPTION) {
			            	Livro livro = new Livro();
			            	livro.setIdLivro(idLivro);

			            	ConexaoBD bd = new ConexaoBD();

			            	if (bd.connect()) {
			            	    LivroDAO livroDAO = new LivroDAO(bd, livro);

			            	    String mensagem = livroDAO.atualizar(model.TipoAtualizaBD.Deletar);

			            	    JOptionPane.showMessageDialog(null, mensagem);

			            	    bd.close();

			            	    modeloTabela.removeRow(linha);
			            	}
			            }
			        }
			    }
			});
			
			


			scrollLivros = new JScrollPane(tabelaLivros);
			scrollLivros.setBounds(10, 60, 967, 505);
			scrollLivros.setVisible(false);
			
			JButton btnBuscar = new JButton("Buscar");
			btnBuscar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					buscarLivros();
					
				}
	        });
			
			btnBuscar.setFont(new Font("Arial", Font.BOLD, 10));
			btnBuscar.setBounds(699, 28, 84, 20);
		setLayout(null);
		add(lblNewLabel);
		add(lblPesquisar);
		add(tfPesquisar);
		add(btnBuscar);
		add(btnCadastrar);
		add(lblFoto);
		add(scrollLivros);

	}
}
