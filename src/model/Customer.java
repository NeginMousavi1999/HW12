package model;

import enums.CookState;
import enums.CustomerState;
import enums.RestaurantState;
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

    @Override
    public String toString() {
        return "Customer{" +
                super.toString() +
                ", orders=" + order +
                ", customerState=" + customerState +
                '}';
    }

    @Override
    public void run() {
        this.customerState = CustomerState.CUSTOMER_STARTING;
        synchronized (restaurant) {
            while (restaurant.getCapacity() <= totalCustomersInRestaurant) {
                try {
                    System.out.println(pName + " wanted to entered but Restaurant is full... so please wait until someone leave");
                    restaurant.setRestaurantState(RestaurantState.FULL);
                    restaurant.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            totalCustomersInRestaurant++;
            System.out.println(pName + " entered to restaurant");

            //order done!
            setCustomerState(CustomerState.PLACED_ORDER);
            System.out.println(pName + " placed order: " + order.toString());

            Cook cook = restaurant.getAvailableCook();
            while (cook == null) {//TODO test
                System.out.println(pName + " is waiting because we have no available cook");
                try {
                    restaurant.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cook = restaurant.getAvailableCook();
            }
            cook.setCookState(CookState.COOK_RECEIVED_ORDER);
            System.out.println(cook.getPName() + " accepted " + getPName() + "'s order");
            synchronized (order) {
                try {
                    order.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cook.setCookState(CookState.COOK_STARTING);//TODO must be in Cook
            System.out.println(getPName() + " getting order and is eating it....");
            try {
                restaurant.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(getPName() + " finished and gonna go out.. so the total will be:" + (totalCustomersInRestaurant - 1));
            totalCustomersInRestaurant--;
            if (totalCustomersInRestaurant == 0) {
                restaurant.setRestaurantState(RestaurantState.CLOSE);
            }
            restaurant.notify();
        }
    }
}