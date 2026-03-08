package com.naveen.springboot.journal.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntity {

  @Id
  private ObjectId id;
  private String title;
  private String content;
  private LocalDateTime date;

}
