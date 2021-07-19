package chenyunlong.zhangli.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Stan
 */
@Component
public class TokenFilter extends ZuulFilter {

    /**
     * token请求头
     */
    private static final String TOKEN = "Authorization";
    /**
     * token参数名称
     */
    private static final String TOKEN_PARAMETER_NAME = "token";

    private final Logger log = LoggerFactory.getLogger(TokenFilter.class);

    /**
     * pre：可以在请求被路由之前调用
     * route：在路由请求时候被调用
     * post：在route和error过滤器之后被调用
     * error：处理请求时发生错误时被调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info("--->>> TokenFilter {},{}", request.getMethod(), request.getRequestURL().toString());

        // 认证的原始信息
        String auth = "studying:hello";

        //获取请求的参数
        String token = getAuthorization(request);
        if (StringUtils.hasText(token)) {
            auth = token;
        }
        // 进行一个加密的处理
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        // 在进行授权的头信息内容配置的时候加密的信息一定要与“Basic”之间有一个空格
        String authHeader = "Basic " + new String(encodedAuth);
        ctx.addZuulRequestHeader("Authorization", authHeader);
        //该方法只是决定到底要不要转发请求
        return null;
    }

    /**
     * 获取Header里面的token
     *
     * @param request 请求
     */
    private String getAuthorization(HttpServletRequest request) {

        String authorization = request.getHeader(TOKEN);

        if (!StringUtils.hasText(authorization)) {
            authorization = request.getParameter(TOKEN_PARAMETER_NAME);
        }

        if (!StringUtils.hasText(authorization)) {
            request.getAttribute(TOKEN_PARAMETER_NAME);
        }

        return authorization;
    }
}
