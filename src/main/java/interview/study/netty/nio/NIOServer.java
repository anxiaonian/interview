package interview.study.netty.nio;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Auther: xujh
 * @Date: 2019/11/22 11:04
 * @Description: NIOServer端
 * 案例要求:
    1)编写一个NIO入门案例，实现服务器端和客户端之间的数据简单通讯(非阻塞)
    2)目的:理解NIO非阻塞网络编程机制
 代码设计
    1.当客户端连接时，会通过ServerSocketChannel得到SocketChannel
    2.将socketChannel注册到Selector上，register(Selector sel, int ops),一个selector上可以注册多个SocketChannel
    3.注册后返回一个SelectionKey.会和该Selector关联(集合)
    4. Selector 进行监听select方法， 返回有事件发生的通道的个数.
    5.进一步得到各个SelectionKey(有事件发生)
    6.在通过SelectionKey反向获取SocketChannel .方法channel)
    7.可以通过得到的channel，完成业务处理


 */
@Slf4j
public class NIOServer {

    public static void main(String[] args) throws Exception {

        //创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel =ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口 6666 ，在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //将socketChannel 注册到 Selector 上 ,关心 事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            //这里等待1秒，如果没有事件发生， 返回
             if (selector.select(1000) == 0){ //没有事件发生
                 log.info("服务器等待1秒，无连接...");
                 continue;
             }

            //如果返回>0, 就获取相关selectionKey集合
            //1，如果返回>0, 表示已经获取到关注的事件
            //2，selector.selectedKeys()  返回关注的事件集合
            //通过 selectionKeys 反向获取 Channel通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 Set<SelectionKey> 集合， 使用Iterator
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            //
            while (keyIterator.hasNext()){
                //获取到 SelectionKey
                SelectionKey key = keyIterator.next();

                //根据key 对应通道的发生事件做相应处理
                if(key.isAcceptable()){ //如果事件为 OP_ACCEPT，表示有新的客户端连接
                    //该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    log.info("客户端连接成功生成了一个socketChannel" + socketChannel.hashCode());

                    //将 socketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);

                    //将 SocketChannel 注册到 selector, 关注事件为 OP_READ  同时给 SocketChannel 关联一个 ByteBuffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));


                }

                if (key.isReadable()){ // 发生 OP_READ 事件
                    //通过key 反向得到 SocketChannel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //通过key 获取到SocketChannel 关联的  ByteBuffer
                    ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
                    channel.read(byteBuffer);
                    log.info("from 客户端" + new String(byteBuffer.array()));

                }
                //手动从集合中删除当前的 selectionKeys, 防止重复操作
                keyIterator.remove();
            }

        }

    }
}
