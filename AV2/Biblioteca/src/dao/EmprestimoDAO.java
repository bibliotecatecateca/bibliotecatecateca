package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ConexaoBD;
import model.Emprestimo;
import model.Livro;
import model.OperacaoBD;
import model.TipoAtualizaBD;

public class EmprestimoDAO implements OperacaoBD {

    private ConexaoBD bd;
    private Emprestimo emprestimo;
    private ArrayList<Emprestimo> emprestimos;
    private String sql;
    private String texto;
    private PreparedStatement statement;

    public EmprestimoDAO(ConexaoBD bd, Emprestimo emprestimo) {
        this.bd = bd;
        this.emprestimo = emprestimo;
        this.emprestimos = new ArrayList<>();
    }

    @Override
    //Busca os empréstimos ativos de um cliente
    public boolean buscar() {
        emprestimos.clear();

        sql = "SELECT " +
        	      "e.idEmp AS CodigoEmprestimo, " +
        	      "l.tituloLivro AS Livro, " +
        	      "e.dataEmpIni AS DataEmprestimo, " +
        	      "e.dataEmpEst AS DataDevolucaoEstimada " +
        	      "FROM emprestimo e " +
        	      "JOIN livros l ON e.idLivro = l.idLivro " +
        	      "WHERE e.cpfCliente = ? " +
        	      "AND e.dataEmpFin IS NULL";

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setString(1, emprestimo.getCliente().getCpf());

            ResultSet resultSet = statement.executeQuery();

            //Percorre os empréstimos encontrados 
            while (resultSet.next()) {
                Emprestimo emp = new Emprestimo();

                emp.setIdEmp(resultSet.getInt("CodigoEmprestimo"));

                Livro livro = new Livro();
                livro.setTitulo(resultSet.getString("Livro"));
                emp.setLivro(livro);

                emp.setDataEmpIni(
                	    resultSet.getTimestamp("DataEmprestimo").toLocalDateTime().toLocalDate()
                	);

                emp.setDataEmpEst(
                	   resultSet.getTimestamp("DataDevolucaoEstimada").toLocalDateTime().toLocalDate()
                );

                //Adiciona o empréstimo na lista
                emprestimos.add(emp);
            }

            return true;

        } catch (SQLException erro) {
            System.err.println("Erro ao buscar empréstimos: " + erro.getMessage());
            return false;
        }
    }

    @Override
    //Escolhe qual operação será feita
    public String atualizar(TipoAtualizaBD operacao) {
        switch (operacao) {
            case Criar:
                return cadastrarEmprestimo();

            case Deletar:
                return deletarEmprestimo();

            default:
                return "Operação Inválida";
        }
    }

    //Método responsável por cadastrar um empréstimo
    private String cadastrarEmprestimo() {
        texto = "Empréstimo cadastrado com sucesso!";

        try {
            bd.connection.setAutoCommit(false);

            sql = "SELECT idEmp FROM emprestimo WHERE idLivro = ? AND dataEmpFin IS NULL";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, emprestimo.getLivro().getIdLivro());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int idEmp = rs.getInt("idEmp");
                
                //Desfaz a operação
                bd.connection.rollback();

                return "O livro informado já está cadastrado no empréstimo de código " + idEmp;
            }

            //Cadastra o empréstimo no banco
            sql = "INSERT INTO emprestimo (idLivro, cpfCliente, dataEmpIni, dataEmpEst)" 
            		+ "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY))";

            statement = bd.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, emprestimo.getLivro().getIdLivro());
            statement.setString(2, emprestimo.getCliente().getCpf());

            int linhas = statement.executeUpdate();

            if (linhas == 0) {
                bd.connection.rollback();
                return "Nenhum empréstimo foi cadastrado.";
            }

            int idGerado = 0;

            //Pega o código gerado para o empréstimo
            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                idGerado = generatedKeys.getInt(1);
            }

            //Marca o livro como indisponível
            sql = "UPDATE livros SET flgDisponivel = false WHERE idLivro = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, emprestimo.getLivro().getIdLivro());
            statement.executeUpdate();

            //Confirma a transação
            bd.connection.commit();

            texto = "Empréstimo cadastrado com sucesso! O código do empréstimo é: " + idGerado;

        } catch (SQLException erro) {
            try {
            		
            		//Desfaz a operação caso ocorra erro
                bd.connection.rollback();
                		
            } catch (SQLException e) {
            	
                System.err.println("Erro ao desfazer operação: " + e.getMessage());
            }

            texto = "Falha na operação - " + erro.getMessage();

        } finally {
            try {
            	
            		//Volta o autoCommit ao normal
                bd.connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autoCommit: " + e.getMessage());
            }
        }

        return texto;
    }

    //Método responsável por deletar um empréstimo
    private String deletarEmprestimo() {
        texto = "Empréstimo deletado com sucesso!";

        try {
            bd.connection.setAutoCommit(false);

            int idLivro = 0;
            boolean emprestimoAtivo = false;

            sql = "SELECT idLivro, dataEmpFin FROM emprestimo WHERE idEmp = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, emprestimo.getIdEmp());

            ResultSet rs = statement.executeQuery();

            
            if (rs.next()) {
                idLivro = rs.getInt("idLivro");
                
                //Verifica se o empréstimo ainda está ativo
                emprestimoAtivo = rs.getTimestamp("dataEmpFin") == null;
            } else {
                bd.connection.rollback();
                return "Empréstimo não encontrado.";
            }

            //Deleta o empréstimo
            sql = "DELETE FROM emprestimo WHERE idEmp = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, emprestimo.getIdEmp());

            int linhas = statement.executeUpdate();

            if (linhas == 0) {
                bd.connection.rollback();
                return "Nenhum empréstimo foi deletado.";
            }

            //Se o empréstimo estava ativo, libera o livro novamente
            if (emprestimoAtivo) {
                sql = "UPDATE livros SET flgDisponivel = true WHERE idLivro = ?";

                statement = bd.connection.prepareStatement(sql);
                statement.setInt(1, idLivro);
                statement.executeUpdate();
            }

            //Confirma a transação
            bd.connection.commit();

        } catch (SQLException erro) {
            try {
                bd.connection.rollback();
            } catch (SQLException e) {
                System.err.println("Erro ao desfazer operação: " + e.getMessage());
            }

            texto = "Falha na operação - " + erro.getMessage();

        } finally {
            try {
                bd.connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autoCommit: " + e.getMessage());
            }
        }

        return texto;
    }

    //Método responsável por finalizar um empréstimo
    public String finalizarEmprestimo() {
        texto = "Empréstimo finalizado com sucesso!";

        try {
            bd.connection.setAutoCommit(false);

            int idLivro = 0;
            double multa = 0;

            sql = "SELECT idLivro, dataEmpEst FROM emprestimo WHERE idEmp = ? AND dataEmpFin IS NULL";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, emprestimo.getIdEmp());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                idLivro = rs.getInt("idLivro");

                //Cria um empréstimo auxiliar para calcular a multa
                Emprestimo empCalculado = new Emprestimo();

                empCalculado.setIdEmp(emprestimo.getIdEmp());

                empCalculado.setDataEmpEst(
                    rs.getTimestamp("dataEmpEst")
                      .toLocalDateTime()
                      .toLocalDate()
                );

                empCalculado.setDataEmpFin(java.time.LocalDate.now());

                //Calcula a multa, se houver atraso
                multa = empCalculado.calcularMulta();

            } else {
                bd.connection.rollback();
                return "Empréstimo não encontrado ou já finalizado.";
            }

            //Finaliza o empréstimo e salva a multa
            sql = "UPDATE emprestimo SET dataEmpFin = NOW(), vMulta = ? WHERE idEmp = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setDouble(1, multa);
            statement.setInt(2, emprestimo.getIdEmp());
            statement.executeUpdate();

            //Libera o livro novamente
            sql = "UPDATE livros SET flgDisponivel = true WHERE idLivro = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, idLivro);
            statement.executeUpdate();

            bd.connection.commit();

            //Caso tenha multa, altera a mensagem final
            if (multa > 0) {
                texto = String.format(
                    "Empréstimo finalizado. Atenção: Há uma multa de R$ %.2f",
                    multa
                );
            }

        } catch (SQLException erro) {
            try {
                bd.connection.rollback();
            } catch (SQLException e) {
                System.err.println("Erro ao desfazer operação: " + e.getMessage());
            }

            texto = "Falha na operação - " + erro.getMessage();

        } finally {
            try {
                bd.connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autoCommit: " + e.getMessage());
            }
        }

        return texto;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}