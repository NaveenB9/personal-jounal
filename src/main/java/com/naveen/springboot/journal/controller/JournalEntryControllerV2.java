package com.naveen.springboot.journal.controller;

import com.naveen.springboot.journal.entity.JournalEntity;
import com.naveen.springboot.journal.entity.User;
import com.naveen.springboot.journal.service.JournalEntryService;
import com.naveen.springboot.journal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/journal")
@Tag(name = "Journal Entry Management", description = "API endpoints for managing journal entries")
public class JournalEntryControllerV2 {

  @Autowired
  private JournalEntryService journalEntryService;

  @Autowired
  private UserService userService;

  @GetMapping("{userName}")
  @Operation(summary = "Get all journal entries for a user",
      description = "Retrieve all journal entries associated with a specific username")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieved journal entries",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = JournalEntity.class))),
      @ApiResponse(responseCode = "404",
          description = "No journal entries found for the user",
          content = @Content)
  })
  public ResponseEntity<?> getAllJournalEntriesOfUser(
      @Parameter(description = "Username to retrieve journal entries for", required = true)
      @PathVariable String userName) {
    User user = userService.findByUserName(userName);
    List<JournalEntity> journalEntries = journalEntryService.getAllJournalEntries();
    if (journalEntries != null && !journalEntries.isEmpty()) {
      return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("{userName}")
  @Operation(summary = "Create a new journal entry",
      description = "Create a new journal entry for the specified user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Journal entry created successfully",
          content = @Content),
      @ApiResponse(responseCode = "400",
          description = "Bad request - Invalid journal entry data",
          content = @Content)
  })
  public ResponseEntity<?> createJournalEntry(
      @Parameter(description = "Journal entry data to be created", required = true)
      @RequestBody JournalEntity journalEntity,
      @Parameter(description = "Username to associate the journal entry with", required = true)
      @PathVariable String userName) {
    try {
      journalEntity.setDate(LocalDateTime.now());
      journalEntryService.saveEntry(journalEntity, userName);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/id/{id}")
  @Operation(summary = "Get journal entry by ID",
      description = "Retrieve a specific journal entry using its unique identifier")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieved journal entry",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = JournalEntity.class))),
      @ApiResponse(responseCode = "404",
          description = "Journal entry not found",
          content = @Content)
  })
  public ResponseEntity<JournalEntity> getJournalEntryById(
      @Parameter(description = "Unique identifier of the journal entry", required = true)
      @PathVariable ObjectId id) {
    Optional<JournalEntity> journalEntryById = journalEntryService.getJournalEntryById(id);
    return journalEntryById.map(journalEntity -> new ResponseEntity<>(journalEntity, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping(value = "/id/{userName}/{id}")
  @Operation(summary = "Update journal entry",
      description = "Update an existing journal entry with new data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Journal entry updated successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = JournalEntity.class))),
      @ApiResponse(responseCode = "404",
          description = "Journal entry not found",
          content = @Content)
  })
  public ResponseEntity<?> updateJournalEntry(
      @Parameter(description = "Unique identifier of the journal entry to update", required = true)
      @PathVariable ObjectId id,
      @Parameter(description = "Updated journal entry data", required = true)
      @RequestBody JournalEntity updateEntity,
      @Parameter(description = "Username associated with the journal entry", required = true)
      @PathVariable String userName) {
    JournalEntity journalEntity = journalEntryService.getJournalEntryById(updateEntity.getId())
        .orElse(null);
    if (journalEntity != null) {
      journalEntity.setTitle(updateEntity.getTitle() != null && !updateEntity.getTitle().isEmpty()
          ? updateEntity.getTitle() : journalEntity.getTitle());
      journalEntity.setContent(
          updateEntity.getContent() != null && !updateEntity.getContent().isEmpty()
              ? updateEntity.getContent() : journalEntity.getContent());
      journalEntryService.saveEntry(journalEntity, userName);
      return new ResponseEntity<>(journalEntity, HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/id/{userName}/{id}")
  @Operation(summary = "Delete journal entry",
      description = "Delete a journal entry by its unique identifier")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204",
          description = "Journal entry deleted successfully",
          content = @Content),
      @ApiResponse(responseCode = "404",
          description = "Journal entry not found",
          content = @Content)
  })
  public ResponseEntity<?> deleteJournalEntry(
      @Parameter(description = "Unique identifier of the journal entry to delete", required = true)
      @PathVariable ObjectId id,
      @Parameter(description = "Username associated with the journal entry", required = true)
      @PathVariable String userName) {
    journalEntryService.deleteEntityById(id, userName);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
