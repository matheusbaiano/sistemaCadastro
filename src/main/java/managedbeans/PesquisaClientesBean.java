package managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import db.ClienteDAO;
import model.Cliente;
import model.Endereco;
import repository.filter.ClienteFilter;
import util.jsf.FacesUtil;

@ManagedBean
@SessionScoped
public class PesquisaClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ClienteFilter filtro;
	private List<Cliente> clientesFiltrados;
	
	private Cliente clienteSelecionado;
	private Endereco enderecoSelecionado;
	private Endereco endereco;
	private boolean editandoEndereco;
	private DataTable table;
	
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
	
	public void limpar() {
		clientesFiltrados = null;
	}	
	
	public void salvar() {
		ClienteDAO.getInstance().guardar(clienteSelecionado);
		FacesUtil.addInfoMessage("Cliente "+clienteSelecionado.getNome()+" salvo com sucesso!");
	}
	
	public void excluirEndereco(Endereco endereco) {
		this.clienteSelecionado.getEnderecos().remove(endereco);
	}
	
	public void confirmarEndereco() {
		if (!this.editandoEndereco) {
			this.clienteSelecionado.getEnderecos().add(this.endereco);
		}
	}
	
	public void novoEndereco() {
		this.endereco = new Endereco();
		this.endereco.setCliente(this.clienteSelecionado);
		this.editandoEndereco = false;
	}
	
	public void editarEndereco() {
		this.editandoEndereco = true;
		this.endereco = enderecoSelecionado;
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

	public Endereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(Endereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

	public boolean isEditandoEndereco() {
		return editandoEndereco;
	}

	public void setEditandoEndereco(boolean editandoEndereco) {
		this.editandoEndereco = editandoEndereco;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setFiltro(ClienteFilter filtro) {
		this.filtro = filtro;
	}

	public void setClientesFiltrados(List<Cliente> clientesFiltrados) {
		this.clientesFiltrados = clientesFiltrados;
	}

	public DataTable getTable() {
		return table;
	}

	public void setTable(DataTable table) {
		this.table = table;
	}
	
}
