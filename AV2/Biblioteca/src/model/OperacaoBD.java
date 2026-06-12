package model;

public interface OperacaoBD {
	public boolean buscar();
	
	public String atualizar(TipoAtualizaBD operacao);
}
