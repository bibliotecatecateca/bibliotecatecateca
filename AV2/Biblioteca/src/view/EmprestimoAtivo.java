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

        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Livro");
        modeloTabela.addColumn("Data Empréstimo");
        modeloTabela.addColumn("Data Estimada");
        modeloTabela.addColumn("Excluir");
        modeloTabela.addColumn("Finalizar");

        tabela = new JTable(modeloTabela);
        
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(280);  // Livro
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);  // Data Empréstimo
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);  // Data Estimada
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);   // Excluir
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);   // Finalizar

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 80, 620, 320);
        contentPane.add(scroll);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.getSelectedRow();
                int coluna = tabela.getSelectedColumn();

                if (linha == -1) {
                    return;
                }

                int idEmp = Integer.parseInt(tabela.getValueAt(linha, 0).toString());

                if (coluna == 4) {
                    excluirEmprestimo(idEmp, linha);
                }

                if (coluna == 5) {
                    finalizarEmprestimo(idEmp);
                }
            }
        });

        buscarEmprestimos();
    }

    private void buscarEmprestimos() {
        modeloTabela.setRowCount(0);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setCliente(cliente);

        ConexaoBD bd = new ConexaoBD();

        if (bd.connect()) {
            EmprestimoDAO dao = new EmprestimoDAO(bd, emprestimo);

            boolean estado = dao.buscar();

            if (estado) {
                List<Emprestimo> lista = dao.getEmprestimos();

                for (Emprestimo emp : lista) {
                    modeloTabela.addRow(new Object[] {
                        emp.getIdEmp(),
                        emp.getLivro().getTitulo(),
                        emp.getDataEmprestimo(),
                        emp.getDataDevolucao(),
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

    private void excluirEmprestimo(int idEmp, int linha) {
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Deseja excluir este empréstimo?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            Emprestimo emp = new Emprestimo();
            emp.setIdEmp(idEmp);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                EmprestimoDAO dao = new EmprestimoDAO(bd, emp);

                String mensagem = dao.atualizar(TipoAtualizaBD.Deletar);

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();

                modeloTabela.removeRow(linha);
            }
        }
    }

    private void finalizarEmprestimo(int idEmp) {
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Confirmar devolução deste livro?",
            "Finalizar empréstimo",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            Emprestimo emp = new Emprestimo();
            emp.setIdEmp(idEmp);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                EmprestimoDAO dao = new EmprestimoDAO(bd, emp);

                String mensagem = dao.finalizarEmprestimo();

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();
            }
        }
    }
}