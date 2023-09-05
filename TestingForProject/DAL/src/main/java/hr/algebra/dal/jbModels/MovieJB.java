/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.jbModels;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author antev
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "title",
    "description",
    "releaseYear",
    "genre",
    "pubDate",
    "actors",
    "director",
    "duration",
    "poster"
})
public class MovieJB {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @XmlElement(name = "title")
    private String title;

    @XmlJavaTypeAdapter(PubDateAdapter.class)
    @XmlElement(name = "pubDate")
    private LocalDateTime pubDate;

    @XmlElement(name = "description")
    private String description;

    @XmlElementWrapper(name = "redatelji")
    @XmlElement(name = "redatelj")
    private List<DirectorJB> director;

    @XmlElementWrapper(name = "glumci")
    @XmlElement(name = "glumac")
    private List<ActorJB> actors;

    @XmlElement(name = "trajanje")
    private int duration;

    @XmlElement(name = "godina")
    private int releaseYear;

    @XmlElement(name = "zanr")
    private String genre;

    @XmlElement(name = "plakat")
    private String poster;

    public MovieJB() {
    }

    public MovieJB(String title, LocalDateTime pubDate, String description, List<DirectorJB> director, List<ActorJB> actors, int duration, int releaseYear, String genre, String poster) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.director = director;
        this.actors = actors;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.poster = poster;
    }

    public static DateTimeFormatter getDATE_FORMATTER() {
        return DATE_FORMATTER;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public List<DirectorJB> getDirectors() {
        return director;
    }

    public List<ActorJB> getActors() {
        return actors;
    }

    public int getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public String getPoster() {
        return poster;
    }
}
