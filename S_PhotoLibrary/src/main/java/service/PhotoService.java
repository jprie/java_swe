package service;

import java.util.List;

import exception.ServiceException;
import model.Photo;
import repository.PhotoRepository;
import repository.PhotoRepositoryJPA;

public class PhotoService {

	
	private PhotoRepository repository = new PhotoRepositoryJPA(); 

	public void add(Photo photo) throws ServiceException {
		
		repository.add(photo);

	}


	public Photo update(Photo photo) throws ServiceException {

		return repository.update(photo);
	}

	public List<Photo> getAll() throws ServiceException {

		List<Photo> photoList = repository.getAll();
		return photoList;

	}

	
	public void delete(Photo photo) throws ServiceException {
		
		repository.delete(photo);
	}

}
