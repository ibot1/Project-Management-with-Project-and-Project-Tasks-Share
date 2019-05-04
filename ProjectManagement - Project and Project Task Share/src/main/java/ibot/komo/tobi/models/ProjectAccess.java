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
public class ProjectAccess {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id = 0;
	
	@Column(nullable=false)
	private String username;
	
	private String projectIdentifier;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="projectIds",nullable=false)
	@JsonIgnore
	private Projects project;
	
	public ProjectAccess() {}
	public ProjectAccess(ProjectAccess projectAccess) {
		this.username = projectAccess.username;
		this.projectIdentifier = projectAccess.projectIdentifier;
		this.project = projectAccess.project;
	}
}
