package model;

public class Funcionario {
	private int idFunc;
	private String nome;
	private LoginFunc login;
	
	public Funcionario(LoginFunc login){
		this.setIdFunc(0);
		this.setNome(null);
		this.setLogin(login);
	}
	
	
	
	public int getIdFunc() {
		return this.idFunc;
	}
	
	public void setIdFunc(int idFunc) {
		this.idFunc = idFunc;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public LoginFunc getLogin() {
		return this.login;
	}
	
	public void setLogin(LoginFunc login) {
		this.login = login;
	}
	
	
}
