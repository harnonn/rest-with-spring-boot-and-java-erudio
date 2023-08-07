package ca.com.arnon.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.com.arnon.controller.BookController;
import ca.com.arnon.data.vo.BookVO;
import ca.com.arnon.exeptions.RequiredObjectIsNullException;
import ca.com.arnon.exeptions.ResourceNotFoundException;
import ca.com.arnon.exeptions.WrongHttpMethodException;
import ca.com.arnon.mapper.DozerMapper;
import ca.com.arnon.model.Book;
import ca.com.arnon.repositories.BookRepository;

@Service
public class BookServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	BookRepository repository;

	public BookServices() {
	}
	
	public List<BookVO> findAll() {
		logger.info("Finding all the books!");
		List<Book> books = repository.findAll();
		List<BookVO> booksVO = DozerMapper.parseListObjects(books, BookVO.class);
		booksVO.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getId())).withSelfRel()));
		return booksVO;
	}
	
	public BookVO findById(Long id) {
		logger.info("Finding a book");
		Book book = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		BookVO bookVO = DozerMapper.parseObject(book, BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return bookVO;
	}

	public BookVO create(BookVO bookVO) {
		if (bookVO == null) throw new RequiredObjectIsNullException();
		if (bookVO.getId() != null) throw new WrongHttpMethodException("You cannot specify the id for a new record. To update an existing record use http PUT");
		logger.info("Creating one book!");
		Book book = DozerMapper.parseObject(bookVO, Book.class);
		repository.save(book);
		BookVO bookVOReturn = DozerMapper.parseObject(book, BookVO.class);
		bookVOReturn.add(linkTo(methodOn(BookController.class).findById(bookVOReturn.getId())).withSelfRel());
		return bookVOReturn;
	}
	
	public BookVO update(BookVO bookVO) {
		if (bookVO == null) throw new RequiredObjectIsNullException();
		logger.info("Updating a book!");
		repository.findById(bookVO.getId()).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		Book book = DozerMapper.parseObject(bookVO, Book.class);
		repository.save(book);
		BookVO bookVOReturn = DozerMapper.parseObject(book, BookVO.class);
		bookVOReturn.add(linkTo(methodOn(BookController.class).findById(bookVOReturn.getId())).withSelfRel());
		return bookVOReturn;
	}
	
	public void delete (Long id) {
		logger.info("Deleting a book");
		Book book = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		repository.delete(book);
	}
}
