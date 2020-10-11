package interview.study.thread.concernt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @Auther: xujh
 * @Date: 2020/10/11
 * @Description: 使用 LockSupport 的 park&Unpark 交替输出 案例
 *  * 线程a输出5次，线程b输出5次,线程c输出5次,求输出abcabcabcabcabc
 */
@Slf4j
public class parkUnparkDemo {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        ParkUnpark pk = new ParkUnpark(5);
        t1=new Thread(()->{
            pk.print("a",t2);
        });
        t2=new Thread(()->{
            pk.print("b",t3);
        });
        t3=new Thread(()->{
            pk.print("c",t1);
        });
        t1.start();
        t2.start();
        t3.start();
        //主线程发起者
        LockSupport.unpark(t1);
    }
}

class ParkUnpark{
    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);

        }
    }
}