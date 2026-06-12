package dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ConexaoBD;
import model.Emprestimo;
import model.Livro;
import model.OperacaoBD;
import model.TipoAtualizaBD;

public class EmprestimoDAO implements OperacaoBD{
	private ConexaoBD bd;
	private Emprestimo emprestimo;
	private ArrayList<Emprestimo> emprestimos;
	private String sql;
	private String texto;
    private PreparedStatement statement;

    
    public EmprestimoDAO(ConexaoBD bd, Emprestimo emprestimo) {
    	emprestimos = new ArrayList<>();
    	this.bd = bd;
    	this.emprestimo = emprestimo;
    }
    
    @Override
    public boolean buscar() {
        emprestimos.clear();

        sql = "{CALL conEmprestimos(?)}";

        try (CallableStatement statement = bd.connection.prepareCall(sql)) {

            statement.setString(1, emprestimo.getCliente().getCpf());

            try (ResultSet resultSet = statement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();

                if (resultSet.next()) {

                    if (metaData.getColumnName(1).equalsIgnoreCase("mensagem")) {
                        System.out.println(resultSet.getString("mensagem"));
                        return false;
                    }

                    do {
                        Emprestimo emp = new Emprestimo();

                        emp.setIdEmp(resultSet.getInt("CodigoEmprestimo"));

                        Livro livro = new Livro();
                        livro.setTitulo(resultSet.getString("Livro"));
                        emp.setLivro(livro);

                        emp.setDataEmprestimo(
                            resultSet.getDate("DataEmprestimo").toLocalDate()
                        );

                        emp.setDataDevolucao(
                            resultSet.getDate("DataDevolucaoEstimada").toLocalDate()
                        );

                        emprestimos.add(emp);

                    } while (resultSet.next());

                    return true;
                }

            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos: " + e.getMessage());
            return false;
        }

        return false;
    }
	
	
	
	//Não iremos alterar emprestimo
	
	@Override
	public String atualizar(TipoAtualizaBD operacao) {
  		switch(operacao) {
  		case Criar:
  			return cadastrarEmprestimo();
  		case Deletar:
  			return deletarEmprestimo();
  		default:
  			return "Operação Inválida";
  			
  		}
	}
	
	private String cadastrarEmprestimo() {
	    texto = "Falha ao cadastrar empréstimo.";

	    try {
	        sql = "{CALL cadEmprestimo(?, ?)}";

	        try (CallableStatement statement = bd.connection.prepareCall(sql)) {

	            statement.setInt(1, emprestimo.getLivro().getIdLivro());
	            statement.setString(2, emprestimo.getCliente().getCpf());

	            boolean temResultado = statement.execute();

	            if (temResultado) {
	                try (ResultSet rs = statement.getResultSet()) {
	                    if (rs.next()) {
	                        texto = rs.getString("mensagem");
	                    }
	                }
	            }
	        }

	    } catch (SQLException erro) {
	        texto = "Falha na operação - " + erro.getMessage();
	    }

	    return texto;
	}
  	
  	
  	private String deletarEmprestimo() {
  	    texto = "Emprestimo deletado com sucesso!";

  	    try {
  	        sql = "DELETE FROM emprestimo WHERE idEmp = ?";

  	        statement = bd.connection.prepareStatement(sql);
  	        statement.setInt(1, emprestimo.getIdEmp());

  	        statement.executeUpdate();

  	    } catch (SQLException erro) {
  	        texto = "Falha na operação - " + erro.getMessage();
  	    }

  	    return texto;
   }
  	
  	
	
  	public String finalizarEmprestimo() {
  	    texto = "Emprestimo finalizado com sucesso!";

  	    try {
  	        sql = "{CALL encEmprestimo(?)}";

  	        try (CallableStatement call = bd.connection.prepareCall(sql)) {
  	            call.setInt(1, emprestimo.getIdEmp());

  	            // Usamos execute() em vez de executeUpdate() porque a procedure retorna um SELECT
  	            boolean hasResults = call.execute();

  	            // Se a procedure retornou o SELECT do final, nós o lemos aqui
  	            if (hasResults) {
  	                try (ResultSet rs = call.getResultSet()) {
  	                    if (rs.next()) {
  	                        double multa = rs.getDouble("Multa");
  	                        
  	                        // Se houver multa, adicionamos a informação no texto de retorno
  	                        if (multa > 0) {
  	                            texto = "Emprestimo finalizado. Atenção: Há uma multa de R$ " + multa;
  	                        }
  	                    }
  	                }
  	            }
  	        }

  	    } catch (SQLException erro) {
  	        texto = "Falha na operação - " + erro.getMessage();
  	        System.err.println("Erro ao finalizar empréstimo: " + erro.getMessage());
  	    }

  	    return texto;
  	}
  	
  	
  	public List<Emprestimo> getEmprestimos() {
  	    return emprestimos;
  	}
    

}
