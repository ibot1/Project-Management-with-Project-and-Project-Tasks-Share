 package ibot.komo.tobi.web;

import javax.validation.Valid;

import static ibot.komo.tobi.security.SecurityConstants.TOKEN_PREFIX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibot.komo.tobi.models.Users;
import ibot.komo.tobi.payload.JWTLoginSuccessResponse;
import ibot.komo.tobi.payload.LoginRequest;
import ibot.komo.tobi.security.JwtTokenProvider;
import ibot.komo.tobi.service.MapValidationErrorService;
import ibot.komo.tobi.service.UserService;
import ibot.komo.tobi.validator.UserValidator;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		
		ResponseEntity<?> errMap = mapValidationErrorService.MapValidationService(result);
		
		if(errMap != null)	return errMap;
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new JWTLoginSuccessResponse(true,jwt));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody Users user, BindingResult result){
	
		userValidator.validate(user, result);

		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		
		if(errorMap != null)	return errorMap;
		
		Users newUser = userService.saveUser(user);
		
		return new ResponseEntity<Users>(newUser, HttpStatus.CREATED);
		
	}
}
