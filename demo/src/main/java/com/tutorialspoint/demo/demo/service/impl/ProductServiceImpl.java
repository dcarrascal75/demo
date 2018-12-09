package com.tutorialspoint.demo.demo.service.impl;


import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.tutorialspoint.demo.demo.model.Book;
import com.tutorialspoint.demo.demo.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {

	
	private static Map<Long, Book> productRepo = new HashMap<>();
	
	@Override
	public void createProduct(Book product) {
		productRepo.put(product.getId(), product);
		
	}

	@Override
	public void updateProduct(Long id, Book product) {
		productRepo.remove(id);
		product.setId(id);
		productRepo.put(id, product);
		
	}

	@Override
	public void deleteProduct(Long id) {
		productRepo.remove(new Long(id));
		
	}

	@Override
	public Collection<Book> getProducts() {
		return productRepo.values();
	}
	
	private void listBooks() {
		System.out.println("-- List of books...");
		Arrays.asList(productRepo)
        .forEach(book -> System.out.println("--> " + book));
	    
	}

	@Override
	public Boolean containsKey(Long id) {
		return productRepo.containsKey(id);
	}

	@Override
	public Book getProduct(Long id) {
		return productRepo.get(id);
	}

	
}