package repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

import application.DatabaseContext;
import exception.ServiceException;
import model.Photographer;

public class PhotographerRepositoryJPA implements PhotographerRepository {

	@Override
	public void add(Photographer photographer) throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		transaction.begin();

		em.persist(photographer);

		transaction.commit();

		System.out.println("Inserted Photographer: " + photographer.getId());

	}

	@Override
	public Photographer update(Photographer photographer) throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		transaction.begin();

		Photographer mergedPhotographer = em.merge(photographer);

		transaction.commit();

		System.out.println("Updated Photographer: " + photographer.getId());

		return mergedPhotographer;
	}

	@Override
	public List<Photographer> getAll() throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		System.out.println("Read all Photographers");
		transaction.begin();

		@SuppressWarnings("unchecked")
		// check that query was UPDATED!!!
		List<Photographer> photographers = (List<Photographer>) em.createQuery("select p from Photographer p")
				.getResultList();

		transaction.commit();

		return photographers;
	}

	@Override
	public Optional<Photographer> get(long id) throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		System.out.println("Read Photographer with id:" + id);
		transaction.begin();

		Photographer photographer = em.find(Photographer.class, id);

		transaction.commit();

		return Optional.ofNullable(photographer);

	}

	@Override
	public void delete(Photographer photographer) throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		System.out.println("Delete Photographer: " + photographer.getId());
		transaction.begin();

		if (!em.contains(photographer))
			photographer = em.merge(photographer);

		em.remove(photographer);

		try {
			transaction.commit();
		} catch (RollbackException e) {
			throw new ServiceException(e.getMessage());
		}

	}

}
