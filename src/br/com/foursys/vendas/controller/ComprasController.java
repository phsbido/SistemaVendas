package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.CompraDAO;
import br.com.foursys.vendas.model.Compra;
import br.com.foursys.vendas.model.Fornecedor;
import br.com.foursys.vendas.model.Funcionario;
import br.com.foursys.vendas.model.ItemCompra;
import br.com.foursys.vendas.model.Produto;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.view.ComprasPrincipal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Classe responsável por controlar todo o processamento de dados relacionados a
 * tela e a tabela de compras e item compras do banco de dados
 *
 * @authors vfurtado, pbido
 * @since 17/03/2020
 * @version 0.1
 */
public class ComprasController {

    private ComprasPrincipal viewCompras;
    private Compra compra = new Compra();
    private List<Produto> listaProdutos;
    private List<ItemCompra> listaItemCompra;
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

    //* Campos que estarao bloqueados ao abrir a tela
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

    //* Habilita o botão iniciar compra quando foi escolhido um fornecedor e um funcionário
    //* Falta fazer a validação pra caso o usuário habilite o botão e mude o que está selecionado na combo
    //* ideia: carregar o usuario como funcionário padrão e deixar desabilitado ?
    public void liberaIniciarCompra() {
        if ((this.viewCompras.getJcbFuncionario().getSelectedIndex() != 0) && this.viewCompras.getJcbFornecedor().getSelectedIndex() != 0) {
            this.viewCompras.getJbtIniciarCompra().setEnabled(true);
        }
    }

    //* Quando inserir um produto na tabela, adicionar o produto numa lista para poder salvar essa lista de produtos
    //  no banco de dados posteriormente
    public void inserirProduto() {
        if (this.viewCompras.getJcbProduto().getSelectedIndex() != 0) {
            DefaultTableModel modelo = (DefaultTableModel) this.viewCompras.getTabelaProdutos().getModel();
            modelo.setRowCount(0);
            modelo.addRow(new String[]{this.viewCompras.getJcbProduto().getSelectedItem().toString()});
            ItemCompra itemCompra = new ItemCompra();
            Produto produto = listaProdutos.get(this.viewCompras.getJcbProduto().getSelectedIndex() - 1);
            int quantidade = Integer.parseInt(this.viewCompras.getJtfQuantidade() + "");
            Double desconto = Double.parseDouble(this.viewCompras.getJtfDescontoProduto() + "");
            Double valorTotal = ((produto.getValorCusto() * quantidade) - desconto);
            itemCompra.setProdutoIdProduto(produto);
            itemCompra.setCompraIdCompra(compra);
            itemCompra.setQuantidadeProduto(quantidade + "");
            itemCompra.setValorTotal(valorTotal);
            listaItemCompra.add(itemCompra);
            habilitarFormaPagamento();
        } else {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        }
    }

//    public void salvarCompra() {
//        CompraDAO dao = new CompraDAO();
//        compra.set
//        for (ItemCompra itemCompra : listaItemCompra) {
//          
//        }
//    }

    //* assim como no inserir, o excluir deve remover o produto selecionado na tabela da lista na qual estamos
    //  salvando todos os produtos inseridos
    //* ideia: o método deveria se chamar remover produto já que não estamos fazendo uma exclusão, mas apenas removendo
    //  da tabela para que ele não seja constado na compra
    public void excluirProduto() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewCompras.getTabelaProdutos().getModel();
        if (this.viewCompras.getTabelaProdutos().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        } else {
            modelo.removeRow(this.viewCompras.getTabelaProdutos().getSelectedRow());
            desabilitarFormaPagamento();
        }

    }

    public void habilitarFormaPagamento() {
        if (this.viewCompras.getTabelaProdutos().getRowCount() > 0) {
            this.viewCompras.getJcbFormaPagamento().setEnabled(true);
            this.viewCompras.getJtfDescontoPagamento().setEditable(true);
            this.viewCompras.getJbtIncluirFormaPagamento().setEnabled(true);
            this.viewCompras.getJbtExcluirFormaPagamento().setEnabled(true);
            this.viewCompras.getTabelaFormaPagamento().setEnabled(true);
        }
    }

    public void desabilitarFormaPagamento() {
        if (this.viewCompras.getTabelaProdutos().getRowCount() == 0) {
            this.viewCompras.getJcbFormaPagamento().setEnabled(false);
            this.viewCompras.getJtfDescontoPagamento().setEditable(false);
            this.viewCompras.getJbtIncluirFormaPagamento().setEnabled(false);
            this.viewCompras.getJbtExcluirFormaPagamento().setEnabled(false);
            this.viewCompras.getTabelaFormaPagamento().setEnabled(false);
        }
    }

    //* ideia: aqui como podemos adicionar um unico registro à tabela, ela pode ser apenas visual enquanto usamos
    //  o que está selecionado na combobox para salvar
    public void inserirFormaPagamento() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewCompras.getTabelaFormaPagamento().getModel();
        modelo.setRowCount(0);
        modelo.addRow(new String[]{this.viewCompras.getJcbFormaPagamento().getSelectedItem() + ""});
    }

    public void acaoBotaoIniciarCompra() {
        this.viewCompras.getJcbProduto().setEnabled(true);
        this.viewCompras.getJbtCancelar().setEnabled(true);
        this.viewCompras.getJtfQuantidade().setEditable(true);
        this.viewCompras.getJtfDescontoProduto().setEditable(true);
        this.viewCompras.getJbtAdicionarProduto().setEnabled(true);
        this.viewCompras.getJbtExcluirProduto().setEnabled(true);
        this.viewCompras.getTabelaProdutos().setEnabled(true);
        compra = new Compra();
    }
}
