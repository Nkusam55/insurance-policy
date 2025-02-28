package com.ipms.policy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Welcome")
public class WelcomController {

	@GetMapping("/welcome")
	public String welcome() {
      return "Welcome, now you are in public access";
	}
}
