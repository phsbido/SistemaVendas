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
}
