package com.example.testassignment.service;

import com.example.testassignment.entity.Book;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookService {
    private List<Book> books;
    @Value("${csv.file}")
    private String csvFile;

    public List<Book> getTopTen(Integer year, String column, String sort) {
        books = readCSV(csvFile);
        List<Book> filteredBooks = new ArrayList<>(books);
        if (year != null) {
            filteredBooks = filteredBooks.stream()
                    .filter(book -> book.getPublicationDate() != null)
                    .filter(book -> book.getPublicationDate().getYear() == year)
                    .collect(Collectors.toList());
        }

        Comparator<Book> comparator = null;
        switch (column) {
            case "book":
                comparator = Comparator.comparing(Book::getBook);
                break;
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            case "numPages":
                comparator = Comparator.comparing(Book::getNumPages, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "publicationDate":
                comparator = Comparator.comparing(Book::getPublicationDate, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "rating":
                comparator = Comparator.comparing(Book::getRating, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "numberOfVoters":
                comparator = Comparator.comparing(Book::getNumberOfVoters, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                return null;
        }
        if (!sort.equalsIgnoreCase("ASC") && !sort.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Invalid sort argument. Use only DESC or ASC");
        }
        if (comparator != null) {
            if (sort.equalsIgnoreCase("DESC")) {
                comparator = comparator.reversed();
            }
            filteredBooks.sort(comparator);
        } else {
            return null;
        }
        // Исключение книги со значением null из результирующего списка только при сортировке по указанным полям
        if (column.equals("book") || column.equals("author") || column.equals("publicationDate") || column.equals("rating") || column.equals("numberOfVoters")) {
            filteredBooks = filteredBooks.stream().filter(book -> getFieldValueByColumn(book, column) != null).collect(Collectors.toList());
        }
        List<Book> topTen = filteredBooks.stream().limit(10).collect(Collectors.toList());
        return topTen;
    }

    private Comparable<?> getFieldValueByColumn(Book book, String column) {
        switch (column) {
            case "book":
                return book.getBook();
            case "author":
                return book.getAuthor();
            case "numPages":
                return book.getNumPages();
            case "publicationDate":
                return book.getPublicationDate();
            case "rating":
                return book.getRating();
            case "numberOfVoters":
                return book.getNumberOfVoters();
            default:
                return null;
        }
    }

    public List<Book> readCSV(String csvFilePath) {
        List<String[]> csvData = null;
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath)).withSkipLines(1).build()) {
            csvData = reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (csvData == null || csvData.isEmpty()) {
            return Collections.emptyList();
        }

        return createBooksFromCSV(csvData);
    }

    private List<Book> createBooksFromCSV(List<String[]> csvData) {
        List<Book> books = new ArrayList<>();
        for (String[] line : csvData) {
            Book book = createBookFromCSVLine(line);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

    private Book createBookFromCSVLine(String[] csvLine) {
        try {
            Long id = Long.valueOf(csvLine[0]);
            String bookName = csvLine[2];
            String series = csvLine[3];
            String releaseNumber = csvLine[4];
            String author = csvLine[5];
            String description = csvLine[8];
            String numPages = csvLine[9];
            String format = csvLine[10];
            List<String> genres = Collections.singletonList(csvLine[11]);
            String publicationDate = csvLine[12];
            Double rating = Double.valueOf(csvLine[13]);
            Long numberOfVoters = Double.valueOf(csvLine[14]).longValue();

            LocalDate localDate = toLocalDate(publicationDate);
            Integer pages = pagesToInteger(numPages);

            Book book = new Book();
            book.setId(id);
            book.setBook(bookName);
            book.setSeries(series);
            book.setReleaseNumber(releaseNumber);
            book.setAuthor(author);
            book.setDescription(description);
            book.setNumPages(pages);
            book.setFormat(format);
            book.setGenres(genres);
            book.setPublicationDate(localDate);
            book.setRating(rating);
            book.setNumberOfVoters(numberOfVoters);
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private LocalDate toLocalDate(String dateString) {

        if (dateString == null || dateString.equals("")) {
            return null;
        }
        DateTimeFormatter formatter;
        if (dateString.startsWith("Published")) {
            formatter = DateTimeFormatter.ofPattern("'Published' MMMM d, yyyy", Locale.ENGLISH);
        } else {
            formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        }
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }

    private Integer pagesToInteger(String numPages) {
        Integer pageNum = null;
        if (numPages.matches("\\d+")) {
            pageNum = Integer.parseInt(numPages);
        } else if (numPages.contains("pages")) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(numPages);
            if (matcher.find()) {
                pageNum = Integer.parseInt(matcher.group());
            }
        }
        return pageNum;
    }
}


