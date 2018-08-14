package db;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import model.Produto;
import repository.filter.ProdutoFilter;

public class ProdutoDAO {

	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("SistemaCadastro");
	private EntityManager manager = factory.createEntityManager();
	private static ProdutoDAO instanceSingleton;
	
	private ProdutoDAO(){
		
	}
	
	public static ProdutoDAO getInstance(){
		if (instanceSingleton != null) {
			return instanceSingleton;
		}
		instanceSingleton = new ProdutoDAO();
		return instanceSingleton;
	}

	public Produto getProduto(String nomeProduto) {

		try {
			Produto produto = (Produto) manager
					.createQuery("SELECT p from Produto p where p.nome = :name")
					.setParameter("name", nomeProduto).getSingleResult();
			return produto;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Produto porId(Long id) {
		return this.manager.find(Produto.class, id);
	}
	
	public Produto porSku(String sku) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
				.setParameter("sku", sku.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Produto> filtrados(ProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);
		
		if (StringUtils.isNotBlank(filtro.getSku())) {
			predicates.add(builder.equal(produtoRoot.get("sku"), filtro.getSku()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(produtoRoot.get("nome")), 
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		criteriaQuery.select(produtoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("nome")));
		
		TypedQuery<Produto> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public List<Produto> porNome(String nome) {
		return this.manager.createQuery("from Produto " +
				"where upper(nome) like :nome", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	}
	

	public boolean guardar(Produto produto) {
		try {
			manager.getTransaction().begin();
			if (produto.getId() == null) {
				manager.persist(produto);
			} else {
				manager.merge(produto);
			}
			manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
	public void remover(Produto produto) {
			produto = porId(produto.getId());
			manager.getTransaction().begin();
			manager.remove(produto);
			manager.getTransaction().commit();
	}
	

}