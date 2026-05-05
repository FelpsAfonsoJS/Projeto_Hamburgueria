package model;

public class Estoque {
    private Produto produto;
    private int quantidade;

    public Estoque(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto invalido");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade invalida");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }
    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void adicionar(int qtd){
        if (qtd <= 0) {
            throw new IllegalArgumentException("Quantidade invalida para adicionar");
        }
        this.quantidade += qtd;

    }
    public void remover(int qtd){
        if (qtd > quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente no estoque");
        }
        this.quantidade -= qtd;
    }
}
