package interview.study.blockqueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @Auther: xujh
 * @Date: 2019/11/11 22:46
 * @Description: 生产者消费者模式（传统版）多线程交互
 */


/**
 * 共享资源类
 */
class ShareData {
    private  int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {

        lock.lock();                //加锁
        try {
            //1 判断
            while (number != 0) {   //多线程要用while循环，除非就两个线程竞争时可以用if
                //等待，不能生产
                condition.await();  //condition控制资源类，阻塞
            }
            //2 干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //3 通知唤醒
            condition.signalAll();  //condition控制资源类，唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();          //释放锁
        }
    }
    public void decrement() throws Exception {
        lock.lock();
        try {
            //1 判断
            while (number == 0) {
                //等待，不能生产
                condition.await();
            }
            //2 干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //3 通知唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

/**
 题:一个初始值为零的变量，两个线程对其交替操作， 一个加1一个减1，来5轮
 1  线程       操作(方法)        资源类
 2  判断       干话              通知
 3  防止虚假唤醒机制
 */

public class ProdConsumerTraditionDemo {

    public static void main(String[] args) {
        ShareData shareData =new ShareData();
        
        new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"CC").start();

        new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"DD").start();
    }
    

}
