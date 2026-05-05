package model;

public class ItemEntradaEstoque {
    private Produto produto;
    private int quantidade;
    private double valorTotal;

    public ItemEntradaEstoque(Produto produto, int quantidade, double valorTotal) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto invalido");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade invalida");
        }
        if (valorTotal < 0) {
            throw new IllegalArgumentException("Valor total invalido");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}
