package model;

import enums.CookState;
import enums.FoodType;
import enums.MachineState;
import enums.RestaurantState;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Negin Mousavi
 */
@Data
public class Machine extends Thread {
    private int mId;
    private String mName;
    private FoodType foodTypes;
    private int capability;
    private Integer countOfFoods = 0;
    private Set<Thread> currentCookThread = new HashSet<>();
    private MachineState machineState;
    private Restaurant restaurant;

    public Machine(int id, String mName, int capability, FoodType foodTypes, Restaurant restaurant) {
        this.mId = id;
        this.mName = mName;
        this.capability = capability;
        this.foodTypes = foodTypes;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + mId +
                ", name='" + mName + '\'' +
                ", foodTypes=" + foodTypes +
                ", capability=" + capability +
                ", currentCookThread=" + currentCookThread +
                ", machineState=" + machineState +
                '}';
    }

    @Override
    public void run() {
        this.machineState = MachineState.MACHINE_STARTING;
        mainLoop:
        while (true) {
            synchronized (restaurant) {
                restaurant.notifyAll();
                while (countOfFoods == 0) {
                    if (restaurant.getRestaurantState().equals(RestaurantState.CLOSE))
                        break mainLoop;
                    try {
                        System.out.println(mName + ": no food yet for me");
//                        restaurant.notifyAll();
                        restaurant.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(mName + " received food and gonna start making so it takes some time...");

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(mName + " done food :)");
                setMachineState(MachineState.MACHINE_DONE_FOOD);
                countOfFoods = 0;
                restaurant.notifyAll();
            }
        }
    }
}