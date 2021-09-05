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
        request.getServletContext().setAttribute("contextPath", contextPath);

        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        if(null != user){
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for(OrderItem oi : ois){
                cartTotalItemNumber += oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);

        List<Category> cs = (List<Category>) request.getAttribute("cs");
        if(null == cs){
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }

        String uri = request.getRequestURI(); // 获取根路径到地址结尾
        uri = StringUtils.remove(uri, contextPath);
        if(uri.startsWith("/fore") && !uri.startsWith("/foreSevlet")){
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
