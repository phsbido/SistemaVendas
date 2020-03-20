package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.ContasPagarDAO;
import br.com.foursys.vendas.model.ContasPagar;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.util.Valida;
import br.com.foursys.vendas.view.ContasPagarPrincipal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Classe responsavel pelo controle da View ContasPagarPrincipal
 *
 * @author fcorrea
 * @since 19/03/2020
 */
public class ContasPagarController {

    private ContasPagarPrincipal viewContasPagar;
    private ContasPagar conta = new ContasPagar();
    private List<ContasPagar> listaContas;

    public ContasPagarController() {

    }

    public ContasPagarController(ContasPagarPrincipal viewContasPagar) {
        this.viewContasPagar = viewContasPagar;
    }

    //Metodo que salva as alteracoes do banco de dados
    public void salvarAlteracoes() {
        if (validarSalvar()) {
            conta.setPagamento(this.viewContasPagar.getJcbPagamento().getSelectedItem().toString());
            conta.setVencida(this.viewContasPagar.getJcbVencimento().getSelectedItem().toString());
            conta.setDataPagamento(this.viewContasPagar.getJtfDataPagamento().getText());
            ContasPagarDAO dao = new ContasPagarDAO();
            try {
                dao.salvar(conta);
                LoginController.verificaLog(Mensagem.salvar, Mensagem.tabelaContasPagar);
                JOptionPane.showMessageDialog(null, Mensagem.contaAlteradaSucesso);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, Mensagem.contaAlteradaErro);
                Logger.getLogger(ContasPagarController.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparCampos();
            bloquearCampos();
            listarContas();
        }
    }

    //Metodo captura os dados do item selecionado da tabela, coloca eles em seus campos respectivos
    //e habilita o botão alterar
    public void selecionarItemTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewContasPagar.getTabelaContas().getModel();

        this.viewContasPagar.getJbtEditar().setEnabled(true);
        conta = listaContas.get(this.viewContasPagar.getTabelaContas().getSelectedRow());
        this.viewContasPagar.getJlbFuncionarioResponsavel().setText(conta.getCompraIdCompra().getFuncionarioIdFuncionario().getPessoaFisicaIdPessoaFisica().getNome());
        this.viewContasPagar.getJlbNomeFornecedor().setText(conta.getCompraIdCompra().getFornecedorIdFornecedor().getPessoaJuridicaIdPessoaJuridica().getRazaoSocial());
        this.viewContasPagar.getJlbValorVenda().setText(conta.getCompraIdCompra().getValorTotal() + "");
        this.viewContasPagar.getJlbFormaPagamento().setText(conta.getCompraIdCompra().getFormaPagamento());
        this.viewContasPagar.getJlbDataVencimento().setText(conta.getDataVencimento());
        this.viewContasPagar.getJlbDataLancamento().setText(conta.getCompraIdCompra().getDataCompra());
        this.viewContasPagar.getJcbPagamento().setSelectedItem(conta.getPagamento());
        this.viewContasPagar.getJcbVencimento().setSelectedItem(conta.getVencida());
        this.viewContasPagar.getJtfDataPagamento().setText(conta.getDataPagamento());

    }

    //Metodo de validação do salvar
    public boolean validarSalvar() {
        if (Valida.verificarCombo(this.viewContasPagar.getJcbPagamento().getSelectedIndex())) {
            JOptionPane.showMessageDialog(null, Mensagem.pagamentoSimNao, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasPagar.getJcbPagamento().grabFocus();
            return false;
        } else if (Valida.verificarCombo(this.viewContasPagar.getJcbVencimento().getSelectedIndex())) {
            JOptionPane.showMessageDialog(null, Mensagem.pagamentoVencido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasPagar.getJcbVencimento().grabFocus();
            return false;
        } else if (Valida.verificarVazio(this.viewContasPagar.getJtfDataPagamento().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.dataVazia, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasPagar.getJtfDataPagamento().grabFocus();
            return false;
        } else if (!Valida.validarData(this.viewContasPagar.getJtfDataPagamento().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.dataInvalida, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasPagar.getJtfDataPagamento().grabFocus();
            return false;
        }
        return true;
    }

    //Metodo que lista todas as contas que serão chamadas na tela  
    public void listarContas() {
        try {
            ContasPagarDAO dao = new ContasPagarDAO();
            listaContas = dao.buscarTodos();
            carregarTabela();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo para carregar a tabela de compras 
    public void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewContasPagar.getTabelaContas().getModel();
        modelo.setRowCount(0);
        for (ContasPagar conta : listaContas) {
            if (conta.getPagamento().equals("Não")) {
                if (conta.getCompraIdCompra().getFormaPagamento().equals("Cheque")) {
                    modelo.addRow(new String[]{conta.getCompraIdCompra().getFornecedorIdFornecedor().getPessoaJuridicaIdPessoaJuridica().getRazaoSocial(),
                        conta.getCompraIdCompra().getValorTotal() + "", conta.getCompraIdCompra().getDataCompra(), conta.getDataVencimento(), conta.getVencida(), conta.getPagamento()});
                } else if (conta.getCompraIdCompra().getFormaPagamento().equals("Crédito")) {
                    modelo.addRow(new String[]{conta.getCompraIdCompra().getFornecedorIdFornecedor().getPessoaJuridicaIdPessoaJuridica().getRazaoSocial(),
                        conta.getCompraIdCompra().getValorTotal() + "", conta.getCompraIdCompra().getDataCompra(), conta.getDataVencimento(), conta.getVencida(), conta.getPagamento()});
                }
            }
        }
    }

    //Metodo para bloquear campos da tela
    public void bloquearCampos() {
        this.viewContasPagar.getJbtSair().setEnabled(true);
        this.viewContasPagar.getJcbPagamento().setEnabled(false);
        this.viewContasPagar.getJcbVencimento().setEnabled(false);
        this.viewContasPagar.getJtfDataPagamento().setEditable(false);
        this.viewContasPagar.getJbtEditar().setEnabled(false);
        this.viewContasPagar.getJbtSalvar().setEnabled(false);
        this.viewContasPagar.getJbtCancelar().setEnabled(false);

        limparCampos();
    }

    //Metodo para liberar campos da tela para alteracao 
    public void liberarCamposAlteracao() {
        this.viewContasPagar.getJbtEditar().setEnabled(false);
        this.viewContasPagar.getJbtSair().setEnabled(false);
        this.viewContasPagar.getJcbPagamento().setEnabled(true);
        this.viewContasPagar.getJcbVencimento().setEnabled(true);
        this.viewContasPagar.getJtfDataPagamento().setEditable(true);
        this.viewContasPagar.getJbtSalvar().setEnabled(true);
        this.viewContasPagar.getJbtCancelar().setEnabled(true);
    }

    //Metodo de limpar campos
    public void limparCampos() {
        this.viewContasPagar.getJlbFuncionarioResponsavel().setText(null);
        this.viewContasPagar.getJlbNomeFornecedor().setText(null);
        this.viewContasPagar.getJlbValorVenda().setText(null);
        this.viewContasPagar.getJlbValorVenda().setText(null);
        this.viewContasPagar.getJlbFormaPagamento().setText(null);
        this.viewContasPagar.getJlbDataLancamento().setText(null);
        this.viewContasPagar.getJlbDataVencimento().setText(null);
        this.viewContasPagar.getJcbPagamento().setSelectedIndex(0);
        this.viewContasPagar.getJcbVencimento().setSelectedIndex(0);
        this.viewContasPagar.getJtfDataPagamento().setText(null);
    }

}//fim da classe
