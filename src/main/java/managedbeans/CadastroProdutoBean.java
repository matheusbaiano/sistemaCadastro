package managedbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import db.ProdutoDAO;
import model.Produto;
import util.jsf.FacesUtil;

@ManagedBean
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Produto produto;

	public CadastroProdutoBean() {
		limpar();
	}
	
	public void inicializar() {
		if (this.produto == null) {
			limpar();
		}
	}
	
	private void limpar() {
		produto = new Produto();
	}
	
	public void salvar() {
		Produto produtoExistente = ProdutoDAO.getInstance().porSku(produto.getSku());
		if (produtoExistente != null && !produtoExistente.equals(produto)) {
			FacesUtil.addInfoMessage("Já existe um produto com o SKU informado.");
		}
		ProdutoDAO.getInstance().guardar(this.produto);
		limpar();
		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
	}

	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public boolean isEditando() {
		return this.produto.getId() != null;
	}

}