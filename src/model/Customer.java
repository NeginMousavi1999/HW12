package model;

import enums.CookState;
import enums.CustomerState;
import lombok.Data;

import java.util.List;

/**
 * @author Negin Mousavi
 */
@Data
public class Customer extends Person {
    private static Integer totalCustomersInRestaurant = 0;
    private Order order;
    private CustomerState customerState;

    public Customer(int id, String name, Restaurant restaurant, Order order) {
        super(id, name, restaurant);
        this.order = order;
    }

    public static Integer getTotalCustomersInRestaurant() {
        return totalCustomersInRestaurant;
    }

    public synchronized void enterToRestaurant(int restaurantCapacity) throws InterruptedException {
        synchronized (totalCustomersInRestaurant) {
            if (totalCustomersInRestaurant >= restaurantCapacity) {
                System.out.println("Restaurant is full... please wait until someone leave");
                this.wait();
            }
            System.out.println(this.pName + " entered");
            totalCustomersInRestaurant++;
            customerState = CustomerState.CUSTOMER_ENTERED;
        }
    }

    @Override
    public void run() {
        this.customerState = CustomerState.CUSTOMER_STARTING;
        synchronized (restaurant) {
            while (restaurant.getCapacity() <= totalCustomersInRestaurant) {
                try {
                    System.out.println(pName + " wanted to entered but Restaurant is full... so please wait until someone leave");
                    restaurant.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            totalCustomersInRestaurant++;
            System.out.println(pName + " entered to restaurant");
/*            //order done!
            setCustomerState(CustomerState.PLACED_ORDER);
            System.out.println(restaurant.getAvailableCook());

            while (restaurant.getAvailableCook() == null) {//TODO test
                System.out.println("i am waiting");
                try {
                    restaurant.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            System.out.println(getPName() + " wait some times");
            try {
                restaurant.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getPName() + " wake and " + "total: " + totalCustomersInRestaurant);
            totalCustomersInRestaurant--;
            restaurant.notify();


        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                super.toString() +
                ", orders=" + order +
                ", customerState=" + customerState +
                '}';
    }

    public Cook getCookToAcceptOrder(List<Cook> availableCooks) {
        synchronized (availableCooks) {
            if (availableCooks.isEmpty()) {
                System.out.println("we have n't available cook to accept your order... so please wait");
                try {
                    availableCooks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
            return availableCooks.remove(0);
        }
    }
}