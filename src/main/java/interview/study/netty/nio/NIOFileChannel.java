package interview.study.netty.nio;
import	java.io.File;

import	java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Auther: xujh
 * @Date: 2019/11/20 12:36
 * @Description: 输入输出流Channel
 */
public class NIOFileChannel {

    public static void main(String[] args) throws Exception {
        //输出流
        //FileOutputStream01();
        //输入流
        //FileInputStream02();

        /**
         *
         使用一个buffer，文件1.txt ---> 文件2.txt
         */
        FileInputStream fileInputStream = new FileInputStream("E:\\aa\\bb\\1.txt");
        //通过输出流得到一个FileChannel
        FileChannel fileChannelIn = fileInputStream.getChannel();
        //创建一个输出流
        FileOutputStream fileOututStream =new FileOutputStream("E:\\aa\\bb\\2.txt");
        //通过输出流得到一个FileChannel

        FileChannel fileChannelOut = fileOututStream.getChannel();

        //创建一个缓冲区buffer
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);

        while (true)
        {
            //需要清空buffer
            byteBuffer.clear();
            //读取1.txt
            int read = fileChannelIn.read(byteBuffer);
            if(read ==-1) break;//读完跳出
            //将byteBuffer反转flip()，
            byteBuffer.flip();
            //将byteBuffer写入2.txt
            fileChannelOut.write(byteBuffer);
        }
        //最后关闭流
        fileInputStream.close();
        fileOututStream.close();

    }

    private static void FileInputStream02() throws IOException {
        //创建文件
        File file = new File("E:\\aa\\bb\\33.txt");
        //创建一个文件的输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过输出流得到一个FileChannel
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建一个缓存区
        ByteBuffer buffer = ByteBuffer.allocate((int)file.length());

        //将fileChannel通道的数据读入到buffer中
        fileChannel.read(buffer);

        //将buffer里的byte数据，转成String
        String str1 = new String(buffer.array());
        System.out.println(str1);

        //关闭输入流
        fileInputStream.close();
    }

    private static void FileOutputStream01() throws IOException {
        String str ="anxiaonian";
        //创建一个输出流
        FileOutputStream fileOututStream =new FileOutputStream("E:\\aa\\bb\\33.txt");

        //通过输出流得到一个FileChannel
        FileChannel fileChannel = fileOututStream.getChannel();

        //创建一个缓冲区buffer
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);

        //将str放入buffer中
        byteBuffer.put(str.getBytes());

        //对byteBuffer 进行flip(),重置position
        byteBuffer.flip();

        //通过FileChannel通道将byteBuffer里面的数据写入文件
        fileChannel.write(byteBuffer);

        //关闭输出流
        fileOututStream.close();
    }
}
