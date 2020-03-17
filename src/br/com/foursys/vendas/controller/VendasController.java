package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.model.Venda;
import br.com.foursys.vendas.view.VendasPrincipal;
import java.util.List;

/**
 *
 * @author ecioffi
 */
public class VendasController {

    private VendasPrincipal viewVendas;
    private Venda venda = new Venda();
    private List<Venda> listaVendas;

    public VendasController() {

    }

    public VendasController(VendasPrincipal viewVendas) {
        this.viewVendas = viewVendas;
    }

    public void bloqueioInicial() {

        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtAlterarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbDescricao().setEnabled(false);

        bloquearCampos();
    }

    public void bloquearCampos() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(false);
        this.viewVendas.getJbtConfirmar().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);

    }

    public void liberarCampos1() {

        this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        this.viewVendas.getJbtAlterarProduto().setEnabled(true);
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJcbDescricao().setEnabled(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);
        this.viewVendas.getJbtExcluirProduto().setEnabled(true);

    }

    public void liberarCampos2() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(true);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(true);
        this.viewVendas.getJbtConfirmar().setEnabled(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);

    }

    public void limparCampos() {
        this.viewVendas.getJtfQuantidade().setText(null);
        this.viewVendas.getJtfDescontoProduto().setText(null);
        this.viewVendas.getJtfDescontoFormaDePagamento().setText(null);
        this.viewVendas.getJcbCliente().setSelectedIndex(0);
        this.viewVendas.getJcbFuncionario().setSelectedIndex(0);
        this.viewVendas.getJcbDescricao().setSelectedIndex(0);
        this.viewVendas.getJcbFormaDePagamento().setSelectedIndex(0);
        this.viewVendas.getJcbCliente().setSelectedIndex(0);

    }

    public void acaoBotaoCancelar() {

        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(false);
        this.viewVendas.getJbtConfirmar().setEnabled(false);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtAlterarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);

        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbDescricao().setEnabled(false);

        limparCampos();
        bloquearCampos();
    }

}
