package interview.study.lock;
import	java.util.HashMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class  MyCache {//资源类

    private volatile Map<String, Object> map =new HashMap<> ();
    public ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void put(String key ,Object value){
        rwLock.writeLock().lock();//写锁
        try {

            System.out.println(Thread.currentThread().getName() +"\t正在写入 \t"+key);
            //模拟延迟
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            map.put(key,value);
            System.out.println(Thread.currentThread().getName() +"\t写入完成 \t");

        }catch (Exception e){
             e.printStackTrace();
        }finally{
            rwLock.writeLock().unlock();
        }

    }

    public void get(String key){
        rwLock.readLock().lock();//读锁

        try {
            System.out.println(Thread.currentThread().getName() +"\t正在读取\t");
            //模拟延迟
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            Object result = map.get(key);
            System.out.println(Thread.currentThread().getName() +"\t读取完成\t"+result);

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            rwLock.readLock().unlock();
        }
    }
    public void clear(){
        map.clear();
    }


}

/**
 * @Auther: xujh
 * @Date: 2019/11/8 20:02
 * @Description:独占锁(写)/共享锁(读)/互斥锁
 */

/**
 *  独占锁:指该锁一次只能被一个线程所持有。对ReentrantLock和Synchronized而言都是独占锁
    共享锁:指该锁可被多个线程所持有。

 对ReentrantReadWriteLock其读锁是共享锁，其写锁是独占锁。
 读锁的共享锁可保证并发读是非常高效的。读写，写读，写写的过程是互斥的。

 多个线程同时读一个资源类没有任何问题，所以为了满足并发量，读取共资源应该可以同时进行。
 如果有一个想去写共享资源来，就不应该再有其它线程可以对该资源进行读或写
 小总结：
     读-读能共存
     读-写不能共存
     写-写不能共存
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 1; i <= 5; i++){
            final  int temp = i;
            new Thread(()->{
                myCache.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }

        for (int i = 1; i <= 5; i++){
            final  int temp = i;
            new Thread(()->{
                myCache.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}
/**
 * console.out:

 1	正在写入 	1
 1	写入完成
 2	正在写入 	2
 2	写入完成
 3	正在写入 	3
 3	写入完成
 4	正在写入 	4
 4	写入完成
 5	正在写入 	5
 5	写入完成
 1	正在读取
 2	正在读取
 3	正在读取
 4	正在读取
 5	正在读取
 1	读取完成	1
 3	读取完成	3
 4	读取完成	4
 2	读取完成	2
 5	读取完成	5

 */