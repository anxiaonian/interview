package interview.study.thread.concernt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: xujh
 * @Date: 2020/10/11
 * @Description: 使用 ReentrantLock 的 await&signal 交替输出 案例
 * 线程a输出5次，线程b输出5次,线程c输出5次,求输出abcabcabcabcabc
 */
@Slf4j
public class AwaitSignalDemo {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);

        //ReentrantLock多条件变量，（各自的休息室等待）
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(()->{
            awaitSignal.print("a",a,b);
        }).start();
        new Thread(()->{
            awaitSignal.print("b",b,c);
        }).start();
        new Thread(()->{
            awaitSignal.print("c",c,a);
        }).start();
        //一开始都是进入等待室等待着，需要一个线程（主线程）发起者
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("开始。。。");
        awaitSignal.lock();
        try{
            a.signal();//唤醒a
        }finally {
            awaitSignal.unlock();
        }
    }
}

class AwaitSignal extends ReentrantLock {
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str,Condition current,Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();//开始大家都进入各自的休息室await等待，由主线程启动开始唤醒
                System.out.print(str);
                next.signal();//当前current执行完打印，下一个线程唤醒
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}