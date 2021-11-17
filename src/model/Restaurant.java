package model;


import enums.CookState;
import enums.FoodType;
import enums.RestaurantState;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Negin Mousavi
 */
@Data
public class Restaurant {
    private int capacity = 4;
    private RestaurantState restaurantState = RestaurantState.OPEN;
    private List<Customer> customers = new ArrayList<>();
    private List<Cook> cooks = new ArrayList<>();
    private List<Machine> machines = new ArrayList<>();
    private CreateOrder create = new CreateOrder();

    public Restaurant() {
        customers.add(new Customer(1, "Costumer1", this, create.getC1Order()));
        customers.add(new Customer(2, "Costumer2", this, create.getC2Order()));
        customers.add(new Customer(3, "Costumer3", this, create.getC3Order()));
        customers.add(new Customer(4, "Costumer4", this, create.getC4Order()));
        customers.add(new Customer(5, "Costumer5", this, create.getC5Order()));
        customers.add(new Customer(6, "Costumer6", this, create.getC6Order()));
        for (Customer customer : customers)
            customer.getOrder().setCustomerName(customer.getPName());


        cooks.add(new Cook(1, "Cook1", this));
        cooks.add(new Cook(2, "Cook2", this));
        cooks.add(new Cook(3, "Cook3", this));

        machines.add(new Machine(1, "Machine1", 2, FoodType.A));
        machines.add(new Machine(2, "Machine2", 1, FoodType.B));

        customers.forEach(Thread::start);
        cooks.forEach(Thread::start);
        machines.forEach(Thread::start);
    }

    public Integer getCustomerCount() {
        return Customer.getTotalCustomersInRestaurant();
    }

    public synchronized Cook getAvailableCook() {
        for (Cook cook : cooks) {
            if (cook.getCookState().equals(CookState.COOK_STARTING))
                return cook;
        }
        return null;
    }

    public void close() {
        setRestaurantState(RestaurantState.CLOSE);
        System.out.println("restaurant is close. bye bye ^_^");
    }
}
