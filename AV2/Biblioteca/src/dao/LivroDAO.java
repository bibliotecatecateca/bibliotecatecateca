package dao;

import model.Livro;
import model.OperacaoBD;
import model.TipoAtualizaBD;
import model.ConexaoBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements OperacaoBD {

    private ConexaoBD bd;
    private Livro livro;
    private List<Livro> livros;
    private String sql;
    private String texto;
    private PreparedStatement statement;

    public LivroDAO(ConexaoBD bd, Livro livro) {
        this.bd = bd;
        this.livro = livro;
        this.livros = new ArrayList<>();
    }

    @Override
    //Método responsável por buscar livros pelo título
    public boolean buscar() {
        livros.clear();

        sql = "SELECT idLivro, tituloLivro, generoLivro, editoraLivro, autorLivro, flgDisponivel FROM livros WHERE tituloLivro LIKE ?";

        try {
            statement = bd.connection.prepareStatement(sql);

            //% no inicio e final para que na aba de busca só de digitar uma letra apareça todos os livros que contenham ela no titulo 
            statement.setString(1, "%" + livro.getTitulo() + "%");

            ResultSet resultSet = statement.executeQuery();

            //Percorre os resultados encontrados no banco
            while (resultSet.next()) {
                Livro livroEncontrado = new Livro();

                livroEncontrado.setIdLivro(resultSet.getInt("idLivro"));
                livroEncontrado.setTitulo(resultSet.getString("tituloLivro"));
                livroEncontrado.setGenero(resultSet.getString("generoLivro"));
                livroEncontrado.setEditora(resultSet.getString("editoraLivro"));
                livroEncontrado.setAutor(resultSet.getString("autorLivro"));
                livroEncontrado.setDisponivel(resultSet.getBoolean("flgDisponivel"));

                //Adiciona o livro encontrado na lista
                livros.add(livroEncontrado);
            }

            return true;

        } catch (SQLException erro) {
            System.err.println("Erro ao buscar livros: " + erro.getMessage());
            return false;
        }
    }

    @Override
    //Método que decide qual operação sera feita
    public String atualizar(TipoAtualizaBD operacao) {
        switch (operacao) {
            case Criar:
                return cadastrarLivro();

            case Alterar:
                return alterarLivro();

            case Deletar:
                return deletarLivro();

            default:
                return "Operação inválida.";
        }
    }

    //Método responsável por cadastrar um novo livro
    private String cadastrarLivro() {
        texto = "Livro cadastrado com sucesso!";

        sql = "INSERT INTO livros " +
              "(tituloLivro, generoLivro, editoraLivro, autorLivro, flgDisponivel) VALUES (?, ?, ?, ?, ?)";

        try {
            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, livro.getTitulo());
            statement.setString(2, livro.getGenero());
            statement.setString(3, livro.getEditora());
            statement.setString(4, livro.getAutor());

            //Todo livro novo começa disponível
            statement.setBoolean(5, true);

            statement.executeUpdate();

        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    //Método responsável por alterar os dados de um livro
    private String alterarLivro() {
        texto = "Livro alterado com sucesso!";

        sql = "UPDATE livros SET tituloLivro = ?, generoLivro = ?, editoraLivro = ?, autorLivro = ? WHERE idLivro = ?";

        try {
            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, livro.getTitulo());
            statement.setString(2, livro.getGenero());
            statement.setString(3, livro.getEditora());
            statement.setString(4, livro.getAutor());
            statement.setInt(5, livro.getIdLivro());

            statement.executeUpdate();


        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    //Método responsável por deletar um livro pelo ID
    private String deletarLivro() {
        texto = "Livro deletado com sucesso!";

        sql = "DELETE FROM livros WHERE idLivro = ?";

        try {
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