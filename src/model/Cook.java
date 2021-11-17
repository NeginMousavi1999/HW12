package model;

import enums.CookState;
import enums.FoodType;
import enums.MachineState;
import enums.RestaurantState;
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
        mainLoop:
        while (true) {
            synchronized (restaurant) {
                restaurant.notifyAll();
                while (order == null) {
                    if (restaurant.getRestaurantState().equals(RestaurantState.CLOSE))
                        break mainLoop;
                    try {
                        System.out.println(pName + ": no order yet for me");
//                        restaurant.notify();
                        restaurant.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(getPName() + " accepted " + order.getCustomerName() + "'s order");


                Integer countA = order.getFoods().get(FoodType.A);
                Integer countB = order.getFoods().get(FoodType.B);
                while (true) {
                    while (countA != 0) {
                        countA = prepareFood(FoodType.A, countA);
                    }

                    while (countB != 0) {
                        countB = prepareFood(FoodType.B, countB);
                    }
                    if (countA == 0 && countB == 0)
                        break;
                }

                setCookState(CookState.COOK_COMPLETED_ORDER);
                System.out.println(order.getCustomerName() + "'s order is ready");
                setCookState(CookState.COOK_STARTING);
                order.setWakeCustomerByCook(true);
                order = null;
                restaurant.notify();
            }
        }
    }

    @Override
    public String toString() {
        return "Cook{" +
                super.toString() +
                ", cookState=" + cookState +
                ", order=" + order +
                '}';
    }


    public synchronized int prepareFood(FoodType foodType, int count) {
        System.out.println("type " + foodType);
        Machine machine = restaurant.getAvailableSameTypeMachine(foodType);
        while (machine == null) {
            System.out.println(pName + " is waiting because we have no available machine");
            try {
//                                restaurant.notify();
                restaurant.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            machine = restaurant.getAvailableSameTypeMachine(foodType);
        }

        //find machine with capacity and A,B type
        System.out.println(machine.getMName() + " found and gonna make " + foodType + ":" + count);
        System.out.println(getPName() + " is preparing order so it takes some time...");
        machine.setMachineState(MachineState.MACHINE_STARTING_FOOD);
        if (count > machine.getCapability()) {
            machine.setCountOfFoods(machine.getCapability());
            count -= machine.getCapability();
            System.out.println("log bigger");
        } else {
            machine.setCountOfFoods(count);
            count = 0;
            System.out.println("logging else");
        }

        while (machine.getCountOfFoods() != 0) {
            System.out.println(pName + " waiting");
            try {
//                                restaurant.notify();
//                restaurant.notifyAll();
                restaurant.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(pName + " waking");
        }
        cookState = CookState.COOK_FINISHED_FOOD;
        System.out.println(count);
        return count;
    }
}
