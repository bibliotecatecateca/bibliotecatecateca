package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
	private String host;
	private int port;
	private String dbName;
	private String login;
	private String senha;
	public Connection connection;
	
	public ConexaoBD() {
		this.host = "localhost";
		this.port = 3306;
		this.dbName = "Biblioteca";
		this.login = "root";
		this.senha = "";
		this.connection = null;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Connection getConnection() {
		return this.connection;
	}

	public boolean connect() {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
			connection = DriverManager.getConnection(url, login, senha);
			System.out.println("Conectou");
			return true;
		}
		catch (ClassNotFoundException erro) {
			System.out.println("Driver nao encontrado " + erro.toString());
			return false;
		}
		catch (SQLException erro) {
			System.out.println("Falha ao conectar " + erro.toString());
			return false;
		}
	}
	
	public boolean close() {
		try {
			connection. close();
			//System.out.println("Desconectou");
			this.connection = null;
			return true;
		}
		catch (SQLException erro) {
			System.out.println("Falha ao conectar " + erro.toString());
			return false;
		}
	}

}
