package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.service.Delete;
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

        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
//        propertyDAO.delete(id);
        new Delete().deleteProperty(id); // 解决外键无法删除的问题
        return "@admin_property_list?cid=" + p.getCategory().getId(); // 传递一个 cid ，  list 需要用到类别
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

        return "@admin_property_list?cid=" + p.getCategory().getId();// 传递一个 cid ，  list 需要用到类别
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        List<Property> ps = propertyDAO.list(cid, page.getStart(), page.getCount()); // 传递到前端的数据
        int total = propertyDAO.getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + c.getId());  // 传递 cid 参数。在地址栏上有显示。 为什么放在page里，不直接搞一个 cid 的参数呢？因为page里面，所有的jsp就都可以用了啊

        request.setAttribute("ps", ps);
        request.setAttribute("c", c); // 新增属性会用到。
        request.setAttribute("page", page);

        return "admin/listProperty.jsp";
    }
}
