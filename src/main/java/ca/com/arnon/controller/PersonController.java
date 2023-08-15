package ca.com.arnon.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.com.arnon.data.vo.PersonVO;
import ca.com.arnon.services.PersonServices;
import ca.com.arnon.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for menage People")
public class PersonController {
	
	@Autowired
	private PersonServices personServices;
	
	//FIND ALL
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Operation(summary = "Finds all people", description = "Find all people", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "Succes", responseCode = "200", 
				content = {
					@Content (mediaType = "application/json",array = @ArraySchema(schema= @Schema(implementation = PersonVO.class)))	
				}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll (
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
		){
		var sortDirection = "desc".equalsIgnoreCase(direction)?Direction.DESC:Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(personServices.findAll(pageable));
	}
	
	//FIND BY NAME
	@GetMapping(value = "/findPersonsByName/{firstName}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Operation(summary = "Find people by name", description = "Find people by name", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "Succes", responseCode = "200", 
				content = {
					@Content (mediaType = "application/json",array = @ArraySchema(schema= @Schema(implementation = PersonVO.class)))	
				}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonsByName (
			
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@PathVariable(value = "firstName" ) String firstName
		){
		var sortDirection = "desc".equalsIgnoreCase(direction)?Direction.DESC:Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(personServices.findPersonsByName(firstName, pageable));
	}
	
	//FIND BY ID
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Operation(summary = "Finds a person", description = "Finds a person", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "Succes", responseCode = "200", 
				content = {
					@Content (mediaType = "application/json", schema= @Schema(implementation = PersonVO.class))	
				}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	public PersonVO findById(@PathVariable(value = "id") Long id) {
		return personServices.findById(id);
	}
	
	//CREATE
	@CrossOrigin(origins = {"http://localhost:8080", "https://erudio.com.br"})
	@PostMapping(
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	@Operation(summary = "Creates a person", description = "Creates a new person", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "Succes", responseCode = "200", 
				content = {
					@Content (mediaType = "application/json", schema= @Schema(implementation = PersonVO.class))	
				}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	public PersonVO create(@RequestBody PersonVO person) {
		return personServices.create(person);
	}
	
	//UPDATE
	@Operation(summary = "Updates a person", description = "Updates an existing person", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "Updated", responseCode = "200", 
				content = {
					@Content (mediaType = "application/json", schema= @Schema(implementation = PersonVO.class))	
				}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	@PutMapping(
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	public PersonVO update(@RequestBody PersonVO person) {
		return personServices.update(person);
	}
	
	//DISABLE
	@PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}) 
		@Operation(summary = "Disables a person", description = "Disables a person", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	public PersonVO disablePerson(@PathVariable(value = "id") Long id) {
		return personServices.disablePerson(id);
	}
	
	//DELETE
	@DeleteMapping(value = "/{id}")
		@Operation(summary = "Deletes a person", description = "Deletes a person", tags = {"People"}, 
		responses = {
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		personServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}