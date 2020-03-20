package br.com.foursys.vendas.controller;

import br.com.foursys.vendas.dao.LogUsuarioDAO;
import br.com.foursys.vendas.model.Funcionario;
import br.com.foursys.vendas.model.LogUsuario;
import static br.com.foursys.vendas.model.LogUsuario_.funcionarioIdFuncionario;
import static br.com.foursys.vendas.model.LogUsuario_.operacao;
import static br.com.foursys.vendas.model.LogUsuario_.tabela;
import br.com.foursys.vendas.util.Mensagem;
import br.com.foursys.vendas.view.LoginPrincipal;
import br.com.foursys.vendas.view.MenuPrincipal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author dmunhoz
 */
public class LoginController {

    private LoginPrincipal viewLogin;
    private static Funcionario fun = new Funcionario();

    public LoginController(LoginPrincipal viewLogin) {
        this.viewLogin = viewLogin;
    }

    public void loginSistema() {
        if (this.viewLogin.getJtfLogin().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, Mensagem.loginVazio);
        } else {
            List<Funcionario> listaFuncionario = new FuncionarioController().buscarTodos(this.viewLogin.getJtfLogin().getText());
            int contLogin = 0;
            int contSenha = 0;
            for (Funcionario funcionario : listaFuncionario) {
                contLogin++;
                if (funcionario.getSenha().equals(this.viewLogin.getJtfSenha().getText())) {
                    fun = funcionario;
                    LoginController.verificaLog(Mensagem.login, "");
                    this.viewLogin.dispose();
                    new MenuPrincipal(funcionario.getPessoaFisicaIdPessoaFisica().getNome());
                } else {
                    contSenha++;
                }
            }
            if (contLogin == 0) {
                JOptionPane.showMessageDialog(null, Mensagem.loginInvalido, Mensagem.erro, JOptionPane.ERROR_MESSAGE);
            } else if (contSenha > 0) {
                JOptionPane.showMessageDialog(null, Mensagem.senhaInvalido, Mensagem.erro, JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    public static void salvar(LogUsuario log) {
        LogUsuarioDAO dao = new LogUsuarioDAO();
        dao.salvar(log);
    }

    public static void verificaLog(String operacao, String tabela) {
        LogUsuario log = new LogUsuario();
        log.setFuncionarioIdFuncionario(fun);
        log.setOperacao(operacao);
        log.setTabela(tabela);

        java.util.Date atual = new java.util.Date();
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        log.setTimestamp(formater.format(atual));
        salvar(log);

    }

}
