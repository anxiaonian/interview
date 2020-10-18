package interview.study.thread.concernt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: xujh
 * @Date: 2020/10/11
 * @Description: 哲学家就餐问题 使用ReentrantLock解决
 */
@Slf4j
public class PhilosopherDemo {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("亚里士多德",c1,c2).start();
        new Philosopher("苏格拉底",c2,c3).start();
        new Philosopher("柏拉图",c3,c4).start();
        new Philosopher("赫拉克利特",c4,c5).start();
        new Philosopher("阿基米德",c5,c1).start();
    }
}
@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
       super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        //方法一：synchronized会导致哲学家们都拿着一把锁 形成死锁等待链
       /* while (true) {
            //尝试获得左手筷子
            synchronized (left) {
                //尝试获得又手筷子
                synchronized (right) {
                    // 吃饭
                    eat();
                }
            }
        }*/

        //方法二：使用ReentrantLock解决
        while (true){
            //尝试获得左手筷子
            if (left.tryLock()) {
                try {
                    //尝试获得又手筷子
                    if (right.tryLock()) {
                        try{
                            // 吃饭
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
        }
    }

    private void eat() {
        log.info("eating...");
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

    }

}
//使用ReentrantLock解决
class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return "筷子{" + name + "}";
    }
}