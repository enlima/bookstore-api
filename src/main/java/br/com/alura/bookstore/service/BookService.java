package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.dto.BookUpdateFormDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.model.Book;
import br.com.alura.bookstore.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public BookDetailsDto register(BookFormDto dto) {

        checkIfBookAlreadyExists(dto.getTitle());

        Book book = modelMapper.map(dto, Book.class);
        book.setId(null);
        book.setAuthor(authorService.getAuthor(dto.getAuthorId()));

        bookRepository.save(book);

        return modelMapper.map(book, BookDetailsDto.class);
    }

    public Page<BookDetailsDto> list(Pageable paging) {

        Page<Book> books = bookRepository.findAll(paging);
        return books.map(b -> modelMapper.map(b, BookDetailsDto.class));
    }

    public BookDetailsDto detail(Long id) {

        Book book = getBook(id);
        return modelMapper.map(book, BookDetailsDto.class);
    }

    @Transactional
    public BookDetailsDto update(BookUpdateFormDto dto) {

        Book book = getBook(dto.getId());

        if (!book.getTitle().trim().equals(dto.getTitle().trim())) {
            checkIfBookAlreadyExists(dto.getTitle());
        }

        Author author = authorService.getAuthor(dto.getAuthorId());
        book.updateInfo(dto.getTitle(), dto.getPublicationDate(), dto.getPages(), author);

        return modelMapper.map(book, BookDetailsDto.class);
    }

    public void checkIfBookAlreadyExists(String title) {

        if (bookRepository.existsByTitle(title.trim())) {
            throw new DataIntegrityViolationException("Book title '" + title.trim() + "' already exists " +
                    "associated with a different Book ID!");
        }
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Informed book " +
                "(ID: " + id + ") not found!"));
    }

    public boolean existsBookByAuthor(Author author) {
        return bookRepository.existsByAuthor(author);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
