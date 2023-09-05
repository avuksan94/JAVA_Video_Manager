/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.command;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.bll.services.ActorService;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antev
 */
public class RemoveActorCommand implements Command {

    private final Set<ActorModel> actors;
    private final ActorModel actor;
    private final MovieModel movie;
    private final ActorService _actorService;

    public RemoveActorCommand(Set<ActorModel> actors, ActorModel actor, MovieModel movie) throws SQLException {
        this.actors = actors;
        this.actor = actor;
        this.movie = movie;
        _actorService = new ActorService();
    }

    @Override
    public void execute() {
        if (actors.remove(actor)) {
            try {
                _actorService.removeActorFromMovieDB(movie, actor);
            } catch (Exception ex) {
                actors.add(actor); // Revert local change if DB change error
                Logger.getLogger(RemoveActorCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void undo() {
        if (actors.add(actor)) {
            try {
                _actorService.addActorToMovieDB(movie, actor);
            } catch (Exception ex) {
                actors.remove(actor); // Revert local change if DB change error
                Logger.getLogger(RemoveActorCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
