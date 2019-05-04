package ibot.komo.tobi.service;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class MapValidationErrorService {
	
	public ResponseEntity<?> MapValidationService(BindingResult result){
		if(result.hasErrors()) {
			HashMap<String,String> errorFields= new HashMap<String,String>();
			
			for(FieldError err : result.getFieldErrors()) {
				errorFields.put(err.getField(), err.getDefaultMessage());
			}
			return new ResponseEntity<HashMap<String,String>>(errorFields, HttpStatus.BAD_REQUEST);
		}
		
		return null;
	}
}
