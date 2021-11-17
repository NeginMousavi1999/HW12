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
                        System.out.println("type A");
                        Machine machine = restaurant.getAvailableSameTypeMachine(FoodType.A);
                        while (machine == null) {
                            System.out.println(pName + " is waiting because we have no available machine");
                            try {
//                                restaurant.notify();
                                restaurant.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            machine = restaurant.getAvailableSameTypeMachine(FoodType.A);
                        }

                        //find machine with capacity and A type
                        System.out.println(machine.getMName() + " found and gonna make A:" + countA);
                        System.out.println(getPName() + " is preparing order so it takes time...");
                        machine.setMachineState(MachineState.MACHINE_STARTING_FOOD);
                        if (countA > machine.getCapability()) {
                            machine.setCountOfFoods(machine.getCapability());
                            countA -= machine.getCapability();
                            System.out.println("logging countA > machine.getCapability()");
                        } else {
                            machine.setCountOfFoods(countA);
                            countA = 0;
                            System.out.println("logging else");
                        }

                        while (machine.getCountOfFoods() != 0) {
                            System.out.println(pName + " waiting");
                            try {
//                                restaurant.notify();
//                                restaurant.notifyAll();
                                restaurant.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(pName + " waking");
                        }
                        cookState = CookState.COOK_FINISHED_FOOD;
                        System.out.println(countA);
                    }

                    while (countB != 0) {
                        System.out.println("type B");
                        Machine machine = restaurant.getAvailableSameTypeMachine(FoodType.B);
                        while (machine == null) {
                            System.out.println(pName + " is waiting because we have no available machine");
                            try {
//                                restaurant.notify();
                                restaurant.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            machine = restaurant.getAvailableSameTypeMachine(FoodType.B);
                        }

                        //find machine with capacity and B type
                        System.out.println(machine.getMName() + " found and gonna make B:" + countB);
                        System.out.println(getPName() + " is preparing order so it takes time...");
                        machine.setMachineState(MachineState.MACHINE_STARTING_FOOD);
                        if (countB > machine.getCapability()) {
                            machine.setCountOfFoods(machine.getCapability());
                            countB -= machine.getCapability();
                            System.out.println("logging countB > machine.getCapability()");
                        } else {
                            machine.setCountOfFoods(countB);
                            countB = 0;
                            System.out.println("logging else");
                        }

                        while (machine.getCountOfFoods() != 0) {
                            System.out.println(pName + " waiting");
                            try {
//                                restaurant.notify();
//                                restaurant.notifyAll();
                                restaurant.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(pName + " waking");
                        }
                        cookState = CookState.COOK_FINISHED_FOOD;
                        System.out.println(countB);
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
