package model;

public class Livro {
	private int idLivro;
	private String titulo;
	private String autor;
	private String genero;
	private String editora;
	private boolean disponivel;
	
	public Livro(String titulo) {
		this.setTitulo(titulo);

	}

	public Livro() {

	}

	public int getIdLivro() {
		return this.idLivro;
	}
	
	public void setIdLivro(int idLivro) {
		this.idLivro = idLivro;
	}
	
	public String getTitulo() {
		return this.titulo;
	}  
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getAutor() {
		return this.autor;
	}
	
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEditora() {
		return this.editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public boolean getDisponivel() {
		return this.disponivel;
	}
	
	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
	
	

}
