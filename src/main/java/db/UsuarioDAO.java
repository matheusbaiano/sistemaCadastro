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

import model.Usuario;
import repository.filter.UsuarioFilter;

public class UsuarioDAO {

	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("SistemaCadastro");
	private EntityManager em = factory.createEntityManager();
	private static UsuarioDAO instanceSingleton;
	
	private UsuarioDAO(){
		
	}
	
	public static UsuarioDAO getInstance(){
		if (instanceSingleton != null) {
			return instanceSingleton;
		}
		instanceSingleton = new UsuarioDAO();
		return instanceSingleton;
	}

	public Usuario getUsuario(String nomeUsuario, String senha) {

		try {
			Usuario usuario = (Usuario) em
					.createQuery("SELECT u from Usuario u where u.nomeUsuario = :name and u.senha = :senha")
					.setParameter("name", nomeUsuario).setParameter("senha", senha).getSingleResult();

			return usuario;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Usuario getUsuario(String nomeUsuario) {

		try {
			Usuario usuario = (Usuario) em
					.createQuery("SELECT u from Usuario u where u.nomeUsuario = :name")
					.setParameter("name", nomeUsuario).getSingleResult();

			return usuario;
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean inserirUsuario(Usuario usuario) {
		try {
			em.getTransaction().begin();
			if (usuario.getId() == null) {
				em.persist(usuario);
			} else {
				em.merge(usuario);
			}
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Usuario> filtrados(UsuarioFilter filtro) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Usuario> usuarioRoot = criteriaQuery.from(Usuario.class);
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(usuarioRoot.get("nomeUsuario")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		criteriaQuery.select(usuarioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(usuarioRoot.get("nomeUsuario")));
		TypedQuery<Usuario> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public boolean deletarUsuario(Usuario usuario) {
		try {
			em.getTransaction().begin();
			em.remove(usuario);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}