package model;

import lombok.Data;

/**
 * @author Negin Mousavi
 */
@Data
public class Person extends Thread {
    protected int pId;
    protected String pName;
    protected Restaurant restaurant;

    public Person(int pId, String pName, Restaurant restaurant) {
        this.pId = pId;
        this.pName = pName;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "id=" + pId +
                ", name='" + pName + '\'';
    }
}
