package tmall.dao;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select count(*) from orderitem"; // C大写是否有问题？

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void add(OrderItem bean){

        String sql = "insert into orderitem values(null, ?, ?, ?, ?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getProduct().getId());

            // 订单项在创建的时候，是没有订单信息的。赋值为 -1 .方便区分购物车中的订单项和已经完成的订单项.
            if(null == bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());

            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());

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

    public void update(OrderItem bean){

        String sql = "update orderitem set pid = ?, oid = ?, uid = ?, number = ? where id = ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){

            ps.setInt(1, bean.getProduct().getId());

            if(null == bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());

            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
            ps.setInt(5, bean.getId());

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from orderitem where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem get(int id){
        OrderItem bean = null;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){

            String sql = "select * from orderitem where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if(rs.next()){
                bean = new OrderItem();
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                bean.setProduct(new ProductDAO().get(pid));
                bean.setUser(new UserDAO().get(uid));
                bean.setNumber(number);

                if(-1 != oid){
                    bean.setOrder(new OrderDAO().get(oid));
                }

                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bean;
    }

    public List<OrderItem> listByUser(int uid){
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    // 查询某个用户的未省成订单的订单项（即购物车中的订单项）
    public List<OrderItem> listByUser(int uid, int start, int count){
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from orderitem where uid = ? and oid = -1 order by id desc limit ?, ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                bean.setProduct(new ProductDAO().get(pid));
                if(-1 != oid)
                    bean.setOrder(new OrderDAO().get(oid));

                bean.setUser(new UserDAO().get(uid));
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }

    // 查询某种订单下所有的订单项
    public List<OrderItem> listByOrder(int oid){
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByOrder(int oid, int start, int count){
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from orderitem where oid = ? order by id desc limit ?, ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                bean.setProduct(new ProductDAO().get(pid));
                if(-1 != oid)
                    bean.setOrder(new OrderDAO().get(oid));

                bean.setUser(new UserDAO().get(uid));
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }

    // 为订单设置订单项集合    拿着数据库看，这样比较好理解一点
    public void fill(List<Order> os){
        for(Order o : os){
            List<OrderItem> ois = listByOrder(o.getId()); // order的id 是 orderItem 的外键，说得通很合理啊
            float total = 0;
            int totalNumber = 0;
            for(OrderItem oi : ois){
                total += oi.getNumber() * oi.getProduct().getPromotePrice(); // 计算总共多少钱
                totalNumber += oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(ois); // 把ois给了order。
            o.setTotalNumber(totalNumber);
        }
    }

    // 为订单设置订单项集合
    public void fill(Order o){
        List<OrderItem> ois = listByOrder(o.getId()); // 把属于订单 o 的订单项全部取出来，给我计算。外键啊作用啊
        float total = 0;
        for(OrderItem oi : ois){
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
        }
        o.setTotal(total);
        o.setOrderItems(ois);
    }

    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from OrderItem where pid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().get(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().get(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().get(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 获取某一种产品的销量，产品销量就是这种产品对应的订单项OrderItem的number字段的总和 (应该还有一个限定条件:已付款)
    public int getSaleCount(int pid) {

        List<OrderItem> ois = listByProduct(pid);
        int total = 0;

        for(OrderItem oi : ois){
            if(oi.getOrder() != null){
                Order o = oi.getOrder();
                if(o.getStatus().equals(OrderDAO.waitPay) ||
                        o.getStatus().equals(OrderDAO.deleteWithoutPay) ) // equals
                   continue;
                else
                    total += oi.getNumber();
            }
        }


//        int total = 0;
//        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
//
//            String sql = "select sum(number) from OrderItem where oid != -1 and pid = " + pid;
//
//            ResultSet rs = s.executeQuery(sql);
//            while (rs.next()) {
//                total = rs.getInt(1);
//            }
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
        return total;
    }
}
