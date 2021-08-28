package tmall.dao;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select count(*) from property"; // C大写是否有问题？

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(Property bean){

        String sql = "insert into property values(null, ?, ?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getCategory().getId()); // category的 id 映射 category对象
            ps.setString(2, bean.getName());

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

    public void update(Property bean){

        String sql = "update property set cid = ? , name = ? where id = ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){

            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
            ps.setInt(3, bean.getId());

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from property where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Property get(int id){
        Property bean = null;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select * from property where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if(rs.next()){
                bean = new Property();
                String name = rs.getString("name");
                int cid = rs.getInt("cid");

                bean.setCategory(new CategoryDAO().get(cid));
                bean.setName(name);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bean;
    }

    public List<Property> list(int cid){
        return list(cid, 0, Short.MAX_VALUE);
    }

    public List<Property> list(int cid, int start, int count){
        List<Property> beans = new ArrayList<Property>();

        String sql = "select * from property where cid = ? order by id desc limit ?, ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Property bean = new Property();
                int id = rs.getInt(1);

                String name = rs.getString("name");
                bean.setId(id);
                bean.setName(name);
                bean.setCategory(new CategoryDAO().get(cid));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }
}
