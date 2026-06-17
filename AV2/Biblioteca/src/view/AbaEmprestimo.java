package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.ClienteDAO;
import dao.EmprestimoDAO;
import model.Cliente;
import model.ConexaoBD;
import model.Emprestimo;
import model.Livro;
import model.TipoAtualizaBD;

public class AbaEmprestimo extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField tfPesquisarCpf;
	private JTextField tfNome;
	private JTextField tfCpf;
	private JTextField tfEmail;
	private JTextField tfTelefone;
	private JTextField textField;
	private boolean existeCliente;
	private Cliente cliente;

	//Remove todos os caracteres que não são números do CPF 
	// XXX.XXX.XXX-XX = XXXXXXXXXXX / XXX XXX XXX XX = XXXXXXXXXXX
	private String limparCpf(String cpf) {
	    return cpf.replaceAll("[^0-9]", "");
	}

    //Método responsável por formatar o CPF
    //XXXXXXXXXXX vira XXX.XXX.XXX-XX
	private String formatarCpf(String cpf) {
	    return cpf.substring(0, 3) + "." +
	           cpf.substring(3, 6) + "." +
	           cpf.substring(6, 9) + "-" +
	           cpf.substring(9, 11);
	}
	
    //Método responsável por remover tudo que não for número do telefone
    // (21) 9XXXX-XXXX = 219XXXXXXXX / 21 9XXXXXXXX = 219XXXXXXXX 
	private String limparTelefone(String telefone) {
	    return telefone.replaceAll("[^0-9]", "");
	}

    //Método responsável por formatar o telefone
    //11987654321 vira (11) 98765-4321
	private String formatarTelefone(String telefone) {
	    return "(" + telefone.substring(0, 2) + ") " +
	           telefone.substring(2, 7) + "-" +
	           telefone.substring(7, 11);
	}
	

	public AbaEmprestimo() {
		
		JLabel lblNewLabel = new JLabel("Emprestimo");
		lblNewLabel.setBounds(10, -4, 130, 40);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		tfPesquisarCpf = new JTextField();
		tfPesquisarCpf.setBounds(373, 37, 106, 18);
		tfPesquisarCpf.setColumns(10);
		
		JLabel lblPesquisarCpf = new JLabel("Pesquisar CPF");
		lblPesquisarCpf.setBounds(425, 14, 86, 12);
		lblPesquisarCpf.setHorizontalAlignment(SwingConstants.CENTER);
		lblPesquisarCpf.setFont(new Font("Arial", Font.BOLD, 12));
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//Pega o cpf digitado e remove os espaços extras com o .trim()
				String pesquisaCpf = tfPesquisarCpf.getText().trim();

				//Cria uma variavel que chama o método para remover o que não for número
				String cpfLimpo = limparCpf(pesquisaCpf);

				//Validação para o cpf conter 11 numeros
				if (cpfLimpo.length() != 11) {
				    JOptionPane.showMessageDialog(
				        null,
				        "CPF inválido. Digite exatamente 11 números."
				    );
				    return;
				}

				//Formata o cpf que foi limpo e validado
				String cpfFormatado = formatarCpf(cpfLimpo);

				//Mostra formatado no campo de pesquisa
				tfPesquisarCpf.setText(cpfFormatado);

				//Criar um objeto cliente com o cpfFormatado para preencher o DAO
				cliente = new Cliente(cpfFormatado);
			    
			    ConexaoBD bd = new ConexaoBD();
			    
			    if (bd.connect()) {
			    	ClienteDAO clienteDao = new ClienteDAO(bd, cliente);

			    	//Busca Cliente
			    	boolean estado = clienteDao.buscar();
			        
			    	bd.close();

			    	//Se encontrado
			    	if (estado) {
			    		
			    		//Marca que o cliente já existe no banco
			    		existeCliente = true;
			    		
			    		//Preenche os campos com os dados do cliente encontrado
			    		tfCpf.setText(cliente.getCpf());
			    		tfNome.setText(cliente.getNome());
			    		tfTelefone.setText(cliente.getTelefone());
			    		tfEmail.setText(cliente.getEmail());
			    	} else {
			    		
			    		//Marca que o cliente ainda não existe no banco
			    		existeCliente = false;
			   
			    		//Printa mensagem avisando ao usuario
			    		JOptionPane.showMessageDialog(null, "Cliente não cadastrado.");

			    		//Preenche automaticamente o CPF digitado 
			    		tfCpf.setText(cpfFormatado);
			    		
			    		//Limpa os outros campos
			    		tfNome.setText("");
			    		tfTelefone.setText("");
			    		tfEmail.setText("");
			    		
			    		//Posiciona o cursor no campo nome
			    	    tfNome.requestFocus(); 
			    	}	    		        
			        
			    } else {
	    			JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
	    		}		
			}
		});
		btnBuscar.setBounds(489, 35, 84, 20);
		
		JLabel lblLivroEmprest = new JLabel("Livro");
		lblLivroEmprest.setHorizontalAlignment(SwingConstants.CENTER);
		lblLivroEmprest.setFont(new Font("Arial", Font.BOLD, 14));
		lblLivroEmprest.setBounds(457, 119, 45, 12);
		
		JLabel lblCadastreCliente = new JLabel("Cliente");
		lblCadastreCliente.setFont(new Font("Arial", Font.BOLD, 14));
		lblCadastreCliente.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastreCliente.setBounds(447, 227, 64, 12);
		
		JLabel lblCadastreNome = new JLabel("Nome");
		lblCadastreNome.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCadastreNome.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastreNome.setBounds(171, 268, 45, 12);
		
		tfNome = new JTextField();
		tfNome.setBounds(251, 265, 130, 18);
		tfNome.setColumns(10);
		
		JLabel lblCadastreTelefone = new JLabel("Telefone");
		lblCadastreTelefone.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCadastreTelefone.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastreTelefone.setBounds(170, 373, 46, 12);
		
		JLabel lblCadastreCPF = new JLabel("CPF");
		lblCadastreCPF.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCadastreCPF.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastreCPF.setBounds(597, 268, 47, 12);
		
		JLabel lblCadastreEmail = new JLabel("Email");
		lblCadastreEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastreEmail.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCadastreEmail.setBounds(596, 373, 48, 12);
		
		tfCpf = new JTextField();
		tfCpf.setBounds(672, 265, 130, 18);
		tfCpf.setColumns(10);
		
		tfEmail = new JTextField();
		tfEmail.setBounds(672, 370, 130, 18);
		tfEmail.setColumns(10);
		
		tfTelefone = new JTextField();
		tfTelefone.setBounds(251, 370, 130, 18);
		tfTelefone.setColumns(10);
		setLayout(null);
		add(lblLivroEmprest);
		add(lblCadastreCliente);
		add(lblCadastreNome);
		add(tfNome);
		add(lblCadastreTelefone);
		add(lblCadastreCPF);
		add(lblCadastreEmail);
		add(tfCpf);
		add(tfEmail);
		add(tfTelefone);
		add(lblNewLabel);
		add(tfPesquisarCpf);
		add(btnBuscar);
		add(lblPesquisarCpf);
		
		JButton btnNewButton = new JButton("Fazer Empréstimo");
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 10));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Pega o id do livro
		        String idLivroTexto = textField.getText();
		        
		        //Pega o cpf e remove espaços extras com o .trim()
		        String cpfDigitado = tfCpf.getText().trim();
		        
		        //Remove o que não for número para o cpf
		        String cpfLimpo = limparCpf(cpfDigitado);

		        
		        //Válidação 
		        if (cpfLimpo.length() != 11) {
		            JOptionPane.showMessageDialog(null, "CPF inválido. Digite exatamente 11 números.");
		            return;
		        }

		        //Formata o cpf
		        String cpf = formatarCpf(cpfLimpo);
		        tfCpf.setText(cpf);
		        
		        //Pega nome digitado
		        String nome = tfNome.getText();
		        
		        //Pega o telefone digitado e tira os espaços extras
		        String telefoneDigitado = tfTelefone.getText().trim();
		        
		        //Tira tudo que não for numero
		        String telefoneLimpo = limparTelefone(telefoneDigitado);

		        //Válidação para confirmar tamanho do telefone
		        if (telefoneLimpo.length() != 11) {
		            JOptionPane.showMessageDialog(
		                null,
		                "Telefone inválido. Digite exatamente 11 números."
		            );
		            return;
		        }

		        //Formata o telefone validado
		        String telefone = formatarTelefone(telefoneLimpo);
		        tfTelefone.setText(telefone);
		       
		        //Pega o email digitado
		        String email = tfEmail.getText();

		        //Se algum campo estiver vazio printa na tela
		        if (idLivroTexto.isEmpty() || cpf.isEmpty() || nome.isEmpty()
		                || telefone.isEmpty() || email.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
		            return;
		        }

		        //Variável que receberá o ID do livro convertido
		        int idLivro;

		        try {
		        	//Converte o id para numero inteiro
		            idLivro = Integer.parseInt(idLivroTexto);
		        } catch (NumberFormatException erro) {
		        	
		        	// Caso não seja número, exibe mensagem de erro
		            JOptionPane.showMessageDialog(null, "O ID do livro deve ser um número.");
		            return;
		        }

		        //Cria o cliente com CPF e dados informados
		        Cliente cliente = new Cliente(cpf);
		        cliente.setNome(nome);
		        cliente.setTelefone(telefone);
		        cliente.setEmail(email);

		        //Cria o livro apenas com o id
		        Livro livro = new Livro();
		        livro.setIdLivro(idLivro);

		        //Cria um emprestimo definindo o livro e o cliente
		        Emprestimo emprestimo = new Emprestimo();
		        emprestimo.setCliente(cliente);
		        emprestimo.setLivro(livro);

		        ConexaoBD bd = new ConexaoBD();

		        if (bd.connect()) {

		        	//Se o cliente não existir, cadastra ele primeiro
		            if (!existeCliente) {
		                ClienteDAO clienteDAO = new ClienteDAO(bd, cliente);
		                
		                //Cadastra o cliente no banco de dados
		                String msgCliente = clienteDAO.atualizar(TipoAtualizaBD.Criar);
		                
		                JOptionPane.showMessageDialog(null, msgCliente);
		            }

		            EmprestimoDAO emprestimoDAO = new EmprestimoDAO(bd, emprestimo);
		            
		            //Cria o emprestimo no banco de dados
		            String msgEmprestimo = emprestimoDAO.atualizar(TipoAtualizaBD.Criar);

		            JOptionPane.showMessageDialog(null, msgEmprestimo);

		            bd.close();

		            //Limpa os campos da tela pós criação do emprestimo 
		            textField.setText("");
		            tfPesquisarCpf.setText("");
		            tfCpf.setText("");
		            tfNome.setText("");
		            tfTelefone.setText("");
		            tfEmail.setText("");

		        } else {
		            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
		        }
			}
		});
		btnNewButton.setBounds(412, 417, 130, 20);
		add(btnNewButton);
		
		JButton btnEmprestimos = new JButton("Empréstimos");
		btnEmprestimos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//Verifica se existe o cliente pesquisado
				if (cliente == null) {
				    JOptionPane.showMessageDialog(null, "Pesquise um cliente primeiro.");
				    return;
				}

				//Abre a tela de emprestimos ativos do cliente pesquisado
				EmprestimoAtivo cadastroAtivo = new EmprestimoAtivo(cliente);
				cadastroAtivo.setVisible(true);
			}
		});
		btnEmprestimos.setBounds(861, 10, 114, 21);
		add(btnEmprestimos);
		
		JLabel lblIdLivro = new JLabel("Id do Livro");
		lblIdLivro.setHorizontalAlignment(SwingConstants.CENTER);
		lblIdLivro.setFont(new Font("Arial", Font.PLAIN, 12));
		lblIdLivro.setBounds(372, 170, 84, 12);
		add(lblIdLivro);
		
		textField = new JTextField();
		textField.setBounds(466, 167, 96, 18);
		add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Clientes");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        ClientesAtivos tela = new ClientesAtivos();
		        tela.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(737, 10, 114, 20);
		add(btnNewButton_1);

	}

}
