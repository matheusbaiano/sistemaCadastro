package managedbeans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import db.ClienteDAO;
import model.Cliente;
import repository.filter.ClienteFilter;
import util.jsf.FacesUtil;

@ManagedBean
@ViewScoped
public class PesquisaClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ClienteFilter filtro;
	private List<Cliente> clientesFiltrados;
	
	private Cliente clienteSelecionado;
	
	public PesquisaClientesBean() {
		filtro = new ClienteFilter();
	}
	
	public void pesquisar() {
		clientesFiltrados = ClienteDAO.getInstance().filtrados(filtro);
	}
	
	public void excluir() {
			ClienteDAO.getInstance().remover(clienteSelecionado);
			clientesFiltrados.remove(clienteSelecionado);
			FacesUtil.addInfoMessage("Cliente " + clienteSelecionado.getNome() + 
					" excluído com sucesso!");
	}
	
	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public ClienteFilter getFiltro() {
		return filtro;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}
	
}
