package managedbeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import db.ClienteDAO;
import model.Cliente;

@ManagedBean
@ViewScoped
public class SelecaoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	
	private List<Cliente> clientesFiltrados;
	
	public void pesquisar() {
		clientesFiltrados = ClienteDAO.getInstance().porNome(nome);
	}

	public void selecionar(Cliente cliente) {
		RequestContext.getCurrentInstance().closeDialog(cliente);
	}
	
	public void abrirDialogo() {
		Map<String, Object> opcoes = new HashMap<>();
		opcoes.put("modal", true);
		opcoes.put("resizable", false);
		opcoes.put("height", 470);
		
		RequestContext.getCurrentInstance().openDialog("/dialogos/SelecaoCliente", opcoes, null);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

}