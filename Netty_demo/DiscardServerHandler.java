package test.Netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * 继承ChannelHandlerAdapter，实现自定义处理的规则
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {
    /**
     * 这里我们覆盖了chanelRead()事件处理方法。 每当从客户端收到新的数据时，调用
     * 这个例子中，收到的消息的类型是ByteBuf
     * @param ctx 通道处理的上下文信息
     * @param msg 接收的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {
            ByteBuf in = (ByteBuf) msg;
            // 打印客户端输入，传输过来的的字符
            System.out.print(in.toString(CharsetUtil.UTF_8));
            ctx.writeAndFlush(Unpooled.copiedBuffer("netty server call_back",CharsetUtil.UTF_8)).addListener(ChannelFutureListener.CLOSE);
        } finally {
            /**
             * ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放。
             * 请记住处理器的职责是释放所有传递到处理器的引用计数对象。
             */
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }

    }

    /***
     * 这个方法会在发生异常时触发
     * 打印异常，关闭channel
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }

}
