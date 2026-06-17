package model;

public class Funcionario {
	private int idFuncionario;
	private String nome;
	private LoginFuncionario login;
	
	public Funcionario(LoginFuncionario login){
		this.setIdFuncionario(0);
		this.setNome(null);
		this.setLogin(login);
	}
	
	
	
	public int getIdFuncionario() {
		return this.idFuncionario;
	}
	
	public void setIdFuncionario(int idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public LoginFuncionario getLogin() {
		return this.login;
	}
	
	public void setLogin(LoginFuncionario login) {
		this.login = login;
	}
	
	
}
