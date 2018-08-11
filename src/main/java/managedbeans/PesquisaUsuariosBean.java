package managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;

import db.UsuarioDAO;
import model.Usuario;
import repository.filter.UsuarioFilter;
import util.jsf.FacesUtil;

@ManagedBean
@SessionScoped
public class PesquisaUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private UsuarioFilter filtro;
	private List<Usuario> usuariosFiltrados;
	private DataTable dataTable;
	private Usuario usuarioSelecionado;
	private Dialog dialog;
	
	public PesquisaUsuariosBean() {
		filtro = new UsuarioFilter();
	}

	public void excluir() {
		usuarioDAO.deletarUsuario(usuarioSelecionado);
		usuariosFiltrados.remove(usuarioSelecionado);
		FacesUtil.addInfoMessage("Usuario " + usuarioSelecionado.getNomeUsuario() + " excluído com sucesso.");
	}

	public void salvar() {
		usuarioDAO.inserirUsuario(usuarioSelecionado);
		FacesUtil.addSuccessMessage("Usuario " + usuarioSelecionado.getNomeUsuario() + " editado com sucesso.");
	}

	public void pesquisar() {
		usuariosFiltrados = usuarioDAO.filtrados(filtro);
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

}