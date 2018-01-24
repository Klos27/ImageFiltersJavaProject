package Host;

// Main Class for testing server stability
public class MainThreadsSimulation {


    public static void main(String[] args) {


        for (int i = 0; i < 10; i++) {
            Thread t1 = new ThreadSimulation(i);
            t1.start();
        }

    }






}
