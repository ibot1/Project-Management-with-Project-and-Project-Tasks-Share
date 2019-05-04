package ibot.komo.tobi.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Projects {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id = 0;
	
	@NotBlank(message = "Project name is required")
	private String projectName;
	
	@NotBlank(message = "Project Identifier is required")
	@Size(min = 4, max = 5, message = "Please use 4 to 5 characters")
	@Column(updatable = false, unique = true)
	private String projectIdentifier;
	
	@NotBlank(message = "Project description is required")
	private String description;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date start_date;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date end_date;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date created_At;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updated_At;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="creator",nullable=false)
	@JsonIgnore
	private Users users;
	
	private String projectLeader;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projects")
	@JsonIgnore
	List<ProjectTasks> projectTasks = new ArrayList<ProjectTasks>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
	@JsonIgnore
	List<ProjectAccess> projectAccess = new ArrayList<ProjectAccess>();
	
	private int counter = 0;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Date created_At) {
		this.created_At = created_At;
	}

	public Date getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		this.updated_At = updated_At;
	}


	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	@PrePersist
	protected void onCreatedAt() {
		created_At = new Date();
	}
	
	@PreUpdate
	protected void onUpdatedAt() {
		updated_At = new Date();
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public List<ProjectTasks> getProjectTasks() {
		return projectTasks;
	}

	public void setProjectTasks(List<ProjectTasks> projectTasks) {
		this.projectTasks = projectTasks;
	}

	public List<ProjectAccess> getProjectAccess() {
		return projectAccess;
	}

	public void setProjectAccess(List<ProjectAccess> projectAccess) {
		this.projectAccess = projectAccess;
	}
	
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}	

}
