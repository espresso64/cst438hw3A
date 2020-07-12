package cst438.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cst438.domain.User;
import cst438.domain.UserRepository;

@RestController
public class W3ArestController {
	
	@Autowired
	UserRepository userRepository;
	
	
	/*
	 * return a list of all users 
	 */
	@GetMapping("/v1/users")
	public ResponseEntity<List<User>> getUsers(Model model) {
		System.out.println("W3AController : get /v1/users called");
		List<User> users = userRepository.findAllOrderbyNameSequence();
		model.addAttribute("users", users);
		System.out.println("W3AController : get /v1/users exited. ");
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	/* 
	 * create a new User.  No data is returned.
	 */
	@PostMapping("/v1/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		System.out.println("W3AController : post /v1/users called");
		User u = new User(user);
		userRepository.save(u);
		System.out.println("W3AController : post /v1/users exited.");
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

}
