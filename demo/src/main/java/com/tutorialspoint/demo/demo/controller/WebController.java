package com.tutorialspoint.demo.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}
	
	
	@RequestMapping("/view-products")
	public String viewProducts() {
	return "view-products";}

	@RequestMapping("/add-products")
	public String addProducts() {
	return "add-products";}
	
}
