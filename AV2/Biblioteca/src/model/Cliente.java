package model;

public class Cliente {
	private String cpf;
	private String telefone;
	private String nome;
	private String email;
	
	

	
	public Cliente(String cpf) {
		this.setCpf(cpf);

	}


	public String getCpf() {
		return this.cpf;
	}
	
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	
	public String getTelefone() {
		return this.telefone;
	}
	
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	
	public String getNome() {
		return this.nome;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public String getEmail() {
		return this.email;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
