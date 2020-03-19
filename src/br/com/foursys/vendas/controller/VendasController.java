package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.ProdutoDAO;
import br.com.foursys.vendas.model.Cliente;
import br.com.foursys.vendas.model.Funcionario;
import br.com.foursys.vendas.model.Produto;
import br.com.foursys.vendas.model.Venda;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.view.VendasPrincipal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ecioffi
 */
public class VendasController {

    private VendasPrincipal viewVendas;
    private Venda venda = new Venda();
    private List<Cliente> listaClientes;
    private List<Funcionario> listaFuncionarios;
    private List<Produto> listaProdutos;
    private List<Funcionario> listaFuncionario;
    private boolean alterar;
    private double valorTotal;

    public VendasController() {

    }

    public VendasController(VendasPrincipal viewVendas) {
        this.viewVendas = viewVendas;
    }

    public void carregarCliente() {
        ClienteController controller = new ClienteController();
        try {
            listaClientes = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbCliente().removeAllItems();
        this.viewVendas.getJcbCliente().addItem(Mensagem.defaultComboCliente);
        for (Cliente cliente : listaClientes) {
            this.viewVendas.getJcbCliente().addItem(cliente.getPessoaFisicaIdPessoaFisica().getNome());
            this.viewVendas.getJcbCliente().addItem(cliente.getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
        }
    }

    public void carregarFuncionario() {
        FuncionarioController controller = new FuncionarioController();
        try {
            listaFuncionario = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbFuncionario().removeAllItems();
        this.viewVendas.getJcbFuncionario().addItem(Mensagem.defaultComboFuncionario);
        for (Funcionario funcionario : listaFuncionarios) {
            this.viewVendas.getJcbCliente().addItem(funcionario.getPessoaFisicaIdPessoaFisica().getNome());

        }
    }

    public void carregarProduto() {
        ProdutoController controller = new ProdutoController();
        try {
            listaProdutos = controller.buscarProdutos();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbProduto().removeAllItems();
        this.viewVendas.getJcbProduto().addItem(Mensagem.defaultComboFornecedor);
        for (Produto produto : listaProdutos) {
            this.viewVendas.getJcbProduto().addItem(produto.getDescricao());

        }
    }

    public void listarProdutos() {
        try {
            ProdutoDAO dao = new ProdutoDAO();
            listaProdutos = dao.buscarTodos();
            carregarTabela();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Não tem porque carregar tabela nessa tela, favor verificar nome dos métodos.
    public void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
        modelo.setRowCount(0);
        for (Produto produto : listaProdutos) {
            valorTotal = (produto.getValorVenda() * Integer.parseInt(this.viewVendas.getJtfQuantidade().getText())) - Integer.parseInt(this.viewVendas.getJtfDescontoProduto().getText());
            modelo.addRow(new String[]{produto.getDescricao(), this.viewVendas.getJtfQuantidade().getText(), produto.getValorVenda() + "", this.viewVendas.getJtfDescontoProduto().getText(), valorTotal + ""});
        }

    }

    public void bloqueioInicial() {

        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbProduto().setEnabled(false);

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

    //Métodos com ambiguidade e sem descrição, favor mudar o nome e/ou adicionar comentários.
    public void liberarCampos1() {

        this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJcbProduto().setEnabled(true);
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
        this.viewVendas.getJcbProduto().setSelectedIndex(0);
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
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);

        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbProduto().setEnabled(false);

        limparCampos();
        bloquearCampos();
    }

}
