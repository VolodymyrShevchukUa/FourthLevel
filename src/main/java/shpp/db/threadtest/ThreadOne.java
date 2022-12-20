package shpp.db.threadtest;

public class ThreadOne extends Thread {

    ThreadTwo threadTwo = new ThreadTwo();

    @Override
    public void run() {
        threadTwo.start();
        for(int i=0;i<20;i++){
            threadTwo.interrupt();
            System.out.println("I`m thread number one");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }




}
