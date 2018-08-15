package managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import db.ClienteDAO;
import db.PedidoDAO;
import db.ProdutoDAO;
import db.UsuarioDAO;
import model.Cliente;
import model.EnderecoEntrega;
import model.FormaPagamento;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.Usuario;
import service.CadastroPedidoService;
import service.NegocioException;
import util.jsf.FacesUtil;

@ManagedBean
@SessionScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sku;
	
	@Produces
	@PedidoEdicao
	private Pedido pedido;
	
	private List<Usuario> vendedores;
	private List<Cliente> clientesFiltrados;
	private Produto produtoLinhaEditavel;
	private String nome;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setVendedores(List<Usuario> vendedores) {
		this.vendedores = vendedores;
	}

	public CadastroPedidoBean() {
		limpar();
	}
	
	public void inicializar() {
		if(this.isEditando() && pedido.getId()!=null){
			pedido = PedidoDAO.getInstance().porId(pedido.getId());
		}else{
			limpar();
		}
		pedido.setVendedor(UsuarioDAO.getInstance().getUsuario((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario")));
		System.out.println(pedido.getId()+" idpedido");
		this.vendedores = UsuarioDAO.getInstance().vendedores();
		this.pedido.adicionarItemVazio();
		this.recalcularPedido();
	}
	
	public void pesquisar() {
		setClientesFiltrados(ClienteDAO.getInstance().porNome(nome));
	}

	
	public void clienteSelecionado(Cliente cliente) {
		pedido.setCliente(cliente);
	}
	
	private void limpar() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}
	
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event) {
		this.pedido = event.getPedido();
	}
	
	public void salvar() {
		this.pedido.removerItemVazio();
		
		for (ItemPedido item: pedido.getItens()) {
			System.out.println(item.getProduto().getNome()+"-nomeprodutos");
		}
		try {
			this.pedido = new CadastroPedidoService().salvar(this.pedido);
		} catch (NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
		this.pedido.adicionarItemVazio();
	}
	
	public void recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}
	}
	
	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = ProdutoDAO.getInstance().porSku(this.sku);
			this.carregarProdutoLinhaEditavel();
		}
	}
	
	public void carregarProdutoLinhaEditavel() {
		ItemPedido item = this.pedido.getItens().get(0);
		
		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());
				
				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;
				
				this.pedido.recalcularValorTotal();
			}
		}
	}
	
	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;
		for (ItemPedido item : this.getPedido().getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}
		return existeItem;
	}

	public List<Produto> completarProduto(String nome) {
		return ProdutoDAO.getInstance().porNome(nome);
	}
	
	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}
		
		this.pedido.recalcularValorTotal();
	}
	
	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}
	
	public List<Cliente> completarCliente(String nome) {
		return ClienteDAO.getInstance().porNome(nome);
	}

	public Pedido getPedido() {
		return pedido;
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}
	
	public boolean isEditando() {
		return this.pedido.getId() != null;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getNomeCliente() {
		return pedido.getCliente() == null ? null : pedido.getCliente().getNome();
	}
	
	public void setNomeCliente(String nome) {
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public void setClientesFiltrados(List<Cliente> clientesFiltrados) {
		this.clientesFiltrados = clientesFiltrados;
	}

}