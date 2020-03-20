package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.ContasReceberDAO;
import br.com.foursys.vendas.model.ContasReceber;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.util.Valida;
import br.com.foursys.vendas.view.ContasReceberPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ContasReceberController {
    
   
    private ContasReceberPrincipal viewContasReceber;
    private ContasReceber conta = new ContasReceber();
    private List<ContasReceber> listaContas;

    public ContasReceberController() {

    }

    public ContasReceberController(ContasReceberPrincipal viewContasReceber) {
        this.viewContasReceber = viewContasReceber;
    }

    //Carregar a tabela de vendas
    public void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.viewContasReceber.getTabelaContas().getModel();
        modelo.setRowCount(0);
        for (ContasReceber conta : listaContas) {
            
                modelo.addRow(new String[]{conta.getVendaIdVenda().getClienteIdCliente().getPessoaFisicaIdPessoaFisica().getNome(), conta.getVendaIdVenda().getValorTotal()+"", conta.getVendaIdVenda().getDataVenda(), conta.getDataVencimento(), conta.getVencida(), conta.getPagamento()});
            }
        }
    

    //Bloquear campos
    public void bloquearCampos() {
        this.viewContasReceber.getJbtSalvar().setEnabled(false);
        this.viewContasReceber.getJtfDataPagamento().setEditable(false);
        this.viewContasReceber.getJcbPagamento().setEnabled(false);
        this.viewContasReceber.getJcbVencimento().setEnabled(false);
        this.viewContasReceber.getJbtEditar().setEnabled(false);
        this.viewContasReceber.getJbtCancelar().setEnabled(false);
    }

    //Habilitar campos da tela
    public void HabilitarCampos() {
        this.viewContasReceber.getJbtSalvar().setEnabled(true);
        this.viewContasReceber.getJtfDataPagamento().setEditable(true);
        this.viewContasReceber.getJcbPagamento().setEnabled(true);
        this.viewContasReceber.getJcbVencimento().setEnabled(true);
        this.viewContasReceber.getJbtEditar().setEnabled(true);
        this.viewContasReceber.getJbtCancelar().setEnabled(true);
    }

    //Limpar campos
    public void limparCampos() {
        this.viewContasReceber.getJlbFuncionario().setText(null);
        this.viewContasReceber.getJlbNomeCliente().setText(null);
        this.viewContasReceber.getJlbValorTotal().setText(null);
        this.viewContasReceber.getJlbFormaPagamento().setText(null);
        this.viewContasReceber.getJlbDataVencimento().setText(null);
        this.viewContasReceber.getJlbDataVenda().setText(null);
        this.viewContasReceber.getJcbPagamento().setSelectedIndex(0);
        this.viewContasReceber.getJcbVencimento().setSelectedIndex(0);
        this.viewContasReceber.getJtfDataPagamento().setText(null);
    }
    
    public void acaoBotaoCancelar(){
        
        bloquearCampos();
        limparCampos();
    }
    
    
    //Valida os campos
    public boolean validarSalvar() {
        if (Valida.verificarCombo(this.viewContasReceber.getJcbPagamento().getSelectedIndex())) {
            JOptionPane.showMessageDialog(null, Mensagem.pagamentoSimNao, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasReceber.getJcbPagamento().grabFocus();
            return false;
        } else if (Valida.verificarCombo(this.viewContasReceber.getJcbVencimento().getSelectedIndex())) {
            JOptionPane.showMessageDialog(null, Mensagem.pagamentoVencido, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasReceber.getJcbVencimento().grabFocus();
            return false;
        } else if (Valida.verificarVazio(this.viewContasReceber.getJtfDataPagamento().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.dataVazia, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasReceber.getJtfDataPagamento().grabFocus();
            return false;
        } else if (Valida.validarData(this.viewContasReceber.getJtfDataPagamento().getText())) {
            JOptionPane.showMessageDialog(null, Mensagem.dataInvalida, Mensagem.atencao, JOptionPane.WARNING_MESSAGE);
            this.viewContasReceber.getJtfDataPagamento().grabFocus();
            return false;
        }
        return true;
    }
    
     //Lista todas as contas e aparecerá na tela
    public void listarContasReceber() {
        try {
            ContasReceberDAO dao = new ContasReceberDAO();
            listaContas = dao.buscarTodos();
            carregarTabela();
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//fim da classe