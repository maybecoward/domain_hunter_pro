package test;

import java.util.concurrent.*;

public class threadExample {
    public static void main(String args[]){
         BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<Integer>();

         ExecutorService pes = Executors.newFixedThreadPool(2);
         ExecutorService ces = Executors.newFixedThreadPool(2);

         pes.submit(new ProducerExample(sharedQueue,1));
         pes.submit(new ProducerExample(sharedQueue,2));
         ces.submit(new ConsumerExample(sharedQueue,1));
         ces.submit(new ConsumerExample(sharedQueue,2));
         // shutdown should happen somewhere along with awaitTermination
         /* https://stackoverflow.com/questions/36644043/how-to-properly-shutdown-java-executorservice/36644320#36644320 */
         pes.shutdown();
         ces.shutdown();
    }
}
class ProducerExample implements Runnable {
    private final BlockingQueue<Integer> sharedQueue;
    private int threadNo;
    public ProducerExample(BlockingQueue<Integer> sharedQueue,int threadNo) {
        this.threadNo = threadNo;
        this.sharedQueue = sharedQueue;
    }
    @Override
    public void run() {
        for(int i=1; i<= 5; i++){
            try {
                int number = i+(10*threadNo);
                System.out.println("Produced:" + number + ":by thread:"+ threadNo);
                sharedQueue.put(number);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}

class ConsumerExample implements Runnable{
    private final BlockingQueue<Integer> sharedQueue;
    private int threadNo;
    public ConsumerExample (BlockingQueue<Integer> sharedQueue,int threadNo) {
        this.sharedQueue = sharedQueue;
        this.threadNo = threadNo;
    }
    @Override
    public void run() {
        while(true){
            try {
                int num = sharedQueue.take();
                System.out.println("Consumed: "+ num + ":by thread:"+threadNo);
            } catch (Exception err) {
               err.printStackTrace();
            }
        }
    }   
}
