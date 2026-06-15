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

import dao.EmprestimoDAO;
import model.Cliente;
import model.ConexaoBD;
import model.Emprestimo;
import model.TipoAtualizaBD;

public class EmprestimoAtivo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Cliente cliente;

    public EmprestimoAtivo(Cliente cliente) {
        this.cliente = cliente;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Empréstimos Ativos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(220, 22, 250, 31);
        contentPane.add(lblTitulo);

        //Cria o modelo da tabela
        modeloTabela = new DefaultTableModel();
        
        //Adiciona as colunas da tabela        
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Livro");
        modeloTabela.addColumn("Data Empréstimo");
        modeloTabela.addColumn("Data Estimada");
        modeloTabela.addColumn("Excluir");
        modeloTabela.addColumn("Finalizar");

        //Cria a tabela usando o modelo criado acima
        tabela = new JTable(modeloTabela);
        
        //Define o tamanho preferencial de cada coluna        
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(280);  // Livro
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);  // Data Empréstimo
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);  // Data Estimada
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);   // Excluir
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);   // Finalizar

        //Cria uma barra de rolagem para a tabela
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 80, 620, 320);
        contentPane.add(scroll);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	
            	//Identifica a linha clicada
                int linha = tabela.getSelectedRow();
                
                //Identifica a coluna clicada
                int coluna = tabela.getSelectedColumn();

                //Se nenhuma linha ou coluna válida foi clicada, interrompe o método
                if (linha == -1) {
                    return;
                }

                //Pega o ID do empréstimo da primeira coluna da linha selecionada
                int idEmp = Integer.parseInt(tabela.getValueAt(linha, 0).toString());

                //EXCLUIR
                if (coluna == 4) {
                    excluirEmprestimo(idEmp, linha);
                }

                //FINALIZAR
                if (coluna == 5) {
                    finalizarEmprestimo(idEmp);
                }
            }
        });

        buscarEmprestimos();
    }
    
    
    //Método responsável por buscar os empréstimos ativos do cliente
    private void buscarEmprestimos() {
    	
    	//Limpa todas as linhas da tabela antes de carregar novamente
        modeloTabela.setRowCount(0);

        Emprestimo emprestimo = new Emprestimo();
        
        // Define o cliente dentro do empréstimo dessa forma a busca será feita com base nesse cliente
        emprestimo.setCliente(cliente);

        ConexaoBD bd = new ConexaoBD();

        if (bd.connect()) {
            EmprestimoDAO dao = new EmprestimoDAO(bd, emprestimo);

            boolean estado = dao.buscar();

            //Se encontrou empréstimos ativos
            if (estado) {
                List<Emprestimo> lista = dao.getEmprestimos();

                //Adiciona cada empréstimo como uma linha na tabela
                for (Emprestimo emp : lista) {
                    modeloTabela.addRow(new Object[] {
                        emp.getIdEmp(),
                        emp.getLivro().getTitulo(),
                        emp.getDataEmpIni(),
                        emp.getDataEmpEst(),
                        "Excluir",
                        "Finalizar"
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum empréstimo ativo encontrado.");
            }

            bd.close();
        }
    }

    //Método responsável por excluir um empréstimo
    private void excluirEmprestimo(int idEmp, int linha) {
    	
    	//Exibe uma mensagem de confirmação antes de excluir
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Deseja excluir este empréstimo?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        //Verifica se o usuario confirmou a exclusão
        if (opcao == JOptionPane.YES_OPTION) {
        	
            Emprestimo emp = new Emprestimo();
            
            //Define o ID do empréstimo que será excluído
            emp.setIdEmp(idEmp);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                EmprestimoDAO dao = new EmprestimoDAO(bd, emp);
                
                //Executa a exclusão do empréstimo no banco
                String mensagem = dao.atualizar(TipoAtualizaBD.Deletar);

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();

                //Remove a linha da tabela após a exclusão
                modeloTabela.removeRow(linha);
            }
        }
    }

    //Método responsável por finalizar um empréstimo
    private void finalizarEmprestimo(int idEmp) {
       
    	//Exibe uma confirmação antes de finalizar o empréstimo
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Confirmar devolução deste livro?",
            "Finalizar empréstimo",
            JOptionPane.YES_NO_OPTION
        );

        //Verifica se o usuário confirmou a finalização
        if (opcao == JOptionPane.YES_OPTION) {
        	
            Emprestimo emp = new Emprestimo();
            
            //Define o ID do empréstimo que será finalizado
            emp.setIdEmp(idEmp);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                EmprestimoDAO dao = new EmprestimoDAO(bd, emp);

                //Finaliza o empréstimo no banco de dados
                String mensagem = dao.finalizarEmprestimo();

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();
            }
        }
    }
    
    
}