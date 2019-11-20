package interview.study.threadPool;
import java.util.concurrent.*;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: xujh
 * @Date: 2019/11/13 19:56
 * @Description: ThreadPool线程池
创建线程池的三种方式：

Executors.newSingleThreadExecutor()
    主要特点如下:
    1.创建一个单线程化的线程池,它只会用唯一的工作线程来执行任务,保证所有任务都按照指定顺序执行.
    2.newSingleThreadExecutor将corePoolSize和MaxmumPoolSize都设置为1,它使用的的LinkedBlockingQueue

Executors.newFixedThreadPool();
    主要特点如下:
    1.创建一个定长线程池,可控制线程的最大并发数,超出的线程会在队列中等待.
    2.newFixedThreadPool创建的线程池corePoolSize和MaxmumPoolSize是 相等的,它使用的的LinkedBlockingQueue

Executors.newCachedThreadPool();
    主要特点如下:
    1.创建一个可缓存线程池,如果线程池长度超过处理需要,可灵活回收空闲线程,若无可回收,则创建新线程.
    2.newCachedThreadPool将corePoolSize设置为0MaxmumPoolSize设置为Integer.MAX_VALUE,它使用的是SynchronousQUeue,
    也就是说来了任务就创建线程运行,如果线程空闲超过60秒,就销毁线程

这三种创建方式哪一个用的多？
答：以上3种都不用，强制 自己手动通过ThreadPoolExecutor创建
 */
public class MyThreadPool {

    public static void main(String[] args) {

        //System.out.println(Runtime.getRuntime().availableProcessors());

        //ExecutorService exec = Executors.newSingleThreadExecutor(5);//一个任务一个线程执行的任务场景
        //ExecutorService exec1 = Executors.newFixedThreadPool(5);//执行一个长期的任务,性能好很多
        //ExecutorService exec2 = Executors.newCachedThreadPool(5);

        //以上3种都不用，强制 自己手动通过ThreadPoolExecutor创建
        ExecutorService esxec = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable> (3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());


        try {
            for(int i = 1; i <= 10; i++) {
                esxec.execute(()->{
                    System.out.println(Thread.currentThread().getName() +"\t 办理业务");
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            esxec.shutdown();
        }
    }
}
