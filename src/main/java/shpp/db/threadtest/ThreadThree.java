package shpp.db.threadtest;

public class ThreadThree extends Thread {

    @Override
    public void run() {
        for(int i=0;i<20;i++){
            System.out.println("I`m thread number three");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
