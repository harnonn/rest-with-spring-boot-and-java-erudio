package ca.com.arnon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.com.arnon.data.vo.BookVO;
import ca.com.arnon.services.BookServices;
import ca.com.arnon.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for menage Books")
public class BookController {
	
	@Autowired
	BookServices service;
	
	//FIND ALL
	@Operation(
		summary = "Find all", description = "Find all books", tags = {"Book"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
		}
	)
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	public List<BookVO> findAll() {
		return service.findAll();
	}
	
	//FIND BY ID
	@Operation(
		summary = "Find by id", description = "Find a book by its id", tags = {"Book"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
		}
	)
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	public BookVO findById(@PathVariable(value = "id")Long id) {
		return service.findById(id);
	}
	
	//CREATE
	@Operation(
		summary = "Create", description = "Creates a new Book", tags = {"Book"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
		}
	)
	@PostMapping(
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	public BookVO create(@RequestBody BookVO bookVO) {
		return service.create(bookVO);
	}

	//UPDATE
	@Operation(
		summary = "Update", description = "Updates an existing book by its id", tags = {"Book"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
		}
	)
	@PutMapping(
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	public BookVO update(@RequestBody BookVO bookVO) {
		return service.update(bookVO);
	}
	
	//DELETE
	@Operation(
		summary = "Delete", description = "Deletes a an existing book by its id", tags = {"Book"},
		responses = {
			@ApiResponse(responseCode = "204", description = "No content", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
		}
	)
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}