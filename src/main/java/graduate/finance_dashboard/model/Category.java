package graduate.finance_dashboard.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "expenses")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreRemove
    protected void onPreRemove() {
        for (Expense expense : new ArrayList<>(expenses)) {
            expense.setCategory(null);
            expenses.remove(expense);
        }
        if (user != null) {
            user.getCategories().remove(this);
        }
    }

    public List<Expense> getExpenses() {
        return expenses != null ? expenses : new ArrayList<>();
    }
}