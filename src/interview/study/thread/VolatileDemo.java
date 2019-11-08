package interview.study.thread;

/**
 * @Auther: anxiaonian
 * @Date: 2019/11/8 09:33
 * @Description:
 */

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 谈谈你对volatile的理解
 * 1.volatile是Java虚拟机提供的轻量级的同步机制
 *  1.1保证可见性
 *  1.2不保证原子性
 *  1.3禁止指令重排
 *
 */
class MyData{
    volatile int number = 0;

    public void addTo60() {
        this.number = 60;
    }
    public  void addPlusPuls()
    {
        number++;
    }
    //使用AtomicInteger,保证原子性
    AtomicInteger atomicInteger = new AtomicInteger();
    public void addMyAtomic(){
        atomicInteger.getAndIncrement();
    }
}

/**
 * 1 验证volatile的可见性
 *  1.1假如 int number = 0;，number变量之前根本没有添加volatile关键字修饰
 *
 *  1.2 验证volatile不保证原子性
 *      原子性：不可分割，完整性，也即某个线程正在做某个业务时（系列操作），中间不可被加塞或者分割
 *      这个业务的系列操作 要么都成功，要么都失败
 *      number++在多线程下是非线程安全的,如何不加synchronized解决?
 *     解决：
 *      1，加synchronized解决(太重不推荐)
 *      2，使用AtomicInteger
 *
 *  1.3禁止指令重排
 */

public class VolatileDemo {

    public static void main(String[] args)
    {
       MyData myData =new MyData();
       for (int i = 1; i <=20; i++)
       {
           new Thread(() -> {
               for (int j = 1; j <=1000; j++){
                   myData.addPlusPuls();
                   myData.addMyAtomic();
               }
           },String.valueOf(i)).start();
       }

        //等待以上20个线程执行完，再往下执行main线程
        //try { TimeUnit.SECONDS.sleep(6); } catch (InterruptedException e) { e.printStackTrace(); }
        while(Thread.activeCount() > 2)
        {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() +"\t finally number value "+ myData.number);
        System.out.println(Thread.currentThread().getName() +"\t finally atomicInteger value "+ myData.atomicInteger);


    }

    //volatile可以保证可见性，及时通知其他线程，主物理内存的值被修改
    private static void seeOkByVolatile() {
        MyData myData =new MyData();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() +"\tcome in");
            //暂停一会儿线程
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            myData.addTo60();
            System.out.println(Thread.currentThread().getName() +"\tupdate number value is "+myData.number);
            System.out.println(Thread.currentThread().getName() +"\tcome out");
        },"AAA").start();

        //第二个线程是我们的main线程
        while(myData.number == 0){
            //mian线程一直等待循环，直到number不在等于0；
        }

        System.out.println(Thread.currentThread().getName() +"\t misson is over main get value is "+myData.number);
    }


}

/*************************
 * seeOkByVolatile()方法的输出
 * console.out :
     "C:\Program Files\Java\jdk1.8.0_171\bin\java" ...
     AAA	come in
     AAA	update number value is 60
     AAA	come out
 -------------------
 分析：
 number变量之前根本没有添加volatile关键字修饰
     主内存number==0；
     线程AAA 暂停一会儿，让main线程和线程AAA都持有number=0的副本 拷贝到自己的工作内存，
     此时线程AAA去修改
     main线程一直得不到通知更新，还是以为持有number值为0
 ---

 volatile int number = 0;volatile关键字修饰后：
 ---------------------------
 console.out :
     "C:\Program Files\Java\jdk1.8.0_171\bin\java" ...
     AAA	come in
     AAA	update number value is 60
     main	 misson is over main get value is60
     AAA	come out

     Process finished with exit code 0
 ----------------------------


 *************************/