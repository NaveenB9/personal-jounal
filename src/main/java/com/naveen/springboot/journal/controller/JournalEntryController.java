/*
package com.naveen.springboot.journal.controller;

import com.naveen.springboot.journal.entity.JournalEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class JournalEntryController {

  private Map<Long, JournalEntity> journalEntries = new HashMap<>();

@GetMapping("/journal")
  public List<JournalEntity> getAllJournalEntries(){
  return new ArrayList<>(journalEntries.values());
}
@PostMapping("/journal")
  public JournalEntity createJournalEntry(@RequestBody JournalEntity journalEntity){
    journalEntries.put(journalEntity.getId(), journalEntity);
    return journalEntity;
  }

  @GetMapping("/journal/{id}")
  public JournalEntity getJournalEntryById(@PathVariable Long id){
  return journalEntries.get(id);
  }

  @PutMapping(value = "/journal/{id}")
  public JournalEntity updateJournalEntry(@RequestBody JournalEntity journalEntity){
    journalEntries.put(journalEntity.getId(), journalEntity);
    return journalEntity;
  }
  @DeleteMapping("/journal")
  public void deleteJournalEntry(@RequestBody JournalEntity journalEntity){
  journalEntries.remove(journalEntity.getId());
  }

}
*/
