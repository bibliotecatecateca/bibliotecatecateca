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

        modeloTabela = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTabela.addColumn("CPF");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("Email");
        modeloTabela.addColumn("Editar");
        modeloTabela.addColumn("Excluir");

        tabela = new JTable(modeloTabela);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(110); // CPF
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100); // Telefone
        tabela.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
        tabela.getColumnModel().getColumn(4).setPreferredWidth(70);  // Editar
        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);  // Excluir

        tabela.setRowHeight(25);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.rowAtPoint(e.getPoint());
                int coluna = tabela.columnAtPoint(e.getPoint());

                if (linha == -1 || coluna == -1) {
                    return;
                }

                if (coluna != 4 && coluna != 5) {
                    return;
                }

                String cpf = tabela.getValueAt(linha, 0).toString();
                String nome = tabela.getValueAt(linha, 1).toString();
                String telefone = tabela.getValueAt(linha, 2).toString();
                String email = tabela.getValueAt(linha, 3).toString();

                if (coluna == 4) {
                    Cliente cliente = new Cliente(cpf);
                    cliente.setNome(nome);
                    cliente.setTelefone(telefone);
                    cliente.setEmail(email);

                    EditarCliente editar = new EditarCliente(
                        cliente,
                        ClientesAtivos.this
                    );

                    editar.setVisible(true);
                }

                if (coluna == 5) {
                    excluirCliente(cpf, linha);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 80, 680, 320);
        contentPane.add(scroll);

        buscarClientes();
    }

    public void buscarClientes() {
        modeloTabela.setRowCount(0);

        ConexaoBD bd = new ConexaoBD();

        if (bd.connect()) {
            ClienteDAO dao = new ClienteDAO(bd, null);

            boolean estado = dao.buscarTodos();

            if (estado) {
                List<Cliente> lista = dao.getClientes();

                for (Cliente c : lista) {
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

    private void excluirCliente(String cpf, int linha) {
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "Deseja excluir este cliente?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            Cliente cliente = new Cliente(cpf);

            ConexaoBD bd = new ConexaoBD();

            if (bd.connect()) {
                ClienteDAO dao = new ClienteDAO(bd, cliente);

                String mensagem = dao.atualizar(TipoAtualizaBD.Deletar);

                JOptionPane.showMessageDialog(null, mensagem);

                bd.close();

                if (mensagem.equals("Cliente deletado com sucesso!")) {
                    modeloTabela.removeRow(linha);
                }
            }
        }
    }
}