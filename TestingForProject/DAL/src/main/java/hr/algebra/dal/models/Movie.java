/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author antev
 */
public class Movie {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private int movieId;
    private String title;
    private String description;
    private int releaseYear;
    private String genre;
    private LocalDateTime addedAt;
    private int duration;
    private String poster;

    public Movie() {
    }

    public Movie(String title, String description, int releaseYear, String genre, LocalDateTime addedAt, int duration,String poster) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.addedAt = addedAt;
        this.duration = duration;
        this.poster = poster;
    }
    
    public Movie(String title, String description, int releaseYear, String genre, int duration,String poster) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.duration = duration;
        this.poster = poster;
    }

    public Movie(int movieId, String title, String description, int releaseYear, String genre, LocalDateTime addedAt, int duration, String poster) {
        this(title,description, releaseYear, genre, addedAt, duration, poster);
        this.movieId = movieId;
    }
    
    //Contructor needed for update
    public Movie(int movieId, String title, String description, int releaseYear, String genre, int duration, String poster) {
        this(title,description, releaseYear, genre, duration, poster);
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
    
     public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
