package com.tutorialspoint.demo.demo.service;

import java.util.Collection;
import com.tutorialspoint.demo.demo.model.Book;

public interface ProductService {
	public abstract void createProduct(Book product);

	public abstract void updateProduct(Long id, Book product);

	public abstract void deleteProduct(Long id);

	public abstract Collection<Book> getProducts();
	
	public abstract Boolean containsKey(Long id);
	
	public abstract Book getProduct(Long id);
}