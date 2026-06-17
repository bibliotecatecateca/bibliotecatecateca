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
	
	//Método responsável por buscar livros no banco de dados
	public void buscarLivros() {
		
		//Pega o que foi digitado e remove os espaços extras com o .trim()
	    String titulo = tfPesquisar.getText().trim();

	    //Cria um objeto da classe Livro e passa o titulo digitado como parametro
	    Livro livro = new Livro();
	    livro.setTitulo(titulo);

	    ConexaoBD bd = new ConexaoBD();

	    if (bd.connect()) {
	        LivroDAO livroDao = new LivroDAO(bd, livro);

	        //Busca os livros por meio do método
	        boolean estado = livroDao.buscar();

	        //Busca sucedida 
	        if (estado) {
	        	
	        	//Pega a lista de livros encontrada
	            List<Livro> listaLivros = livroDao.getLivros();

	            //Limpa as linhas antigas da tabela
	            modeloTabela.setRowCount(0);

	            //Se a lista estiver vazia 
	            if (listaLivros.isEmpty()) {
	            	
	            	//Printa a mensagem
	                JOptionPane.showMessageDialog(null, "Nenhum livro encontrado.");
	                
	                //Esconde a tabela
	                scrollLivros.setVisible(false);
	                
	                //Deixa a foto visivel
	                lblFoto.setVisible(true);
	            } else {
	            	
	            	//Se a lista não estiver vazia percorre a lista de livros encontrados
	                for (Livro l : listaLivros) {
	                	
	                	//Adiciona cada livro como uma linha da tabela
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

	                //Esconde a foto
	                lblFoto.setVisible(false);
	                
	                //Exibe a tabela
	                scrollLivros.setVisible(true);
	            }
	            
	            //Atualiza visualmente o painel:
	            //O Java descobre o tamanho da tabela e prepara o espaço dela na tela
	            revalidate();
	            
	            //O Java limpa o desenho da foto antiga e desenha as linhas da tabela com os textos dos livros
	            repaint();
	        } else {
	            JOptionPane.showMessageDialog(null, "Erro ao buscar livros.");
	        }

	        bd.close();
	    } else {
	        JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
	    }
	}

	public AbaLivros() {
		
		JLabel lblTitulo = new JLabel("Livros");
		lblTitulo.setBounds(10, 10, 66, 20);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBounds(893, 10, 84, 20);
		btnCadastrar.setFont(new Font("Arial", Font.BOLD, 10));
		
		btnCadastrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//Abre a tela CadastroLivro e envia a si própria como parâmetro para atualizar a tabela depois do cadastro
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
			    AbaLivros.class.getResource("/view/Apis-melifera_1.png")
			);

			Image imagemRedimensionada = iconOriginal.getImage()
			        .getScaledInstance(250, 350, Image.SCALE_SMOOTH);

			lblFoto = new JLabel();
			lblFoto.setBounds(10, 60, 967, 505);
			lblFoto.setIcon(new ImageIcon(imagemRedimensionada));
			lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
			
			//Impede edição dos dados direto pela tabela
			modeloTabela = new DefaultTableModel() {
			    @Override
			    public boolean isCellEditable(int row, int column) {
			        return false;
			    }
			};

			//Adiciona as colunas da tabel
			modeloTabela.addColumn("ID");
			modeloTabela.addColumn("Título");
			modeloTabela.addColumn("Gênero");
			modeloTabela.addColumn("Editora");
			modeloTabela.addColumn("Autor");
			modeloTabela.addColumn("Disponível");
			modeloTabela.addColumn("Editar");
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
					
					//Identifica a linha clicada
			        int linha = tabelaLivros.rowAtPoint(e.getPoint());
			        
			        //Identifica a coluna clicada
			        int coluna = tabelaLivros.columnAtPoint(e.getPoint());

	                //Pega os dados do cliente selecionado na tabela
	                //Transforma em String, pois getValueAt retorna objeto
			        int idLivro = Integer.parseInt(tabelaLivros.getValueAt(linha, 0).toString());
			        String titulo = tabelaLivros.getValueAt(linha, 1).toString();
			        String genero = tabelaLivros.getValueAt(linha, 2).toString();
			        String editora = tabelaLivros.getValueAt(linha, 3).toString();
			        String autor = tabelaLivros.getValueAt(linha, 4).toString();
			        String disponivelTexto = tabelaLivros.getValueAt(linha, 5).toString();

			        //Converte o texto "Sim" ou "Não" para boolean
			        boolean disponivel = disponivelTexto.equalsIgnoreCase("Sim");

			        //EDITAR
			        if (coluna == 6) {
			        	
			        	//Cria um objeto livro e preenche ele com os dados das colunas
			            Livro livro = new Livro();
			            livro.setIdLivro(idLivro);
			            livro.setTitulo(titulo);
			            livro.setGenero(genero);
			            livro.setEditora(editora);
			            livro.setAutor(autor);
			            livro.setDisponivel(disponivel);

			            
			            //Cria a tela de edição que recebe como parametro o livro e a AbaLivros para ser atualizada quando apertar o botão
			            AbaEditarLivro editar = new AbaEditarLivro(livro, AbaLivros.this);
			            
			            //Abre a tela de edição do livro
			            editar.setVisible(true);
			        }

			        //EXCLUIR
			        if (coluna == 7) {
			        	
			        	//Exibe uma mensagem de confirmação de exclusão
			            int opcao = JOptionPane.showConfirmDialog(
			                null,
			                "Deseja excluir o livro \"" + titulo + "\"?",
			                "Confirmar exclusão",
			                JOptionPane.YES_NO_OPTION
			            );

			            //Se o usuário confirmou a exclusão
			            if (opcao == JOptionPane.YES_OPTION) {
			            	
			            	//Cria um objeto livro e passa o id para saber qual foi selecionado
			                Livro livro = new Livro();
			                livro.setIdLivro(idLivro);

			                ConexaoBD bd = new ConexaoBD();

			                if (bd.connect()) {
			                    LivroDAO livroDAO = new LivroDAO(bd, livro);

			                    //Executa a exclusão do cliente no banco
			                    String mensagem = livroDAO.atualizar(model.TipoAtualizaBD.Deletar);

			                    JOptionPane.showMessageDialog(null, mensagem);

			                    bd.close();

			                    //Atualiza a lista de livros
			                    buscarLivros();
			                } else {
			                    JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
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
						//Chama o método que pesquisa os livros no banco
						buscarLivros();
					
				}
	        });
			
			btnBuscar.setFont(new Font("Arial", Font.BOLD, 10));
			btnBuscar.setBounds(699, 28, 84, 20);
		setLayout(null);
		add(lblTitulo);
		add(lblPesquisar);
		add(tfPesquisar);
		add(btnBuscar);
		add(btnCadastrar);
		add(lblFoto);
		add(scrollLivros);

	}
}
