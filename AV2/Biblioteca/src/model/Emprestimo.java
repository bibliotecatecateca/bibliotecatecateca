package model;

import java.time.LocalDate;

public class Emprestimo {
    private int idEmp;
    private Livro livro;
    private Cliente cliente;
    private LocalDate dataEmpIni;
    private LocalDate dataEmpEst;
    private LocalDate dataEmpFin;

    public Emprestimo() {

    }

    //Método que verifica se o livro foi devolvido com atraso
    public boolean estaAtrasado() {
        if (dataEmpEst == null || dataEmpFin == null) {
            return false;
        }

        return dataEmpFin.isAfter(dataEmpEst);
    }

    //Mpétodo que calcula a multa de acordo com os dias de atraso
    public double calcularMulta() {
        double valorPorDia = 1.50;
        int diasAtraso = 0;

        //Se a devolução não passou da data estimada, não há multa
        if (!dataEmpFin.isAfter(dataEmpEst)) {
            return 0;
        }

        //Variável temporária para não alterar a data original do empréstimo
        LocalDate dataAuxiliar = dataEmpEst;

        //Conta quantos dias de atraso houve
        while (dataAuxiliar.isBefore(dataEmpFin)){
        	diasAtraso++;
        	dataAuxiliar = dataAuxiliar.plusDays(1);
        	}

        //Retorna o valor total da multa
        return diasAtraso * valorPorDia;
    }

    public int getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataEmpIni() {
        return dataEmpIni;
    }

    public void setDataEmpIni(LocalDate dataEmpIni) {
        this.dataEmpIni = dataEmpIni;
    }

    public LocalDate getDataEmpEst() {
        return dataEmpEst;
    }

    public void setDataEmpEst(LocalDate dataEmpEst) {
        this.dataEmpEst = dataEmpEst;
    }

    public LocalDate getDataEmpFin() {
        return dataEmpFin;
    }

    public void setDataEmpFin(LocalDate dataEmpFin) {
        this.dataEmpFin = dataEmpFin;
    }
}