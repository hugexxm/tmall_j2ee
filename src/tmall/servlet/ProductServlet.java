package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.service.Delete;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ProductServlet")
public class ProductServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Product p = new Product();

        p.setCategory(c);
        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOrignalPrice(orignalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setCreateDate(new Date());

        productDAO.add(p);

        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {

        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.get(id);
        new Delete().deleteProduct(id); // 解决外键存在无法删除的问题。
//        productDAO.delete(id);

        return "@admin_product_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {

        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.get(id);
        request.setAttribute("p", p);

        return "admin/editProduct.jsp";
    }

    public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page){

        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.get(id);
        request.setAttribute("p", p);

        // 初始化属性值对象。对于所有的属性值对象，没有就创建一个。
        propertyValueDAO.init(p);

        List<PropertyValue> pvs = propertyValueDAO.list(p.getId()); // 取出所有的属性值对象，给前端edit用

        request.setAttribute("pvs", pvs);

        return "admin/editProductValue.jsp";
    }

    public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page){
        int pvid = Integer.parseInt(request.getParameter("pvid")); // propertyValue的主键
        String value = request.getParameter("value");

        PropertyValue pv = propertyValueDAO.get(pvid); // 先把对象从数据库里拿出，然后调用update更新value
        pv.setValue(value);
        propertyValueDAO.update(pv); // 更新属性值。只有主键是必须的，其他字段是要更新的，有就更新
        return "%success";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        int id = Integer.parseInt(request.getParameter("id"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        String subTitle = request.getParameter("subTitle");
        String name = request.getParameter("name");

        Product p = new Product();

        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOrignalPrice(orignalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setId(id);
        p.setCategory(c);

        productDAO.update(p);
        return "@admin_product_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        List<Product> ps = productDAO.list(cid, page.getStart(), page.getCount());

        int total = productDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid=" + c.getId());// 分页的需要用到

        request.setAttribute("ps", ps);
        request.setAttribute("c", c);
        request.setAttribute("page", page);

        return "admin/listProduct.jsp";
    }
}
