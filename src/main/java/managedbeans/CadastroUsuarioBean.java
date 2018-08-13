package managedbeans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import db.UsuarioDAO;
import model.Usuario;
import util.jpa.Transactional;

@ManagedBean
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String login;
	private String senha;

	public boolean verificarDisponibilidadeLogin() {
		FacesMessage msg = null;
		boolean retorno;
		if (UsuarioDAO.getInstance().getUsuario(login)!=null &&UsuarioDAO.getInstance().getUsuario(login).getNomeUsuario().equalsIgnoreCase(this.login)) {
			msg = new FacesMessage("Login já está em uso.");
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			retorno = true;
		} else {
			msg = new FacesMessage("Login disponível.");
			retorno = false;
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return retorno;
	}
	
	@Transactional
	public void cadastrar() {
		if (UsuarioDAO.getInstance().getUsuario(login)==null) {
			Usuario user = new Usuario();
			user.setNomeUsuario(login);
			user.setSenha(senha);
			UsuarioDAO.getInstance().inserirUsuario(user);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário "+user.getNomeUsuario()+" cadastrado!"));
			login = new String();
			senha = new String();
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	

}