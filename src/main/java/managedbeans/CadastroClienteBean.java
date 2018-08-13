package managedbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import db.ClienteDAO;
import model.Cliente;
import model.Endereco;
import util.jsf.FacesUtil;

@ManagedBean
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	private Endereco endereco;
	
	private boolean editandoEndereco;
	
	public void inicializar(){
		if (cliente == null) {
			limpar();
		}
	}
	
	public void limpar() {
		this.cliente = new Cliente();
	}
	
	public void salvar() {
			ClienteDAO.getInstance().guardar(cliente);
			FacesUtil.addInfoMessage("Cliente "+cliente.getNome()+" salvo com sucesso!");
			limpar();
	}
	
	public void novoEndereco() {
		this.endereco = new Endereco();
		this.endereco.setCliente(this.cliente);
		this.editandoEndereco = false;
	}
	
	public void editarEndereco(Endereco endereco) {
		this.endereco = endereco;
		this.editandoEndereco = true;
	}
	
	public void excluirEndereco(Endereco endereco) {
		this.cliente.getEnderecos().remove(endereco);
	}
	
	public void confirmarEndereco() {
		if (!this.cliente.getEnderecos().contains(this.endereco)) {
			this.cliente.getEnderecos().add(this.endereco);
		}
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public boolean isEditando() {
		return cliente != null && cliente.getId() == null;
	}
	
	public boolean isEditandoEndereco() {
		return editandoEndereco;
	}
}
