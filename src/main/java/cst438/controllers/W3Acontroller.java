package cst438.controllers;

import java.util.List;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cst438.domain.User;
import cst438.domain.UserRepository;

@Controller
public class W3Acontroller {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Queue queue;

	@GetMapping("/users")
	public String getUsers(Model model) {
		System.out.println("W3AController : getUser called");
		List<User> users = userRepository.findAllOrderbyNameSequence();
		model.addAttribute("users", users);
		System.out.println("W3AController : createUser exited. User list size "+users.size());
		return "users";
	}

	@PostMapping("/users")
	public String createUser(@RequestParam("name") String name, @RequestParam("data") String data, Model model) {
		try {
			System.out.println("W3AController : createUser called");
			User u = new User(name, data);
			userRepository.save(u);

			System.out.println("W3AController queueName: "+ queue.getName());
			rabbitTemplate.convertAndSend(queue.getName(), u);

			List<User> users = userRepository.findAllOrderbyNameSequence();
			model.addAttribute("users", users);
			System.out.println("W3AController : createUser exited normal");
			return "users";
		} catch (Exception e) {
			System.out.println("W3AController : createUser exception. "+e.getMessage());
			model.addAttribute("message", e.getMessage());
			return "error";

		}
	}

}
