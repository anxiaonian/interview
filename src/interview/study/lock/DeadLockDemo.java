package interview.study.lock;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: xujh
 * @Date: 2019/11/14 15:39
 * @Description: 死锁演示
 */

class HoldLock implements Runnable {

    private String LockA;
    private String LockB;

    public HoldLock(String lockA, String lockB) {
        this.LockA = lockA;
        this.LockB = lockB;
    }


    @Override
    public void run() {

        synchronized (LockA)
        {
            System.out.println(Thread.currentThread().getName() +"\t自己持有"+LockA+"\t尝试获得"+LockB);
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
                synchronized (LockB){
                    System.out.println(Thread.currentThread().getName() + "\t自己持有" + LockA + "\t尝试获得" + LockB);
                }
        }
    }
}

public class DeadLockDemo {

    public static void main(String[] args) {
        String LockA = "LockA";
        String LockB = "LockB";

        new Thread(new HoldLock(LockA,LockB),"线程AA").start();
        new Thread(new HoldLock(LockB,LockA),"线程BB").start();
    }
}

/**
 * 控制台命令行
 * jps -l            查看进程号如：72556
 *jstack 72556       找到死锁进程


 *  D:\IdeaProjects\interview>jps -l
      44508
     74500 org.jetbrains.jps.cmdline.Launcher
     56456 sun.tools.jps.Jps
     72556 interview.study.lock.DeadLockDemo


 *  D:\IdeaProjects\interview>jstack 72556
  .....
 Found one Java-level deadlock:
 =============================
 "线程BB":
 waiting to lock monitor 0x0000000017ea0488 (object 0x00000000d618f7f8, a java.lang.String),
 which is held by "线程AA"
 "线程AA":
 waiting to lock monitor 0x0000000017e9dd58 (object 0x00000000d618f830, a java.lang.String),
 which is held by "线程BB"


 Java stack information for the threads listed above:
 ===================================================
 "线程BB":
         at interview.study.lock.HoldLock.run(DeadLockDemo.java:30)
         - waiting to lock <0x00000000d618f7f8> (a java.lang.String)
         - locked <0x00000000d618f830> (a java.lang.String)
         at java.lang.Thread.run(Thread.java:748)
 "线程AA":
         at interview.study.lock.HoldLock.run(DeadLockDemo.java:30)
         - waiting to lock <0x00000000d618f830> (a java.lang.String)
         - locked <0x00000000d618f7f8> (a java.lang.String)
         at java.lang.Thread.run(Thread.java:748)

 */