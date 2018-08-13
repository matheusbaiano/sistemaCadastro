package managedbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import db.ClienteDAO;
import model.Cliente;
import model.Endereco;
import util.jsf.FacesUtil;

@ManagedBean
@SessionScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Cliente cliente = new Cliente();
	private Endereco endereco = new Endereco();
	private Endereco enderecoSelecionado;
	private boolean editandoEndereco;
	
	public void inicializar(){
			limpar();
	}
	
	public void limpar() {
		this.cliente = new Cliente();
		this.endereco = new Endereco();
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
	
	public void editarEndereco() {
		this.editandoEndereco = true;
		this.endereco = enderecoSelecionado;
	}
	
	public void excluirEndereco(Endereco endereco) {
		this.cliente.getEnderecos().remove(endereco);
	}
	
	public void confirmarEndereco() {
		if (!this.editandoEndereco) {
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
}
