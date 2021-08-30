package tmall.servlet;

import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryServlet  extends BaseBackServlet {
    /**
     *
     * 做了两件事。
     * 1、写数据库。
     * 2、保存图片。
     */
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) { //

        Map<String, String> params = new HashMap<>();
        // 获取文件数据（file）和文本数据（text）
        InputStream is = super.parseUpload(request, params);

        String name = params.get("name");
        Category c = new Category();
        c.setName(name);
        categoryDAO.add(c);

        // 创建文件，用来保存图片。文件名称以 category 的 id 唯一命名
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category")); // 图片保存位置
        File file = new File(imageFolder, c.getId() + ".jpg"); // 图片的唯一名，根据 id 来

        // 把上传的图片读取出来，并存储到指定的位置
        try{
            if(null != is && 0 != is.available()){
                try(FileOutputStream fos = new FileOutputStream(file)){
                    byte b[] = new byte[1024 * 1024];
                    int length = 0;
                    while(-1 != (length = is.read(b))){
                        fos.write(b, 0, length);
                    }
                    fos.flush();
                    // 通过如下代码，把文件保存为jpg格式
                    BufferedImage img = ImageUtil.change2jpg(file);
                    ImageIO.write(img, "jpg", file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin_category_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = categoryDAO.get(id); // 从数据库中取数据，更新到 c 中
        request.setAttribute("c", c);
        return "admin/editCategory.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<>();
        InputStream is = super.parseUpload(request, params);

        System.out.println(params);
        String name = params.get("name");
        int id = Integer.parseInt(params.get("id"));


        // 更新数据库中的 category
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        categoryDAO.update(c);

        // 更新图片。输入流为 null时，则没有更新操作
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, c.getId() + ".jpg");
        file.getParentFile().mkdirs(); // 如果父文件夹不存在，就创建，如果存在，就忽视。

        //
        try{
            if(null != is && 0 != is.available()){
                try(FileOutputStream fos = new FileOutputStream(file)){ // FileOutputStream(file)一旦文件被打开，意味着被重写。FileOutputStream(file, true) 这个new出来的就是可以在文件后面进行追加，而不是重写
                    byte b[] = new byte[1024 * 1024];
                    int length = 0;
                    while(-1 != (length = is.read(b))){
                        fos.write(b, 0, length);
                    }
                    fos.flush();
                    // 通过如下代码，把文件保存为jpg格式
                    BufferedImage img = ImageUtil.change2jpg(file);
                    ImageIO.write(img, "jpg", file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);

        request.setAttribute("thecs", cs);
        request.setAttribute("page", page);
        return "admin/listCategory.jsp";
    }
}
