package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.EnderecoDAO;
import br.com.foursys.vendas.model.Endereco;
import br.com.foursys.vendas.util.Mensagem;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Classe responsável por controlar todo o processamento de dados relacionados à
 * salvar e permitir excluir o Endereço
 *
 * @author dmunhoz
 */
public class EnderecoController {

    // metodos responsaveis por salvar, buscar por código e excluir Endereco  
    public void salvarEndereco(Object objeto) {
        try {
            new EnderecoDAO().salvar(objeto);
            LoginController.verificaLog(Mensagem.salvar, Mensagem.tabelaEndereco);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, Mensagem.erroEndereco);
            Logger.getLogger(EnderecoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluirEndereco(Object objeto) {
        try {
            new EnderecoDAO().excluir(objeto);
            LoginController.verificaLog(Mensagem.excluir, Mensagem.tabelaEndereco);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, Mensagem.erroExcluirEndereco);
            Logger.getLogger(EnderecoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Endereco buscarPorCodigo(int id) {
        Endereco endereco = new Endereco();
        try {
            endereco = new EnderecoDAO().buscarPorCodigo(id);
        } catch (Exception ex) {
            Logger.getLogger(EstadoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return endereco;
    }
}
