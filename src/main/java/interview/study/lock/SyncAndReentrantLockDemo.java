package interview.study.lock;
import	java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import	java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: xujh
 * @Date: 2019/11/13 10:12
 * @Description:
 *
  synchronized 和Lock有什么区别?用新lock有什么好处?
    1 原始构成
    synchronized是关键字网FIM层面，
        monitorenter(底层是通过monitor对象来完，其实wait/notify等方法也依赖于monitor对象只有在同步块或方法中才能调wait/notify等
        monitorexit
        Lock是具体类(java.util.concurrent.locks.Lock) 是api层面的锁

    2 使用方法
        synchronized 不需要手动释放锁，当synchronized代码执行完后系统会自动让线程释放对锁的占用
        ReentrantLock 则需要手动释放锁，否则可能导致死锁现象，
        需要lock()和unlock()方法配合try catch finally 块来完成使用

    3 等待是否可中断
        synchronized 不可中断，除非抛出异常或者正常运行完成退出
        ReentrantLock 可中断，1，设置超时方法trylock(Long timeout TimeUnit unit)
                         2,lockInterruptibly()放代码块中，调用interrupt()方可以中断

    4 加锁是否公平
        synchronized 非公平锁
        ReentrantLock 两者都可以，默认非公平锁，构造方法可传人Boolean值，true为公平锁，false为非公平锁

    5 锁绑定多个条件condition
        synchronized没有
        Reentrantlock用来实现分组唤醒需要唤醒的线程们，可以精确唤醒， 面不是像synchronized 费么随机唤醒一个线程要么唤醒全部线程。

 */


/**
 * 题目：多线程之间按顺序调用，实例A->8->C 三个线程启动，要求如下：
     AA打印5次后，通知BB打印10次后，通知CC打印15次
     紧接着
     AA打印5次，BB打印10次，CC打印15次
     .....循环
     来10轮
 */

class MyResource{

    private int number = 1;
    private Lock lock = new ReentrantLock();

    //设置3个条件控制通知唤醒ABC循环打印
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    //AA打印
    public void AAprint5() {
        lock.lock();
        try {
            //判断
            while (number != 1){
                c1.await();
            }
            //干活
            for(int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName()+"\tAA打印"+i+"次");
                }
            //通知
            number =2;
            c2.signal();//AA通知BB
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    //BB打印
    public void BBprint10() {

        lock.lock();
        try {
            //判断
            while (number != 2){
                c2.await();
            }
            //干活
            for(int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName()+"\tBB打印"+i+"次");
            }
            //通知
            number =3;
            c3.signal();//BB通知CC
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    //CC打印
    public void CCprint15() {

        lock.lock();
        try {
            //判断
            while (number != 3){
                c3.await();
            }
            //干活
            for(int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName()+"\tCC打印"+i+"次");
            }
            //通知
            number =1;
            c1.signal();//CC通知AA
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


public class SyncAndReentrantLockDemo {


    public static void main(String[] args) {
        MyResource myResource = new MyResource();

            new Thread(()->{
                for(int i = 1; i <= 10; i++) {
                        myResource.AAprint5();
                    }
            },"A").start();

        new Thread(()->{
            for(int i = 1; i <= 10; i++) {
                myResource.BBprint10();
            }
        },"B").start();

        new Thread(()->{
            for(int i = 1; i <= 10; i++) {
                myResource.CCprint15();
            }
        },"C").start();

    }
}
