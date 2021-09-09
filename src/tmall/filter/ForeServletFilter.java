package tmall.filter;

import org.apache.commons.lang.StringUtils;
import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "ForeServletFilter")
public class ForeServletFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        //chain.doFilter(req, resp);
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String contextPath = request.getServletContext().getContextPath(); // 获取项目的根路径

        // String dd = request.getContextPath(); 和上一句的效果是一样的
        /**
         * https://blog.csdn.net/qq_44813090/article/details/104578634
         * 关于servlet的作用域
         * java四大作用域按作用范围从大到小分为：
         * ServletContext，Session，request，pageContext
         *
         * ServletContext是Web的四大作用域中最大的，范围是整个web项目，ServletContext，是一个
         * 全局的储存信息的空间，服务器开始，其就存在，服务器结束，其才释放。
         * ServletContext一般存储的是整个web项目重要的共享信息。
         */

        // 全局变量
        request.getServletContext().setAttribute("contextPath", contextPath); // 这个可以学习下

        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        if(null != user){
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for(OrderItem oi : ois){
                cartTotalItemNumber += oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber); // 购物车数量

        List<Category> cs = (List<Category>) request.getAttribute("cs");
        if(null == cs){
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }

        String uri = request.getRequestURI(); // 获取根路径到地址结尾
        uri = StringUtils.remove(uri, contextPath);
        if(uri.startsWith("/fore") && ! uri.startsWith("/foreSevlet")){
            String method = StringUtils.substringAfterLast(uri, "/fore");
            request.setAttribute("method", method);
            req.getRequestDispatcher("/foreServlet").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
