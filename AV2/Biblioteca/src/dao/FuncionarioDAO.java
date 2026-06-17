package dao;

import model.LoginFuncionario;
import model.Funcionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ConexaoBD;
import model.OperacaoBD;
import model.TipoAtualizaBD;

public class FuncionarioDAO implements OperacaoBD {

    private ConexaoBD bd;
    private Funcionario funcionario;
    private ArrayList<Funcionario> funcionarios;
    private LoginFuncionario login;
    private String sql;
    private String texto;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public FuncionarioDAO(ConexaoBD bd, Funcionario funcionario) {
        this.bd = bd;
        this.funcionario = funcionario;
        this.funcionarios = new ArrayList<>();

        //Se o funcionário não for nulo, pega os dados de login dele
        if (funcionario != null) {
            this.login = funcionario.getLogin();
        }
    }

    @Override
    //Método responsável por buscar funcionários
    public boolean buscar() {

        //Caso o funcionário seja nulo, busca todos os funcionários usado na tela FuncionariosAtivos
        if (funcionario == null) {
            funcionarios.clear();

            sql = "SELECT idFunc, nomeFunc, loginFunc, senhaFunc FROM funcionario";

            try {
                statement = bd.connection.prepareStatement(sql);
                resultSet = statement.executeQuery();

                //Percorre os funcionários encontrados no banco
                while (resultSet.next()) {
                    LoginFuncionario login = new LoginFuncionario(
                        resultSet.getString("loginFunc"),
                        resultSet.getString("senhaFunc")
                    );

                    Funcionario func = new Funcionario(login);
                    func.setIdFuncionario(resultSet.getInt("idFunc"));
                    func.setNome(resultSet.getString("nomeFunc"));

                    funcionarios.add(func);
                }

                return true;

            } catch (SQLException erro) {
                System.out.println("Erro ao buscar funcionários: " + erro.getMessage());
                return false;
            }
        }

        //Caso tenha um funcionário, busca pelo login usado na TelaLogin
        sql = "SELECT idFunc, nomeFunc, loginFunc, senhaFunc FROM funcionario WHERE loginFunc = ?";

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setString(1, login.getUsuario());

            resultSet = statement.executeQuery();

            //Se encontrar o funcionário, preenche os dados dele
            if (resultSet.next()) {
                funcionario.setIdFuncionario(resultSet.getInt("idFunc"));
                funcionario.setNome(resultSet.getString("nomeFunc"));

                login.setUsuario(resultSet.getString("loginFunc"));
                login.setSenha(resultSet.getString("senhaFunc"));

                return true;
            }

            return false;

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar funcionário: " + erro.getMessage());
            return false;
        }
    }

    @Override
    //Método que escolhe qual operação será feita
    public String atualizar(TipoAtualizaBD operacao) {
        switch (operacao) {
            case Criar:
                return cadastrarFuncionario();

            case Alterar:
                return alterarFuncionario();

            case Deletar:
                return deletarFuncionario();

            default:
                return "Operação Inválida";
        }
    }

    //Método responsável por cadastrar funcionário
    private String cadastrarFuncionario() {
        texto = "Funcionário cadastrado com sucesso!";

        try {
            sql = "INSERT INTO funcionario(nomeFunc, loginFunc, senhaFunc) VALUES (?, ?, ?)";

            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getLogin().getUsuario());
            statement.setString(3, funcionario.getLogin().getSenha());

            statement.executeUpdate();


        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    //Método responsável por alterar nome e senha do funcionário
    private String alterarFuncionario() {
        texto = "Funcionário alterado com sucesso!";

        try {
            sql = "UPDATE funcionario SET nomeFunc = ?, senhaFunc = ? WHERE idFunc = ?";

            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getLogin().getSenha());
            statement.setInt(3, funcionario.getIdFuncionario());

            statement.executeUpdate();

        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    //Método responsável por deletar funcionário pelo id
    private String deletarFuncionario() {
        texto = "Funcionário deletado com sucesso!";

        try {
            sql = "DELETE FROM funcionario WHERE idFunc = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, funcionario.getIdFuncionario());

            statement.executeUpdate();
            
        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }
    
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }
}