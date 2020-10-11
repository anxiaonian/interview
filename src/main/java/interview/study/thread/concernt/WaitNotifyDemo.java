package interview.study.thread.concernt;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: xujh
 * @Date: 2020/10/11
 * @Description: 使用 wait&notify 交替输出 案例
 * 线程a输出5次，线程b输出5次,线程c输出5次,求输出abcabcabcabcabc
 */
@Slf4j
public class WaitNotifyDemo {
    public static void main(String[] args) {

        WaitNotify wn = new WaitNotify(1, 5);
        new Thread(()->{
            wn.print("a",1,2);
        },"t1").start();
         new Thread(()->{
             wn.print("b",2,3);
        },"t2").start();
         new Thread(()->{
             wn.print("c",3,1);
        },"t3").start();


    }
}

/**
 * 输出内容  等待标记 下一个标记
 * a        1           2
 * b        2           3
 * c        3           1
 *
 */
class WaitNotify {
    //等待标记
    private int flag;
    //循环次数
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
    //打印 a      1       2
    public void print(String str,int waitFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (flag!=waitFlag){//不满足条件的线程进入等待
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //满足条件的线程打印
                System.out.print(str);
                this.flag = nextFlag;
                this.notifyAll();//唤醒所有线程
            }
        }
    }
}