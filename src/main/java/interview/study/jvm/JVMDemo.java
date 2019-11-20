package interview.study.jvm;

/**
 * @Auther: xujh
 * @Date: 2019/11/15 16:55
 * @Description:
 */
public class JVMDemo {

    public static void main(String[] args) {

    }
}
/**
 *
 -Xms512m //初始堆内存,默认为物理内存的1/64  等同于 -XX:InitialHeapSize
 -Xmx2048m //最大堆内存,默认为物理内存的1/4	等同于 -XX:MaxHeapSize
 -XX:PermSize=512m //初始持久带内存
 -XX:MaxPermSize=2048m //最大持久带内存

 如何查看一个正在运行中的java程序。 它的某个jvm参数是否开启?具体值是多少?
 jps -l
 jinfo


 第一种，查看参数盘点家底
 jps -l
 jinfo -flag 具体参数java进程编号
 jinfo -flags	java进程编号

 第二种，查看参数盘点家底
 java -XX:+PrintFlagsInitial
 -XX: +PrintFlagsFinal
 参数值   = 表示默认   := 表示修改过

 java -XX:+PrintFlagsFinal 主要查看修改更新

 PrintFlagsFinal举例， 运行java侖令的同时打印出参数
 例如  java -XX:+PrintFlagsFinal -XX:MetaspaceSize=512m T  //T表示运行的Java类名

 java -XX: +PrintCommandLineFlags -version


 常用参数
 -Xms512m       //初始堆内存,默认为物理内存的1/64  等同于 -XX:InitialHeapSize
 -Xmx2048m      //最大堆内存,默认为物理内存的1/4	等同于 -XX:MaxHeapSize
 -Xss           //设置单线程的栈大小，一般默认为512~1024 等同于 -XX:ThreadStackSize
 -Xmn           //设置年轻代大小
 -XX:MetaspaceSize //设置元空间大小，元空间的本质和水久代类似，都是对JVM规范中方法区的实现。
                     不过元空间与永久代之间最大的区别在于:
                     元空间并不在虚拟机中，而是使用本地内存。
                     因此，默认情况下，元空间的大小仅受本地内存限制。

 -XX: +UseSerialGC	    串行垃圾回收器
 -XX: +UseParallelGC	并行垃圾回收器

     典型没置案例
     -Xms128m -Xmx4096m -Xss1024k -XX:MetaspaceSize=512m -XX:+PrintCommandLineFlags
     -XX:+PrintGCDetails -XX: +UseSerialGC

 -XX:SurvivorRatio	//设置新生代中eden和S0/S1空间的比例
     默认
     -XX:SurvivorRatio=8，Eden:S0:S1 =8:1:1
     假如
     -XX:SurvivorRatio=4，Eden:S0:S1 =4:1:1
     SurvivorRatio值就是设置eden区的比例占多少，S0/S1相同

 -XX:NewRatio	//配置年轻代与老年代在堆结构的占比
     默认
     -XX:NewRatio=2新生代占1,老年代2，年轻代占整个堆的1/3
     假如
     -XX:NewRatio=4新生代占1,老年代4，年轻代占整个堆的1/5
     NewRatio值就是设置老年代的占比，剩下的1给新生代
     典型没置案例：
     默认
     Xms10m -Xmx10m xX:+PrintGCDetails -XX:+UseSerialGC -XX:NewRatio=2
     修改
     -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseSerialGC -XX:NewRatio=4

 -XX:MaxTenuringThreshold //设置垃圾最大年龄, 默认15
     -XX:MaxTenuringThreshold=0: 设置垃圾最大年龄。如果设置为0的话，则年轻代对象不经过Survivor区， 直接进入年
     老代。对于年老代比较多的应用，可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多
     次复制，这样可以增加对象再年轻代的存活时间，增加在年轻代即被回收的概论。


 */