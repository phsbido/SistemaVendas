package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.EstoqueDAO;
import br.com.foursys.vendas.model.Estoque;
import br.com.foursys.vendas.model.Fornecedor;
import br.com.foursys.vendas.model.Produto;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.util.Valida;
import br.com.foursys.vendas.view.EstoquePrincipal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Classe responsável por controlar todo o processamento de dados relacionados à
 * tela e tabela de estoque do sistema de vendas
 *
 * @author pbido
 * @since 12/03/2020
 * @version 0.1
 */
public class EstoqueController {

    private EstoquePrincipal viewEstoque;
    private Estoque estoque = new Estoque();
    private List<Estoque> listaEstoques;
    private List<Produto> listaProdutos;
    private boolean alterar;

    public EstoqueController() {

    }

    public EstoqueController(EstoquePrincipal viewEstoque) {
        this.viewEstoque = viewEstoque;
    }

    public void excluirEstoque() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewEstoque.getTabelaEstoque().getModel();
        if (this.viewEstoque.getTabelaEstoque().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueNaoSelecionado);
        } else {
            estoque = listaEstoques.get(this.viewEstoque.getTabelaEstoque().getSelectedRow());
            int opcao = JOptionPane.showConfirmDialog(null, Mensagem.confirmaExclusao, Mensagem.atencao,
                    JOptionPane.YES_OPTION,
                    JOptionPane.CANCEL_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                EstoqueDAO dao = new EstoqueDAO();
                try {
                    dao.excluir(estoque);
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueExcluidoSucesso);
                    listarEstoques();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueExcluidoErro);
                    Logger.getLogger(EstoqueController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void listarEstoque() {
        try {
            EstoqueDAO dao = new EstoqueDAO();
            listaEstoques = dao.buscarTodos();
            carregarTabela();
        } catch (Exception ex) {
            Logger.getLogger(EstoqueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewEstoque.getTabelaEstoque().getModel();
        modelo.setRowCount(0);
        for (Estoque estoque : listaEstoques) {
            modelo.addRow(new String[]{estoque.getProdutoIdProduto().getIdProduto() + "", estoque.getProdutoIdProduto().getDescricao(), estoque.getProdutoIdProduto().getFornecedorIdFornecedor().getPessoaJuridicaIdPessoaJuridica().getRazaoSocial(), estoque.getQuantidadeEstoque() + "", estoque.getQuantidadeMinima() + ""});
        }
    }

    public void carregarComboProduto() {
        ProdutoController controller = new ProdutoController();
        try {
            listaProdutos = controller.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.viewEstoque.getJcbProduto().removeAllItems();
        this.viewEstoque.getJcbProduto().addItem("Mensagem.defaultComboProduto");
        for (Produto produto : listaProdutos) {
            this.viewEstoque.getJcbProduto().addItem(produto.getDescricao());
        }
    }

    public void bloqueioInicial() {
        this.viewEstoque.getJbtNovo().setEnabled(true);
        this.viewEstoque.getJbtAlterar().setEnabled(true);
        this.viewEstoque.getJbtExcluir().setEnabled(true);
        this.viewEstoque.getJbtSair().setEnabled(true);
        this.viewEstoque.getJbtSalvar().setEnabled(false);
        this.viewEstoque.getJbtCancelar().setEnabled(false);
        bloquearCampos();
    }

    public void bloquearCampos() {
        this.viewEstoque.getJtfPesquisarDescricao().setEditable(true);
        this.viewEstoque.getJtfPesquisarDescricao().grabFocus();
        this.viewEstoque.getJtfQtde().setEditable(false);
        this.viewEstoque.getJtfQtdeMin().setEditable(false);

    }

    public void liberarCampos() {
        this.viewEstoque.getJtfPesquisarDescricao().setEditable(false);
        this.viewEstoque.getJtfPesquisarCodigo().setEditable(false);
        this.viewEstoque.getJtfQtdeMin().grabFocus();
        this.viewEstoque.getJtfQtdeMin().setEditable(true);
        this.viewEstoque.getJtfQtde().setEditable(true);
    }

    public void limparCampos() {
        this.viewEstoque.getJtfPesquisarDescricao().setText("");
        this.viewEstoque.getJtfPesquisarCodigo().setText("");
        this.viewEstoque.getJlbDescricao().setText("");
        this.viewEstoque.getJlbFornecedor().setText("");
        this.viewEstoque.getJlbValorCusto().setText("");
        this.viewEstoque.getJlbValorVenda().setText("");
        this.viewEstoque.getJtfQtde().setText("");
        this.viewEstoque.getJtfQtdeMin().setText("");
    }

    public void acaoBotaoCancelar() {
        this.viewEstoque.getJtfPesquisarDescricao().setEditable(true);
        this.viewEstoque.getJtfPesquisarDescricao().grabFocus();
        this.viewEstoque.getJbtNovo().setEnabled(true);
        this.viewEstoque.getJbtAlterar().setEnabled(true);
        this.viewEstoque.getJbtExcluir().setEnabled(true);
        this.viewEstoque.getJbtSair().setEnabled(true);
        this.viewEstoque.getJbtSalvar().setEnabled(false);
        this.viewEstoque.getJbtCancelar().setEnabled(false);
        limparCampos();
        bloquearCampos();
    }

    public void acaoBotaoNovo() {
        this.viewEstoque.getJbtNovo().setEnabled(false);
        this.viewEstoque.getJbtAlterar().setEnabled(false);
        this.viewEstoque.getJbtExcluir().setEnabled(false);
        this.viewEstoque.getJtfPesquisarDescricao().setEditable(false);
        this.viewEstoque.getJbtSair().setEnabled(false);
        this.viewEstoque.getJbtSalvar().setEnabled(true);
        this.viewEstoque.getJbtCancelar().setEnabled(true);
        liberarCampos();
        this.alterar = false;
    }

    public boolean validarSalvar() {
        if (Valida.verificarVazio(this.viewEstoque.getJtfDescricao().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueDescricaoVazio, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewEstoque.getJtfDescricao().grabFocus();
            return false;
        } else if (Valida.verificarCombo(this.viewEstoque.getJcbFornecedor().getSelectedIndex())) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueFornecedorVazio, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewEstoque.getJcbFornecedor().grabFocus();
            return false;
        } else if (Valida.verificarVazio(this.viewEstoque.getJtfValorCusto().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueValorCustoVazio, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewEstoque.getJtfValorCusto().grabFocus();
            return false;
        } else if (!Valida.validarNumero(this.viewEstoque.getJtfValorCusto().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueValorCustoInvalido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewEstoque.getJtfValorCusto().grabFocus();
            return false;
        } else if (Valida.verificarVazio(this.viewEstoque.getJtfValorVenda().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueValorVendaVazio, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewEstoque.getJtfValorVenda().grabFocus();
            return false;
        } else if (!Valida.validarNumero(this.viewEstoque.getJtfValorVenda().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueValorVendaInvalido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewEstoque.getJtfValorVenda().grabFocus();
            return false;
        }
        return true;
    }

    public void salvarEstoque() {
        if (this.alterar == false) {
            if (validarSalvar()) {
                Estoque estoque = new Estoque();
                estoque.setDescricao(this.viewEstoque.getJtfDescricao().getText());
                Fornecedor fornecedor = listaFornecedores.get(this.viewEstoque.getJcbFornecedor().getSelectedIndex() - 1);
                estoque.setFornecedorIdFornecedor(fornecedor);
                estoque.setValorCusto(Double.parseDouble(this.viewEstoque.getJtfValorCusto().getText()));
                estoque.setValorVenda(Double.parseDouble(this.viewEstoque.getJtfValorVenda().getText()));
                EstoqueDAO dao = new EstoqueDAO();
                try {
                    dao.salvar(estoque);
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueInseridoSucesso);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueInseridoErro);
                    Logger.getLogger(EstoqueController.class.getName()).log(Level.SEVERE, null, ex);
                }
                limparCampos();
                bloqueioInicial();
                listarEstoques();
            }
        } else {
            if (validarSalvar()) {
                estoque.setDescricao(this.viewEstoque.getJtfDescricao().getText());
                Fornecedor fornecedor;
                fornecedor = listaFornecedores.get(this.viewEstoque.getJcbFornecedor().getSelectedIndex() - 1);
                estoque.getFornecedorIdFornecedor().setIdFornecedor(fornecedor.getIdFornecedor());
                estoque.setValorCusto(Double.parseDouble(this.viewEstoque.getJtfValorCusto().getText()));
                estoque.setValorVenda(Double.parseDouble(this.viewEstoque.getJtfValorVenda().getText()));
                EstoqueDAO dao = new EstoqueDAO();
                try {
                    dao.salvar(estoque);
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueAlteradoSucesso);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, Mensagem.estoqueAlteradoErro);
                    Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
                }
                limparCampos();
                bloqueioInicial();
                listarEstoques();

            }
        }
    }

    public void acaoBotaoAlterar() {
        this.viewEstoque.getJbtNovo().setEnabled(false);
        this.viewEstoque.getJbtAlterar().setEnabled(false);
        this.viewEstoque.getJbtExcluir().setEnabled(false);
        this.viewEstoque.getJbtSair().setEnabled(false);
        this.viewEstoque.getJbtSalvar().setEnabled(true);
        this.viewEstoque.getJbtCancelar().setEnabled(true);
        liberarCampos();
        this.viewEstoque.getJtfDescricao().setEditable(false);

    }

    public void alterarEstoque() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewEstoque.getTabelaEstoque().getModel();
        if (this.viewEstoque.getTabelaEstoque().getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, Mensagem.estoqueNaoSelecionado);
        } else {
            estoque = listaEstoques.get(this.viewEstoque.getTabelaEstoque().getSelectedRow());
            this.viewEstoque.getJtfDescricao().setText(estoque.getDescricao());
            this.viewEstoque.getJtfValorCusto().setText(estoque.getValorCusto() + "");
            this.viewEstoque.getJtfValorVenda().setText(estoque.getValorVenda() + "");
            this.viewEstoque.getJcbFornecedor().setSelectedItem(estoque.getFornecedorIdFornecedor().getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
            this.alterar = true;
            acaoBotaoAlterar();
        }
    }
}
