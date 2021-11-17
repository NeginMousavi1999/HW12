import model.Restaurant;

/**
 * @author Negin Mousavi
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("****" + Thread.currentThread().getName());
        Restaurant restaurant = new Restaurant();

        restaurant.getCustomers().forEach(c -> {
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        restaurant.getCooks().forEach(c -> {
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        restaurant.getMachines().forEach(m -> {
            try {
                m.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
