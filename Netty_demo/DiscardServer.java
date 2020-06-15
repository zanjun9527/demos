package test.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃任何进入的数据 启动服务端的DiscardServerHandler
 */
public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        super();
        this.port = port;
    }

    public void run() throws Exception {

        /***
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * 服务端的应用，有2个NioEventLoopGroup会被使用。
         * 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
         * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
         * 并且可以通过构造函数来配置他们的关系。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();//用来接收进来的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//用来处理已经被接收的连接
        System.out.println("准备运行端口：" + port);
        try {

            //ServerBootstrap 是一个启动NIO服务的辅助启动类 你可以在这个服务中直接使用Channel
            ServerBootstrap b = new ServerBootstrap();

            //设置线程工作组
            b = b.group(bossGroup, workerGroup);

            //指定channel，类型。服务端使用ServerSocketChannel类型接收新的连接
            b = b.channel(NioServerSocketChannel.class);
            /***
             * 这里的事件处理类经常会被用来处理一个最近的已经接收的Channel。
             * 也许你想通过增加一些处理类比如NettyServerHandler来配置一个新的Channel
             * 当你的程序变的复杂时，可能你会增加更多的处理类到pipline上，
             * 然后提取这些匿名类到最顶层的类上。
             */
            b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DiscardServerHandler());// demo1.discard
                }
            });
            b = b.option(ChannelOption.SO_BACKLOG, 128);
            b = b.childOption(ChannelOption.SO_KEEPALIVE, true);//我们正在写一个TCP/IP的服务端，因此我们被允许设置socket的参数选项比如tcpNoDelay和keepAlive。

             //绑定端口并启动去接收进来的连接
            ChannelFuture f = b.bind(port).sync();

            //这里会一直等待，直到socket被关闭
            f.channel().closeFuture().sync();
        } finally {
            //关闭资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new DiscardServer(port).run();
        System.out.println("server:run()");
    }
}
