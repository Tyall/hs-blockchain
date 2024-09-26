package blockchain.utils;

public class SimulationUtils {

    public static void waitForSimulationToProgress(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            //System.out.println("Error when waiting for simulation: " + e.getMessage());
        }
    }

    private SimulationUtils() {
        throw new IllegalArgumentException("Cannot instantiate utility class");
    }

}
