package interview.study.thread;
import	java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: xujh
 * @Date: 2019/11/8 13:31
 * @Description:
 */

/**
 * 1,CAS是什么？（自旋）
 *      比较并交换
    2,CAS的全称为Compare-And-Swap ,它是一条CPU并发原语.
     它的功能是判断内存某个位置的值是否为预期值,如果是则更新为新的值,这个过程是原子的.

     CAS并发原语提现在Java语言中就是sun.misc.UnSaffe类中的各个方法.调用UnSafe类中的CAS方法,
     JVM会帮我实现CAS汇编指令.这是一种完全依赖于硬件 功能,通过它实现了原子操作,再次强调,由于CAS是一种系统原语,
     原语属于操作系统用于范畴,是由若干条指令组成,用于完成某个功能的一个过程,
     并且原语的执行必须是连续的,在执行过程中不允许中断,也即是说CAS是一条原子指令,不会造成所谓的数据不一致的问题.

 3.CAS缺点
    3.1循环时间长开销很大:（自旋）
    3.2只能保证一个共享变量的原子性
    3.3引出来ABA问题???
 */
public class CASDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println( atomicInteger.compareAndSet(5,2019)+"\t current data "+atomicInteger.get());
        System.out.println( atomicInteger.compareAndSet(5,1024)+"\t current data "+atomicInteger.get());

        atomicInteger.getAndIncrement();
    }
}

   //atomicInteger.getAndIncrement()方法的源代码:
/**
 * Atomically increments by one the current value.
 *
 * @return the previous value
 */
//public final int getAndIncrement() {
      //  return unsafe.getAndAddInt(this, valueOffset, 1);
                                    /*当前对象   内存地址    期望操作*/
//  }
//    印出来一个问题:UnSafe类是什么?

/**
 *
 1.UnSafe
     是CAS的核心类 由于Java 方法无法直接访问底层 ,需要通过本地(native)方法来访问,
     UnSafe相当于一个后面,基于该类可以直接操作特额定的内存数据.
     UnSafe类在于sun.misc包中,其内部方法操作可以向C的指针一样直接操作内存,
     因为Java中CAS操作的助兴依赖于UNSafe类的方法.
     * 注意:UnSafe类中所有的方法都是native修饰的,也就是说UnSafe类中的方法都是直接调用操作底层资源执行响应的任务
 2.变量ValueOffset,便是该变量在内存中的偏移地址,因为UnSafe就是根据内存偏移地址获取数据的

 3.变量value和volatile修饰,保证了多线程之间的可见性.
 */

//atomicInteger.getAndIncrement()方法的源代码:（自旋）
/*****
 public final int getAndAddInt(Object var1, long var2, int var4) {
 int var5;
 do {
 var5 = this.getIntVolatile(var1, var2);
 } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

 return var5;
 }
 ----
 var1 AtomicInterger对象本身，
 var2 该对象的引用地址
 var4 需要变动的数据
 var5 是用过var1 var2后最新找出的主内存中真实的值。
 用该对象当前的值与var5比较，
 如果相同，则更新为var5+var4 并且返回true，
 如果不同，继续取值然后比较，直到更新完成
 ----
 */