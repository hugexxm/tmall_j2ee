package tmall.dao;


import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class PropertyValueDAO {

    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select count(*) from propertyvalue"; // C大写是否有问题？

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(PropertyValue bean){

        String sql = "insert into propertyvalue values(null, ?, ?, ?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());

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

    public void update(PropertyValue bean){

        String sql = "update propertyvalue set pid = ?, ptid = ?, value = ? where id = ?"; // where前面不能有 逗号，太蠢了

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){

            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());
            ps.setInt(4, bean.getId());

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from propertyvalue where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PropertyValue get(int id){
        PropertyValue bean = null;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select * from propertyvalue where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if(rs.next()){
                bean = new PropertyValue();
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                bean.setProduct(new ProductDAO().get(pid));
                bean.setProperty(new PropertyDAO().get(ptid));
                bean.setValue(value);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bean;
    }

    public PropertyValue get(int ptid, int pid){
        PropertyValue bean = null;

        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from propertyvalue where ptid = " + ptid + " and pid = " + pid;

            ResultSet rs = s.executeQuery(sql);

            if(rs.next()){
                bean = new PropertyValue();
                int id = rs.getInt("id");

                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);

                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bean;
    }

    public List<PropertyValue> list(){
        return list(0, Short.MAX_VALUE);
    }

    public List<PropertyValue> list(int start, int count){
        List<PropertyValue> beans = new ArrayList<PropertyValue>();

        String sql = "select * from propertyvalue order by id desc limit ?, ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                bean.setId(id);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setProperty(new PropertyDAO().get(ptid));
                bean.setValue(value);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }

    /**
     * 初始化某个产品对应的属性值，初始化逻辑：
     * 1. 根据分类获取所有的属性
     * 2. 遍历每一个属性
     * 2.1 根据属性和产品，获取属性值
     * 2.2 如果属性值不存在，就创建一个属性值对象
     * @param p
     */
    public void init(Product p){
        List<Property> pts = new PropertyDAO().list(p.getCategory().getId());

        for(Property pt:pts){
            PropertyValue pv = get(pt.getId(), p.getId());
            if(null == pv){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv); // 注意，value是没有值的。应该是 ""
            }
        }
    }

    public List<PropertyValue> list(int pid){
        List<PropertyValue> beans = new ArrayList<PropertyValue>();

        String sql = "select * from propertyvalue where pid = ? order by id desc ";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                bean.setId(id);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setProperty(new PropertyDAO().get(ptid));
                bean.setValue(value);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }
}
