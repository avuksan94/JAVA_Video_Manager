/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.command;

import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.bll.services.DirectorService;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antev
 */
public class AddDirectorCommand implements Command {

    private final Set<DirectorModel> directors;
    private final DirectorModel director;
    private final MovieModel movie;
    private final DirectorService _directorService;

    public AddDirectorCommand(Set<DirectorModel> directors, DirectorModel director, MovieModel movie) throws SQLException {
        this.directors = directors;
        this.director = director;
        this.movie = movie;
        _directorService = new DirectorService();
    }

    @Override
    public void execute() {
        if (directors.add(director)) {
            try {
                _directorService.addDirectorToMovieDB(movie, director);
            } catch (Exception ex) {
                directors.remove(director); // Revert local change if DB change error
                Logger.getLogger(AddDirectorCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void undo() {
        if (directors.remove(director)) {
            try {
                _directorService.removeDirectorFromMovieDB(movie, director);
            } catch (Exception ex) {
                directors.add(director); // Revert local change if DB change error
                Logger.getLogger(AddDirectorCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
