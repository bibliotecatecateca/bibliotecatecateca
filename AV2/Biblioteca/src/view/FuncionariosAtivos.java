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

import dao.FuncionarioDAO;
import model.ConexaoBD;
import model.Funcionario;
import model.LoginFuncionario;
import model.TipoAtualizaBD;

public class FuncionariosAtivos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public FuncionariosAtivos() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Funcionários Ativos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(220, 22, 250, 31);
        contentPane.add(lblTitulo);
        
        //Cria o modelo da tabela
        modeloTabela = new DefaultTableModel();
        
        //Adiciona as colunas da tabela
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Usuário");
        modeloTabela.addColumn("Senha");
        modeloTabela.addColumn("Editar");
        modeloTabela.addColumn("Excluir");
        
        //Cria a tabela usando o modelo criado acima
        tabela = new JTable(modeloTabela);
        
        //Define o tamanho preferencial de cada coluna
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40); //ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180); //Nome
        tabela.getColumnModel().getColumn(2).setPreferredWidth(130); //Usuário
        tabela.getColumnModel().getColumn(3).setPreferredWidth(130); //Senha
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80); //Editar
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80); //Excluir
        
        //Define a altura das linhas da tabela
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

                //Só faz algo se clicar em Editar ou Excluir
                if (coluna != 4 && coluna != 5) {
                    return;
                }
                
                //Pega o ID do funcionário na linha selecionada
                Object idObj = tabela.getValueAt(linha, 0);
                
                //Vê se o Id existe
                if (idObj == null) {
                    JOptionPane.showMessageDialog(null, "ID do funcionário não encontrado.");
                    return;
                }

                //Converte o ID para inteiro, pois antes era String
                int idFunc = Integer.parseInt(idObj.toString());

                //EXCLUIR
                if (coluna == 5) {
                    excluirFuncionario(idFunc, linha);
                    return;
                }

                //EDITAR
                //Pega os dados do funcionário na tabela
                Object nomeObj = tabela.getValueAt(linha, 1);
                Object usuarioObj = tabela.getValueAt(linha, 2);
                Object senhaObj = tabela.getValueAt(linha, 3);

                //Verifica se os dados estão completos
                if (nomeObj == null || usuarioObj == null || senhaObj == null) {
                    JOptionPane.showMessageDialog(null, "Dados do funcionário incompletos.");
                    return;
                }

                //Converte os dados para String
                String nome = nomeObj.toString();
                String usuario = usuarioObj.toString();
                String senha = senhaObj.toString();

                //Cria um objto login com usuario e senha
                LoginFuncionario login = new LoginFuncionario(usuario, senha);
                
                //Cria um objeto funcionario com o objeto login de parametro
                Funcionario funcionario = new Funcionario(login);
                
                //Define o Id e o nome com as conversões feitas a partir do texto digitado
                funcionario.setIdFunc(idFunc);
                funcionario.setNome(nome);

                //Abre a tela EditarFuncionario, enviando o funcionário selecionado e a tela atual
                EditarFuncionario editar = new EditarFuncionario(funcionario, FuncionariosAtivos.this);
                
                //Torna a tela de edição visível
                editar.setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 80, 620, 320);
        contentPane.add(scroll);

        buscarFuncionarios();
    }
    
    //Método responsável por buscar os funcionários no banco de dados
    public void buscarFuncionarios() {
    	
    	//Limpa todas as linhas da tabela antes de carregar novamente
        modeloTabela.setRowCount(0);

        ConexaoBD bd = new ConexaoBD();

        if (bd.connect()) {
        	//Cria o DAO responsável por buscar funcionários
            FuncionarioDAO dao = new FuncionarioDAO(bd, null);

            boolean estado = dao.buscar();
            
            //Se encontrou 
            if (estado) {
            	
            	//Pega a lista de funcionários retornada pelo DAO
                List<Funcionario> lista = dao.getFuncionarios();
                
                //Percorre a lista de funcionários
                for (Funcionario f : lista) {
                	
                	//Adiciona cada funcionário como uma linha na tabela
                    modeloTabela.addRow(new Object[] {
                        f.getIdFunc(),
                        f.getNome(),
                        f.getLogin().getUsuario(),
                        f.getLogin().getSenha(),
                        "Editar",
                        "Excluir"
                    });
                }
            }

            bd.close();
        }
    }
    
    
    //Método responsável por excluir um funcionário
    private void excluirFuncionario(int idFunc, int linha) {
    	
    	//Exibe uma mensagem de confirmação antes de excluir
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Deseja excluir este funcionário?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        //Verifica se o usuario confirmou a exclusão
        if (opcao == JOptionPane.YES_OPTION) {
        	
        	//Cria um login vazio apenas para montar o objeto funcionario
            LoginFuncionario login = new LoginFuncionario("", "");
            
            //Cria o objeto funcionario recebendo o login sem info como parametro
            Funcionario funcionario = new Funcionario(login);
            
            //Define o ID do funcionário que será excluído
            funcionario.setIdFunc(idFunc);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                FuncionarioDAO dao = new FuncionarioDAO(bd, funcionario);

                //Executa a exclusão do funcionario no banco
                String mensagem = dao.atualizar(TipoAtualizaBD.Deletar);

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();
                
                //Remove a linha da tabela após a exclusão
                modeloTabela.removeRow(linha);
            }
        }
    }
}