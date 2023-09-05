/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.parsers;

import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.bll.services.ActorService;
import hr.algebra.bll.services.DirectorService;
import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Person;
import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.utilities.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author antev
 */
public class MovieParser {

    private final ActorService _actorService;
    private final DirectorService _directorService;
    private final DuplicateManagementService _dupeMgmt;
    private final Set<String> processedUrls;

    private static final String RSS_URL = "https://www.blitz-cinestar-bh.ba/rss.aspx?id=2682";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    public MovieParser() throws SQLException {
        _actorService = new ActorService();
        _directorService = new DirectorService();
        _dupeMgmt = new DuplicateManagementService();
        processedUrls = new HashSet<>();
    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        PUB_DATE("pubDate"),
        DESCRIPTION("description"),
        DIRECTOR("redatelj"),
        ACTORS("glumci"),
        DURATION("trajanje"),
        RELEASE_YEAR("godina"),
        GENRE("zanr"),
        POSTER("plakat");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

    public List<MovieModel> parse() throws IOException, XMLStreamException, Exception {
        processedUrls.clear();
        List<MovieModel> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);
        try (InputStream is = con.getInputStream()) { // stream will close the connection
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Optional<TagType> tagType = Optional.empty();
            MovieModel movie = null;
            StartElement startElement = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        if (tagType.isPresent() && tagType.get().equals(TagType.ITEM)) {
                            movie = new MovieModel();
                            movies.add(movie);
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        if (tagType.isPresent() && movie != null) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            switch (tagType.get()) {
                                case TITLE -> {
                                    if (!data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                }
                                case PUB_DATE -> {
                                    if (!data.isEmpty()) {
                                        LocalDateTime publishedDate = LocalDateTime.parse(data, DateTimeFormatter.RFC_1123_DATE_TIME);
                                        movie.setAddedAt(publishedDate);
                                    }
                                }
                                case DESCRIPTION -> {
                                    if (!data.isEmpty()) {
                                        // Remove HTML tags
                                        String cleanData = data.replaceAll("<.*?>", "");
                                        movie.setDescription(cleanData);
                                    }
                                }
                                case GENRE -> {
                                    if (!data.isEmpty()) {
                                        movie.setGenre(data);
                                    }
                                }
                                case DURATION -> {
                                    if (!data.isEmpty()) {
                                        int duration = Integer.parseInt(data);
                                        movie.setDuration(duration);
                                    }
                                }
                                case RELEASE_YEAR -> {
                                    if (!data.isEmpty()) {
                                        int releaseYear = Integer.parseInt(data);
                                        movie.setReleaseYear(releaseYear);
                                    }
                                }
                                case POSTER -> {
                                    if (startElement != null && movie.getPoster() == null) {
                                        String content = event.asCharacters().getData();
                                        if (content != null && !content.trim().isEmpty()) {
                                            handlePicture(movie, content, movie.getTitle());
                                            //System.out.println("Poster URL: " + content);
                                        }
                                    }
                                }
                                case DIRECTOR -> {
                                    if (!data.isEmpty()) {
                                        List<String> directorList = Arrays.asList(data.split(", "));
                                        List<Director> directors = new ArrayList<>();

                                        for (String fullName : directorList) {
                                            String[] names = fullName.split(" ", 2); // Split into 2 
                                            if (names.length != 2) {
                                                continue;
                                            }
                                            try {
                                                String firstName = names[0];
                                                String lastName = names[1];
                                                int directorId;
                                                if (!_dupeMgmt.checkIfPersonExists(firstName, lastName)) {
                                                    directorId = _directorService.createDirector(new Person(firstName, lastName));
                                                } else {
                                                    int personId = _dupeMgmt.getPersonID(firstName, lastName);
                                                    if (!_dupeMgmt.checkIfDPRelationExists(personId)) {
                                                        _directorService.createDirectorByID(personId);
                                                    }
                                                    directorId = _dupeMgmt.getDirectorID(personId);
                                                }
                                                Director director = _directorService.SelectDirector(directorId);
                                                if (director != null) {
                                                    directors.add(director);
                                                }
                                            } catch (Exception exception) {
                                                System.out.println("DIRECTOR PARSING" + exception);
                                            }
                                        }
                                        movie.setDirectors(directors);
                                    }
                                }
                                case ACTORS -> {
                                    if (data == null || data.trim().isEmpty()) {
                                        movie.setActors(new ArrayList<>());
                                        //System.out.println(movie.getTitle() + " has no actors.");
                                    } else {
                                        List<String> actorList = Arrays.asList(data.split(", "));
                                        List<Actor> actors = new ArrayList<>();

                                        for (String fullName : actorList) {
                                            String[] names = fullName.split(" ", 2); // Split into 2 
                                            if (names.length < 2) {
                                                //the name is not split correctly
                                                continue;
                                            }

                                            try {
                                                String firstName = names[0];
                                                String lastName = names[1];
                                                int actorId = 0;
                                                if (!_dupeMgmt.checkIfPersonExists(firstName, lastName)) {
                                                    actorId = _actorService.createActor(new Person(firstName, lastName));
                                                } else {
                                                    int personId = _dupeMgmt.getPersonID(firstName, lastName);
                                                    if (!_dupeMgmt.checkIfAPRelationExists(personId)) {
                                                        _actorService.createActorByID(personId);
                                                    }
                                                    actorId = _dupeMgmt.getActorID(personId);
                                                }
                                                Actor actor = _actorService.selectActor(actorId);
                                                if (actor != null) {
                                                    actors.add(actor);
                                                } else {
                                                    System.out.println("Actor with ID " + actorId + " was not found.");
                                                }
                                            } catch (Exception exception) {
                                                System.out.println("ACTOR PARSING" + exception);
                                            }
                                        }
                                        movie.setActors(actors);
                                        //System.out.println(movie.getTitle() + movie.getActors().size());
                                    }
                                }
                            }
                        }
                    }
                }

            }
            return movies;

        }
    }

    private void handlePicture(MovieModel movie, String pictureUrl, String movieTitle) throws IOException {
        if (processedUrls.contains(pictureUrl)) {
            //URL has already been processed! SKIP
            return;
        }

        // Download and save the image
        String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
        if (ext.length() > 4) {
            ext = EXT;
        }
        String pictureName = removeIllegalChars(movieTitle.trim() + ext);
        String localPicturePath = DIR + File.separator + pictureName;

        // Check if file already exists skipperino
        if (Files.exists(Paths.get(localPicturePath))) {
            movie.setPoster(localPicturePath);
            return;
        }

        try {
            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
            movie.setPoster(localPicturePath);
        } catch (IOException ex) {
            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        processedUrls.add(pictureUrl);
    }

    private String generateUniqueFilename(String originalName) {
        String baseName = originalName.substring(0, originalName.lastIndexOf("."));
        String ext = originalName.substring(originalName.lastIndexOf("."));
        return baseName + "_" + System.currentTimeMillis() + ext;
    }

    String removeIllegalChars(String text) {
        return text.replaceAll("[<>:\"/\\\\|?*]", "_");
    }
}
