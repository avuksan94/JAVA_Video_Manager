/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.jbModels;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author antev
 */
public class PubDateAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String text) throws Exception {
        return LocalDateTime.parse(text, MovieJB.DATE_FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime date) throws Exception {
        return date.format(MovieJB.DATE_FORMATTER);
    }
}
