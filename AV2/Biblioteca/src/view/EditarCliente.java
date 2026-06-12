package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.ClienteDAO;
import model.Cliente;
import model.ConexaoBD;
import model.TipoAtualizaBD;

public class EditarCliente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfCpf;
    private JTextField tfNome;
    private JTextField tfTelefone;
    private JTextField tfEmail;

    public EditarCliente(Cliente cliente, ClientesAtivos telaClientes) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 420, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Cliente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(110, 20, 200, 25);
        contentPane.add(lblTitulo);

        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setBounds(70, 65, 80, 20);
        contentPane.add(lblCpf);

        tfCpf = new JTextField();
        tfCpf.setBounds(160, 65, 170, 20);
        tfCpf.setEditable(false);
        contentPane.add(tfCpf);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(70, 100, 80, 20);
        contentPane.add(lblNome);

        tfNome = new JTextField();
        tfNome.setBounds(160, 100, 170, 20);
        contentPane.add(tfNome);

        JLabel lblTelefone = new JLabel("Telefone");
        lblTelefone.setBounds(70, 135, 80, 20);
        contentPane.add(lblTelefone);

        tfTelefone = new JTextField();
        tfTelefone.setBounds(160, 135, 170, 20);
        contentPane.add(tfTelefone);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(70, 170, 80, 20);
        contentPane.add(lblEmail);

        tfEmail = new JTextField();
        tfEmail.setBounds(160, 170, 170, 20);
        contentPane.add(tfEmail);

        tfCpf.setText(cliente.getCpf());
        tfNome.setText(cliente.getNome());
        tfTelefone.setText(cliente.getTelefone());
        tfEmail.setText(cliente.getEmail());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(150, 215, 110, 25);
        contentPane.add(btnAtualizar);

        btnAtualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String nome = tfNome.getText();
                String telefone = tfTelefone.getText();
                String email = tfEmail.getText();

                if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                    return;
                }

                cliente.setNome(nome);
                cliente.setTelefone(telefone);
                cliente.setEmail(email);

                ConexaoBD bd = new ConexaoBD();

                if (bd.connect()) {
                    ClienteDAO dao = new ClienteDAO(bd, cliente);

                    String mensagem = dao.atualizar(TipoAtualizaBD.Alterar);

                    JOptionPane.showMessageDialog(null, mensagem);

                    bd.close();

                    telaClientes.buscarClientes();

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados.");
                }
            }
        });
    }
}