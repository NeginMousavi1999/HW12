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
    private String customerName;
    private Map<FoodType, Integer> foods;
    private boolean wakeCustomerByCook = false;

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
                ", foods=" + foods +
                '}';
    }
}
