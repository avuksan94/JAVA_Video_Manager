/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.jbModels;

import java.util.List;
//this is really nice because dont have to import the annotation one by one
import javax.xml.bind.annotation.*;

//https://www.baeldung.com/jaxb

/**
 *
 * @author antev
 */
@XmlRootElement(name = "rss")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieArchiveJB {

    @XmlElement(name = "channel")
    private Channel channel;

    public MovieArchiveJB() {
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Channel {

        @XmlElement(name = "item")
        private List<MovieJB> movies;

        public List<MovieJB> getMovies() {
            return movies;
        }

        public void setMovies(List<MovieJB> movies) {
            this.movies = movies;
        }
    }
    
    /*Marshalling:
    Definition: Marshalling refers to the process of converting an object (or data) into a format suitable for storage or transmission.
    Use Case: It's used when you want to send an object over a network or save it to a file.
    Example: Converting a Java object into an XML or JSON representation for transmission over a network.
    
    Unmarshalling:
    Definition: Unmarshalling is the opposite of marshalling. It refers to the process of converting data from a format suitable for storage or transmission back into an object.
    Use Case: It's used when you receive an object over a network or read it from a file and want to use it in its original form.
    Example: Converting an XML or JSON representation received over a network back into a Java object*/
}
