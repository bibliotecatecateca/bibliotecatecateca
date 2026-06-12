package dao;

import model.LoginFunc;
import model.Funcionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ConexaoBD;
import model.OperacaoBD;
import model.TipoAtualizaBD;

public class FuncionarioDAO implements OperacaoBD{
	private ConexaoBD bd;
	private Funcionario funcionario;
	private ArrayList<Funcionario> funcionarios;
	private LoginFunc login;
	private String sql;
	private String texto;
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    public FuncionarioDAO(ConexaoBD bd, Funcionario funcionario) {
        this.bd = bd;
        this.funcionario = funcionario;
        this.funcionarios = new ArrayList<>();

        if (funcionario != null) {
            this.login = funcionario.getLogin();
        }
    		
    }
    
  //Código para verificar se o login existe no banco
    @Override
	public boolean buscar() {

        // CASO 1: buscar todos os funcionários
        // usado na tela FuncionariosAtivos
        if (funcionario == null) {
            funcionarios.clear();

            sql = "SELECT idFunc, nomeFunc, loginFunc, senhaFunc FROM funcionario";

            try {
                statement = bd.connection.prepareStatement(sql);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    LoginFunc login = new LoginFunc(
                        resultSet.getString("loginFunc"),
                        resultSet.getString("senhaFunc")
                    );

                    Funcionario func = new Funcionario(login);
                    func.setIdFunc(resultSet.getInt("idFunc"));
                    func.setNome(resultSet.getString("nomeFunc"));

                    funcionarios.add(func);
                }

                return true;

            } catch (SQLException erro) {
                System.out.println("Erro ao buscar funcionários: " + erro.getMessage());
                return false;
            }
        }

        // CASO 2: buscar um funcionário pelo login
        // usado na TelaLogin
        sql = "SELECT idFunc, nomeFunc, loginFunc, senhaFunc FROM funcionario WHERE loginFunc = ?";

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setString(1, login.getUsuario());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                funcionario.setIdFunc(resultSet.getInt("idFunc"));
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

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }
	
	
	
	@Override
	public String atualizar(TipoAtualizaBD operacao) {
		switch(operacao) {
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
	
	private String cadastrarFuncionario() {
        texto = "Funcionário cadastrado com sucesso!";
        
        try {
        	 	sql = "INSERT into funcionario(nomeFunc, loginFunc, senhaFunc) values (?,?,?)"; 
        	 	
            statement = bd.connection.prepareCall(sql);

            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getLogin().getUsuario());
            statement.setString(3, funcionario.getLogin().getSenha());
            
            //Executa o comando no banco de dados
            statement.execute();
           	}
        //Erro durante a comunicação com um banco
        catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }
        
        return texto;	
	}
	
	private String alterarFuncionario() {
		texto = "Funcionário alterado com sucesso!";
		
		try {
			sql = "UPDATE funcionario SET nomeFunc = ?, senhaFunc = ? WHERE idFunc = ?"; 
			
            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getLogin().getSenha());
            statement.setInt(3, funcionario.getIdFunc());
            
            //Executa o comando no banco de dados
            statement.execute();
			
		} 
		//Erro durante a comunicação com um banco
		catch (SQLException erro) {
			texto = "Falha na operação - " + erro.getMessage();
			
		}
		
		return texto;
		
	}
	
	private String deletarFuncionario() {
		texto = "Funcionário deletado com sucesso!";
		
		try {
			sql = "DELETE FROM funcionario WHERE idFunc = ?"; 
			
            statement = bd.connection.prepareStatement(sql);
            
            statement.setInt(1, funcionario.getIdFunc());
            
            //Executa o comando no banco de dados
            statement.execute();
			
		}
		//Erro durante a comunicação com um banco
		catch (SQLException erro) {
			texto = "Falha na operação - " + erro.getMessage();
		}
		
		return texto;
		
	}





}
