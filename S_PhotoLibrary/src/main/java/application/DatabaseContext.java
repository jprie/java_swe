package application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * DatabaseContext is an optimization for not creating multiple connections to the database
 * The connection can be setup brought down within the application package only.
 * Any repository can access the static EntityManager read-only i.e. but not change it.
 * @author jprie
 *
 */
public class DatabaseContext {

	private static final String PERSISTENCE_UNIT_NAME = "PhotoDBJPA2";
	private static EntityManagerFactory emf;
	private static EntityManager em;
	
	static void setupDatabase() {
		
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
	}
	
	static void bringDownDatabase() {
		
		em.close();
		emf.close();
	}
	
	public static EntityManager getEntityManager() {
		
		return em;
	}
}
