package mv.sdd.sim.thread;

import mv.sdd.sim.Restaurant;

public class Cuisinier implements Runnable {

    private final Restaurant restaurant;
    private boolean ServiceCommence = true;

    public Cuisinier(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void arreter() {
        ServiceCommence = false;
    }

    @Override
    public void run() {
        while (ServiceCommence) {
            restaurant.demarrerPreparations();
        }
    }
}
