package interview.study.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: xujh
 * @Date: 2019/11/11 23:36
 * @Description:        生产者消费者 (阻塞队列版)
 *
    在多线程领域:所谓阻塞，在某些情况下会挂起线程(即阻塞)，一旦条件满足，被挂起的线程义会自动被唤醒
    为什么需要BlockingQueue
    好处是我们不需要关心什么时候需要阻塞线程，什么时候儒要唤醒线程，因为这一切BlockingQueue都给你一手包办了
    在concurrent包发布以前， 在多线程环境下，我们每个程序员都必须去自己控制这些细节，尤其还要兼顾效率和线程安全，而这会给我
    们的程序带来不小的复杂度。



 */

class MyResource {

    private volatile boolean FLAG = true; //默认开启 进行生产消费的交互
     //默认值是0
    private AtomicInteger atomicInteger = new AtomicInteger();
    BlockingQueue<String> blockingQueue =null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void producer()throws Exception {
        String data= null;
        boolean retValue;
        while (FLAG)
        {
           data = atomicInteger.incrementAndGet() + "";
           retValue = blockingQueue.offer(data, 2L,TimeUnit.SECONDS);
            if (retValue)
            {
                System.out.println(Thread.currentThread().getName() +"\t插入队列"+data+"成功");
            }else {
                System.out.println(Thread.currentThread().getName() +"\t插入队列"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t老板叫停了,此时FLAG=false,生产结束 ");
    }

    public void consumer()throws Exception {
        String result= null;
        while (FLAG)
        {
            result = blockingQueue.poll(2L,TimeUnit.SECONDS);
            if(result == null || "".equalsIgnoreCase(result)){
                FLAG = false;
                System.out.println(Thread.currentThread().getName() +"\t超过2秒钟没有消费取到退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName() +"\t取出队列"+result+"成功");

        }

    }

    public void stop()throws Exception{
        this.FLAG = false;
    }
}
public class ProdConsumerBlockQueueDemo {

    public static void main(String[] args)throws Exception {

        MyResource myResource = new MyResource(new ArrayBlockingQueue<String>(3));

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() +"\t生产Producer启动成功");
            try {
                myResource.producer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Prod").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() +"\t消费Producer启动成功");
            try {
                myResource.consumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Consumer").start();

        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("5秒钟时间到叫停，活动结束");
        myResource.stop();
    }
}
/**
 *
 Prod	生产Producer启动成功
 Prod	插入队列1成功
 Consumer	消费Producer启动成功
 Consumer	取出队列1成功
 Prod	插入队列2成功
 Consumer	取出队列2成功
 Prod	插入队列3成功
 Consumer	取出队列3成功
 Prod	插入队列4成功
 Consumer	取出队列4成功
 Prod	插入队列5成功
 Consumer	取出队列5成功



 5秒钟时间到叫停，活动结束
 Prod	老板叫停了,此时FLAG=false,生产结束
 Consumer	超过2秒钟没有消费取到退出
 */