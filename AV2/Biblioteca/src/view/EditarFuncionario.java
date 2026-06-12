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

import dao.FuncionarioDAO;
import model.ConexaoBD;
import model.Funcionario;
import model.TipoAtualizaBD;

public class EditarFuncionario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfNome;
    private JTextField tfSenha;

    public EditarFuncionario(Funcionario funcionario, FuncionariosAtivos telaFuncionarios) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Funcionário");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(95, 20, 200, 25);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(80, 75, 60, 20);
        contentPane.add(lblNome);

        tfNome = new JTextField();
        tfNome.setBounds(150, 75, 150, 20);
        contentPane.add(tfNome);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setBounds(80, 115, 60, 20);
        contentPane.add(lblSenha);

        tfSenha = new JTextField();
        tfSenha.setBounds(150, 115, 150, 20);
        contentPane.add(tfSenha);

        tfNome.setText(funcionario.getNome());
        tfSenha.setText(funcionario.getLogin().getSenha());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(145, 165, 100, 25);
        contentPane.add(btnAtualizar);

        btnAtualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                funcionario.setNome(tfNome.getText());
                funcionario.getLogin().setSenha(tfSenha.getText());

                ConexaoBD bd = new ConexaoBD();

                if (bd.connect()) {
                    FuncionarioDAO dao = new FuncionarioDAO(bd, funcionario);

                    String mensagem = dao.atualizar(TipoAtualizaBD.Alterar);

                    JOptionPane.showMessageDialog(null, mensagem);

                    bd.close();

                    telaFuncionarios.buscarFuncionarios();

                    dispose();
                }
            }
        });
    }
}