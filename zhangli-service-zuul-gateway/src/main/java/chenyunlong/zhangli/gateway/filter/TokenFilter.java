package chenyunlong.zhangli.gateway.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TokenFilter extends ZuulFilter {

    private static final String TOKEN = "Authorization";

    private final Logger log = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public String filterType() {
        return "pre";
//        pre：可以在请求被路由之前调用
//        route：在路由请求时候被调用
//        post：在route和error过滤器之后被调用
//        error：处理请求时发生错误时被调用
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

        //
        //获取请求的参数
        //
        String token = getAuthorization(request);
        // 认证的原始信息
        String auth = "studyjava:hello";
        // 进行一个加密的处理
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        // 在进行授权的头信息内容配置的时候加密的信息一定要与“Basic”之间有一个空格
        String authHeader = "Basic " + new String(encodedAuth);
        ctx.addZuulRequestHeader("Authorization", authHeader);

        try (Entry entry = SphU.entry("HelloWorld")) {
            // Your business logic here.
            System.out.println("hello world");
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);
            return null;
        } catch (BlockException e) {
            // Handle rejected request.
            e.printStackTrace();
            //不对其进行路由
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(400);
            ctx.setResponseBody("token is empty");
            ctx.set("isSuccess", false);
            return null;
        }
    }

    private String getAuthorization(HttpServletRequest request) {

        String authorization = request.getHeader(TOKEN);

        if (StringUtils.isBlank(authorization)) {
            authorization = request.getParameter(TOKEN);
        }

        if (StringUtils.isBlank(authorization)) {
            request.getAttribute(TOKEN);
        }

        return authorization;
    }
}
