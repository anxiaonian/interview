package interview.study.netty;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: xujh
 * @Date: 2019/11/19 23:02
 * @Description: BIO(blocking IO): 同步阻塞

1) Java BIO就是传统的java io编程，其相关的类和接口在java.io

2) BIO(blocking I/O): 同步阻塞，服务器实现模式为-一个连接一个线程， 即客户端有连
接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造
成不必要的线程开销，可以通过线程池机制改善(实现多个客户连接服务器)。

3) BI0方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，
并发局限于应用中，JDK1.4以前的唯一选择， 程序简单易理解

NIO方式适用于连接数目多且连接比较短(轻操作)的架构，比如聊天服务器，弹幕
系统，服务器间通讯等。编程比较复杂，JDK1.4开始支持。

Al0方式使用于连接数目多且连接比较长(重操作)的架构，比如相册服务器，充分.
调用OS参与并发操作，编程比较复杂，JDK7开始支持。

 */
public class BIO {

    public static void main(String[] args) throws IOException {
        //使用线程池机制
        ExecutorService threadPool = Executors.newCachedThreadPool();

        //ServerSocket，监听6666端口
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务启动了。。");

        while (true){
            final Socket socket = serverSocket.accept();
            System.out.println("连接了一个客户端");
            System.out.println(Thread.currentThread().getName());
            //创建一个线程，与之通讯（单独写一个方法）
            threadPool.execute(new Runnable() {

                @Override
                public void run() {
                    //客户端通讯
                    handler(socket);
                }
            });
        }

    }

    //客户端通讯方法
    public static void handler(Socket socket){

        byte []bytes =new byte [1024];
        try {
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            System.out.println(Thread.currentThread().getName());
            //循环读取客户端发送的数据
            while (true){
                int read = inputStream.read();
                if(read !=-1){
                    System.out.println("======"+new String(bytes,0,read).toString());
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
