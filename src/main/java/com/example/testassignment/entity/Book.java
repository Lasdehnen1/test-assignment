package com.example.testassignment.entity;

import java.time.LocalDate;
import java.util.List;

public class Book {
    private Long id;
    private String book;
    private String series;
    private String releaseNumber;
    private String author;
    private String description;
    private Integer numPages;
    private String format;
    private List<String> genres;
    private LocalDate publicationDate;
    private Double rating;
    private Long numberOfVoters;

    public Book() {
    }

    public Book(Long id, String book, String series, String releaseNumber, String author, String description, Integer numPages, String format, List<String> genres, LocalDate publicationDate, Double rating, Long numberOfVoters) {
        this.id = id;
        this.book = book;
        this.series = series;
        this.releaseNumber = releaseNumber;
        this.author = author;
        this.description = description;
        this.numPages = numPages;
        this.format = format;
        this.genres = genres;
        this.publicationDate = publicationDate;
        this.rating = rating;
        this.numberOfVoters = numberOfVoters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumPages() {
        return numPages;
    }

    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getNumberOfVoters() {
        return numberOfVoters;
    }

    public void setNumberOfVoters(Long numberOfVoters) {
        this.numberOfVoters = numberOfVoters;
    }
}