package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.VendaDAO;
import br.com.foursys.vendas.model.Cliente;
import br.com.foursys.vendas.model.Funcionario;
import br.com.foursys.vendas.model.ItemVenda;
import br.com.foursys.vendas.model.Produto;
import br.com.foursys.vendas.model.Venda;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.util.Valida;
import br.com.foursys.vendas.view.VendasPrincipal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fcorrea
 *
 */
public class VendasController {

    private VendasPrincipal viewVendas;
    private Venda venda = new Venda();
    private List<Cliente> listaClientes;
    private List<Funcionario> listaFuncionarios;
    private List<Produto> listaProdutos;
    private List<Funcionario> listaFuncionario;
    private List<ItemVenda> listaItemVenda = new ArrayList<>();

    private boolean alterar;

    public VendasController() {

    }

    public VendasController(VendasPrincipal viewVendas) {
        this.viewVendas = viewVendas;
    }

    //Método acaoBotaoIniciarVenda    
    public void acaoBotaoIniciarVenda() {
        habilitarProduto();
        Cliente cliente = listaClientes.get(this.viewVendas.getJcbCliente().getSelectedIndex() - 1);
        Funcionario funcionario = listaFuncionarios.get(this.viewVendas.getJcbFuncionario().getSelectedIndex() - 1);
        venda = new Venda();
        venda.setDataVenda(LocalDate.now() + "");
        venda.setValorTotal(0);
        venda.setFormaPagamento("0");
        venda.setClienteIdCliente(cliente);
        venda.setFuncionarioIdFuncionario(funcionario);
        VendaDAO vendaDAO = new VendaDAO();
        vendaDAO.salvar(venda);

    }

    //Método inserirProduto
    public void inserirProduto() {
        if (this.viewVendas.getJcbProduto().getSelectedIndex() != 0) {
            Produto produto = listaProdutos.get(this.viewVendas.getJcbProduto().getSelectedIndex() - 1);
            int quantidade = 0;
            if (!Valida.verificarVazio(this.viewVendas.getJtfQuantidade().getText())) {
                quantidade = Integer.parseInt(this.viewVendas.getJtfQuantidade().getText());
            }
            Double desconto = 0.0;
            if (!Valida.verificarVazio(this.viewVendas.getJtfDescontoProduto().getText())) {
                desconto = Double.parseDouble(this.viewVendas.getJtfDescontoProduto().getText());
            }
            Double valorTotal = ((produto.getValorCusto() * quantidade) - desconto);
            DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
            modelo.setRowCount(0);
            modelo.addRow(new String[]{this.viewVendas.getJcbProduto().getSelectedItem().toString(), quantidade + " UN", "R$ " + produto.getValorCusto(), "R$ " + this.viewVendas.getJtfDescontoProduto().getText(), "R$ " + valorTotal});
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setProdutoIdProduto(produto);
            itemVenda.setVendaIdVenda(venda);
            itemVenda.setQuantidadeProduto(quantidade);
            itemVenda.setValorTotal(valorTotal);
            listaItemVenda.add(itemVenda);
            habilitarFormaPagamento();
            liberarIncluirFormaDePagamento();
        } else {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        }
    }

//Método excluirProduto
    public void excluirProduto() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
        if (this.viewVendas.getTabelaProduto().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
        } else {
            modelo.removeRow(this.viewVendas.getTabelaProduto().getSelectedRow());
            desabilitarFormaPagamento();
        }
    }

// Método inserirFormaPagamento
    public void inserirFormaPagamento() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
        modelo.setRowCount(0);
        modelo.addRow(new String[]{this.viewVendas.getJcbFormaDePagamento().getSelectedItem() + ""});
    }

// Método excluirFormaPagamento
    public void excluirFormaPagamento() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
        if (this.viewVendas.getTabelaPagamento().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.pagamentoNaoSelecionado);
        } else {
            modelo.removeRow(this.viewVendas.getTabelaPagamento().getSelectedRow());
            desabilitarFormaPagamento();
        }
    }

    // Método habilitarFormaPagamento
    public void habilitarFormaPagamento() {
        if (this.viewVendas.getTabelaProduto().getRowCount() > 0) {
            this.viewVendas.getJcbFormaDePagamento().setEnabled(true);
            this.viewVendas.getJcbFormaDePagamento().grabFocus();
            this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(true);
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
            this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(true);
            this.viewVendas.getTabelaPagamento().setEnabled(true);
        }
    }

// Método dasabilitarFormaPagamento
    public void desabilitarFormaPagamento() {
        if (this.viewVendas.getTabelaProduto().getRowCount() == 0) {
            this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
            this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(false);
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
            this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
            this.viewVendas.getTabelaPagamento().setEnabled(false);
        }
    }

    // Método habilitaProduto   
    public void habilitarProduto() {
        this.viewVendas.getJcbProduto().setEnabled(true);
        this.viewVendas.getJcbProduto().grabFocus();
        this.viewVendas.getJbtCancelar().setEnabled(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        this.viewVendas.getJbtExcluirProduto().setEnabled(true);
        this.viewVendas.getTabelaProduto().setEnabled(true);
    }

// Método dasabilitarProduto
    public void desabilitarProduto() {
        this.viewVendas.getJcbProduto().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);
        this.viewVendas.getTabelaProduto().setEnabled(false);
    }

    //metodo validarSalvar
    public boolean validarSalvar() {
        if (Valida.verificarVazio(this.viewVendas.getJtfQuantidade().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoQuantidadeVazia, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewVendas.getJtfQuantidade().grabFocus();
            return false;
        } else if (!Valida.validarNumero(this.viewVendas.getJtfQuantidade().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoInsuficiente, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewVendas.getJtfQuantidade().grabFocus();
            return false;
        } else if (!Valida.validarNumero(this.viewVendas.getJtfDescontoProduto().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoDescontoInvalido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewVendas.getJtfDescontoProduto().grabFocus();
            return false;
        } else if (!Valida.validarNumero(this.viewVendas.getJtfDescontoFormaDePagamento().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoDescontoInvalido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewVendas.getJtfDescontoFormaDePagamento().grabFocus();
            return false;
        } else if (Valida.vereficaNaoZero(this.viewVendas.getJlbValorTotal().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.produtoValorTotalInvalido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewVendas.getJlbValorTotal().grabFocus();
            return false;
        }
        return true;
    }

//Método que carrega a combo cliente
    public void carregarComboCliente() {
        ClienteController controller = new ClienteController();
        try {
            listaClientes = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbCliente().removeAllItems();
        this.viewVendas.getJcbCliente().addItem(Mensagem.defaultComboCliente);
        for (Cliente cliente : listaClientes) {
            if (cliente.getTipoPessoa().equals("F")) {
                this.viewVendas.getJcbCliente().addItem(cliente.getPessoaFisicaIdPessoaFisica().getNome());
            } else {
                this.viewVendas.getJcbCliente().addItem(cliente.getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
            }
        }
    }

    //Metodo que insere forma de pagamento na tabela
    public void inserirFormaPagamentoTabela() {
        DefaultTableModel modeloTelaPagamento = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
        modeloTelaPagamento.setRowCount(0);
        modeloTelaPagamento.addRow(new String[]{this.viewVendas.getJcbFormaDePagamento().getSelectedItem().toString()});
    }

    //método que carrega a combo Funcionário
    public void carregarComboFuncionario() {
        FuncionarioController controller = new FuncionarioController();
        try {
            listaFuncionarios = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbFuncionario().removeAllItems();
        this.viewVendas.getJcbFuncionario().addItem(Mensagem.defaultComboFuncionario);
        for (Funcionario funcionario : listaFuncionarios) {
            this.viewVendas.getJcbFuncionario().addItem(funcionario.getPessoaFisicaIdPessoaFisica().getNome());
        }
    }

    //Método que carrega a combo Produto
    public void carregarComboProduto() {
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

    // método de bloqueio inicial da tela
    public void bloqueioInicial() {

        this.viewVendas.getJcbCliente().setEnabled(true);
        this.viewVendas.getJcbCliente().grabFocus();
        this.viewVendas.getJcbFuncionario().setEnabled(true);
        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbProduto().setEnabled(false);
        this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(false);
        this.viewVendas.getJbtConfirmar().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);
        this.viewVendas.getTabelaPagamento().setEnabled(false);
        this.viewVendas.getTabelaProduto().setEnabled(false);
        this.viewVendas.getJbtIniciarVenda().setEnabled(false);
    }

    //método para liberar o campos de produto
    public void liberarCampoProduto() {

        this.viewVendas.getJcbProduto().setEnabled(true);
        this.viewVendas.getJcbProduto().grabFocus();
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);
        this.viewVendas.getJbtExcluirProduto().setEnabled(true);
        this.viewVendas.getTabelaProduto().setEnabled(true);

    }

    //metodo para limpar todos os campos

    public void limparCampos() {
        this.viewVendas.getJcbCliente().setSelectedIndex(0);
        this.viewVendas.getJcbFuncionario().setSelectedIndex(0);
        this.viewVendas.getJcbProduto().setSelectedIndex(0);
        this.viewVendas.getJtfQuantidade().setText(null);
        this.viewVendas.getJtfDescontoProduto().setText(null);
        DefaultTableModel modeloTelaProduto = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
        modeloTelaProduto.setRowCount(0);
        this.viewVendas.getJcbFormaDePagamento().setSelectedIndex(0);
        this.viewVendas.getJtfDescontoFormaDePagamento().setText(null);
        this.viewVendas.getJlbValorTotal().setText("0.00");
        DefaultTableModel modeloTelaPagamento = (DefaultTableModel) this.viewVendas.getTabelaPagamento().getModel();
        modeloTelaPagamento.setRowCount(0);

    }

//Metodo para liberar a venda
    public void liberarIniciarVenda() {
        if ((this.viewVendas.getJcbFuncionario().getSelectedIndex() > 0) && this.viewVendas.getJcbCliente().getSelectedIndex() > 0) {
            this.viewVendas.getJbtIniciarVenda().setEnabled(true);
        } else {
            this.viewVendas.getJbtIniciarVenda().setEnabled(false);
        }
        if (!this.viewVendas.getJbtIniciarVenda().isEnabled()) {
            desabilitarProduto();
        }
    }

    public void liberarAdicionarProduto() {
        if ((!Valida.verificarVazio(this.viewVendas.getJtfQuantidade().getText()))
                && (!Valida.verificarVazio(this.viewVendas.getJtfDescontoProduto().getText()))
                && (!Valida.verificarCombo(this.viewVendas.getJcbProduto().getSelectedIndex()))) {
            this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        } else {
            this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
        }
        if (!this.viewVendas.getJbtAdicionarProduto().isEnabled()) {
            desabilitaAdicionarProduto();

        }
    }

//Metodo para liberar as Formas de pagamento
    public void liberarIncluirFormaDePagamento() {
        if (!Valida.verificarVazio(this.viewVendas.getJtfDescontoFormaDePagamento().getText())) {
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
        } else {
            this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        }
        if (!this.viewVendas.getJbtIncluirFormaDePagamento().isEnabled()) {
            desabilitaFormaDePagamento();

        }
    }

    //método para liberar campos de forma de pagamento
    public void liberarCampoFormaPagamento() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(true);
        this.viewVendas.getJcbFormaDePagamento().grabFocus();

        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(true);
        this.viewVendas.getJbtConfirmar().setEnabled(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);

    }

    public void desabilitaFormaDePagamento() {
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
    }

    public void desabilitaProduto() {

        this.viewVendas.getJcbProduto().setEnabled(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);
        this.viewVendas.getTabelaProduto().setEnabled(false);
    }

    public void desabilitaAdicionarProduto() {
        this.viewVendas.getJbtAdicionarProduto().setEnabled(false);
    }

    //FIM DOS MÉTODOS CHECADOS 
    public void bloquearCampos() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(false);
        this.viewVendas.getJbtConfirmar().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);

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
        this.viewVendas.getJbtIniciarVenda().setEnabled(false);
        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbProduto().setEnabled(false);

        limparCampos();
        bloqueioInicial();
    }

}//fim da classe
