package tmall.dao;


import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO {

    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select count(*) from productimage"; // C大写是否有问题？

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(ProductImage bean){

        String sql = "insert into productimage values(null, ?, ?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getProduct().getId());
            ps.setString(2, bean.getType());

            ps.execute();

            // 返回个key给实体对象
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ProductImage bean){
        // 无
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from productimage where id = " + id;

            ResultSet rs = s.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductImage get(int id){
        ProductImage bean = null;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select * from productimage where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if(rs.next()){
                bean = new ProductImage();
                int pid = rs.getInt("pid");
                String type = rs.getString("type");
                bean.setProduct(new ProductDAO().get(pid));
                bean.setType(type);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bean;
    }

    public List<ProductImage> list(Product p, String type){
        return list(p, type, 0, Short.MAX_VALUE);
    }

    public List<ProductImage> list(Product p, String type, int start, int count){
        List<ProductImage> beans = new ArrayList<ProductImage>();

        String sql = "select * from productimage where pid = ? and type = ? order by id desc limit ?, ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, p.getId()); // 需要保证 product 有 id 的值。这个product一定是和数据库对应的。
            ps.setString(2, type);

            ps.setInt(3, start);
            ps.setInt(4, count);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ProductImage bean = new ProductImage();
                int id = rs.getInt(1);

                bean.setId(id);
                bean.setProduct(p);
                bean.setType(type);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }
}
