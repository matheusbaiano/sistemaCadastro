package managedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import db.UsuarioDAO;
import model.Usuario;

@ManagedBean(name = "LoginMB")
@ViewScoped
public class LoginManagedBean {

	private Usuario usuario = new Usuario();

	public String envia() {
		Usuario user = new Usuario();
		user = UsuarioDAO.getInstance().getUsuario(usuario.getNomeUsuario(), usuario.getSenha());
		if (user == null) {
			if (usuario.getNomeUsuario().equals("admin") && usuario.getSenha().equals("admin")) {
				UsuarioDAO.getInstance().inserirUsuario(usuario);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
						usuario);
				return "/Home";
			} else {
				usuario = new Usuario();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Os campos Usuário e Senha estão incorretos!", "Erro no Login!"));
				return null;
			}
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
					usuario.getNomeUsuario());
			return "/Home";
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}