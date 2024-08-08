package coollaafi.wot.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime created_at;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updated_at;

	public LocalDateTime getCreatedAt() {
		return created_at;
	}

	public LocalDateTime getUpdatedAt() {
		return updated_at;
	}
}
