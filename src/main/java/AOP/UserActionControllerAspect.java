package AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Configuration
@Aspect
public class UserActionControllerAspect {
    private final String userActionControllerAspectPonit = "execution(* com.sf.dataplatform.*.controller.*)";

    //定义切入点,拦截servie包其子包下的所有类的所有方法
    //@Pointcut("execution(* com.sf.dataplatform.*.controller.*)")
    //拦截指定的方法,这里指只拦截TestService.getResultData这个方法
    @Pointcut(userActionControllerAspectPonit)
    public void excuteService() {

    }

    //执行方法前的拦截方法
    @Before("excuteService() && @annotation(AOP.ActionDescription)")
    public void doBeforeMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ActionDescription actionDescription = method.getAnnotation(ActionDescription.class);
        //获取客户端ip
        String ip = getIp(request);
        //获取URL地址
        String url = request.getRequestURI();
        //获取请求方法名
        String className = method.getName();

        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        for (Object argItem : obj) {
            //todo 参数处理
        }

        //具体逻辑
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(value = userActionControllerAspectPonit, returning = "result")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object result) {
        System.out.println("第一个后置返回通知的返回值：" + result);
        if (result instanceof ResultVO) {
            //todo 判断方法是否需要监控log，是设置LogMonitor true
            ((ResultVO) result).setLogMonitor(true);
        }
        System.out.println("修改完毕-->返回ResultVO为:" + result);
    }



    private String getIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (ip != null && ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        if (ip != null && ip.contains(":")) {
            ip = ip.split(":")[0];
        }
        return ip;
    }

}
