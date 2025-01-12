package com.library.app.library.app;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Book> borrowedBooks;

    public Inventory() {
        borrowedBooks = new ArrayList<>();
    }

    public void addBook(Book book) {
        borrowedBooks.add(book);
    }

    public void removeBook(Book book) {
        borrowedBooks.remove(book);
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void displayBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("You have not borrowed any books.");
            return;
        }
        
        System.out.println("Your borrowed books:");
        for (int i = 0; i < borrowedBooks.size(); i++) {
            Book book = borrowedBooks.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " by " + book.getAuthor());
        }
    }
}
