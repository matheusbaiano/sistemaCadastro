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
import model.Cliente;
import repository.filter.ClienteFilter;

public class ClienteDAO {

	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("SistemaCadastro");
	private EntityManager manager = factory.createEntityManager();
	private static ClienteDAO instanceSingleton;
	
	private ClienteDAO(){
		
	}
	
	public static ClienteDAO getInstance(){
		if (instanceSingleton != null) {
			return instanceSingleton;
		}
		instanceSingleton = new ClienteDAO();
		return instanceSingleton;
	}

	public Cliente getCliente(String nomeCliente) {

		try {
			Cliente cliente = (Cliente) manager
					.createQuery("SELECT c from Cliente c where c.nome = :name")
					.setParameter("name", nomeCliente).getSingleResult();

			return cliente;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Cliente porId(Long id) {
		return this.manager.find(Cliente.class, id);
	}
	
	public List<Cliente> porNome(String nome) {
		return this.manager.createQuery("from Cliente " +
				"where upper(nome) like :nome", Cliente.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	}
	
	public List<Cliente> filtrados(ClienteFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteriaQuery = builder.createQuery(Cliente.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Cliente> clienteRoot = criteriaQuery.from(Cliente.class);
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.equal(clienteRoot.get("cpf"), filtro.getCpf()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(clienteRoot.get("nome")), 
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		criteriaQuery.select(clienteRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(clienteRoot.get("nome")));
		
		TypedQuery<Cliente> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public boolean guardar(Cliente cliente) {
		try {
			manager.getTransaction().begin();
			if (cliente.getId() == null) {
				manager.persist(cliente);
			} else {
				manager.merge(cliente);
			}
			manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
	public void remover(Cliente cliente) {
			cliente = porId(cliente.getId());
			manager.getTransaction().begin();
			manager.remove(cliente);
			manager.getTransaction().commit();
	}
	

}