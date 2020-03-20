package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.ContasReceberDAO;
import br.com.foursys.vendas.dao.ContasReceberDAO;
import br.com.foursys.vendas.model.ContasReceber;
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
            if (conta.getPagamento().equals("Não")){
            if (conta.getVendaIdVenda().getFormaPagamento().equals("Cheque")) {
                modelo.addRow(new String[]{conta.getVendaIdVenda().getClienteIdCliente().getPessoaFisicaIdPessoaFisica().getNome(), 
                    conta.getVendaIdVenda().getValorTotal()+"", conta.getVendaIdVenda().getDataVenda(), conta.getDataVencimento(), conta.getVencida(), conta.getPagamento()});
            }else if(conta.getVendaIdVenda().getFormaPagamento().equals("Crédito")){
                modelo.addRow(new String[]{conta.getVendaIdVenda().getClienteIdCliente().getPessoaFisicaIdPessoaFisica().getNome(), 
                    conta.getVendaIdVenda().getValorTotal()+"", conta.getVendaIdVenda().getDataVenda(), conta.getDataVencimento(), conta.getVencida(), conta.getPagamento()});
            }
        }
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
    
     //Metodo que salva as alteracoes do banco de dados
    public void salvarAlteracoes(){
         if (validarSalvar()) {
                conta.setPagamento(this.viewContasReceber.getJcbPagamento().getSelectedItem().toString());
                conta.setVencida(this.viewContasReceber.getJcbVencimento().getSelectedItem().toString());
                conta.setDataPagamento(this.viewContasReceber.getJtfDataPagamento().getText());
                ContasReceberDAO dao = new ContasReceberDAO();
                try {
                    dao.salvar(conta);
                    LoginController.verificaLog(Mensagem.salvar, Mensagem.tabelaContasReceber);
                    JOptionPane.showMessageDialog(null, Mensagem.contaAlteradaSucesso);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, Mensagem.contaAlteradaErro);
                    Logger.getLogger(ContasReceberController.class.getName()).log(Level.SEVERE, null, ex);
                }
                limparCampos();
                bloquearCampos();
                listarContasReceber();
            }    
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
        } else if (!Valida.validarData(this.viewContasReceber.getJtfDataPagamento().getText())) {
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

     //Metodo para liberar campos da tela para alteracao 
    public void liberarCamposAlteracao() {
        this.viewContasReceber.getJbtEditar().setEnabled(false);
        this.viewContasReceber.getJcbPagamento().setEnabled(true);
        this.viewContasReceber.getJcbVencimento().setEnabled(true);
        this.viewContasReceber.getJtfDataPagamento().setEditable(true);   
        this.viewContasReceber.getJbtSalvar().setEnabled(true);
        this.viewContasReceber.getJbtCancelar().setEnabled(true);
    }
    
//Metodo captura os dados do item selecionado da tabela, coloca eles em seus campos respectivos
    //e habilita o botão alterar
    public void selecionarItemTabela(){
         DefaultTableModel modelo = (DefaultTableModel) this.viewContasReceber.getTabelaContas().getModel();
            
            this.viewContasReceber.getJbtEditar().setEnabled(true);
            conta = listaContas.get(this.viewContasReceber.getTabelaContas().getSelectedRow());
            this.viewContasReceber.getJlbFuncionario().setText(conta.getVendaIdVenda().getFuncionarioIdFuncionario().getPessoaFisicaIdPessoaFisica().getNome());
            this.viewContasReceber.getJlbNomeCliente().setText(conta.getVendaIdVenda().getClienteIdCliente().getPessoaFisicaIdPessoaFisica().getNome());
            this.viewContasReceber.getJlbFormaPagamento().setText(conta.getVendaIdVenda().getFormaPagamento());
            this.viewContasReceber.getJlbValorTotal().setText(conta.getVendaIdVenda().getValorTotal()+"");
            this.viewContasReceber.getJlbDataVenda().setText(conta.getVendaIdVenda().getDataVenda());
            this.viewContasReceber.getJcbPagamento().setSelectedItem(conta.getPagamento());
            this.viewContasReceber.getJcbVencimento().setSelectedItem(conta.getVencida());  
            this.viewContasReceber.getJtfDataPagamento().setText(conta.getDataPagamento()); 
            this.viewContasReceber.getJlbDataVencimento().setText(conta.getDataVencimento()); 
            
    }
}//fim da classe