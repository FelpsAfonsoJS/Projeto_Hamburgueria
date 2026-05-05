package controller;

import model.ContaPagar;
import model.Estoque;
import model.Fornecedor;
import model.ItemEntradaEstoque;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import view.LanchoneteView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LanchoneteController {

    private final LanchoneteView view;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final ArrayList<Produto> lanches  = new ArrayList<>();
    private final ArrayList<Produto> bebidas  = new ArrayList<>();
    private final ArrayList<Pedido>  pedidos  = new ArrayList<>();
    private final ArrayList<Estoque> estoques = new ArrayList<>();
    private final ArrayList<Fornecedor> fornecedores = new ArrayList<>();
    private final ArrayList<ContaPagar> contasPagar = new ArrayList<>();

    public LanchoneteController(LanchoneteView view) {
        this.view = view;
        carregarCardapio();
    }

    // ─── Inicialização ────────────────────────────────────────────────────────

    private void carregarCardapio() {
        // Lanches
        lanches.add(new Produto("X-Burguer",      15.0, "Lanche"));
        lanches.add(new Produto("X-Salada",       17.0, "Lanche"));
        lanches.add(new Produto("X-Bacon",        20.0, "Lanche"));
        lanches.add(new Produto("Hamburguer",     12.0, "Lanche"));
        lanches.add(new Produto("Cachorro Quente", 8.0, "Lanche"));

        // Bebidas
        bebidas.add(new Produto("Refrigerante 250ml", 4.0,  "Bebida"));
        bebidas.add(new Produto("Cerveja 250ml",      8.0,  "Bebida"));
        bebidas.add(new Produto("Refrigerante 1lt",   10.0, "Bebida"));
        bebidas.add(new Produto("Suco 800ml",         12.0, "Bebida"));
        bebidas.add(new Produto("Cerveja 800ml",      16.0, "Bebida"));

        // Inicializar estoques com quantidades iniciais para todos os produtos
        for (Produto p : lanches) {
            estoques.add(new Estoque(p, 50)); // Quantidade inicial de 50 para cada lanche
        }
        for (Produto p : bebidas) {
            estoques.add(new Estoque(p, 100)); // Quantidade inicial de 100 para cada bebida
        }
    }

    // ─── Loop principal ───────────────────────────────────────────────────────

    public void iniciar() {
        int opcao;
        do {
            opcao = view.exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> menuAtendimento();
                case 2 -> menuHistorico();
                case 3 -> menuFinanceiro();
                case 0 -> view.exibirMensagem("Encerrando... Ate logo!");
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    // ─── MENU ATENDIMENTO ──────────────────────────────────────────────────────

    private void menuAtendimento() {
        int opcao;
        do {
            opcao = view.exibirMenuAtendimento();
            switch (opcao) {
                case 1 -> novoPedido();
                case 2 -> adicionarItensEmAberto();
                case 3 -> efetuarPagamento();
                case 0 -> {} // voltar
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    // ─── MENU HISTÓRICO ────────────────────────────────────────────────────────

    private void menuHistorico() {
        int opcao;
        do {
            opcao = view.exibirMenuHistorico();
            switch (opcao) {
                case 1 -> verPedidosAbertos();
                case 2 -> verPedidosPagos();
                case 0 -> {} // voltar
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    // ─── MENU FINANCEIRO ───────────────────────────────────────────────────────

    private void menuFinanceiro() {
        int opcao;
        do {
            opcao = view.exibirMenuFinanceiro();
            switch (opcao) {
                case 1 -> faturamentoPorData();
                case 2 -> gerenciarEstoque();
                case 3 -> gerenciarFornecedores();
                case 4 -> gerenciarContasPagar();
                case 0 -> {} // voltar
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    // ─── Case 1: Novo pedido ─────────────────────────────────────────────────

    private void novoPedido() {
        String nome = view.lerNomeCliente();
        if (nome.trim().isEmpty()) {
            view.exibirMensagem("Nome invalido.");
            return;
        }

        Pedido pedido = new Pedido(nome.trim());
        fluxoAdicionarItens(pedido);

        try {
            pedido.finalizar();
        } catch (Exception e) {
            view.exibirMensagem("Erro ao finalizar: " + e.getMessage());
            return;
        }

        view.exibirTotalPedido(pedido.total());

        if (view.perguntarPagarAgora() == 1) {
            fluxoPagamento(pedido);
        } else {
            view.exibirMensagem("Pedido salvo em aberto!");
        }

        pedidos.add(pedido);
    }

    // ─── Case 2: Adicionar itens a pedido em aberto ───────────────────────────

    private void adicionarItensEmAberto() {
        ArrayList<Pedido> naoPagos = filtrarPorStatus("aberto", "parcial");
        if (naoPagos.isEmpty()) {
            view.exibirMensagem("Nenhum pedido em aberto ou parcial.");
            return;
        }

        int idx = view.escolherPedidoDaLista(naoPagos);
        if (idx >= 1 && idx <= naoPagos.size()) {
            fluxoAdicionarItens(naoPagos.get(idx - 1));
            view.exibirMensagem("Itens adicionados!");
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    // ─── Case 3: Efetuar pagamento ────────────────────────────────────────────

    private void efetuarPagamento() {
        ArrayList<Pedido> pendentes = filtrarPorStatus("aberto", "parcial");
        if (pendentes.isEmpty()) {
            view.exibirMensagem("Nenhum pedido pendente de pagamento.");
            return;
        }

        int idx = view.escolherPedidoPendente(pendentes);
        if (idx >= 1 && idx <= pendentes.size()) {
            fluxoPagamento(pendentes.get(idx - 1));
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    // ─── Case 4, 5, 6: Listagens ─────────────────────────────────────────────

    private void verPedidosAbertos() {
        ArrayList<Pedido> lista = filtrarPorStatus("aberto", "parcial");
        if (lista.isEmpty()) {
            view.exibirMensagem("Nenhum pedido em aberto.");
            return;
        }
        double total = lista.stream().mapToDouble(Pedido::totalEmAberto).sum();
        
        int idx = view.exibirListaPedidosComSelecao("PEDIDOS EM ABERTO", lista, total, "Total geral em aberto");
        if (idx >= 1 && idx <= lista.size()) {
            fluxoPagamento(lista.get(idx - 1));
        }
    }

    private void verPedidosPagos() {
        ArrayList<Pedido> lista = filtrarPorStatus("pago");
        if (lista.isEmpty()) {
            view.exibirMensagem("Nenhum pedido totalmente pago.");
            return;
        }
        double total = lista.stream().mapToDouble(Pedido::total).sum();
        view.exibirListaPedidos("PEDIDOS PAGOS", lista, total, "Total arrecadado (pagos)");
    }

    // ─── Case 7: Faturamento ──────────────────────────────────────────────────

    private void faturamentoPorData() {
        int tipo = view.escolherTipoFaturamento();
        double fat = 0;

        if (tipo == 1) {
            String dataStr = view.lerData("Informe a data");
            try {
                LocalDate dia = LocalDate.parse(dataStr, fmt);
                for (Pedido p : pedidos) {
                    if (p.estaTotalmentePago() && p.getData().equals(dia))
                        fat += p.total();
                }
                view.exibirFaturamentoDia(dataStr, fat);
            } catch (Exception e) {
                view.exibirMensagem("Data invalida. Use o formato DD-MM-YYYY.");
            }

        } else if (tipo == 2) {
            String inicioStr = view.lerData("Data inicio");
            String fimStr    = view.lerData("Data fim  ");
            try {
                LocalDate inicio = LocalDate.parse(inicioStr, fmt);
                LocalDate fim    = LocalDate.parse(fimStr,    fmt);
                if (inicio.isAfter(fim)) {
                    view.exibirMensagem("Data de inicio nao pode ser depois da data fim.");
                    return;
                }
                for (Pedido p : pedidos) {
                    if (p.estaTotalmentePago()
                            && !p.getData().isBefore(inicio)
                            && !p.getData().isAfter(fim))
                        fat += p.total();
                }
                view.exibirFaturamentoPeriodo(inicioStr, fimStr, fat);
            } catch (Exception e) {
                view.exibirMensagem("Data invalida. Use o formato DD-MM-YYYY.");
            }
        } else {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    // ─── Case 8: Gerenciar Estoque ───────────────────────────────────────────���

    private void gerenciarEstoque() {
        int opcao;
        do {
            opcao = view.exibirMenuEstoque();
            switch (opcao) {
                case 1 -> verEstoque();
                case 2 -> adicionarAoEstoque();
                case 0 -> {} // voltar
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void verEstoque() {
        view.exibirEstoque(estoques);
    }

    private void adicionarAoEstoque() {
        // Selecionar fornecedor
        Fornecedor fornecedor = selecionarOuAdicionarFornecedor();
        if (fornecedor == null) return;

        // Ler data de vencimento
        String dataStr = view.lerData("Data de vencimento da conta");
        LocalDate dataVencimento;
        try {
            dataVencimento = LocalDate.parse(dataStr, fmt);
        } catch (Exception e) {
            view.exibirMensagem("Data invalida.");
            return;
        }

        // Adicionar itens
        ArrayList<ItemEntradaEstoque> itens = new ArrayList<>();
        do {
            Produto produto = selecionarProdutoParaEstoque();
            if (produto == null) break;

            int qtd = view.lerQuantidade();
            if (qtd <= 0) {
                view.exibirMensagem("Quantidade invalida.");
                continue;
            }

            double valorTotal = view.lerValorTotal();

            itens.add(new ItemEntradaEstoque(produto, qtd, valorTotal));

            // Adicionar ao estoque
            Estoque estoque = estoques.stream()
                    .filter(e -> e.getProduto().equals(produto))
                    .findFirst()
                    .orElse(null);
            if (estoque != null) {
                estoque.adicionar(qtd);
            }

        } while (view.perguntarAdicionarMaisItens() == 1);

        if (itens.isEmpty()) {
            view.exibirMensagem("Nenhum item adicionado.");
            return;
        }

        // Criar conta a pagar
        ContaPagar conta = new ContaPagar(fornecedor, dataVencimento, itens);
        contasPagar.add(conta);

        view.exibirMensagem("Entrada de estoque registrada e conta a pagar criada!");
    }

    private Fornecedor selecionarOuAdicionarFornecedor() {
        if (!fornecedores.isEmpty()) {
            view.exibirMensagem("1 - Escolher fornecedor existente");
            view.exibirMensagem("2 - Adicionar novo fornecedor");
            int op = view.lerOpcao();
            if (op == 1) {
                int idx = view.escolherFornecedor(fornecedores);
                if (idx >= 1 && idx <= fornecedores.size()) {
                    return fornecedores.get(idx - 1);
                }
            }
        }
        // Adicionar novo
        String cnpj = view.lerCnpj();
        String nome = view.lerNomeFornecedor();
        try {
            Fornecedor f = new Fornecedor(cnpj, nome);
            fornecedores.add(f);
            return f;
        } catch (Exception e) {
            view.exibirMensagem("Erro: " + e.getMessage());
            return null;
        }
    }

    private Produto selecionarProdutoParaEstoque() {
        ArrayList<Produto> todos = getTodosProdutos();
        int idx = view.escolherProdutoParaAdicionar(todos);
        if (idx >= 1 && idx <= todos.size()) {
            return todos.get(idx - 1);
        }
        return null;
    }

    // ─── Case 9: Gerenciar Fornecedores ───────────────────────────────────────

    private void gerenciarFornecedores() {
        int opcao;
        do {
            opcao = view.exibirMenuFornecedores();
            switch (opcao) {
                case 1 -> verFornecedores();
                case 2 -> adicionarFornecedor();
                case 3 -> editarFornecedor();
                case 0 -> {} // voltar
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void verFornecedores() {
        view.exibirFornecedores(fornecedores);
    }

    private void adicionarFornecedor() {
        String cnpj = view.lerCnpj();
        String nome = view.lerNomeFornecedor();
        try {
            Fornecedor f = new Fornecedor(cnpj, nome);
            fornecedores.add(f);
            view.exibirMensagem("Fornecedor adicionado!");
        } catch (Exception e) {
            view.exibirMensagem("Erro: " + e.getMessage());
        }
    }

    private void editarFornecedor() {
        int idx = view.escolherFornecedor(fornecedores);
        if (idx >= 1 && idx <= fornecedores.size()) {
            Fornecedor f = fornecedores.get(idx - 1);
            String novoNome = view.lerNomeFornecedor();
            try {
                f.setNome(novoNome);
                view.exibirMensagem("Fornecedor editado!");
            } catch (Exception e) {
                view.exibirMensagem("Erro: " + e.getMessage());
            }
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    // ─── Case 10: Contas a Pagar ──────────────────────────────────────────────

    private void gerenciarContasPagar() {
        int opcao;
        do {
            opcao = view.exibirMenuContasPagar();
            switch (opcao) {
                case 1 -> verTodasContas();
                case 2 -> pesquisarContasPorData();
                case 3 -> pesquisarContasPorSemana();
                case 4 -> pesquisarContasPorPeriodo();
                case 0 -> {} // voltar
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void verTodasContas() {
        view.exibirContasPagar(contasPagar);
    }

    private void pesquisarContasPorData() {
        String dataStr = view.lerData("Informe a data");
        try {
            LocalDate data = LocalDate.parse(dataStr, fmt);
            ArrayList<ContaPagar> filtradas = new ArrayList<>();
            for (ContaPagar c : contasPagar) {
                if (c.getDataVencimento().equals(data)) {
                    filtradas.add(c);
                }
            }
            view.exibirContasPagar(filtradas);
        } catch (Exception e) {
            view.exibirMensagem("Data invalida.");
        }
    }

    private void pesquisarContasPorSemana() {
        // Assume semana atual
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        LocalDate fimSemana = inicioSemana.plusDays(6);
        ArrayList<ContaPagar> filtradas = new ArrayList<>();
        for (ContaPagar c : contasPagar) {
            LocalDate venc = c.getDataVencimento();
            if (!venc.isBefore(inicioSemana) && !venc.isAfter(fimSemana)) {
                filtradas.add(c);
            }
        }
        view.exibirContasPagar(filtradas);
    }

    private void pesquisarContasPorPeriodo() {
        String inicioStr = view.lerData("Data inicio");
        String fimStr = view.lerData("Data fim");
        try {
            LocalDate inicio = LocalDate.parse(inicioStr, fmt);
            LocalDate fim = LocalDate.parse(fimStr, fmt);
            if (inicio.isAfter(fim)) {
                view.exibirMensagem("Data de inicio nao pode ser depois da data fim.");
                return;
            }
            ArrayList<ContaPagar> filtradas = new ArrayList<>();
            for (ContaPagar c : contasPagar) {
                LocalDate venc = c.getDataVencimento();
                if (!venc.isBefore(inicio) && !venc.isAfter(fim)) {
                    filtradas.add(c);
                }
            }
            view.exibirContasPagar(filtradas);
        } catch (Exception e) {
            view.exibirMensagem("Data invalida.");
        }
    }

    // ─── Fluxos auxiliares ────────────────────────────────────────────────────

    private void fluxoAdicionarItens(Pedido pedido) {
        int escolha;
        do {
            escolha = view.escolherCategoria();
            if (escolha == 1) {
                fluxoSelecionarProdutoComEstoque(lanches, pedido);
            } else if (escolha == 2) {
                fluxoSelecionarProdutoComEstoque(bebidas, pedido);
            } else if (escolha != 0) {
                view.exibirMensagem("Opcao invalida.");
            }
        } while (escolha != 0);
    }

    private void fluxoPagamento(Pedido pedido) {
        int forma = view.escolherFormaPagamento(pedido.totalEmAberto());

        if (forma == 1) {
            pagarTudo(pedido);

        } else if (forma == 2) {
            int pessoas = view.lerNumeroPessoas();
            if (pessoas <= 0) {
                view.exibirMensagem("Numero de pessoas invalido.");
                return;
            }
            view.exibirValorPorPessoa(pedido.totalEmAberto() / pessoas);
            pagarTudo(pedido);

        } else if (forma == 3) {
            pagarPorItem(pedido);

        } else if (forma == 4) {
            pagarPorValor(pedido);

        } else {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    private void pagarTudo(Pedido pedido) {
        for (ItemPedido item : pedido.getItens())
            item.pagarQuantidade(item.getQuantidade() - item.getQuantidadePaga());
        view.exibirMensagem("Pedido totalmente pago!");
    }

    private void pagarPorItem(Pedido pedido) {
        int continuar;
        do {
            int itemIdx = view.escolherItemParaPagar(pedido.getItens());
            if (itemIdx == -1) break;  // todos pagos
            if (itemIdx == 0) break;   // usuario escolheu parar

            if (itemIdx >= 1 && itemIdx <= pedido.getItens().size()) {
                ItemPedido item = pedido.getItens().get(itemIdx - 1);
                int restante = item.getQuantidade() - item.getQuantidadePaga();
                if (restante == 0) {
                    view.exibirMensagem("Este item ja foi totalmente pago.");
                } else {
                    int qtd = view.lerQuantidadeParaPagar(restante);
                    try {
                        item.pagarQuantidade(qtd);
                        view.exibirMensagem("Pago!");
                    } catch (Exception e) {
                        view.exibirMensagem("Erro: " + e.getMessage());
                    }
                }
            } else {
                view.exibirMensagem("Opcao invalida.");
            }

            if (pedido.estaTotalmentePago()) {
                view.exibirMensagem("Pedido totalmente pago!");
                break;
            }

            continuar = view.perguntarContinuarPagando();
        } while (continuar == 1);
    }

    private void pagarPorValor(Pedido pedido) {
        double totalEmAberto = pedido.totalEmAberto();
        double valorPagamento = view.lerValorPagamento(totalEmAberto);

        if (valorPagamento <= 0) {
            view.exibirMensagem("Valor invalido.");
            return;
        }

        if (valorPagamento >= totalEmAberto) {
            pagarTudo(pedido);
            return;
        }

        // Distribuir o valor pago proporcionalmente entre os itens
        double restante = valorPagamento;
        for (ItemPedido item : pedido.getItens()) {
            if (restante <= 0) break;

            int qtdNaoPaga = item.getQuantidade() - item.getQuantidadePaga();
            if (qtdNaoPaga <= 0) continue;

            double valorItem = item.getProduto().getPreco();
            int qtdAReceber = (int) (restante / valorItem);

            if (qtdAReceber > qtdNaoPaga) {
                qtdAReceber = qtdNaoPaga;
            }

            if (qtdAReceber > 0) {
                item.pagarQuantidade(qtdAReceber);
                restante -= (qtdAReceber * valorItem);
            }
        }

        view.exibirMensagem("Pagamento parcial realizado!");
        view.exibirMensagem("Valor restante a pagar: R$ " + String.format("%.2f", pedido.totalEmAberto()));

        if (pedido.estaTotalmentePago()) {
            view.exibirMensagem("Pedido totalmente pago!");
        } else {
            view.exibirMensagem("Status do pedido: " + pedido.status());
        }
    }
    //Adicionar o Estoque -------------------------------------
    private void fluxoSelecionarProdutoComEstoque(ArrayList<Produto> lista, Pedido pedido) {
        int idx = view.escolherProduto(lista, estoques);
        if (idx >= 1 && idx <= lista.size()) {
            Produto produto = lista.get(idx - 1);
            Estoque estoque = estoques.stream()
                    .filter(e -> e.getProduto().equals(produto))
                    .findFirst()
                    .orElse(null);

            if (estoque == null) {
                view.exibirMensagem("Produto sem estoque definido, item nao adicionado.");
                return;
            }

            int qtd = view.lerQuantidade();
            if (qtd <= 0) {
                view.exibirMensagem("Quantidade invalida, item nao adicionado.");
                return;
            }
            if (qtd > estoque.getQuantidade()) {
                view.exibirMensagem("Quantidade solicitada excede estoque disponivel.");
                return;
            }

            pedido.adicionarItem(new ItemPedido(produto, qtd));
            estoque.remover(qtd);
            view.exibirMensagem("Item adicionado!");
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    // ─── Utilitário ───────────────────────────────────────────────────────────

    private ArrayList<Pedido> filtrarPorStatus(String... statuses) {
        ArrayList<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            String s = p.status();
            for (String filtro : statuses) {
                if (s.equalsIgnoreCase(filtro)) {
                    resultado.add(p);
                    break;
                }
            }
        }
        return resultado;
    }

    private ArrayList<Produto> getTodosProdutos() {
        ArrayList<Produto> todos = new ArrayList<>(lanches);
        todos.addAll(bebidas);
        return todos;
    }
}
