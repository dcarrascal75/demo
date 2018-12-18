package com.tutorialspoint.demo.demo.model;

import org.springframework.hateoas.ResourceSupport;

public class BookWrapper extends ResourceSupport {
    private Long bookid;
    private String title;
 
    public BookWrapper (Book b) {
    	this.bookid=b.getId();
    	this.title=b.getTitle();
    }
  
    public String getTitle() {
        return title;
    }
 
    
    public Long getBookid() {
		return bookid;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	public void setTitle(String title) {
        this.title = title;
    }
 
    @Override
    public String toString() {
        return "Book{" +
                "id=" + bookid +
                ", title='" + title + '\'' +
                '}';
    }
    
}