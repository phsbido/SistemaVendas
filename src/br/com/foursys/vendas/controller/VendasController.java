package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.VendaDAO;
import br.com.foursys.vendas.dao.ContasReceberDAO;
import br.com.foursys.vendas.dao.ItemVendaDAO;
import br.com.foursys.vendas.model.Venda;
import br.com.foursys.vendas.model.ContasReceber;
import br.com.foursys.vendas.model.Cliente;
import br.com.foursys.vendas.model.Estoque;
import br.com.foursys.vendas.model.Funcionario;
import br.com.foursys.vendas.model.ItemVenda;
import br.com.foursys.vendas.model.Produto;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.util.Valida;
import br.com.foursys.vendas.view.VendasPrincipal;
import br.com.foursys.vendas.view.ConfirmarContasReceber;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Classe responsável por controlar todo o processamento de dados relacionados a
 * tela e a tabela de vendas e item vendas do banco de dados
 *
 * @authors vfurtado, pbido
 * @since 17/03/2020
 * @version 0.1
 */
public class VendasController {

    private VendasPrincipal viewVendas;
    private Venda venda = new Venda();
    private List<Estoque> listaEstoque;
    private List<Produto> listaProdutos;
    private List<ItemVenda> listaItemVenda = new ArrayList<>();
    private List<Cliente> listaClientees;
    private List<Funcionario> listaFuncionarios;
    private int quantidade = 0;

    public VendasController() {

    }

    public VendasController(VendasPrincipal viewVendas) {
        this.viewVendas = viewVendas;
    }

    public void carregarComboCliente() {
        ClienteController controller = new ClienteController();
        try {
            listaClientees = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbCliente().removeAllItems();
        this.viewVendas.getJcbCliente().addItem(Mensagem.defaultComboCliente);
        for (Cliente fornecedor : listaClientees) {
            if (fornecedor.getTipoPessoa().equals("J")) {
                this.viewVendas.getJcbCliente().addItem(fornecedor.getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
            } else {
                this.viewVendas.getJcbCliente().addItem(fornecedor.getPessoaFisicaIdPessoaFisica().getNome());
            }
        }
    }

    public void carregarComboFuncionario() {
        FuncionarioController controller = new FuncionarioController();
        try {
            listaFuncionarios = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbFuncionario().removeAllItems();
        this.viewVendas.getJcbFuncionario().addItem(Mensagem.defaultComboFuncionario);
        for (Funcionario fornecedor : listaFuncionarios) {
            this.viewVendas.getJcbFuncionario().addItem(fornecedor.getPessoaFisicaIdPessoaFisica().getNome());
        }
    }

    public void carregarComboProduto() {
        EstoqueController controller = new EstoqueController();
        listaEstoque = controller.buscarEstoque();
        this.viewVendas.getJcbProduto().removeAllItems();
        this.viewVendas.getJcbProduto().addItem(Mensagem.defaultComboProduto);
        for (Estoque estoque : listaEstoque) {
            this.viewVendas.getJcbProduto().addItem(estoque.getProdutoIdProduto().getDescricao());
        }
    }

    //* Campos que estarao bloqueados ao abrir a tela
    public void bloqueioInicial() {
        this.viewVendas.getJcbFuncionario().grabFocus();
        this.viewVendas.getJcbFuncionario().setEnabled(true);
        this.viewVendas.getJcbCliente().setEnabled(true);
        this.viewVendas.getJbtIniciarVenda().setEnabled(false);
        this.viewVendas.getJcbProduto().setEnabled(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);
        this.viewVendas.getTabelaProduto().setEnabled(false);
        this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoPagamento().setEditable(false);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getTabelaPagamento().setEnabled(false);
        this.viewVendas.getJbtConfirmar().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJbtSair().setEnabled(true);
    }

    //* Habilita o botão iniciar venda quando foi escolhido um fornecedor e um funcionário
    //* Falta fazer a validação pra caso o usuário habilite o botão e mude o que está selecionado na combo
    //* ideia: carregar o usuario como funcionário padrão e deixar desabilitado ?
    public void liberaIniciarVenda() {
        if ((this.viewVendas.getJcbFuncionario().getSelectedIndex() != 0) && this.viewVendas.getJcbCliente().getSelectedIndex() != 0) {
            this.viewVendas.getJbtIniciarVenda().setEnabled(true);
        } else {
            this.viewVendas.getJbtIniciarVenda().setEnabled(false);
        }
        if (!this.viewVendas.getJbtIniciarVenda().isEnabled()) {
            desabilitaProduto();
        }
    }
//Metodo para liberar o Adicionar produto

    public void liberarAdicionarProduto() {
        if (!Valida.verificarCombo(this.viewVendas.getJcbProduto().getSelectedIndex()) && (!Valida.verificarVazio(this.viewVendas.getJtfQuantidade().getText())) && (!Valida.verificarVazio(this.viewVendas.getJtfDescontoProduto().getText()))) {
            this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        } else {
            this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        }
        if (!this.viewVendas.getJbtAdicionarProduto().isEnabled()) {
            desabilitarFormaDePagamento();
        }
    }

    public void liberarFormaDePagamento() {
        if (!Valida.verificarCombo(this.viewVendas.getJcbFormaDePagamento().getSelectedIndex())) {
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
        } else {
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        }
    }

    //* Quando inserir um produto na tabela, adicionar o produto numa lista para poder salvar essa lista de produtos
    //  no banco de dados posteriormente
    public void inserirProduto() {
        if (this.viewVendas.getJcbProduto().getSelectedIndex() != 0) {
            if (validarProduto()) {
                Estoque estoque = listaEstoque.get(this.viewVendas.getJcbProduto().getSelectedIndex() - 1);

                if (!Valida.verificarVazio(this.viewVendas.getJtfQuantidade().getText())) {
                    quantidade = Integer.parseInt(this.viewVendas.getJtfQuantidade().getText());
                }
                Double desconto = 0.0;
                if (!Valida.verificarVazio(this.viewVendas.getJtfDescontoProduto().getText())) {
                    desconto = Double.parseDouble(this.viewVendas.getJtfDescontoProduto().getText());
                }
                if (Valida.quantidadeEstoque(Integer.parseInt(this.viewVendas.getJtfQuantidade().getText()), estoque)) {
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueIndisponivel);
                    this.viewVendas.getJtfQuantidade().grabFocus();
                } else {
                    Double valorTotal = ((estoque.getProdutoIdProduto().getValorCusto() * quantidade) - desconto);
                    if (valorTotal > 0) {
                        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
                        modelo.addRow(new String[]{this.viewVendas.getJcbProduto().getSelectedItem().toString(), quantidade + " UN", "R$ " + estoque.getProdutoIdProduto().getValorCusto(), "R$ " + this.viewVendas.getJtfDescontoProduto().getText(), "R$ " + valorTotal});
                        ItemVenda itemVenda = new ItemVenda();
                        itemVenda.setProdutoIdProduto(estoque.getProdutoIdProduto());
                        itemVenda.setVendaIdVenda(venda);
                        itemVenda.setQuantidadeProduto(quantidade);
                        itemVenda.setValorTotal(valorTotal);
                        listaItemVenda.add(itemVenda);
                        ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
                        itemVendaDAO.salvar(itemVenda);
                        habilitarFormaDePagamento();
                        campoValorTotal();
                    } else {
                        JOptionPane.showMessageDialog(null, Mensagem.valorMenorQueZero);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
            }
        }
    }
    //* assim como no inserir, o excluir deve remover o produto selecionado na tabela da lista na qual estamos
    //  salvando todos os produtos inseridos
    //* ideia: o método deveria se chamar remover produto já que não estamos fazendo uma exclusão, mas apenas removendo
    //  da tabela para que ele não seja constado na venda

    public void excluirProduto() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
        if (this.viewVendas.getTabelaProduto().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        } else {
            listaItemVenda.remove(this.viewVendas.getTabelaProduto().getSelectedRow());
            modelo.removeRow(this.viewVendas.getTabelaProduto().getSelectedRow());
            desabilitarFormaDePagamento();
            campoValorTotal();
        }

    }

    public void habilitarFormaDePagamento() {
        if (this.viewVendas.getTabelaProduto().getRowCount() > 0) {
            this.viewVendas.getJcbFormaDePagamento().setEnabled(true);
            this.viewVendas.getJcbFormaDePagamento().grabFocus();
            this.viewVendas.getJtfDescontoPagamento().setEditable(true);
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
            this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(true);
            this.viewVendas.getTabelaPagamento().setEnabled(true);
        }
    }

    public void desabilitarFormaDePagamento() {
        if (this.viewVendas.getTabelaProduto().getRowCount() == 0) {
            this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
            this.viewVendas.getJtfDescontoPagamento().setEditable(false);
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
            this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
            this.viewVendas.getTabelaPagamento().setEnabled(false);
        }
    }

    //* ideia: aqui como podemos adicionar um unico registro à tabela, ela pode ser apenas visual enquanto usamos
    //  o que está selecionado na combobox para salvar
    // inserir forma de pagamento
    public void inserirFormaDePagamento() {
        if (this.viewVendas.getJcbFormaDePagamento().getSelectedIndex() != 0) {
            DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
            modelo.setRowCount(0);
            modelo.addRow(new String[]{this.viewVendas.getJcbFormaDePagamento().getSelectedItem().toString()});
            habilitaConfirmar();
        } else {
            JOptionPane.showMessageDialog(null, Mensagem.pagamentoNaoSelecionado);
        }
    }

    public void removerFormaDePagamento() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
        if (this.viewVendas.getTabelaPagamento().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        } else {
            modelo.removeRow(this.viewVendas.getTabelaPagamento().getSelectedRow());
            habilitaConfirmar();
        }

    }

    //* o botão iniciar venda não pode ser pressionado mais de uma vez em uma venda já que ele gera um registro no banco
    //  nao podemos dar essa permissão pro usuário.
    public void acaoBotaoIniciarVenda() {
        desabilitaDados();
        habilitaProduto();
        Cliente fornecedor = listaClientees.get(this.viewVendas.getJcbCliente().getSelectedIndex() - 1);
        Funcionario funcionario = listaFuncionarios.get(this.viewVendas.getJcbFuncionario().getSelectedIndex() - 1);
        venda = new Venda();
        venda.setDataVenda(LocalDate.now() + "");
        venda.setValorTotal(0);
        venda.setFormaPagamento("0");
        venda.setClienteIdCliente(fornecedor);
        venda.setFuncionarioIdFuncionario(funcionario);
        VendaDAO vendaDAO = new VendaDAO();
        vendaDAO.salvar(venda);
        liberarAdicionarProduto();
    }

    public void desabilitaDados() {
        this.viewVendas.getJcbCliente().setEnabled(false);
        this.viewVendas.getJcbFuncionario().setEnabled(false);
        this.viewVendas.getJbtIniciarVenda().setEnabled(false);
    }

    public void habilitaProduto() {
        this.viewVendas.getJcbProduto().setEnabled(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        this.viewVendas.getJbtExcluirProduto().setEnabled(true);
        this.viewVendas.getTabelaProduto().setEnabled(true);
    }

    public void desabilitaProduto() {
        this.viewVendas.getJcbProduto().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);
        this.viewVendas.getTabelaProduto().setEnabled(false);
    }

    public void campoValorTotal() {
        Double valorTotal = 0.0;
        Double desconto = 0.0;
        try {
            desconto = Double.parseDouble(this.viewVendas.getJtfDescontoPagamento().getText());
        } catch (NumberFormatException e) {
            this.viewVendas.getJtfDescontoPagamento().setText("0.0");
        }
        if (desconto < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.descontoMenorQueZero);
        }
        for (ItemVenda itemVenda : listaItemVenda) {
            valorTotal += itemVenda.getValorTotal();
        }
        if ((valorTotal - desconto) < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.valorMenorQueZero);
        } else {
            this.viewVendas.getJlbValorTotal().setText("R$ " + (valorTotal - desconto));
        }
    }

    public void acaoBotaoCancelar() {
        int x = JOptionPane.showConfirmDialog(null, Mensagem.confirmaCancelar, Mensagem.atencao,
                JOptionPane.YES_NO_OPTION);
        if ((x == JOptionPane.YES_OPTION)) {
            //* limpar os campos do painel forma de pagamento, produto e dados da venda
            limparDados();
            limparProduto();
            limparFormaDePagamento();
            //* excluir a venda gerada pelo botão iniciar venda
            cancelarVenda();
            //* voltar a tela ao estado inicial igual quando foi aberta
            bloqueioInicial();
        }
    }

    public void limparDados() {
        carregarComboCliente();
        carregarComboFuncionario();
    }

    public void limparProduto() {
        carregarComboProduto();
        this.viewVendas.getJtfQuantidade().setText("0");
        this.viewVendas.getJtfDescontoProduto().setText("0.0");
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
        modelo.setRowCount(0);
        listaItemVenda.clear();
        this.viewVendas.getJlbValorTotal().setText("0.0");
    }

    public void limparFormaDePagamento() {
        this.viewVendas.getJcbFormaDePagamento().setSelectedIndex(0);
        this.viewVendas.getJtfDescontoPagamento().setText("0.0");
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
        modelo.setRowCount(0);
    }

    public void cancelarVenda() {
        VendaDAO dao = new VendaDAO();
        new ItemVendaController().excluirItemVenda(venda);
        try {
            dao.excluir(venda);
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void habilitaConfirmar() {
        if (this.viewVendas.getTabelaPagamento().getRowCount() > 0) {
            this.viewVendas.getJbtConfirmar().setEnabled(true);
        } else {
            this.viewVendas.getJbtConfirmar().setEnabled(false);
        }

    }

    public void salvar() {
        EstoqueController estoqueController = new EstoqueController();
        if ((this.viewVendas.getJcbFormaDePagamento().getSelectedItem().equals("Dinheiro")) || (this.viewVendas.getJcbFormaDePagamento().getSelectedItem().equals("Débito"))) {
            ItemVendaDAO dao = new ItemVendaDAO();
            VendaDAO vendaDAO = new VendaDAO();
            ContasReceberDAO contasPagarDAO = new ContasReceberDAO();
            ContasReceber conta = new ContasReceber();
            conta.setVendaIdVenda(venda);
            conta.setDataPagamento(LocalDate.now() + "");
            conta.setDataVencimento(LocalDate.now() + "");
            conta.setPagamento("Sim");
            conta.setVencida("Não");
            contasPagarDAO.salvar(conta);
            for (Estoque estoque : listaEstoque) {
                estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() - quantidade);
                estoqueController.salvarEstoqueDAO(estoque);
            }
            venda.setFormaPagamento(this.viewVendas.getJcbFormaDePagamento().getSelectedItem().toString());
            venda.setValorTotal(Double.parseDouble(this.viewVendas.getJlbValorTotal().getText().replace("R$ ", "")));
            try {
                vendaDAO.salvar(venda);
                LoginController.verificaLog(Mensagem.salvar, Mensagem.tabelaVendas);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, Mensagem.erroSalvarVenda);
            }
            JOptionPane.showMessageDialog(null, Mensagem.sucessoSalvarVenda);
            limparDados();
            limparProduto();
            limparFormaDePagamento();
            bloqueioInicial();
        } else if ((this.viewVendas.getJcbFormaDePagamento().getSelectedItem().equals("Cheque")) || (this.viewVendas.getJcbFormaDePagamento().getSelectedItem().equals("Crédito"))) {
            ItemVendaDAO dao = new ItemVendaDAO();
            VendaDAO vendaDAO = new VendaDAO();
            venda.setFormaPagamento(this.viewVendas.getJcbFormaDePagamento().getSelectedItem().toString());
            venda.setValorTotal(Double.parseDouble(this.viewVendas.getJlbValorTotal().getText().replace("R$ ", "")));
            for (Estoque estoque : listaEstoque) {
                estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() - quantidade);
                estoqueController.salvarEstoqueDAO(estoque);
            }
            try {
                vendaDAO.salvar(venda);
                LoginController.verificaLog(Mensagem.salvar, Mensagem.tabelaVendas);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, Mensagem.erroSalvarVenda);
            }
            new ConfirmarContasReceber(venda);
            this.viewVendas.dispose();
        }
    }

    public boolean validarProduto() {
        if (!Valida.validarNumero(this.viewVendas.getJtfQuantidade().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.numeroInvalido);
            this.viewVendas.getJtfQuantidade().grabFocus();
            return false;
        } else if (Valida.verificarVazio(this.viewVendas.getJtfQuantidade().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.numeroInvalido);
            this.viewVendas.getJtfQuantidade().grabFocus();
            return false;
        } else if (Valida.verificaQuantidade(Integer.parseInt(this.viewVendas.getJtfQuantidade().getText()))) {
            JOptionPane.showMessageDialog(null, Mensagem.numeroInvalido);
            this.viewVendas.getJtfQuantidade().grabFocus();
            return false;

        }

        return true;
    }

}//fim da classe
