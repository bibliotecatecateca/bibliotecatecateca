package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ClienteDAO;
import model.Cliente;
import model.ConexaoBD;
import model.TipoAtualizaBD;

public class ClientesAtivos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public ClientesAtivos() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Clientes Ativos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(250, 22, 250, 31);
        contentPane.add(lblTitulo);
        
        modeloTabela = new DefaultTableModel();

        
        modeloTabela.addColumn("CPF");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("Email");
        modeloTabela.addColumn("Editar");
        modeloTabela.addColumn("Excluir");

  
        tabela = new JTable(modeloTabela);
        
        //Define a largura de cada coluna
        tabela.getColumnModel().getColumn(0).setPreferredWidth(110); // CPF
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100); // Telefone
        tabela.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
        tabela.getColumnModel().getColumn(4).setPreferredWidth(70);  // Editar
        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);  // Excluir

        //Define a altura de todas as colunas
        tabela.setRowHeight(25);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	//Identifica a linha clicada
                int linha = tabela.rowAtPoint(e.getPoint());
                
                //Identifica a coluna clicada
                int coluna = tabela.columnAtPoint(e.getPoint());

                //Se nenhuma linha ou coluna válida foi clicada, interrompe o método
                if (linha == -1 || coluna == -1) {
                    return;
                }
                
                //Só executa alguma ação se o usuário clicar em Editar ou Excluir
                if (coluna != 4 && coluna != 5) {
                    return;
                }

                //Pega os dados do cliente selecionado na tabela
                //Transforma em String, pois getValueAt retorna objeto
                String cpf = tabela.getValueAt(linha, 0).toString();
                String nome = tabela.getValueAt(linha, 1).toString();
                String telefone = tabela.getValueAt(linha, 2).toString();
                String email = tabela.getValueAt(linha, 3).toString();

                
                //EDITAR
                if (coluna == 4) {
                	
                    Cliente cliente = new Cliente(cpf);
                    
                    //Preenche o objeto cliente com os dados da tabela, tirando o cpf que foi passado como parametro 
                    cliente.setNome(nome);
                    cliente.setTelefone(telefone);
                    cliente.setEmail(email);

                    //Abre a tela de edição do cliente e passa a tela atual como parametro para atualizar a tabela depois 
                    //e passa o cliente, mostrando os dados preenchidos quando abrir a tela EditarCliente
                    EditarCliente editar = new EditarCliente(cliente, ClientesAtivos.this);
                    editar.setVisible(true);
                }

                //EXCLUIR
                if (coluna == 5) {
                	//Chama o método que exclui o cliente
                    excluirCliente(cpf, linha);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 80, 680, 320);
        contentPane.add(scroll);

        buscarClientes();
    }

    //Método responsável por buscar todos os clientes no banco de dados
    public void buscarClientes() {
    	 
    	//Limpa todas as linhas da tabela antes de carregar novamente
        modeloTabela.setRowCount(0);

        ConexaoBD bd = new ConexaoBD();

        if (bd.connect()) {
            ClienteDAO dao = new ClienteDAO(bd, null);

            //Cria uma variavel recebendo o dao + método que busca todos os clientes
            boolean estado = dao.buscarTodos();

            //Se encontrou clientes
            if (estado) {
            	
            	//Pega a lista de clientes retornada pelo dao
                List<Cliente> lista = dao.getClientes();

                //percorre a lista de clientes
                for (Cliente c : lista) {
                	
                	//Adiciona cada cliente como uma linha na tabela
                    modeloTabela.addRow(new Object[] {
                        c.getCpf(),
                        c.getNome(),
                        c.getTelefone(),
                        c.getEmail(),
                        "Editar",
                        "Excluir"
                    });
                }
            }

            bd.close();
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
        }
    }

    //Método responsável por excluir um cliente
    private void excluirCliente(String cpf, int linha) {
    	
    	//Exibe uma mensagem de confirmação de exclusão
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Deseja excluir este cliente?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        //Se o usuário confirmou a exclusão
        if (opcao == JOptionPane.YES_OPTION) {
        	
        	//Cria um objeto Cliente com o CPF selecionado
            Cliente cliente = new Cliente(cpf);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                ClienteDAO dao = new ClienteDAO(bd, cliente);

                //Executa a exclusão do cliente no banco
                String mensagem = dao.atualizar(TipoAtualizaBD.Deletar);

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();

                //Remove a linha da tabela somente se o cliente foi deletado com sucesso
                if (mensagem.equals("Cliente deletado com sucesso!")) {
                    modeloTabela.removeRow(linha);
                }
            }
        }
    }
}