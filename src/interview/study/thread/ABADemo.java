package interview.study.thread;

/**
 * @Auther: xujh
 * @Date: 2019/11/8 14:20
 * @Description:
 */


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题解决:
 *时间戳原子引用 AtomicStampedReference
 *
 */
public class ABADemo {

   static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
   static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100,1);

    public static void main(String[] args) {
        System.out.println("===============以下是ABA问题的产生================");
        new Thread(()->{
            atomicReference.compareAndSet(100,101);
            atomicReference.compareAndSet(101,100);
        },"t1").start();

        new Thread(()->{
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("ABA操作后修改成功与否？"+atomicReference.compareAndSet(100,2019)+"\t atomicReference最新值为"+atomicReference.get().toString());
        },"t2").start();


        //暂一会儿停
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        System.out.println("===============以下是ABA问题的解决================");


        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() +"\t 第1次版本号 "+stamp);

            //暂停1秒钟，保证t4获取t3同样的初始版本号
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() +"\t 第2次版本号 "+atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() +"\t 第3次版本号 "+atomicStampedReference.getStamp());


        },"t3").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() +"\t 第1次版本号 "+stamp);
            System.out.println();
            //暂停3秒钟，保证t3完成ABA操作后去获取
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            //尝试修改
            boolean result = atomicStampedReference.compareAndSet(101,100,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName() +"\t修改成功与否？"+result+"\t atomicStampedReference的当前最新版本为："+atomicStampedReference.getStamp());
        },"t4").start();


    }
}
/**
 * console.out:
 ===============以下是ABA问题的产生================
 ABA操作后修改成功与否？true	 atomicReference最新值为2019

 ===============以下是ABA问题的解决================
 t3	 第1次版本号 1
 t4	 第1次版本号 1

 t3	 第2次版本号 2
 t3	 第3次版本号 3
 t4	修改成功与否？false	 atomicStampedReference的当前最新版本为：3

 Process finished with exit code 0
 */
