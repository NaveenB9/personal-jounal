package com.naveen.springboot.journal.service;

import com.naveen.springboot.journal.entity.User;
import com.naveen.springboot.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private ObjectId testObjectId;

  @BeforeEach
  public void setUp() {
    testObjectId = new ObjectId();
    testUser = new User();
    testUser.setId(testObjectId);
    testUser.setUserName("testuser");
    testUser.setPassword("password123");
    testUser.setRoles(Arrays.asList("USER"));
  }

  // Tests for findByUserName method
  @Test
  public void findByUserName_WhenUserExists_ShouldReturnUser() {
    // Given
    when(userRepository.findByUserName("testuser")).thenReturn(testUser);

    // When
    User result = userService.findByUserName("testuser");

    // Then
    assertNotNull(result);
    assertEquals("testuser", result.getUserName());
    assertEquals("password123", result.getPassword());
    verify(userRepository, times(1)).findByUserName("testuser");
  }

  @Test
  public void findByUserName_WhenUserDoesNotExist_ShouldReturnNull() {
    // Given
    when(userRepository.findByUserName("nonexistentuser")).thenReturn(null);

    // When
    User result = userService.findByUserName("nonexistentuser");

    // Then
    assertNull(result);
    verify(userRepository, times(1)).findByUserName("nonexistentuser");
  }

  @Test
  public void findByUserName_WhenUserNameIsEmpty_ShouldReturnNull() {
    // Given
    when(userRepository.findByUserName("")).thenReturn(null);

    // When
    User result = userService.findByUserName("");

    // Then
    assertNull(result);
    verify(userRepository, times(1)).findByUserName("");
  }

  @Test
  public void findByUserName_WhenUserNameIsNull_ShouldHandleGracefully() {
    // Given
    when(userRepository.findByUserName(null)).thenReturn(null);

    // When
    User result = userService.findByUserName(null);

    // Then
    assertNull(result);
    verify(userRepository, times(1)).findByUserName(null);
  }

  // Tests for saveUser method
  @Test
  public void saveUser_WithValidUser_ShouldSaveSuccessfully() {
    // Given
    when(userRepository.save(any(User.class))).thenReturn(testUser);

    // When
    userService.saveUser(testUser);

    // Then
    verify(userRepository, times(1)).save(testUser);
  }

  @Test
  public void saveUser_WithNullUser_ShouldHandleGracefully() {
    // When & Then
    assertDoesNotThrow(() -> userService.saveUser(null));
    verify(userRepository, times(1)).save(null);
  }

  @Test
  public void saveUser_WithUserHavingNullFields_ShouldSaveSuccessfully() {
    // Given
    User userWithNullFields = new User();
    userWithNullFields.setUserName("testuser");
    when(userRepository.save(any(User.class))).thenReturn(userWithNullFields);

    // When
    userService.saveUser(userWithNullFields);

    // Then
    verify(userRepository, times(1)).save(userWithNullFields);
  }

  // Tests for getAll method
  @Test
  public void getAll_WhenUsersExist_ShouldReturnAllUsers() {
    // Given
    User user1 = new User();
    user1.setUserName("user1");
    user1.setPassword("pass1");
    
    User user2 = new User();
    user2.setUserName("user2");
    user2.setPassword("pass2");
    
    List<User> expectedUsers = Arrays.asList(user1, user2);
    when(userRepository.findAll()).thenReturn(expectedUsers);

    // When
    List<User> result = userService.getAll();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("user1", result.get(0).getUserName());
    assertEquals("user2", result.get(1).getUserName());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void getAll_WhenNoUsersExist_ShouldReturnEmptyList() {
    // Given
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    // When
    List<User> result = userService.getAll();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(userRepository, times(1)).findAll();
  }

  // Tests for getUserById method
  @Test
  public void getUserById_WhenUserExists_ShouldReturnOptionalWithUser() {
    // Given
    when(userRepository.findById(testObjectId)).thenReturn(Optional.of(testUser));

    // When
    Optional<User> result = userService.getUserById(testObjectId);

    // Then
    assertTrue(result.isPresent());
    assertEquals("testuser", result.get().getUserName());
    assertEquals(testObjectId, result.get().getId());
    verify(userRepository, times(1)).findById(testObjectId);
  }

  @Test
  public void getUserById_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
    // Given
    ObjectId nonExistentId = new ObjectId();
    when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // When
    Optional<User> result = userService.getUserById(nonExistentId);

    // Then
    assertFalse(result.isPresent());
    verify(userRepository, times(1)).findById(nonExistentId);
  }

  @Test
  public void getUserById_WithNullId_ShouldHandleGracefully() {
    // Given
    when(userRepository.findById(null)).thenReturn(Optional.empty());

    // When
    Optional<User> result = userService.getUserById(null);

    // Then
    assertFalse(result.isPresent());
    verify(userRepository, times(1)).findById(null);
  }

  // Tests for deleteUserById method
  @Test
  public void deleteUserById_WithValidId_ShouldDeleteSuccessfully() {
    // Given
    doNothing().when(userRepository).deleteById(testObjectId);

    // When
    userService.deleteUserById(testObjectId);

    // Then
    verify(userRepository, times(1)).deleteById(testObjectId);
  }

  @Test
  public void deleteUserById_WithNonExistentId_ShouldHandleGracefully() {
    // Given
    ObjectId nonExistentId = new ObjectId();
    doNothing().when(userRepository).deleteById(nonExistentId);

    // When
    assertDoesNotThrow(() -> userService.deleteUserById(nonExistentId));

    // Then
    verify(userRepository, times(1)).deleteById(nonExistentId);
  }

  @Test
  public void deleteUserById_WithNullId_ShouldHandleGracefully() {
    // Given
    doNothing().when(userRepository).deleteById(null);

    // When
    assertDoesNotThrow(() -> userService.deleteUserById(null));

    // Then
    verify(userRepository, times(1)).deleteById(null);
  }

  // Integration tests for edge cases
  @Test
  public void userService_WhenRepositoryThrowsException_ShouldPropagateException() {
    // Given
    when(userRepository.findByUserName(anyString())).thenThrow(new RuntimeException("Database error"));

    // When & Then
    assertThrows(RuntimeException.class, () -> userService.findByUserName("testuser"));
    verify(userRepository, times(1)).findByUserName("testuser");
  }

  @Test
  public void saveUser_WhenRepositoryThrowsException_ShouldPropagateException() {
    // Given
    when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Save failed"));

    // When & Then
    assertThrows(RuntimeException.class, () -> userService.saveUser(testUser));
    verify(userRepository, times(1)).save(testUser);
  }

  @Test
  public void deleteUserById_WhenRepositoryThrowsException_ShouldPropagateException() {
    // Given
    doThrow(new RuntimeException("Delete failed")).when(userRepository).deleteById(testObjectId);

    // When & Then
    assertThrows(RuntimeException.class, () -> userService.deleteUserById(testObjectId));
    verify(userRepository, times(1)).deleteById(testObjectId);
  }
}
