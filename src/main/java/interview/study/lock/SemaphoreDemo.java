package interview.study.lock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xujh
 * @Date: 2019/11/11 14:09
 * @Description: SemaphoreDemo 抢车位案例(信号量控制)
 *
 * 信号量的主要用作两个目的,一个是用于多喝共享资源的相互排斥使用,另一个用于并发资源数的控制.
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);//模拟三个车位

        for(int i = 1; i <= 6; i++) { //模拟六辆车抢车位

            new Thread(()->{
                try {
                    semaphore.acquire();//信号量占用 -1
                    System.out.println(Thread.currentThread().getName() +"\t抢到车位");
                    try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println(Thread.currentThread().getName() +"\t停车3秒钟后离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();//释放信号量 +1
                }
            },String.valueOf(i)).start();
            }


    }
}
