package interview.study.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 公平锁
    是指多个线程按照申请锁的顺序来获取锁类似排队打饭 先来后到
 非公平锁
     是指在多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的线程优先获取到锁,在高并发的情况下,有可能造成优先级反转或者饥饿现象

 区别 公平锁/非公平锁
 并发包ReentrantLock的创建可以指定构造函数的boolean类型来得到公平锁或者非公平锁 默认是非公平锁

 Java ReentrantLock而言,
 通过构造函数指定该锁是否是公平锁 默认是非公平锁 非公平锁的优点在于吞吐量必公平锁大.
 */
class Phone implements Runnable{

    public synchronized void sendSMS()throws  Exception{
        System.out.println(Thread.currentThread().getName() +"\t invoked sendSMS() ");
        sendEmail();
    }
    public synchronized void sendEmail() throws Exception {
        System.out.println(Thread.currentThread().getName() +"\t ######invoked sendEmail()");
    }
    //=============================
    Lock lock = new ReentrantLock();
    @Override
    public void run() {
        get();
    }

    public void get(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() +"\t invoked get");
            set();
        }finally {
            lock.unlock();
        }
    }
    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() +"\t invoked set");
        }finally {
            lock.unlock();
        }
    }
}
/**
 * 可重入锁(又名递归锁)
 * ReentrantLock/synchronized就是一个典型的可重入锁
 *
 * 案例一： synchronized
 * 作用：可重入锁最大的作用就是避免死锁
 *
 * 同一线程外层函数获得锁后，内层递归函数仍能获取该锁代码
 * 在同一个线程在外层方法获取锁的时候，在进入内层方法时会自动获得该锁
 *
 * 类似内层函数获得的锁和外层的锁的是同一把
 *
     t1	 invoked sendSMS()
     t1	 ######invoked sendEmail()
     t2	 invoked sendSMS()
     t2	 ######invoked sendEmail()
--------------------------
    案例二  ReentrantLock


 */
public class ReentrantLockDemo {

    public static void main(String[] args) {

        Phone phone =new Phone();
        new Thread(()->{
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }

        },"t1").start();

        new Thread(()->{
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t2").start();


        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        Thread t3 = new Thread(phone);
        Thread t4 = new Thread(phone);
        t3.start();
        t4.start();
    }
}
