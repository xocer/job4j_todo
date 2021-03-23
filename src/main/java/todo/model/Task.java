package todo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private LocalDateTime created;
    private boolean done;

    public Task(String description, LocalDateTime created, boolean done) {
        this.description = description;
        this.created = created;
        this.done = done;
    }
}
