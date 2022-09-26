package com.estudo.secao20.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.estudo.secao20.data.vo.v1.PersonVO;
import com.estudo.secao20.services.PersonServices;
import com.estudo.secao20.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// SWAGGER ROUTE: /swagger-ui/index.html

@RestController
@RequestMapping("/api/v1/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {

  // O spring cuida da instaciacao e injecao de dependencia
  @Autowired 
  private PersonServices services;
  // private PersonServices services = new PersonServices();

  @CrossOrigin(origins = "http://localhost:8080")
  @GetMapping(
    value = "/{id}", 
    produces = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Find a Person", 
    description = "Finds a Person",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "Success", 
        responseCode = "200", 
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public PersonVO findById(@PathVariable(value = "id") Long id) {
    return services.findById(id);
  }

  // @RequestMapping
  @GetMapping(
    produces = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Finds all People", 
    description = "Finds all People",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "Success", 
        responseCode = "200", 
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(
              schema = @Schema(implementation = PersonVO.class)
            )
          )
        }
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
    @RequestParam(value = "page", defaultValue = "0") Integer page,
    @RequestParam(value = "size", defaultValue = "12") Integer size,
    @RequestParam(value = "direction", defaultValue = "asc") String direction
  ) {
    var sortDirection = "desc".equalsIgnoreCase(direction) 
      ? Direction.DESC : Direction.ASC;
    
    Pageable pageable = PageRequest.of(
      page, 
      size, 
      Sort.by(sortDirection, "firstName")
    );

    return ResponseEntity.ok(services.findAll(pageable));
  }

  // @RequestMapping
  @GetMapping(
    value = "/findPersonByName/{firstName}",
    produces = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Finds People by name", 
    description = "Finds People by name",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "Success", 
        responseCode = "200", 
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(
              schema = @Schema(implementation = PersonVO.class)
            )
          )
        }
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonsByName(
    @PathVariable(value = "firstName") String firstName,
    @RequestParam(value = "page", defaultValue = "0") Integer page,
    @RequestParam(value = "size", defaultValue = "12") Integer size,
    @RequestParam(value = "direction", defaultValue = "asc") String direction
  ) {
    var sortDirection = "desc".equalsIgnoreCase(direction) 
      ? Direction.DESC : Direction.ASC;
    
    Pageable pageable = PageRequest.of(
      page, 
      size, 
      Sort.by(sortDirection, "firstName")
    );

    return ResponseEntity.ok(services.findPersonsByName(firstName, pageable));
  }

  @CrossOrigin(origins = { "http://localhost:8080", "https://devjoao.tech" })
  // @RequestMapping
  @PostMapping(
    consumes = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    },
    produces = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Add a new Person", 
    description = "Add a new Person by passing in a JSON, XML or YML representation of Person",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "Success", 
        responseCode = "200", 
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public PersonVO create(@RequestBody PersonVO personVO) {
    return services.create(personVO);
  }

  @PutMapping(
    consumes = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    },
    produces = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Updates a new Person", 
    description = "Updates a new Person by passing in a JSON, XML or YML representation of Person",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "Updated", 
        responseCode = "200", 
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public PersonVO update(@RequestBody PersonVO personVO) {
    return services.update(personVO);
  }

  @PatchMapping(
    value = "/{id}", 
    produces = { 
      MediaType.APPLICATION_JSON, 
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Disable a specific Person by your ID", 
    description = "Disable a specific Person by your ID",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "Success", 
        responseCode = "200", 
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public PersonVO disablePerson(@PathVariable(value = "id") Long id) {
    return services.disablePerson(id);
  }

  @DeleteMapping(
    value = "/{id}" 
  )
  @Operation(
    summary = "Deletes a Person", 
    description = "Deletes a Person by Id",
    tags = { "People" },
    responses = { 
      @ApiResponse(
        description = "No Content", 
        responseCode = "204", 
        content = @Content
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
    services.delete(id);
    return ResponseEntity.noContent().build();
  }

}
