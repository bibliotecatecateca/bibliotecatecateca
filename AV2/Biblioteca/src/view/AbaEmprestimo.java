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

	
	private String limparCpf(String cpf) {
	    return cpf.replaceAll("[^0-9]", "");
	}

	private String formatarCpf(String cpf) {
	    return cpf.substring(0, 3) + "." +
	           cpf.substring(3, 6) + "." +
	           cpf.substring(6, 9) + "-" +
	           cpf.substring(9, 11);
	}
	
	private String limparTelefone(String telefone) {
	    return telefone.replaceAll("[^0-9]", "");
	}

	private String formatarTelefone(String telefone) {
	    return "(" + telefone.substring(0, 2) + ") " +
	           telefone.substring(2, 7) + "-" +
	           telefone.substring(7, 11);
	}
	
	/**
	 * Create the panel.
	 */
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
				String pesquisaCpf = tfPesquisarCpf.getText().trim();

				String cpfLimpo = limparCpf(pesquisaCpf);

				if (cpfLimpo.length() != 11) {
				    JOptionPane.showMessageDialog(
				        null,
				        "CPF inválido. Digite exatamente 11 números."
				    );
				    return;
				}

				String cpfFormatado = formatarCpf(cpfLimpo);

				// Mostra formatado no campo de pesquisa
				tfPesquisarCpf.setText(cpfFormatado);

				// Use este se você quer salvar/buscar CPF com ponto e traço no banco
				cliente = new Cliente(cpfFormatado);
			    
			    ConexaoBD bd = new ConexaoBD();
			    
			    if (bd.connect()) {
			    	ClienteDAO clienteDao = new ClienteDAO(bd, cliente);

			    	boolean estado = clienteDao.buscar();
			        
			    	bd.close();

			    	if (estado) {
			    		existeCliente = true;
			    		
			    		tfCpf.setText(cliente.getCpf());
			    		tfNome.setText(cliente.getNome());
			    		tfTelefone.setText(cliente.getTelefone());
			    		tfEmail.setText(cliente.getEmail());
			    	} else {
			    		existeCliente = false;
			   
			    		JOptionPane.showMessageDialog(null, "Cliente não cadastrado.");

			    		tfCpf.setText(cpfFormatado);; // preenche automaticamente o CPF digitado
			    		
			    		tfNome.setText("");
			    		tfTelefone.setText("");
			    		tfEmail.setText("");
			    		
			    	    tfNome.requestFocus(); //posiciona o cursor no nome
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
		        String idLivroTexto = textField.getText();
		        String cpfDigitado = tfCpf.getText().trim();
		        String cpfLimpo = limparCpf(cpfDigitado);

		        if (cpfLimpo.length() != 11) {
		            JOptionPane.showMessageDialog(null, "CPF inválido. Digite exatamente 11 números.");
		            return;
		        }

		        String cpf = formatarCpf(cpfLimpo);
		        tfCpf.setText(cpf);
		        String nome = tfNome.getText();
		        String telefoneDigitado = tfTelefone.getText().trim();
		        String telefoneLimpo = limparTelefone(telefoneDigitado);

		        if (telefoneLimpo.length() != 11) {
		            JOptionPane.showMessageDialog(
		                null,
		                "Telefone inválido. Digite exatamente 11 números."
		            );
		            return;
		        }

		        String telefone = formatarTelefone(telefoneLimpo);
		        tfTelefone.setText(telefone);
		        String email = tfEmail.getText();

		        if (idLivroTexto.isEmpty() || cpf.isEmpty() || nome.isEmpty()
		                || telefone.isEmpty() || email.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
		            return;
		        }

		        int idLivro;

		        try {
		            idLivro = Integer.parseInt(idLivroTexto);
		        } catch (NumberFormatException erro) {
		            JOptionPane.showMessageDialog(null, "O ID do livro deve ser um número.");
		            return;
		        }

		        Cliente cliente = new Cliente(cpf);
		        cliente.setNome(nome);
		        cliente.setTelefone(telefone);
		        cliente.setEmail(email);

		        Livro livro = new Livro();
		        livro.setIdLivro(idLivro);

		        Emprestimo emprestimo = new Emprestimo();
		        emprestimo.setCliente(cliente);
		        emprestimo.setLivro(livro);

		        ConexaoBD bd = new ConexaoBD();

		        if (bd.connect()) {

		            if (!existeCliente) {
		                ClienteDAO clienteDAO = new ClienteDAO(bd, cliente);
		                String msgCliente = clienteDAO.atualizar(TipoAtualizaBD.Criar);
		                JOptionPane.showMessageDialog(null, msgCliente);
		            }

		            EmprestimoDAO emprestimoDAO = new EmprestimoDAO(bd, emprestimo);
		            String msgEmprestimo = emprestimoDAO.atualizar(TipoAtualizaBD.Criar);

		            JOptionPane.showMessageDialog(null, msgEmprestimo);

		            bd.close();

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
				if (cliente == null) {
				    JOptionPane.showMessageDialog(null, "Pesquise um cliente primeiro.");
				    return;
				}

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
