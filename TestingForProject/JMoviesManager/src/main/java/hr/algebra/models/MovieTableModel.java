/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.models;

import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.bll.services.ActorService;
import hr.algebra.bll.services.DirectorService;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author antev
 */
public class MovieTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Id", "Movie Title", "Description", "Release year", "Genre", "Added at", "Duration", "Actors", "Directors"};

    private List<MovieModel> movies;
    private final ActorService _actorService;
    private final DirectorService _directorService;

    public MovieTableModel(List<MovieModel> movies) throws SQLException {
        _actorService = new ActorService();
        _directorService = new DirectorService();
        this.movies = movies;
    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getMovieId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getDescription();
            case 3:
                return movies.get(rowIndex).getReleaseYear();
            case 4:
                return movies.get(rowIndex).getGenre();
            case 5:
                return movies.get(rowIndex).getAddedAt();
            case 6:
                return movies.get(rowIndex).getDuration();
            case 7:
            try {
                return _actorService.getActorNames(movies.get(rowIndex).getActors());
            } catch (Exception e) {
                return "Error fetching actors";
            }
            case 8:
            try {
                return _directorService.getDirectorNames(movies.get(rowIndex).getDirectors());
            } catch (Exception e) {
                return "Error fetching directors";
            }
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

}
