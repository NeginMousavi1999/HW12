package model;

import enums.FoodType;

import java.util.HashMap;

/**
 * @author Negin Mousavi
 */
public class CreateOrder {
    public Order getC1Order() {
        HashMap<FoodType, Integer> food = new HashMap<>();
        food.put(FoodType.A, 2);
        food.put(FoodType.B, 1);
        return new Order(1, food);
    }

    public Order getC2Order() {
        HashMap<FoodType, Integer> food = new HashMap<>();
        food.put(FoodType.A, 0);
        food.put(FoodType.B, 1);
        return new Order(2, food);
    }

    public Order getC3Order() {
        HashMap<FoodType, Integer> food = new HashMap<>();
        food.put(FoodType.A, 1);
        food.put(FoodType.B, 1);
        return new Order(3, food);
    }

    public Order getC4Order() {
        HashMap<FoodType, Integer> food = new HashMap<>();
        food.put(FoodType.A, 1);
        food.put(FoodType.B, 10);
        return new Order(4, food);
    }

    public Order getC5Order() {
        HashMap<FoodType, Integer> food = new HashMap<>();
        food.put(FoodType.A, 1);
        food.put(FoodType.B, 2);
        return new Order(5, food);
    }

    public Order getC6Order() {
        HashMap<FoodType, Integer> food = new HashMap<>();
        food.put(FoodType.A, 2);
        food.put(FoodType.B, 1);
        return new Order(6, food);
    }
}
