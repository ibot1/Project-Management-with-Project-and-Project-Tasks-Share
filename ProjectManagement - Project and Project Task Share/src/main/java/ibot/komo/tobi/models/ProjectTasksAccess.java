package ibot.komo.tobi.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class ProjectTasksAccess {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String projectIdentifier;
	
	@Column(unique=true)
	private String projectSequence;
	
	private String username;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectTasks", nullable = false)
	@JsonIgnore
	ProjectTasks projectTasks;
	
	
}
