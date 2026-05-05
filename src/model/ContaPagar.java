package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ContaPagar {
    private Fornecedor fornecedor;
    private LocalDate dataVencimento;
    private double valorTotal;
    private ArrayList<ItemEntradaEstoque> itens;

    public ContaPagar(Fornecedor fornecedor, LocalDate dataVencimento, ArrayList<ItemEntradaEstoque> itens) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor invalido");
        }
        if (dataVencimento == null) {
            throw new IllegalArgumentException("Data de vencimento invalida");
        }
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Itens invalidos");
        }
        this.fornecedor = fornecedor;
        this.dataVencimento = dataVencimento;
        this.itens = new ArrayList<>(itens);
        this.valorTotal = itens.stream().mapToDouble(ItemEntradaEstoque::getValorTotal).sum();
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public ArrayList<ItemEntradaEstoque> getItens() {
        return new ArrayList<>(itens);
    }

    public String getDataVencimentoFormatada() {
        return dataVencimento.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
