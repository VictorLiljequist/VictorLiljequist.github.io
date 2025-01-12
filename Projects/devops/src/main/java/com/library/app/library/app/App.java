package com.library.app.library.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    private boolean isLoggedIn = false;
    private User currentUser; 
    private Map<String, String> users;
    private Map<String, User> userInventory; 
    private ArrayList<Book> books;  

    public static void main(String[] args) {
        App app = new App();
        Scanner scanner = new Scanner(System.in); 

        app.usersMap(); 
        app.booksMap();  

        System.out.println("Version 3.0 of The Library!");
        System.out.println("Welcome to the Library!");

        app.login(scanner);
      
        if (app.isLoggedIn) {
            boolean continueBrowsing = true;
            while (continueBrowsing) {
                System.out.print("Would you like to borrow, return or exit? (1/2/3): ");
                String action = scanner.nextLine().toLowerCase();

                switch (action) {
                    case "1":
                        app.borrowBook(scanner);
                        break;
                    case "2":
                        app.returnBook(scanner); 
                        break;
                    case "3":
                        continueBrowsing = false;  
                        break;
                    default:
                        System.out.println("Error");
                        break;
                }
            }
            app.displayUserInventory();
            System.out.println("Thank you for visiting the library!");        
           	} else {
            System.out.println("You must log in to view the library.");
        }
        
        scanner.close(); 
    }


    public void usersMap() {
        users = new HashMap<>();
        users.put("victor", "password123");
        users.put("hermes", "password123");
        userInventory = new HashMap<>(); 
    }

    public ArrayList<Book> booksMap() {
        books = new ArrayList<>();

        books.add(new Book("Dune", "Frank Herbert", "978-0593099322", true));
        books.add(new Book("1984", "George Orwell", "978-0451524935", true));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", true));
        books.add(new Book("Moby Dick", "Herman Melville", "978-1503280786", true));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084", true)); 
        books.add(new Book("A Game of Thrones", "George R. R. Martin", "978-0553106626", true));
        
        return books;
    }

    public void displayBooks() {
        int i = 1;
        for (Book book : books) {
            System.out.println(i + ".");
            System.out.println("Title:  " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("ISBN:   " + book.getIsbn());
            System.out.println("Available: " + (book.isAvailable() ? "Yes" : "No"));
            i++;
        }
    }

    public void borrowBook(Scanner scanner) {
    	displayBooks();
        System.out.print("Enter the number of the book you want to borrow: ");
        int bookNumber = scanner.nextInt();
        scanner.nextLine();

        if (bookNumber > 0 && bookNumber <= books.size()) {
            Book selectedBook = books.get(bookNumber - 1);

            if (selectedBook.isAvailable()) {
                selectedBook.setAvailable(false);
                currentUser.getInventory().addBook(selectedBook); 
                System.out.println("You have successfully borrowed \"" + selectedBook.getTitle() + "\".");
                currentUser.getInventory().displayBorrowedBooks(); 
            } else {
                System.out.println("Sorry, \"" + selectedBook.getTitle() + "\" is currently unavailable.");
            }
        } else {
            System.out.println("Invalid book number.");
        }
    }
    
    // For testing
    public void borrowBook(int indexx) {
 
        int bookNumber = indexx;

        if (bookNumber > 0 && bookNumber <= books.size()) {
            Book selectedBook = books.get(bookNumber - 1);

            if (selectedBook.isAvailable()) {
                selectedBook.setAvailable(false);
                currentUser.getInventory().addBook(selectedBook); 
            } else {
                System.out.println("Sorry, \"" + selectedBook.getTitle() + "\" is currently unavailable.");
            }
        } else {
            System.out.println("Invalid book number.");
        }
    }

    public void returnBook(Scanner scanner) {
        System.out.println("You can return the following borrowed books:");
        currentUser.getInventory().displayBorrowedBooks();

        System.out.print("Enter the number of the book you want to return: ");
        int bookNumber = scanner.nextInt();
        scanner.nextLine();

        if (bookNumber > 0 && bookNumber <= currentUser.getInventory().getBorrowedBooks().size()) {
            Book returnedBook = currentUser.getInventory().getBorrowedBooks().get(bookNumber - 1);
            returnedBook.setAvailable(true);
            currentUser.getInventory().removeBook(returnedBook); 
            System.out.println("You have successfully returned \"" + returnedBook.getTitle() + "\".");
        } else {
            System.out.println("Invalid book number.");
        }
    }
    
    // For testing
    public void returnBook(int index) {

        int bookNumber = index;

        if (bookNumber > 0 && bookNumber <= currentUser.getInventory().getBorrowedBooks().size()) {
            Book returnedBook = currentUser.getInventory().getBorrowedBooks().get(bookNumber - 1);
            returnedBook.setAvailable(true);
            currentUser.getInventory().removeBook(returnedBook); 
        } else {
            System.out.println("Invalid book number.");
        }
    }

    // For testing purposes
    public void setCurrentUser(User user) {
    	currentUser = user;
    }

    public void displayUserInventory() {
        currentUser.getInventory().displayBorrowedBooks(); 
    }

    public void login(Scanner scanner) {
        int attempts = 0;
        int max_attempts = 3;  

        while (attempts < max_attempts) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();

            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            if (authenticate(username, password)) {
                System.out.println("Login successful! Welcome, " + username + ".");
                isLoggedIn = true;
                currentUser = new User(username);
                userInventory.put(username, currentUser);  
                break; 
            } else {
                attempts++;
                System.out.println("Login failed. Incorrect username or password.");
                if (attempts < max_attempts) {
                    System.out.println("Try again (" + (max_attempts - attempts) + " attempts left).");
                } else {
                    System.out.println("You are banned! Closing program.");
                }
            }
        }
    }

    private boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
    
}






