package view;

import model.ItemPedido;
import model.Pedido;
import model.Produto;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LanchoneteView {

    private final Scanner sc;

    public LanchoneteView(Scanner sc) {
        this.sc = sc;
    }

    // ─── Menu principal ───────────────────────────────────────────────────────

    public int exibirMenuPrincipal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        LANCHONETE FOFIS BURGUER");
        System.out.println("=".repeat(50));
        System.out.println("1 - Atendimento");
        System.out.println("2 - Histórico");
        System.out.println("3 - Financeiro");
        System.out.println("0 - Sair");
        System.out.println("=".repeat(50));
        return lerOpcao();
    }

    public int exibirMenuAtendimento() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("              ATENDIMENTO");
        System.out.println("─".repeat(50));
        System.out.println("1 - Novo Pedido");
        System.out.println("2 - Adicionar item a pedido em aberto");
        System.out.println("3 - Efetuar Pagamento");
        System.out.println("0 - Voltar");
        System.out.println("─".repeat(50));
        return lerOpcao();
    }

    public int exibirMenuHistorico() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("              HISTÓRICO");
        System.out.println("─".repeat(50));
        System.out.println("1 - Ver Pedidos em Aberto");
        System.out.println("2 - Ver Pedidos Pagos");
        System.out.println("0 - Voltar");
        System.out.println("─".repeat(50));
        return lerOpcao();
    }

    public int exibirMenuFinanceiro() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("              FINANCEIRO");
        System.out.println("─".repeat(50));
        System.out.println("1 - Faturamento por Data");
        System.out.println("2 - Gerenciar Estoque");
        System.out.println("3 - Gerenciar Fornecedores");
        System.out.println("4 - Contas a Pagar");
        System.out.println("0 - Voltar");
        System.out.println("─".repeat(50));
        return lerOpcao();
    }

    // ─── Leitura de dados ─────────────────────────────────────────────────────

    public String lerNomeCliente() {
        System.out.print("Nome do Cliente: ");
        return sc.nextLine();
    }

    public int perguntarPagarAgora() {
        System.out.println("1 - Pagar agora");
        System.out.println("2 - Deixar em aberto");
        return lerOpcao();
    }

    public int escolherCategoria() {
        System.out.println("\n--- O que deseja adicionar? ---");
        System.out.println("1 - Lanches");
        System.out.println("2 - Bebidas");
        System.out.println("0 - Finalizar");
        return lerOpcao();
    }

    public int escolherProduto(ArrayList<Produto> lista, ArrayList<model.Estoque> estoques) {
        System.out.println();
        for (int i = 0; i < lista.size(); i++) {
            Produto p = lista.get(i);
            int qtdEstoque = 0;
            for (model.Estoque e : estoques) {
                if (e.getProduto().equals(p)) {
                    qtdEstoque = e.getQuantidade();
                    break;
                }
            }
            System.out.printf("%d - %s  R$ %.2f (Estoque: %d)%n", i + 1,
                    p.getNome(), p.getPreco(), qtdEstoque);
        }
        System.out.println("0 - Voltar");
        return lerOpcao();
    }

    public int lerQuantidade() {
        while (true) {
            System.out.print("Quantidade: ");
            try {
                int qtd = sc.nextInt();
                sc.nextLine();
                return qtd;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public int escolherPedidoDaLista(ArrayList<Pedido> lista) {
        System.out.println("\n--- Pedidos em aberto / parciais ---");
        for (int i = 0; i < lista.size(); i++) {
            Pedido p = lista.get(i);
            System.out.printf("%d - %s | Total: R$ %.2f | Em aberto: R$ %.2f%n",
                    i + 1, p.getCliente(), p.total(), p.totalEmAberto());
        }
        System.out.println("0 - Voltar");
        while (true) {
            System.out.print("Escolha o pedido: ");
            try {
                int idx = sc.nextInt();
                sc.nextLine();
                return idx;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public int escolherPedidoPendente(ArrayList<Pedido> lista) {
        System.out.println("\n--- Pedidos pendentes ---");
        for (int i = 0; i < lista.size(); i++) {
            Pedido p = lista.get(i);
            System.out.printf("%d - %s | Total: R$ %.2f | Em aberto: R$ %.2f | Status: %s%n",
                    i + 1, p.getCliente(), p.total(), p.totalEmAberto(), p.status());
        }
        System.out.println("0 - Voltar");
        while (true) {
            System.out.print("Escolha o pedido: ");
            try {
                int idx = sc.nextInt();
                sc.nextLine();
                return idx;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    // ─── Pagamento ────────────────────────────────────────────────────────────

    public int escolherFormaPagamento(double valorEmAberto) {
        System.out.printf("%nValor ainda em aberto: R$ %.2f%n", valorEmAberto);
        System.out.println("1 - Pagar tudo");
        System.out.println("2 - Dividir igualmente");
        System.out.println("3 - Pagar separado (item por item)");
        System.out.println("4 - Pagar valor parcial");
        while (true) {
            System.out.print("Escolha: ");
            try {
                int op = sc.nextInt();
                sc.nextLine();
                return op;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public int lerNumeroPessoas() {
        while (true) {
            System.out.print("Quantas pessoas: ");
            try {
                int p = sc.nextInt();
                sc.nextLine();
                return p;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public void exibirValorPorPessoa(double valor) {
        System.out.printf("Cada um paga: R$ %.2f%n", valor);
    }

    public int escolherItemParaPagar(ArrayList<ItemPedido> itens) {
        System.out.println("\n--- Itens pendentes ---");
        boolean temPendente = false;
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido item = itens.get(i);
            int restante = item.getQuantidade() - item.getQuantidadePaga();
            if (restante > 0) {
                System.out.printf("%d - %s | Restante: %d | R$ %.2f%n",
                        i + 1, item.getProduto().getNome(), restante,
                        item.getProduto().getPreco() * restante);
                temPendente = true;
            }
        }
        if (!temPendente) {
            exibirMensagem("Todos os itens ja foram pagos!");
            return -1;
        }
        System.out.println("0 - Parar");
        while (true) {
            System.out.print("Escolha item: ");
            try {
                int idx = sc.nextInt();
                sc.nextLine();
                return idx;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public int lerQuantidadeParaPagar(int max) {
        while (true) {
            System.out.print("Quantidade a pagar (max " + max + "): ");
            try {
                int qtd = sc.nextInt();
                sc.nextLine();
                return qtd;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public double lerValorPagamento(double valorEmAberto) {
        while (true) {
            System.out.printf("Valor a pagar (máximo R$ %.2f): R$ ", valorEmAberto);
            try {
                double valor = sc.nextDouble();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public int perguntarContinuarPagando() {
        while (true) {
            System.out.print("Continuar pagando? 1-Sim | 0-Nao: ");
            try {
                int op = sc.nextInt();
                sc.nextLine();
                return op;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    // ─── Faturamento ──────────────────────────────────────────────────────────

    public int escolherTipoFaturamento() {
        System.out.println("1 - Por dia");
        System.out.println("2 - Por periodo");
        while (true) {
            System.out.print("Escolha: ");
            try {
                int op = sc.nextInt();
                sc.nextLine();
                return op;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public String lerData(String label) {
        System.out.print(label + " (DD-MM-YYYY): ");
        return sc.nextLine().trim();
    }

    public void exibirFaturamentoDia(String data, double valor) {
        System.out.printf("Faturamento em %s (somente pagos): R$ %.2f%n", data, valor);
    }

    public void exibirFaturamentoPeriodo(String inicio, String fim, double valor) {
        System.out.printf("Faturamento de %s a %s (somente pagos): R$ %.2f%n",
                inicio, fim, valor);
    }

    // ─── Exibição de pedidos ──────────────────────────────────────────────────

    public void exibirPedido(Pedido p) {
        System.out.println("\nCliente : " + p.getCliente());
        System.out.println("Data    : " + p.getDataFormatada());
        System.out.println("Status  : " + p.status());

        System.out.println("  [Lanches]");
        boolean temLanche = false;
        for (ItemPedido i : p.getItens()) {
            if (i.getProduto().getCategoria().equals("Lanche")) {
                System.out.printf("    %s x%d = R$ %.2f  (pago: %d)%n",
                        i.getProduto().getNome(), i.getQuantidade(), i.subtotal(), i.getQuantidadePaga());
                temLanche = true;
            }
        }
        if (!temLanche) System.out.println("    (nenhum)");

        System.out.println("  [Bebidas]");
        boolean temBebida = false;
        for (ItemPedido i : p.getItens()) {
            if (i.getProduto().getCategoria().equals("Bebida")) {
                System.out.printf("    %s x%d = R$ %.2f  (pago: %d)%n",
                        i.getProduto().getNome(), i.getQuantidade(), i.subtotal(), i.getQuantidadePaga());
                temBebida = true;
            }
        }
        if (!temBebida) System.out.println("    (nenhum)");

        System.out.printf("  Total: R$ %.2f | Em aberto: R$ %.2f%n",
                p.total(), p.totalEmAberto());
    }

    public void exibirListaPedidos(String titulo, ArrayList<Pedido> lista, double totalGeral, String labelTotal) {
        System.out.println("\n===== " + titulo + " =====");
        for (Pedido p : lista) exibirPedido(p);
        System.out.printf("%n" + labelTotal + ": R$ %.2f%n", totalGeral);
    }

    public int exibirListaPedidosComSelecao(String titulo, ArrayList<Pedido> lista, double totalGeral, String labelTotal) {
        System.out.println("\n===== " + titulo + " =====");
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d - ", i + 1);
            exibirPedido(lista.get(i));
        }
        System.out.printf("%n" + labelTotal + ": R$ %.2f%n", totalGeral);
        System.out.println("0 - Voltar");
        while (true) {
            System.out.print("Selecione um pedido para pagar (ou 0 para voltar): ");
            try {
                int idx = sc.nextInt();
                sc.nextLine();
                return idx;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public void exibirTotalPedido(double total) {
        System.out.printf("%nTotal do pedido: R$ %.2f%n", total);
    }

    // ─── Estoque ──────────────────────────────────────────────────────────────

    public int exibirMenuEstoque() {
        System.out.println("\n--- Gerenciar Estoque ---");
        System.out.println("1 - Ver Estoque");
        System.out.println("2 - Adicionar ao Estoque");
        System.out.println("0 - Voltar");
        return lerOpcao();
    }

    public void exibirEstoque(ArrayList<model.Estoque> estoques) {
        System.out.println("\n--- Estoque Atual ---");
        for (model.Estoque e : estoques) {
            System.out.printf("%s: %d unidades%n", e.getProduto().getNome(), e.getQuantidade());
        }
    }

    public int escolherProdutoParaAdicionar(ArrayList<Produto> lista) {
        System.out.println("\n--- Produtos ---");
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, lista.get(i).getNome());
        }
        System.out.println("0 - Voltar");
        while (true) {
            System.out.print("Escolha o produto: ");
            try {
                int idx = sc.nextInt();
                sc.nextLine();
                return idx;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    // ─── Fornecedores ─────────────────────────────────────────────────────────

    public int exibirMenuFornecedores() {
        System.out.println("\n--- Gerenciar Fornecedores ---");
        System.out.println("1 - Ver Fornecedores");
        System.out.println("2 - Adicionar Fornecedor");
        System.out.println("3 - Editar Fornecedor");
        System.out.println("0 - Voltar");
        return lerOpcao();
    }

    public void exibirFornecedores(ArrayList<model.Fornecedor> fornecedores) {
        System.out.println("\n--- Fornecedores ---");
        for (int i = 0; i < fornecedores.size(); i++) {
            model.Fornecedor f = fornecedores.get(i);
            System.out.printf("%d - CNPJ: %s | Nome: %s%n", i + 1, f.getCnpj(), f.getNome());
        }
    }

    public String lerCnpj() {
        System.out.print("CNPJ: ");
        return sc.nextLine().trim();
    }

    public String lerNomeFornecedor() {
        System.out.print("Nome do Fornecedor: ");
        return sc.nextLine().trim();
    }

    public int escolherFornecedor(ArrayList<model.Fornecedor> fornecedores) {
        exibirFornecedores(fornecedores);
        System.out.println("0 - Voltar");
        while (true) {
            System.out.print("Escolha o fornecedor: ");
            try {
                int idx = sc.nextInt();
                sc.nextLine();
                return idx;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    // ─── Contas a Pagar ───────────────────────────────────────────────────────

    public int exibirMenuContasPagar() {
        System.out.println("\n--- Contas a Pagar ---");
        System.out.println("1 - Ver Todas as Contas");
        System.out.println("2 - Pesquisar por Data");
        System.out.println("3 - Pesquisar por Semana");
        System.out.println("4 - Pesquisar por Periodo");
        System.out.println("0 - Voltar");
        return lerOpcao();
    }

    public void exibirContasPagar(ArrayList<model.ContaPagar> contas) {
        System.out.println("\n--- Contas a Pagar ---");
        for (model.ContaPagar c : contas) {
            System.out.printf("Fornecedor: %s | Vencimento: %s | Total: R$ %.2f%n",
                    c.getFornecedor().getNome(), c.getDataVencimentoFormatada(), c.getValorTotal());
            for (model.ItemEntradaEstoque i : c.getItens()) {
                System.out.printf("  - %s x%d = R$ %.2f%n", i.getProduto().getNome(), i.getQuantidade(), i.getValorTotal());
            }
        }
    }

    public double lerValorTotal() {
        while (true) {
            System.out.print("Valor total: R$ ");
            try {
                double val = sc.nextDouble();
                sc.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    public int perguntarAdicionarMaisItens() {
        while (true) {
            System.out.print("Adicionar mais itens? 1-Sim | 0-Nao: ");
            try {
                int op = sc.nextInt();
                sc.nextLine();
                return op;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }

    // ─── Mensagens simples ────────────────────────────────────────────────────

    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }

    public int lerOpcao() {
        while (true) {
            System.out.print("Escolha: ");
            try {
                int op = sc.nextInt();
                sc.nextLine();
                return op;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                sc.nextLine();
            }
        }
    }
}
