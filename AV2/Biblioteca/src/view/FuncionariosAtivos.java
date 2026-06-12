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
import model.LoginFunc;
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

        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Usuário");
        modeloTabela.addColumn("Senha");
        modeloTabela.addColumn("Editar");
        modeloTabela.addColumn("Excluir");

        tabela = new JTable(modeloTabela);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);

        tabela.setRowHeight(25);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.rowAtPoint(e.getPoint());
                int coluna = tabela.columnAtPoint(e.getPoint());

                if (linha == -1 || coluna == -1) {
                    return;
                }

                // Só faz algo se clicar em Editar ou Excluir
                if (coluna != 4 && coluna != 5) {
                    return;
                }

                Object idObj = tabela.getValueAt(linha, 0);

                if (idObj == null) {
                    JOptionPane.showMessageDialog(null, "ID do funcionário não encontrado.");
                    return;
                }

                int idFunc = Integer.parseInt(idObj.toString());

                // EXCLUIR
                if (coluna == 5) {
                    excluirFuncionario(idFunc, linha);
                    return;
                }

                // EDITAR
                Object nomeObj = tabela.getValueAt(linha, 1);
                Object usuarioObj = tabela.getValueAt(linha, 2);
                Object senhaObj = tabela.getValueAt(linha, 3);

                if (nomeObj == null || usuarioObj == null || senhaObj == null) {
                    JOptionPane.showMessageDialog(null, "Dados do funcionário incompletos.");
                    return;
                }

                String nome = nomeObj.toString();
                String usuario = usuarioObj.toString();
                String senha = senhaObj.toString();

                LoginFunc login = new LoginFunc(usuario, senha);
                Funcionario funcionario = new Funcionario(login);

                funcionario.setIdFunc(idFunc);
                funcionario.setNome(nome);

                EditarFuncionario editar = new EditarFuncionario(
                    funcionario,
                    FuncionariosAtivos.this
                );

                editar.setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 80, 620, 320);
        contentPane.add(scroll);

        buscarFuncionarios();
    }

    public void buscarFuncionarios() {
        modeloTabela.setRowCount(0);

        ConexaoBD bd = new ConexaoBD();

        if (bd.connect()) {
            FuncionarioDAO dao = new FuncionarioDAO(bd, null);

            boolean estado = dao.buscar();

            if (estado) {
                List<Funcionario> lista = dao.getFuncionarios();

                for (Funcionario f : lista) {
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
    
    

    private void excluirFuncionario(int idFunc, int linha) {
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Deseja excluir este funcionário?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            LoginFunc login = new LoginFunc("", "");
            Funcionario funcionario = new Funcionario(login);
            funcionario.setIdFunc(idFunc);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                FuncionarioDAO dao = new FuncionarioDAO(bd, funcionario);

                String mensagem = dao.atualizar(TipoAtualizaBD.Deletar);

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();

                modeloTabela.removeRow(linha);
            }
        }
    }
}