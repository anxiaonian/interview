package interview.study.thread;

/**
 * @Auther: xujh
 * @Date: 2019/11/8 12:42
 * @Description:
 */


/**
 * DLC（双检锁）不一定线程安全，原因有指令重排序的存在，加入volatile可以禁止指令重排
 *  原因：在某一个线程执行到第一次检测，读取到的instance不为null时，instance的引用对象 可能没有完成初始化
 *
 * instance = new SingletonDemo();可分为3步：
 *      memory= allocate();  //1分配对象内存空间
 *      instance(memory);    //2对象初始化
 *      instance = memory;   //3设置instance指向刚分配的内存地址，此时instance != null
 *
 * 步骤2和步骤3不存在数据依赖关系，而且无论重排序前或者重排后执行结果在单线程中都没有改变，
 * 因此单线程下这种情况重排序是允许的。
 *---------------
 *
 * instance = new SingletonDemo();可分为3步：
 *      memory= allocate();  //1分配对象内存空间
 *      instance = memory;   //3设置instance指向刚分配的内存地址，此时instance != null，但是instance对象还没初始化完成！！！
 *      instance(memory);    //2对象初始化
 *
 * 指令重排只会保证单线程下的语义一致性，此时不能保证多线程下的语义一致性。
 * 所以只要一条线程访问instance != nul时，由于instance对象未必已经初始化完成！造成线程不安全问题
 *
 * 解决：
 * 双检锁机制 + instance实例变量使用关键字volatile控制
 */
public class SingletonDemo {

    private static volatile SingletonDemo instance = null; //volatile禁止指令重排

    private SingletonDemo()
    {
        System.out.println(Thread.currentThread().getName() +"\t 我是构造方法SingletonDemo()");
    }

    //双检锁机制Double Check Lock
    public static SingletonDemo getInstance()//不锁方法上
    {
        if (instance == null)
        {
            synchronized (SingletonDemo.class)//锁代码块，并再次检查
            {
                if (instance == null)
                {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println();
        for (int i = 1; i <=10; i++)
        {
            new Thread(() -> {
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
