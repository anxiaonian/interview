package interview.study.lock;

/**
 * @Auther: xujh
 * @Date: 2019/11/9 15:26
 * @Description: 关门案例&秦灭六国
 */

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch  作用：让一些线程阻塞直到另外一些完成后才被唤醒
 *
 * 主要有两个方法,当一个或多个线程调用await方法时,
 * 调用线程会被阻塞.其他线程调用countDown方法计数器减1(调用countDown方法时线程不会阻塞),
 * 当计数器的值变为0,因调用await方法被阻塞的线程会被唤醒,继续执行
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch downLatch =new CountDownLatch(5);
        for(int i = 1; i <= 5; i++) { 
                new Thread(()->{
                   // System.out.println(Thread.currentThread().getName() +"\t同学离开教室");
                    System.out.println(Thread.currentThread().getName() +"国，被灭");

                    downLatch.countDown();
                },CountryEnum.getCountryEnum(i).getRetMessage()).start();
            }

            //需要保证全部同学离开后，调用await()方法 通知班长线程(main)才可以离开(执行）
            downLatch.await();
       // System.out.println(Thread.currentThread().getName() +"\t全部同学已经离开，班长最后关门离开教室");
        System.out.println(Thread.currentThread().getName() +"\t======秦灭六国，一统华夏");
    }
}


/**
 *console.out
 --------------
 1	同学离开教室
 3	同学离开教室
 2	同学离开教室
 4	同学离开教室
 5	同学离开教室
 main	全部同学已经离开，班长最后关门离开教室
 --------------

 齐国，被灭
 楚国，被灭
 燕国，被灭
 韩国，被灭
 赵国，被灭
 main	======秦灭六国，一统华夏

 */