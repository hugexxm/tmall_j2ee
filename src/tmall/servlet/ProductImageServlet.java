package tmall.servlet;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductImageDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductImageServlet")
public class ProductImageServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        // 上传文件的输入流
        InputStream is = null;
        // 提交上传文件时的其他参数
        Map<String, String> params = new HashMap<>();

        // 解析上传 包括文件和参数
        is = parseUpload(request, params);

        // 根据上传的参数生成productImage对象
        String type = params.get("type");
        int pid = Integer.parseInt(params.get("pid"));
        Product p = productDAO.get(pid);

        // 1、数据库中插入数据
        ProductImage pi = new ProductImage();
        pi.setType(type);
        pi.setProduct(p);
        productImageDAO.add(pi);

        // 2、生成文件。将图片进行保存
        String fileName = pi.getId() + ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if(ProductImageDAO.type_single.equals(pi.getType())){
            imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
            imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
        }
        else
            imageFolder = request.getSession().getServletContext().getRealPath("img/productDetail");

        File f = new File(imageFolder, fileName);
        f.getParentFile().mkdirs();

        // 复制文件
        try{
            if(null != is && 0 != is.available()){
                try(FileOutputStream fos = new FileOutputStream(f)){
                    byte b[] = new byte[1024 * 1024];
                    int length = 0;
                    while(-1 != (length = is.read(b))){
                        fos.write(b, 0, length);
                    }
                    fos.flush();
                    // 通过如下代码，把文件保存为jpg格式
                    BufferedImage img = ImageUtil.change2jpg(f);
                    ImageIO.write(img, "jpg", f);

                    if(ProductImageDAO.type_single.equals(pi.getType())){
                        File f_small = new File(imageFolder_small, fileName);
                        File f_middle = new File(imageFolder_middle, fileName);

                        ImageUtil.resizeImage(f, 56, 56, f_small);
                        ImageUtil.resizeImage(f, 217, 190, f_middle);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "@admin_productImage_list?pid=" + p.getId();
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        // 删除数据库数据
        int id = Integer.parseInt(request.getParameter("id"));
        ProductImage pi = productImageDAO.get(id);
        productImageDAO.delete(id);

        // 删除图片
        if(ProductImageDAO.type_single.equals(pi.getType())){
            String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
            String imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");

            File f_single = new File(imageFolder_single, pi.getId() + ".jpg");
            f_single.delete();
            File f_small = new File(imageFolder_small, pi.getId()+ ".jpg");
            f_small.delete();
            File f_middle = new File(imageFolder_middle, pi.getId() + ".jpg");
            f_middle.delete();
        }
        else{
            String imageFolder_detail = request.getSession().getServletContext().getRealPath("img/productDetail");
            File f_detail = new File(imageFolder_detail, pi.getId() + ".jpg");
            f_detail.delete();
        }
        return "@admin_productImage_list?pid=" + pi.getProduct().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {

        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        List<ProductImage> pisSingle = productImageDAO.list(p, ProductImageDAO.type_single); // 两种图片要分开，因为是不同的集合
        List<ProductImage> pisDetail = productImageDAO.list(p, ProductImageDAO.type_detail); // 两种图片要分开，因为是不同的集合

        request.setAttribute("p", p);
        request.setAttribute("pisSingle", pisSingle);
        request.setAttribute("pisDetail", pisDetail);

        return "admin/listProductImage.jsp";
    }
}
