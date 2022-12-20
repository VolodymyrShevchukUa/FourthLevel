package shpp.db.threadtest;

public class Run {


    public static void main(String[] args) {
        ThreadOne threadOne = new ThreadOne();
        ThreadThree threadThree = new ThreadThree();

        threadOne.start();
        threadThree.start();
        try {
            threadOne.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
