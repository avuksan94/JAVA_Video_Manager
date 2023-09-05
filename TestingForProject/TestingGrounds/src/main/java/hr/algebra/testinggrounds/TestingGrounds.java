/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package hr.algebra.testinggrounds;

import hr.algebra.bll.services.UserService;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import hr.algebra.uow.UnitOfWork;
import hr.algebra.dal.repo.UserRepo;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *
 * @author antev
 */
public class TestingGrounds {

    public static void main(String[] args) throws SQLException, Exception {
     UnitOfWork unitOfWork = new UnitOfWork();
     
     //Optional<User> userOptional = unitOfWork.getUserRepository().selectEntity(1);
     //Optional<Person> personOptional = unitOfWork.getPersonRepository().selectEntity(1);
     
     //Person p = new Person("Tara", "Tarich", "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 21);
     //User u = new User("tara13", "1233", "tara13@gmail.com", "Admin");
     //Person p2 = new Person("Darko", "Perich", "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 24);
     //unitOfWork.getPersonRepository().createEntity(p);
     //unitOfWork.getPersonRepository().updateEntity(3, p2);
     //unitOfWork.getPersonRepository().deleteEntity(2);
     
     //UserService _userService = new UserService();
     //_userService.UpdateUser(5,u, p);
     //_userService.DeleteUser(4);
     
     //unitOfWork.getUserRepository().updateEntity(4, u);
     //unitOfWork.getActorRepository().deleteEntity(4);
     
     /*if (userOptional.isPresent()) {
     User user = userOptional.get();
     System.out.println("User ID: " + user.getUserId());
     System.out.println("Username: " + user.getUsername());
     System.out.println("PassHash: " + user.getPasswordHash());
     System.out.println("Created at: " + user.getCreatedAt());
     // Print other user details
     } else {
     System.out.println("User not found");
     }
     
     if (personOptional.isPresent()) {
     Person person = personOptional.get();
     System.out.println("Person ID: " + person.getPersonId());
     System.out.println("Firstname: " + person.getFirstName());
     System.out.println("Lastname: " + person.getLastName());
     System.out.println("Age: " + person.getAge());
     // Print other user details
     } else {
     System.out.println("Person not found");
     }*/
    
     //Movie m = new Movie("Testiranje dodavanja2", "Description test2", 2011,"Western",133,"Testing2");
     /*Optional<Movie> movieOptional = unitOfWork.getMovieRepository().selectEntity(1);
     
     if (movieOptional.isPresent()) {
     Movie movie = movieOptional.get();
     System.out.println(" ID: " + movie.getMovieId());
     System.out.println("Description: " + movie.getDescription());
     System.out.println("Added at: " + movie.getAddedAt());
     // Print other user details
     } else {
     System.out.println("Movie not found");
     }*/
     
     /*UserService _userService = new UserService();
     boolean validate = _userService.ValidateUser("avuksan", "1234");
     System.out.println(validate);*/
     
     /*Person p = new Person("Danac", "Danichcich", "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 23);
     int personId = unitOfWork.getPersonRepository().createEntity(p);*/
    
     //unitOfWork.getActorRepository().deleteEntity(1);
     
    
     Optional<Movie> movieOptional = unitOfWork.getMovieRepository().selectEntity(1);
     Optional<Actor> actorOptional =  unitOfWork.getActorRepository().selectEntity(3);
     Movie movie;
     Actor actor;
     
     if (movieOptional.isPresent()) {
        movie = movieOptional.get();
        actor = actorOptional.get();
        unitOfWork.getMovieActorRepository().unlinkEntities(movie, actor);
     // Print other user details
     } else {
     System.out.println("Movie not found");
     }
    }
}
