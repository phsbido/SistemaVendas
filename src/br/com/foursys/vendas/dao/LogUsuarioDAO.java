package br.com.foursys.vendas.dao;

import br.com.foursys.vendas.model.ItemCompra;
import br.com.foursys.vendas.model.LogUsuario;
import br.com.foursys.vendas.util.HibernateUtil;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author pbido
 */
public class LogUsuarioDAO extends GenericDAO {

    @SuppressWarnings("unchecked")
    public ArrayList<LogUsuario> buscarTodos() throws Exception {
        ArrayList<LogUsuario> listaRetorno = new ArrayList<>();
        Session sessao = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = sessao.createCriteria(LogUsuario.class);
        criteria.addOrder(Order.asc("idLogUsuario"));
        listaRetorno = (ArrayList<LogUsuario>) criteria.list();
        return listaRetorno;
    }

    public LogUsuario buscarPorCodigo(int codigo) throws Exception {
        Session sessao = HibernateUtil.getSessionFactory().openSession();
        LogUsuario LogUsuario = (LogUsuario) sessao.get(LogUsuario.class, codigo);
        return LogUsuario;
    }
}
