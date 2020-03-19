package repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import application.DatabaseContext;
import exception.ServiceException;
import model.Photo;

public class PhotoRepositoryJPA implements PhotoRepository {


	@Override
	public void add(Photo photo) throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		transaction.begin();

		em.persist(photo);

		transaction.commit();

		System.out.println("Inserted Photo: " + photo.getId());

	}

	@Override
	public Photo update(Photo photo) throws ServiceException {
		
		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		transaction.begin();

		Photo mergedPhoto = em.merge(photo);

		transaction.commit();

		System.out.println("Updated Photo: " + photo.getId());

		return mergedPhoto;
	}

	@Override
	public List<Photo> getAll() throws ServiceException {

		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		System.out.println("Read all Photos");
		transaction.begin();

		@SuppressWarnings("unchecked")
		List<Photo> photos = (List<Photo>) em.createQuery("select p from Photo p").getResultList();

		transaction.commit();

		return photos;
	}

	@Override
	public Optional<Photo> get(long id) throws ServiceException {
		
		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		System.out.println("Read Photo");
		transaction.begin();

		Photo photo = em.find(Photo.class, id);

		transaction.commit();

		return Optional.ofNullable(photo);

	}

	@Override
	public void delete(Photo photo) throws ServiceException {
		
		EntityManager em = DatabaseContext.getEntityManager();

		EntityTransaction transaction = em.getTransaction();

		System.out.println("Delete Photo: " + photo.getId());
		transaction.begin();

		if (!em.contains(photo))
			photo = em.merge(photo);

		em.remove(photo);

		transaction.commit();

	}

}
