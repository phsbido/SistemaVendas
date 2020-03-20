package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.PessoaFisicaDAO;
import br.com.foursys.vendas.model.PessoaFisica;
import br.com.foursys.vendas.util.Mensagem;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *Classe responsável por controlar todo o processamento de dados relacionados à
 * salvar, excluir e buscar Pessoa Fisica
 * @author dmunhoz
 */
public class PessoaFisicaController {

    
   // metodos responsaveis por salvar, buscar por código e excluir contato  
    public void salvarPessoaFisica(Object objeto) {
        try {
            new PessoaFisicaDAO().salvar(objeto);
            LoginController.verificaLog(Mensagem.salvar, Mensagem.tabelaPessoaFisica);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar pessoa fisica!");
            Logger.getLogger(PessoaFisicaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluirPessoaFisica(Object objeto) {
        try {
            new PessoaFisicaDAO().excluir(objeto);
            LoginController.verificaLog(Mensagem.excluir, Mensagem.tabelaPessoaFisica);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir pessoa fisica!");
            Logger.getLogger(PessoaFisicaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public PessoaFisica buscarPorCodigo(int id) {
        PessoaFisicaDAO dao = new PessoaFisicaDAO();
        PessoaFisica pessoaFisica = new PessoaFisica();
        try {
            pessoaFisica = dao.buscarPorCodigo(id);
        } catch (Exception ex) {
            Logger.getLogger(EstadoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pessoaFisica;
    }
}
