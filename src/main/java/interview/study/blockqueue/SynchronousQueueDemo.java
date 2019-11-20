package interview.study.blockqueue;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xujh
 * @Date: 2019/11/11 15:51
 * @Description: SynchronousQueueDemo

 SynchronousQueue:不存储元素的阻塞队列,也即是单个元素的队列.
 SynchronousQueue没有容量
与其他BlcokingQueue不同,SynchronousQueue是一个不存储元素的BlcokingQueue
每个put操作put添加一个元素后必须要等待一个take操作取出,否则不能继续put添加元素,反之取出亦然.


ArrayBlockingQueue: 由数组结构组成的有界阻塞队列.
LinkedBlockingDeque: 由链表结构组成的有界(但大小默认值Integer>MAX_VALUE)阻塞队列.
PriorityBlockingQueue:支持优先级排序的无界阻塞队列.
DelayQueue: 使用优先级队列实现的延迟无界阻塞队列.
LinkedTransferQueue:由链表结构组成的无界阻塞队列.
LinkedBlockingDeque:由了解结构组成的双向阻塞队列.

 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<Integer> ();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() +"\tput 1");
                synchronousQueue.put(1);
                System.out.println(Thread.currentThread().getName() +"\tput 2");
                synchronousQueue.put(2);
                System.out.println(Thread.currentThread().getName() +"\tput 3");
                synchronousQueue.put(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AAA").start();


        new Thread(()->{
            try {
                try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println(Thread.currentThread().getName() +"\t take 1");
                synchronousQueue.take();

                try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println(Thread.currentThread().getName() +"\t take 2");
                synchronousQueue.take();

                try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println(Thread.currentThread().getName() +"\t take 3");
                synchronousQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BBB").start();

    }
}

/**
 console.out
 -----------
 AAA	put 1
 BBB	 take 1
 AAA	put 2
 BBB	 take 2
 AAA	put 3
 BBB	 take 3
 -----------
 */