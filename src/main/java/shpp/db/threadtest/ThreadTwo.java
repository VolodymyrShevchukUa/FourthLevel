package shpp.db.threadtest;

public class ThreadTwo extends Thread {
    ThreadThree threadThree = new ThreadThree();


    @Override
    public void run() {
        double d = 1000;
        for (int i = 0;i<90000;i++){
             d /= 1.2/1.11/1.111/1/1.11111;
        }
        System.out.println(d);
        System.out.println(isInterrupted());
        for(int i=0;i<20;i++){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("I`m thread number two");
        }
    }
}
