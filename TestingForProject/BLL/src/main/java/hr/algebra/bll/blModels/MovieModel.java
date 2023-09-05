/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.blModels;

import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

/**
 *
 * @author antev
 */
public class MovieModel {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String toString() {
        return "Movie Details{"+"MovieId=" + movieId + ", Title=" + title + ", Description=" + description + ", ReleaseYear=" + releaseYear + ", Genre=" + genre + ", AddedAt=" + addedAt + ", Duration=" + duration + ", Poster=" + poster + '}';
    }

    public MovieModel() {

    }
    
    public MovieModel(String title, String description, int releaseYear, String genre, LocalDateTime addedAt, int duration, String poster) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.addedAt = addedAt;
        this.duration = duration;
        this.poster = poster;
    }

    public MovieModel(String title, String description, int releaseYear, String genre, LocalDateTime addedAt, int duration, String poster, List<Actor> actors, List<Director> directors) {
        this(title,description,releaseYear,genre,addedAt,duration,poster);
        this.actors = actors;
        this.directors = directors;
    }
    
    public MovieModel(int movieId,String title, String description, int releaseYear, String genre, LocalDateTime addedAt, int duration, String poster, List<Actor> actors, List<Director> directors) {
        this(title,description,releaseYear,genre,addedAt,duration,poster,actors,directors);
        this.movieId = movieId;
    }

    private int movieId;

    //@NotNull(message = "Title cannot be null")
    @NotEmpty(message = "Title cannot be empty!")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters!")
    private String title;

    //@NotNull(message = "Description cannot be null")
    @NotEmpty(message = "Description cannot be empty!")
    @Size(min = 1, max = 2048, message = "Description must be between 1 and 2048 characters!")
    private String description;

    @NotNull(message = "Release year cannot be null")
    @Min(value = 18, message = "Invalid year, the date cannot be less than 1895")
    private int releaseYear;

    @NotEmpty(message = "Genre cannot be empty!")
    @Size(min = 1, max = 255, message = "Genre must be between 1 and 255 characters!")
    private String genre;

    @NotNull(message = "Date cannot be null")
    private LocalDateTime addedAt;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Movie duration must be atleast 1 minute!")
    private int duration;

    //@NotNull(message = "Poster cannot be null")
    @NotEmpty(message = "Poster cannot be empty!")
    private String poster;

    //@NotEmpty(message = "Actors list cannot be empty!")
    private List<Actor> actors;

    //@NotEmpty(message = "Directors list cannot be empty!")
    private List<Director> directors;

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

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

}
