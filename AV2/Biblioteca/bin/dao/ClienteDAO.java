package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.ConexaoBD;
import model.OperacaoBD;
import model.TipoAtualizaBD;

public class ClienteDAO implements OperacaoBD {

    private ConexaoBD bd;
    private Cliente cliente;
    private ArrayList<Cliente> clientes;
    private String sql;
    private String texto;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public ClienteDAO(ConexaoBD bd, Cliente cliente) {
        this.bd = bd;
        this.cliente = cliente;
        this.clientes = new ArrayList<>();
    }

    @Override
    public boolean buscar() {
        sql = "SELECT cpfCliente, nomeCliente, telefoneCliente, emailCliente FROM cliente WHERE cpfCliente = ?";

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setString(1, cliente.getCpf());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                cliente.setCpf(resultSet.getString("cpfCliente"));
                cliente.setNome(resultSet.getString("nomeCliente"));
                cliente.setTelefone(resultSet.getString("telefoneCliente"));
                cliente.setEmail(resultSet.getString("emailCliente"));

                return true;
            }

            return false;

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar cliente: " + erro.getMessage());
            return false;
        }
    }

    public boolean buscarTodos() {
        clientes.clear();

        sql = "SELECT cpfCliente, nomeCliente, telefoneCliente, emailCliente FROM cliente";

        try {
            statement = bd.connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Cliente cliente = new Cliente(resultSet.getString("cpfCliente"));

                cliente.setNome(resultSet.getString("nomeCliente"));
                cliente.setTelefone(resultSet.getString("telefoneCliente"));
                cliente.setEmail(resultSet.getString("emailCliente"));

                clientes.add(cliente);
            }

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar clientes: " + erro.getMessage());
            return false;
        }
    }

    @Override
    public String atualizar(TipoAtualizaBD operacao) {
        switch (operacao) {
            case Criar:
                return cadastrarCliente();

            case Alterar:
                return alterarCliente();

            case Deletar:
                return deletarCliente();

            default:
                return "Operação Inválida";
        }
    }

    private String cadastrarCliente() {
        texto = "Cliente cadastrado com sucesso!";

        try {
            sql = "INSERT INTO cliente (cpfCliente, nomeCliente, telefoneCliente, emailCliente) VALUES (?, ?, ?, ?)";

            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, cliente.getCpf());
            statement.setString(2, cliente.getNome());
            statement.setString(3, cliente.getTelefone());
            statement.setString(4, cliente.getEmail());

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas == 0) {
                texto = "Nenhum cliente foi cadastrado.";
            }

        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    private String alterarCliente() {
        texto = "Cliente alterado com sucesso!";

        try {
            sql = "UPDATE cliente " +
                  "SET nomeCliente = ?, telefoneCliente = ?, emailCliente = ? " +
                  "WHERE cpfCliente = ?";

            statement = bd.connection.prepareStatement(sql);

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getTelefone());
            statement.setString(3, cliente.getEmail());
            statement.setString(4, cliente.getCpf());

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas == 0) {
                texto = "Nenhum cliente foi alterado.";
            }

        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    private String deletarCliente() {
        texto = "Cliente deletado com sucesso!";

        try {
            sql = "DELETE FROM cliente WHERE cpfCliente = ?";

            statement = bd.connection.prepareStatement(sql);
            statement.setString(1, cliente.getCpf());

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas == 0) {
                texto = "Nenhum cliente foi deletado.";
            }

        } catch (SQLException erro) {
            texto = "Falha na operação - " + erro.getMessage();
        }

        return texto;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}