package model;

import enums.CookState;
import enums.FoodType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Negin Mousavi
 */
@Data
public class Cook extends Person {
    private CookState cookState;
    private Order order;

    public Cook(int rId, String rName, Restaurant restaurant) {
        super(rId, rName, restaurant);
    }

    @Override
    public void run() {
        this.cookState = CookState.COOK_STARTING;
    }

    @Override
    public String toString() {
        return "Cook{" +
                super.toString() +
                ", cookState=" + cookState +
                ", order=" + order +
                '}';
    }


    public synchronized Machine getMachineToCookThisType(List<Machine> availableMachines, Order order) throws InterruptedException {
        if (availableMachines.isEmpty()) {
            System.out.println("we have n't free machine to cook your food... so please wait");
            availableMachines.wait();
            return null;
        }
        for (Machine machine : availableMachines) {
            Map<FoodType, Integer> foods = order.getFoods();
            for (int i = 0; i < foods.size(); i++) {
                if (!foods.get(machine.getFoodTypes()).equals(0)) {
                    cookState = CookState.COOK_STARTED_FOOD;
                    return machine;
                }
            }
        }
        System.out.println("we have n't machine to cook this type of food now... so please wait");

        availableMachines.wait();
        return null;
    }
}
