package interview.study.netty.nio;

import java.nio.IntBuffer;
import java.nio.channels.Channel;

/**
 * @Auther: xujh
 * @Date: 2019/11/20 10:24
 * @Description: 缓冲区(Buffer)
 *
 * 缓冲区(Buffer) :缓冲区本质上是一个可以读写数据的内存块，底层是有一个数组,
 * 可以理解成是一个容器对象(含数组)，该对象提供了一组方法，可以更轻松地使用内存块，
 * 缓冲区对象内置了一些机制，能够跟踪和记录缓冲区的状态变化情况。Channel 提供从文件、
 * 网络读取数据的渠道，但是读取或写入的数据都必须经由Buffer。

    Buffer就是_一个内存块，底层是有一个数组
    数据的读取写入是通过Buffer,这个和BIO不同，BIO 中要么是输入流，或者是
    输出流，不能双向，但是NIO的Buffer 是可以读也可以写，需要flip方法切换。
    channel是双向的，可以返回底层操作系统的情况，比如Linux，底层的操作系统

 *  BIO基于字节流和字符流进行操作，而NIO 基于Channel(通道)和Buffer(缓冲区)进
    行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到Channel通道中。
    Selector(选择器)用于监听多个通道的事件(比如:连接请求，数据到达等)，
    因此使用单个线程就可以监听多个客户端通道

Channel在NIO中是-一个接口
public interface Channel extends Closeable{}
常用的Channel类有: FileChannel、
DatagramChannel、ServerSocketChannel 和
SocketChannel.
FileChannel用于文件的数据读写，.
DatagramChannel用于UDP的数据读写，
ServerSocketChannel和SocketChannel用于TCP
的数据读写。

 */
public class BasicBuffer {
    public static void main(String[] args) {

        //说明buffer的使用
        //创建一个buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for(int i = 1; i <= intBuffer.capacity(); i++) {
                intBuffer.put(i*2);
        }
        //将buffer转换，读写切换,重置position
        intBuffer.flip();
        //读取buffer数据
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
