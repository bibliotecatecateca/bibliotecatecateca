package dao;

import model.Livro;
import model.OperacaoBD;
import model.TipoAtualizaBD;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ConexaoBD;

public class LivroDAO implements OperacaoBD{
	private ConexaoBD bd;
	private Livro livro;
	private List<Livro> livros;
	private String sql;
	private String texto;
    private PreparedStatement statement;
    
    public LivroDAO(ConexaoBD bd, Livro livro) {
    	livros = new ArrayList<>();
    	this.bd = bd;
    	this.livro = livro;
    }
    
    //Código para verificar se o livro existe no banco
  	public boolean buscar() {
  		//Limpa buscas anteriores
  	    livros.clear();
        String sql = "{CALL conLivro(?)}";

        //CallableStatement:chama a função PreparedStatement:roda direto
        try (CallableStatement statement = bd.connection.prepareCall(sql)) {
            
            // Passamos apenas o texto. A procedure já coloca os '%' no SQL dela.
        	// % = contém "partedotitulo" em qualquer posição
        	statement.setString(1, livro.getTitulo());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                
                // Pegamos os metadados para saber quais colunas a procedure retornou
                ResultSetMetaData metaData = resultSet.getMetaData();
                
                if (resultSet.next()) {
                    // Verifica se a primeira coluna retornada se chama "mensagem" (Livro não localizado)
                    if (metaData.getColumnName(1).equalsIgnoreCase("mensagem")) {
                        // Apenas imprime a mensagem do banco de dados e retorna a lista vazia
                        System.out.println(resultSet.getString("mensagem"));
                    } else {
                        // Se não é a mensagem, então retornou os livros
                        do {
                            Livro livro = new Livro();
                            livro.setIdLivro(resultSet.getInt("CodigoLivro"));
                            livro.setTitulo(resultSet.getString("Titulo"));
                            livro.setGenero(resultSet.getString("Genero"));
                            livro.setEditora(resultSet.getString("Editora"));
                            livro.setAutor(resultSet.getString("Autor"));
                            String status = resultSet.getString("Disponivel");
                            //Se disponivel for igual a status então é disponivel
                            //Se for diferente dará indisponivel 
                            //equals retorna true ou false
                            livro.setDisponivel("Disponível".equalsIgnoreCase(status));
                            
                            livros.add(livro);
                        } while (resultSet.next()); // Continua lendo os próximos registros
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros na procedure: " + e.getMessage());
            return false;
        }
        
        return true;
    }
  	
  	
  	@Override
  	public String atualizar(TipoAtualizaBD operacao) {
  		switch(operacao) {
  		case Criar:
  			return cadastrarLivro();
  		case Alterar:
  			return alterarLivro();
  		case Deletar:
  			return deletarLivro();
  		default:
  			return "Operação Inválida";
  			
  		}
  		
  	}
  	
  	private String cadastrarLivro() {
          texto = "Livro cadastrado com sucesso!";
          
          try {
        	  sql = "INSERT into livros(tituloLivro, generoLivro, editoraLivro, autorLivro, flgDisponivel) values (?,?,?,?,1)";
        	  //sql = "{call nome_da_sua_procedure(?, ?, ?, ?, ?, ?,?)}"; 
          	 	
              statement = bd.connection.prepareCall(sql);

              //Não passou id, pois o banco incrementa sozinho
              //statement.setInt(1, livro.getIdLivro());
              statement.setString(1, livro.getTitulo());
              statement.setString(2, livro.getGenero());
              statement.setString(3, livro.getEditora());
              statement.setString(4, livro.getAutor());
              
              
              //Executa o comando no banco de dados
              statement.execute();
             	}
          //Erro durante a comunicação com um banco
          catch (SQLException erro) {
              texto = "Falha na operação - " + erro.getMessage();
          }
          
          return texto;	
  	}
  	
  	
  	
  	private String alterarLivro() {
  		texto = "Livro alterado com sucesso!";
  		
  		try {
  			sql = "UPDATE livros SET tituloLivro = ?, generoLivro = ?, editoraLivro = ?, autorLivro = ? WHERE idLivro = ?";
  			
              statement = bd.connection.prepareCall(sql);


              statement.setString(1, livro.getTitulo());
              statement.setString(2, livro.getGenero());
              statement.setString(3, livro.getEditora());
              statement.setString(4, livro.getAutor());
              statement.setInt(5, livro.getIdLivro());
             // statement.setBoolean(6, livro.getDisponivel());
              
              //Executa o comando no banco de dados
              statement.execute();
  			
  		} 
  		//Erro durante a comunicação com um banco
  		catch (SQLException erro) {
  			texto = "Falha na operação - " + erro.getMessage();
  			
  		}
  		
  		return texto;
  		
  	}
  	
  	private String deletarLivro() {
  	    texto = "Livro deletado com sucesso!";

  	    try {
  	        sql = "DELETE FROM livros WHERE idLivro = ?";

  	        statement = bd.connection.prepareStatement(sql);
  	        statement.setInt(1, livro.getIdLivro());

  	        statement.executeUpdate();

  	    } catch (SQLException erro) {
  	        texto = "Falha na operação - " + erro.getMessage();
  	    }

  	    return texto;
   }
  	
  	public List<Livro> getLivros() {
  	    return livros;
  	}

}
