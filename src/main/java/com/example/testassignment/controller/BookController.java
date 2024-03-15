package com.example.testassignment.controller;

import com.example.testassignment.entity.Book;
import com.example.testassignment.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/top10")
public class BookController {
    String csvFilePath = "C:\\Users\\79154\\Desktop\\goodreads_top100_from1980to2023_final.csv";
    @Autowired
    private BookService bookService;


    @GetMapping
    public ResponseEntity<?> getTopTen(@RequestParam(required = false) Integer year,
                                               @RequestParam String column,
                                               @RequestParam String sort) {
        if (column != null && sort != null) {
            return ResponseEntity.ok(bookService.getTopTen(year, column, sort));
        }
        else {
            return ResponseEntity.badRequest().body("Missing required parameters");
        }
    }
}
