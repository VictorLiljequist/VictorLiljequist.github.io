package com.library.app.library.app;

import java.util.ArrayList;

public class Library {
	String name;
	ArrayList<Book> books;
	
	public Library(String name, ArrayList<Book> books) {
		this.name = name;
		this.books = books;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public ArrayList<Book> getBooks() {
		return this.books;
	}
	
}
