package love.wangqi.filter;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import love.wangqi.config.GatewayConfig;
import love.wangqi.context.ContextUtil;
import love.wangqi.handler.GatewayRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wangqi
 * @description:
 * @date: Created in 2018/9/5 下午2:16
 */
public class SendResponseFilter extends GatewayFilter {
    private static final Logger logger = LoggerFactory.getLogger(SendResponseFilter.class);


    private GatewayConfig config = GatewayConfig.getInstance();

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public void filter(Channel channel) throws Exception {
        Exception e = ContextUtil.getException(channel);
        if (e == null) {
            try {
                FullHttpResponse response = ContextUtil.getResponse(channel);
                config.getResponseHandler().send(channel, response);
            } catch (Exception sendException) {
                logger.error(sendException.getMessage(), sendException);
                throw sendException;
            }

        } else {
            GatewayRunner.getInstance().errorAction(channel);
        }
    }
}
