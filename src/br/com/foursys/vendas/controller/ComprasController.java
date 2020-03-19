/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.model.Compra;
import br.com.foursys.vendas.model.Fornecedor;
import br.com.foursys.vendas.model.Funcionario;
import br.com.foursys.vendas.model.Produto;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.view.ComprasPrincipal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vfurtado
 */
public class ComprasController {

    private ComprasPrincipal viewCompras;
    private Compra compra = new Compra();
    private List<Produto> listaProdutos;
    private List<Fornecedor> listaFornecedores;
    private List<Funcionario> listaFuncionarios;

    public ComprasController() {

    }

    public ComprasController(ComprasPrincipal viewCompras) {
        this.viewCompras = viewCompras;
    }

    public void carregarComboFornecedor() {
        FornecedorController controller = new FornecedorController();
        try {
            listaFornecedores = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewCompras.getJcbFornecedor().removeAllItems();
        this.viewCompras.getJcbFornecedor().addItem(Mensagem.defaultComboFornecedor);
        for (Fornecedor fornecedor : listaFornecedores) {
            this.viewCompras.getJcbFornecedor().addItem(fornecedor.getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
        }
    }

    public void carregarComboFuncionario() {
        FuncionarioController controller = new FuncionarioController();
        try {
            listaFuncionarios = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewCompras.getJcbFuncionario().removeAllItems();
        this.viewCompras.getJcbFuncionario().addItem(Mensagem.defaultComboFuncionario);
        for (Funcionario fornecedor : listaFuncionarios) {
            this.viewCompras.getJcbFuncionario().addItem(fornecedor.getPessoaFisicaIdPessoaFisica().getNome());
        }
    }

    public void carregarComboProduto() {
        ProdutoController controller = new ProdutoController();
        listaProdutos = controller.buscarProdutos();
        this.viewCompras.getJcbProduto().removeAllItems();
        this.viewCompras.getJcbProduto().addItem(Mensagem.defaultComboProduto);
        for (Produto listaProduto : listaProdutos) {
            this.viewCompras.getJcbProduto().addItem(listaProduto.getDescricao());
        }
    }

    public void bloqueioInicial() {
        this.viewCompras.getJcbFuncionario().grabFocus();
        this.viewCompras.getJcbFuncionario().setEnabled(true);
        this.viewCompras.getJcbFornecedor().setEnabled(true);
        this.viewCompras.getJbtIniciarCompra().setEnabled(false);
        this.viewCompras.getJcbProduto().setEnabled(false);
        this.viewCompras.getJtfQuantidade().setEditable(false);
        this.viewCompras.getJtfDescontoProduto().setEditable(false);
        this.viewCompras.getJbtAdicionarProduto().setEnabled(false);
        this.viewCompras.getJbtExcluirProduto().setEnabled(false);
        this.viewCompras.getTabelaProdutos().setEnabled(false);
        this.viewCompras.getJcbFormaPagamento().setEnabled(false);
        this.viewCompras.getJtfDescontoPagamento().setEditable(false);
        this.viewCompras.getJbtIncluirFormaPagamento().setEnabled(false);
        this.viewCompras.getJbtExcluirFormaPagamento().setEnabled(false);
        this.viewCompras.getTabelaFormaPagamento().setEnabled(false);
        this.viewCompras.getJbtConfirmar().setEnabled(false);
        this.viewCompras.getJbtCancelar().setEnabled(false);
        this.viewCompras.getJbtSair().setEnabled(true);
    }

    public void liberaIniciarCompra() {
        if ((this.viewCompras.getJcbFuncionario().getSelectedIndex() != 0) && this.viewCompras.getJcbFornecedor().getSelectedIndex() != 0) {
            this.viewCompras.getJbtIniciarCompra().setEnabled(true);
        }
    }

    public void inserirProduto() {
        if (this.viewCompras.getJcbProduto().getSelectedIndex() != 0) {
            DefaultTableModel modelo = (DefaultTableModel) this.viewCompras.getTabelaProdutos().getModel();
            modelo.setRowCount(0);
            modelo.addRow(new String[]{this.viewCompras.getJcbProduto().getSelectedItem().toString()});
        } else {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        }
    }

    public void carregarFormaPagamento() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewCompras.getTabelaFormaPagamento().getModel();
        modelo.setRowCount(0);
        modelo.addRow(new String[]{this.viewCompras.getJcbFormaPagamento().getSelectedItem() + ""});
    }

    public void acaoBotaoIniciarCompra() {
        this.viewCompras.getJcbProduto().setEnabled(true);
        this.viewCompras.getJtfQuantidade().setEditable(true);
        this.viewCompras.getJtfDescontoProduto().setEditable(true);
        this.viewCompras.getJbtAdicionarProduto().setEnabled(true);
        this.viewCompras.getJbtExcluirProduto().setEnabled(true);
        this.viewCompras.getTabelaProdutos().setEnabled(true);
    }
}
