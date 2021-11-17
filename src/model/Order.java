package model;

import enums.FoodType;
import lombok.Data;

import java.util.Map;

/**
 * @author Negin Mousavi
 */
@Data
public class Order {
    private int id;
    private Customer customer;//TODO must be delete
    private Cook cook;
    private Map<FoodType, Integer> foods;
    private String customerName;
    private boolean wakeCustomerByCook = false;
    private boolean wakeCookByMachine = false;

    public Order(int id, Map<FoodType, Integer> foods) {
        this.id = id;
        this.foods = foods;
    }

    public Map<FoodType, Integer> getFoods() {
        return foods;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", cook=" + cook +
                ", foods=" + foods +
                '}';
    }
}
