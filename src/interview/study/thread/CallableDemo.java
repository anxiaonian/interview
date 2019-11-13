package interview.study.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xujh
 * @Date: 2019/11/13 15:56
 * @Description: Callable接口
 */

class MyCallablle implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println(Thread.currentThread().getName() +"**********调用callable");
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        return 1024;
    }
}

public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<Integer> futureTask =new FutureTask<Integer>(new MyCallablle());

        //借助Thread启动futureTask
        new Thread(futureTask,"AA").start();
        new Thread(futureTask,"BB").start();//此BB线程未进入call()方法，多个线程抢夺同一个futureTask，只操作call一次，用的同一个futureTask

        int a =100;

        while (!futureTask.isDone()){

        }
        //取得返回值
        //此处要求取得call方法返回结果，如果还在计算中强行取，则会一直阻塞着等待计算结果完成返回
        int result  = futureTask.get();//防止call计算阻塞太久影响其他线程 尽量放在代码逻辑最后面去取
        System.out.println("*********result="+(a+result));
        System.out.println("**************main线程");


    }
}
/**
 *console.out
 -------------
 AA**********调用callable
 *********result=1124
 **************main线程

 */