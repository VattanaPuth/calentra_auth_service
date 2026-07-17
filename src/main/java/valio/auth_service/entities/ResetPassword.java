package valio.auth_service.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ResetPassword {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String code;
	private String hashCode;
	private LocalDateTime expiredAt;
	private boolean token = false;
	
	@ManyToOne
	@JoinColumn(name = "register_id", referencedColumnName = "id")
	private Register register;
}
