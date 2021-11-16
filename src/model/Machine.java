package model;

import enums.FoodType;
import enums.MachineState;
import exception.RestaurantExc;
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
    private static int countOfFoodInProcess = 0;
    private Set<Thread> currentCookThread = new HashSet<>();
    private MachineState machineState;

    public Machine(int id, String mName, int capability, FoodType foodTypes) {
        this.mId = id;
        this.mName = mName;
        this.capability = capability;
        this.foodTypes = foodTypes;
    }

    public synchronized boolean processNewFood(Order order) {
        if (countOfFoodInProcess == capability) {// i am sure that if countOfFoodInProcess == capability this method won't call
            throw new RestaurantExc("Machines are full. please wait... ");//just for sure
        }
        this.setMachineState(MachineState.MACHINE_STARTING_FOOD);
        currentCookThread.add(Thread.currentThread());
        countOfFoodInProcess++;
        System.out.println("start cooking........ it takes 10 seconds");
        return finishFood();
    }

    private boolean finishFood() {
        try {
            this.wait(10000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
        countOfFoodInProcess--;
        System.out.println("finished food");
        setNewState();
        return true;
    }

    @Override
    public void run() {
        this.machineState = MachineState.MACHINE_STARTING;
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

    public synchronized void setNewState() {
        if (countOfFoodInProcess == 0)
            this.setMachineState(MachineState.MACHINE_DONE_FOOD);
    }
}