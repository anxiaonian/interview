package interview.study.netty.nio;
import lombok.extern.slf4j.Slf4j;

import	java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Auther: xujh
 * @Date: 2019/11/22 10:50
 * @Description:
 */
@Slf4j
public class NIOClient {

    public static void main(String[] args) throws Exception {

        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器 端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",6666);
        //连接服务器
        if (!socketChannel.connect(address)) {

            while (!socketChannel.finishConnect()){
                log.info("因为连接而要时间，客户端不会阻塞，可以做其它工作..");
            }
        }

        //如果连接成功，发送数据
        String str ="helloAA~";

        //Wraps a byte array into a buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());

        //发送数据，将 byteBuffer 写入 socketChannel
        socketChannel.write(byteBuffer);

        System.in.read();

    }
}
