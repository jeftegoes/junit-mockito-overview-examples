package com.example.repositories;

import com.example.entities.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookImplTest {
    @Mock
    private EntityManager entityManager;

    @Mock
    TypedQuery<Book> query;

    @InjectMocks
    private JpaBookRepository jpaBookRepository;

    @Test
    public void testSaveMethod() {
        Book book = new Book("Test", "Test", 33.3f);

        jpaBookRepository.save(book);

        verify(entityManager, times(1)).persist(book);
    }

    @Test
    public void testFindById() {
        int expectedBookId = 10;
        Book expectedBook = new Book("Test", "Test", 33.2f);
        expectedBook.setId(expectedBookId);

        when(entityManager.find(Book.class, expectedBookId)).thenReturn(expectedBook);

        Book bookInDb = jpaBookRepository.findById(expectedBookId);

        assertEquals(expectedBook, bookInDb);
    }

    @Test
    public void testFindAll() {
        List<Book> expectedBooks = Arrays.asList(
                new Book("Book 1", "Author 1", 10.10f),
                new Book("Book 2", "Author 2", 20.20f),
                new Book("Book 3", "Author 3", 30.30f)
        );

        when(entityManager.createQuery("select s FROM Book s ORDER BY s.name ASC", Book.class)).thenReturn(query);

        when(query.getResultList()).thenReturn(expectedBooks);

        List<Book> booksInDb = jpaBookRepository.findAll();

        assertEquals(expectedBooks, booksInDb);
    }

    @Test
    public void testFindByName() {
        List<Book> expectedBooks = Arrays.asList(
                new Book("Book 1", "Author 1", 10.10f),
                new Book("Book 2", "Author 2", 20.20f),
                new Book("Book 3", "Author 3", 30.30f)
        );

        when(entityManager.createQuery("select s FROM Book s WHERE s.name LIKE :name", Book.class)).thenReturn(query);

        String name = "Book 1";

        query.setParameter("name", "%" + name + "%");

        when(query.getResultList()).thenReturn(expectedBooks);

        List<Book> booksInDb = jpaBookRepository.findByName(name);

        assertEquals(expectedBooks, booksInDb);
    }

    @Test
    public void testUpdate() {
        Book originalBook = new Book("Book 1", "Author 1", 10.10f);
        Book bookToUpdate = new Book("Book to Modify", "Author to Modify", 10.10f);

        int bookToUpdateId = 1;
        originalBook.setId(bookToUpdateId);
        bookToUpdate.setId(bookToUpdateId);

        when(entityManager.find(Book.class, bookToUpdateId)).thenReturn(originalBook);

//        originalBook.setName(bookToUpdate.getName());
//        originalBook.setAuthor(bookToUpdate.getAuthor());
//        originalBook.setPrice(bookToUpdate.getPrice());

        jpaBookRepository.update(bookToUpdateId, bookToUpdate);

        verify(entityManager, times(1)).merge(originalBook);
        verify(entityManager, times(1)).find(Book.class, bookToUpdateId);
    }
}