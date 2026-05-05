package model;

public class Produto {

    private String nome;
    private double preco;
    private String categoria;

    public Produto(String nome, double preco, String categoria) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome invalido");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preco esta negativo, e nao pode ser negativo");
        }
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return Double.compare(produto.preco, preco) == 0 &&
               nome.equals(produto.nome) &&
               categoria.equals(produto.categoria);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nome, preco, categoria);
    }
}
