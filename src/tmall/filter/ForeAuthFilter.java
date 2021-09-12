package tmall.filter;

import org.apache.commons.lang.StringUtils;
import tmall.bean.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebFilter(filterName = "ForeAuthFilter")
public class ForeAuthFilter implements Filter {
    public void destroy() {
    }

    /**
     * 这是个好东西啊,可以解决没有登录需要验证的问题.
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
//        chain.doFilter(req, resp);

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String contextpath = request.getContextPath();

        String[] noNeedAuthPage = new String[]{
                "home",
                "homepage",
                "checkLogin", // checkLogin,有Ajax验证,这里就不需要了
                "register",
                "loginAjax", // 模态的验证,这里同样不需要
                "login",
                "product",
                "category",
                "search"
        };

        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextpath);
        if(uri.startsWith("/fore") && !uri.startsWith("/foreServlet")){
            String method = StringUtils.substringAfterLast(uri, "/fore");
            if(!Arrays.asList(noNeedAuthPage).contains(method)){
                User user = (User) request.getSession().getAttribute("user");
                if(null == user){
                    response.sendRedirect("login.jsp");
                    return;
                }
            }
        }

        chain.doFilter(request, response);

    }


    public void init(FilterConfig config) throws ServletException {

    }

}
