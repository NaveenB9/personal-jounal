package com.naveen.springboot.journal.service;

import com.naveen.springboot.journal.entity.JournalEntity;
import com.naveen.springboot.journal.entity.User;
import com.naveen.springboot.journal.repository.JournalEntryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JournalEntryService {

  @Autowired
  private JournalEntryRepository journalEntryRepository;

  @Autowired
  private UserService userService;

  @Transactional
  public void saveEntry(JournalEntity journalEntity, String userName) {
    try {
      User user = userService.findByUserName(userName);

      journalEntity.setDate(LocalDateTime.now());
      JournalEntity saved = journalEntryRepository.save(journalEntity);
      user.getJournalEntries().add(saved);
      userService.saveUser(user);
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException("Error while saving journal entry", e);
    }
  }

  public List<JournalEntity> getAllJournalEntries() {
    return journalEntryRepository.findAll();
  }

  public Optional<JournalEntity> getJournalEntryById(ObjectId id) {
    return journalEntryRepository.findById(id);
  }

  public void deleteEntityById(ObjectId id, String userName) {
    User user = userService.findByUserName(userName);
    user.getJournalEntries().removeIf(journalEntity -> journalEntity.getId().equals(id));
    userService.saveUser(user);
    journalEntryRepository.deleteById(id);
  }

  public void deleteEntity(JournalEntity entity) {
    journalEntryRepository.delete(entity);
  }

  public void deleteAllEntities() {
    journalEntryRepository.deleteAll();
  }
}
