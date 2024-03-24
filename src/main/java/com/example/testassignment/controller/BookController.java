package com.example.testassignment.controller;

import com.example.testassignment.entity.Book;
import com.example.testassignment.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/top10")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> getTopTen(@RequestParam(required = false) Integer year,
                                       @RequestParam String column,
                                       @RequestParam String sort) {
        try {
            if (!isValidColumn(column)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный параметр. Доступные значения: book, author, numPages, publicationDate, rating, numberOfVoters"); //
                }
            if (!isValidSort(sort)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверные значения сортировки по возрастанию/убыванию. Возможные значения: ASC, DESC");
            }
                List<Book> topTen = bookService.getTopTen(year, column, sort);
                return ResponseEntity.ok(topTen);

        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("Invalid sort")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.ok().build(); // Возвращаем 200 OK с пустым телом
            }
        } catch (Exception e) { // Обрабатываем другие исключения
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращаем 500 Internal Server Error
        }
    }
    private boolean isValidColumn(String column) {
        return column.equals("book") || column.equals("author")
                || column.equals("numPages") || column.equals("publicationDate")
                || column.equals("rating") || column.equals("numberOfVoters");
    }
    private boolean isValidSort(String sort) {
        return sort.equalsIgnoreCase("ASC") || sort.equalsIgnoreCase("DESC");
    }
}
