package com.library.app.library.app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	App app = new App();
	
	ArrayList<Book> testBooks = new ArrayList<>();
	
	@BeforeEach
	void setUp() {
		app.usersMap();
        testBooks = app.booksMap();
	}

    /**
     * Rigorous Test :-)
     */
    
	@Test
    void testBooksMapInitialAvailability() {
        for (Book book : testBooks) {
            assertTrue(book.isAvailable(), "Book should initially be available");
        }
    }
	
	@Test
    void testBorrowBookSuccess() {
        Book book = testBooks.get(0);
        User user = new User("victor");
        app.setCurrentUser(user);
        
        assertTrue(book.isAvailable(), "Book should be available before borrowing");
        
        app.borrowBook(1);
        
        assertFalse(book.isAvailable(), "Book should be unavailable after borrowing");
        assertTrue(user.getInventory().getBorrowedBooks().contains(book), "Book should be in user's inventory");
    }

	@Test
    void testReturnBookSuccess() {
        Book book = testBooks.get(0);
        User user = new User("victor");
        app.setCurrentUser(user);
        app.borrowBook(1);
        
        app.returnBook(1);
        
        assertTrue(book.isAvailable(), "Book should be available after return");
        assertFalse(user.getInventory().getBorrowedBooks().contains(book), "Book should be removed from user's inventory");
    }
	
}
