package graduate.finance_dashboard.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"category", "user"})
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
        if (category != null) {
            category.getExpenses().remove(this);
        }
        if (user != null) {
            user.getExpenses().remove(this);
        }
    }

    public void setCategory(Category newCategory) {
        if (this.category != null && !this.category.equals(newCategory)) {
            this.category.getExpenses().remove(this);
        }
        
        this.category = newCategory;

        if (newCategory != null && !newCategory.getExpenses().contains(this)) {
            newCategory.getExpenses().add(this);
        }
    }
}
