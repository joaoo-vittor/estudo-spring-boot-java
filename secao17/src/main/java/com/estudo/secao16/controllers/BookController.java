package com.estudo.secao16.controllers;

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

import com.estudo.secao16.data.vo.v1.BookVO;
import com.estudo.secao16.services.BookServices;
import com.estudo.secao16.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/book")
@Tag(name = "Book", description = "Endpoints for Managing Books")
public class BookController {
  
  @Autowired
  private BookServices services;

  @GetMapping(
    value = "/{id}",
    produces = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Find a Book",
    description = "Finds a Book",
    tags = { "Book" },
    responses = {
      @ApiResponse(
        description = "Success",
        responseCode = "200",
        content = @Content(
          schema = @Schema(implementation = BookVO.class)
        )
      ),
      @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public BookVO findById(@PathVariable(value = "id") Long id) {
    return services.findById(id);
  }


  @GetMapping(
    produces = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YAML
    }
  )
  @Operation(
    summary = "Find all Books",
    description = "Finds all Books",
    tags = { "Book" },
    responses = {
      @ApiResponse(
        description = "Success",
        responseCode = "200",
        content = @Content(
          mediaType = "application/json",
          array = @ArraySchema(
            schema = @Schema(implementation = BookVO.class)
          )
        )
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public List<BookVO> findAll() {
    return services.findAll();
  }

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
    summary = "Add a new Book", 
    description = "Add a new Book by passing in a JSON, XML or YML representation of Book",
    tags = { "Book" },
    responses = { 
      @ApiResponse(
        description = "Success", 
        responseCode = "200", 
        content = @Content(schema = @Schema(implementation = BookVO.class))
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public BookVO create(@RequestBody BookVO bookVO) {
    return services.create(bookVO);
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
    summary = "Updates a new Book", 
    description = "Updates a new Book by passing in a JSON, XML or YML representation of Book",
    tags = { "Book" },
    responses = { 
      @ApiResponse(
        description = "Updated", 
        responseCode = "200", 
        content = @Content(schema = @Schema(implementation = BookVO.class))
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public BookVO update(@RequestBody BookVO bookVO) {
    return services.update(bookVO);
  }

  @DeleteMapping(
    value = "/{id}"
  )
  @Operation(
    summary = "Deletes a Book", 
    description = "Deletes a Book by Id",
    tags = { "Book" },
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
  public ResponseEntity<?> deleteById(@PathParam(value = "id") Long id) {
    services.delete(id);
    return ResponseEntity.noContent().build();
  }

}
