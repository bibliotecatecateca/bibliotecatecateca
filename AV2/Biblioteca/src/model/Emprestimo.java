package model;

import java.time.LocalDate;

public class Emprestimo {
	private int idEmp;
	private Livro livro;
	private Cliente cliente;
	private LocalDate dataEmprestimo;
	private LocalDate dataDevolucao;
	private LocalDate dataDevolucaoReal;
	private float valorMulta;
	
	public Emprestimo() {
		
	}
	
	
	public boolean EstaAtrasado() {		
		if(this.getDataDevolucaoReal().isAfter(this.getDataDevolucao())) {
			System.out.println("Atrasado");
			return true;
		} else {
			System.out.println("Em dia");
			return false;
		}
		
	}

	public int getIdEmp() {
		return this.idEmp;
	}
	
	
	public void setIdEmp(int idEmp) {
		this.idEmp = idEmp;
	}
	
	
	public Livro getLivro() {
		return this.livro;
	}
	
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
	
	public Cliente getCliente() {
		return this.cliente;
	}
	
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	public LocalDate getDataEmprestimo() {
		return this.dataEmprestimo;
	}
	
	
	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	
	
	public LocalDate getDataDevolucao() {
		return this.dataDevolucao;
	}
	
	
	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	
	public LocalDate getDataDevolucaoReal() {
		return this.dataDevolucaoReal;
	}
	
	
	public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
		this.dataDevolucaoReal = dataDevolucaoReal;
	}
	
	
	public float getValorMulta() {
		return this.valorMulta;
	}
	
	
	public void setValorMulta(float valorMulta) {
		this.valorMulta = valorMulta;
	}
	
	
	
	
	
}
