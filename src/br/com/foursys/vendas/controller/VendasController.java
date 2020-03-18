package br.com.foursys.vendas.controller;

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
 * @author fcorrea
 * 
 */
public class VendasController {

    private VendasPrincipal viewVendas;
    private Venda Venda = new Venda();
    private List<Cliente> listaClientes;
    private List<Funcionario> listaFuncionarios;
    private List<Produto> listaProdutos;
    private List<Funcionario> listaFuncionario;
    private boolean alterar;

  public VendasController() {

    }

    public VendasController(VendasPrincipal viewVendas) {
        this.viewVendas = viewVendas;
    }   
    
    
    
// Método  para excluir Produto
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


//Método para listar Produtos
//     public void listarProdutos() {
//         try {
//             ProdutoDAO dao = new ProdutoDAO();
//             listaProdutos = dao.buscarTodos();
//             carregarTabela();
//         } catch (Exception ex) {
//             Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
//         }
//     }

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
            if (cliente.getTipoPessoa().equals("F")){
                this.viewVendas.getJcbCliente().addItem(cliente.getPessoaFisicaIdPessoaFisica().getNome());
            }else{
                this.viewVendas.getJcbCliente().addItem(cliente.getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
            }
        }
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

    }
    
    //método para liberar o campos de produto
    public void liberarCampoProduto() {

        this.viewVendas.getJcbProduto().setEnabled(true);
        this.viewVendas.getJcbProduto().grabFocus();
        this.viewVendas.getJbtAdicionarProduto().setEnabled(true);
        this.viewVendas.getJtfDescontoProduto().setEditable(true);
        this.viewVendas.getJtfQuantidade().setEditable(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);
        this.viewVendas.getJbtExcluirProduto().setEnabled(true);
        this.viewVendas.getTabelaProduto().setEnabled(true);
    }

    //método para liberar campos de forma de pagamento
    public void liberarCampoFormaPagamento() {

        this.viewVendas.getJcbFormaDePagamento().setEnabled(true);
        this.viewVendas.getJcbFormaDePagamento().grabFocus();
        this.viewVendas.getJbtIncluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJbtExcluirFormaDePagamento().setEnabled(true);
        this.viewVendas.getJtfDescontoFormaDePagamento().setEditable(true);
        this.viewVendas.getJbtConfirmar().setEnabled(true);
        this.viewVendas.getJbtCancelar().setEnabled(true);

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
         DefaultTableModel modeloTelaPagamento = (DefaultTableModel) this.viewVendas.getTabelaProduto().getModel();
         modeloTelaPagamento.setRowCount(0);
                 
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

        this.viewVendas.getJtfDescontoProduto().setEditable(false);
        this.viewVendas.getJtfQuantidade().setEditable(false);
        this.viewVendas.getJcbProduto().setEnabled(false);

        limparCampos();
        bloquearCampos();
    }

}//fim da classe
