package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "PropertyServlet")
public class PropertyServlet extends BaseBackServlet {


    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        String name = request.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setName(name);
        propertyDAO.add(p);
        System.out.println("诱导我啦" + name);
        System.out.println("诱导我啦" + name);
        System.out.println("诱导我啦" + name);
        System.out.println("诱导我啦" + name);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        propertyDAO.delete(id);
        return "@admin_property_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        request.setAttribute("p", p);

        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setName(name);
        p.setId(id);
        propertyDAO.update(p);
        return "admin_property_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        List<Property> ps = propertyDAO.list(cid, page.getStart(), page.getCount()); // 传递到前端的数据
        int total = propertyDAO.getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + c.getId());  // 这一句的作用，就是传递 cid 参数。告诉后端，取哪一个 category 。在地址栏上有显示。

        request.setAttribute("ps", ps);
        request.setAttribute("c", c);
        request.setAttribute("page", page);

        return "admin/listProperty.jsp";
    }
}
