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
    
    
    
// MÃ©todo  para excluir Produto
// public void excluirProduto() {
//         DefaultTableModel modelo = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
//         if (this.viewVendas.getTabelaProduto().getSelectedRow() < 0) {
//             JOptionPane.showMessageDialog(null, Mensagem.produtoNaoSelecionado);
//         } else {
//             produto = listaProdutos.get(this.viewVendas.getTabelaProduto().getSelectedRow());
//             int opcao = JOptionPane.showConfirmDialog(null, Mensagem.confirmaExclusao, Mensagem.atencao,
//                     JOptionPane.YES_OPTION,
//                     JOptionPane.CANCEL_OPTION);
//             if (opcao == JOptionPane.YES_OPTION) {
//                 ProdutoDAO dao = new ProdutoDAO();
//                 try {
//                     dao.excluir(produto);
//                     JOptionPane.showMessageDialog(null, Mensagem.produtoExcluidoSucesso);
//                     listarProdutos();
//                 } catch (Exception ex) {
//                     JOptionPane.showMessageDialog(null, Mensagem.produtoExcluidoErro);
//                     Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
//                 }
//             }
//         }
//     }


//MÃ©todo para listar Produtos
//     public void listarProdutos() {
//         try {
//             ProdutoDAO dao = new ProdutoDAO();
//             listaProdutos = dao.buscarTodos();
//             carregarTabela();
//         } catch (Exception ex) {
//             Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
//         }
//     }

//MÃ©todo que carrega a combo cliente
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
            if (cliente.getTipoPessoa().equals("F")){
                this.viewVendas.getJcbCliente().addItem(cliente.getPessoaFisicaIdPessoaFisica().getNome());
            }else{
                this.viewVendas.getJcbCliente().addItem(cliente.getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
            }
        }
    }

    //mÃ©todo que carrega a combo FuncionÃ¡rio
    public void carregarComboFuncionario() {
        FuncionarioController controller = new FuncionarioController();
        try {
            listaFuncionario = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewVendas.getJcbFuncionario().removeAllItems();
        this.viewVendas.getJcbFuncionario().addItem(Mensagem.defaultComboFuncionario);
        for (Funcionario funcionario : listaFuncionarios) {
            this.viewVendas.getJcbFuncionario().addItem(funcionario.getPessoaFisicaIdPessoaFisica().getNome());
        }
    }

    //MÃ©todo que carrega a combo Produto
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
        
        this.viewVendas.getJcbCliente().setEnabled(true);
        this.viewVendas.getJcbCliente().grabFocus();
        this.viewVendas.getJcbFuncionario().setEnabled(true);
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
        this.viewVendas.getTabelaPagamento().setEnabled(false);
        this.viewVendas.getTabelaProduto().setEnabled(false);

    }
    
    //mÃ©todo para liberar o campos de produto
    public void liberarCamposProduto() {

        this.viewVendas.getJcbProduto().setEnabled(true);
        this.viewVendas.getJcbProduto().grabFocus();
        this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);
        this.viewVendas.getJbtExcluirProduto().setEnabled(true);
        this.viewVendas.getTabelaProduto().setEnabled(true);
    }

    //mÃ©todo para liberar campos de forma de pagamento
    public void liberarCamposFormaPagamento() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(true);
        this.viewVendas.getJcbFormaDePagamento().grabFocus();
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(true);
        this.viewVendas.getJbtConfirmar().setEnabled(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);

    }
    
    
    //FIM DOS MÃ‰TODOS CHECADOS 
    
    public void bloquearCamposFormaPagamento() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(false);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(false);
        this.viewVendas.getJbtConfirmar().setEnabled(false);
        this.viewVendas.getJbtCancelar().setEnabled(false);
        this.viewVendas.getJbtExcluirProduto().setEnabled(false);
        
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

}//fim da classe
