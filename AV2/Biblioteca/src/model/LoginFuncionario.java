package model;

public class LoginFuncionario {
	private int idLogin;
	private String usuario;
	private String senha;
	
	public LoginFuncionario(String usuario, String senha) {
		this.setIdLogin(idLogin);
		this.setUsuario(usuario);
		this.setSenha(senha);
	}
	
	//Método que valida se o usuário e a senha digitados são iguais aos cadastrados
	public boolean validarLogin(String usuario, String senha) {
		if(this.usuario.equals(usuario) && this.senha.equals(senha)) {
			return true;
		} else {
			return false;
		}
		
	}

	public int getIdLogin() {
		return this.idLogin;
	}
	
	public void setIdLogin(int idLogin) {
		this.idLogin = idLogin;
	}
	
	public String getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getSenha() {
		return this.senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
