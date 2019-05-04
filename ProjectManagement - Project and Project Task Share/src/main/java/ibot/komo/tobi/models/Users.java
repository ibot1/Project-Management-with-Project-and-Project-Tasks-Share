package ibot.komo.tobi.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Users implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Email(message = "Username needs to be an email")
	@NotBlank(message = "username is required")
	@Column(unique=true)
	private String username;
	
	@NotBlank(message = "Please enter your full name")
	private String fullname;
	
	@NotBlank(message = "Password field is required")
	private String password;
	
	private Date created_At, updated_At;
	
	
	
	@Transient
	private String confirmPasswd;
	
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "users", orphanRemoval = true)
	private List<Projects> projects = new ArrayList<>();
	
	

	public Users() {
		
	}
	
	public List<Projects> getProjects() {
		return projects;
	}



	public void setProjects(List<Projects> projects) {
		this.projects = projects;
	}

	
	public String getConfirmPasswd() {
		return confirmPasswd;
	}



	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	

	@PrePersist
	protected void onCreate() {
		this.created_At = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_At = new Date();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", fullname=" + fullname + ", password=" + password
				+ ", confirmPassword=" + confirmPasswd + ", created_At=" + created_At + ", updated_At=" + updated_At
				+ "]";
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
 
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
