package config;

import org.glassfish.jersey.server.ResourceConfig;
/*999*/
public class RestApplication extends ResourceConfig {
    public RestApplication() {
     //服务类所在的包路径
     packages("restfulEasyDemo");
     //注册JSON转换器
//     register(JacksonJsonProvider.class);
      //打印访问日志，便于跟踪调试，正式发布可清除 
//     register(LoggingFilter.class);
    }
}