package managedbeans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import db.ProdutoDAO;
import db.UsuarioDAO;
import model.Produto;
import repository.filter.ProdutoFilter;
import util.jsf.FacesUtil;

@ManagedBean
@SessionScoped
public class PesquisaProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProdutoFilter filtro;
	private List<Produto> produtosFiltrados;

	private Produto produtoSelecionado;

	public PesquisaProdutosBean() {
		filtro = new ProdutoFilter();
	}

	public void excluir() {
		ProdutoDAO.getInstance().remover(produtoSelecionado);
		produtosFiltrados.remove(produtoSelecionado);
		FacesUtil.addInfoMessage("Produto " + produtoSelecionado.getSku() + " excluído com sucesso.");
	}
	
	public void salvar() {
		ProdutoDAO.getInstance().guardar(produtoSelecionado);
		FacesUtil.addSuccessMessage("Usuario " + produtoSelecionado.getSku() + " editado com sucesso.");
	}

	public void selecionarProduto(Produto prod) {
		produtoSelecionado = prod;
	}
	
	public void pesquisar() {
		produtosFiltrados = ProdutoDAO.getInstance().filtrados(filtro);
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public ProdutoFilter getFiltro() {
		return filtro;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

}