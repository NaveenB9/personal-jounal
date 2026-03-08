package com.naveen.springboot.journal.controller;

import com.naveen.springboot.journal.entity.User;
import com.naveen.springboot.journal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@RequestMapping("/api")
@Tag(name = "User Management", description = "API endpoints for managing users in the journal application")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/users")
  @Operation(summary = "Get all users",
      description = "Retrieve a list of all registered users in the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieved list of users",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = User.class)))),
      @ApiResponse(responseCode = "500",
          description = "Internal server error",
          content = @Content)
  })
  public List<User> getAll() {
    return userService.getAll();
  }

  @PostMapping("/users")
  @Operation(summary = "Create a new user",
      description = "Register a new user in the journal application")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "User created successfully",
          content = @Content),
      @ApiResponse(responseCode = "400",
          description = "Bad request - Invalid user data or user already exists",
          content = @Content)
  })
  public ResponseEntity<?> saveUser(
      @Parameter(description = "User data to be created", required = true)
      @RequestBody User user) {
    try {
      userService.saveUser(user);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{userName}")
  @Operation(summary = "Update user information",
      description = "Update an existing user's information by username")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "User updated successfully",
          content = @Content),
      @ApiResponse(responseCode = "404",
          description = "User not found",
          content = @Content),
      @ApiResponse(responseCode = "400",
          description = "Bad request - Invalid user data",
          content = @Content)
  })
  public ResponseEntity<?> updateUser(
      @Parameter(description = "Updated user data", required = true)
      @RequestBody User user,
      @Parameter(description = "Username of the user to update", required = true)
      @PathVariable String userName) {
    User userInDb = userService.findByUserName(userName);
    if (userInDb != null) {
      userInDb.setUserName(user.getUserName());
      userInDb.setPassword(user.getPassword());
      userService.saveUser(userInDb);
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{userId}")
  @Operation(summary = "Delete user",
      description = "Delete a user from the system by user ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "User deleted successfully",
          content = @Content),
      @ApiResponse(responseCode = "404",
          description = "User not found",
          content = @Content),
      @ApiResponse(responseCode = "400",
          description = "Bad request - Invalid user ID",
          content = @Content)
  })
  public ResponseEntity<?> deleteUser(
      @Parameter(description = "ID of the user to delete", required = true)
      @PathVariable ObjectId userId) {
    try {
      userService.deleteUserById(userId);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
