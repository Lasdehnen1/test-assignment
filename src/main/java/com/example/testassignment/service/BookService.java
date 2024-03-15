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
                comparator = Comparator.comparing(Book::getNumPages);
                break;
            case "publicationDate":
                comparator = Comparator.comparing(Book::getPublicationDate);
                break;
            case "rating":
                comparator = Comparator.comparing(Book::getRating);
                break;
            case "numberOfVoters":
                comparator = Comparator.comparing(Book::getNumberOfVoters);
                break;
            default:
                return null;
        }
        if (comparator != null) {
            if (sort.equalsIgnoreCase("DESC")) {
                comparator = comparator.reversed();
            }
            filteredBooks.sort(comparator);
        } else {
            return null;
        }
        List<Book> topTen = filteredBooks.subList(0, Math.min(10, filteredBooks.size()));

        return topTen;
    }

    public List<Book> readCSV(String csvFilePath) {
        List<Book> books = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath)).withSkipLines(1) // пропускаем заголовок
                .build()) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {

                Long id = Long.valueOf(nextLine[0]);
                String bookName = nextLine[2];
                String series = nextLine[3];
                String releaseNumber = nextLine[4];
                String author = nextLine[5];
                String description = nextLine[8];
                String numPages = nextLine[9];
                String format = nextLine[10];
                List<String> genres = Collections.singletonList(nextLine[11]);
                String publicationDate = nextLine[12];
                Double rating = Double.valueOf(nextLine[13]);
                Long numberOfVoters = Double.valueOf(nextLine[14]).longValue();

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
                books.add(book);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return books;
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


