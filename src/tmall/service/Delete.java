package tmall.service;

import tmall.bean.*;
import tmall.dao.*;

import java.util.List;

public class Delete {
    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    // 删除需调用
    public void deleteCategory(int cid){
        List<Property> properties = propertyDAO.list(cid);
        List<Product> products = productDAO.list(cid);

        if(null != properties){
            for(Property property : properties){
                this.deleteProperty(property.getId());
            }
        }

        if(null != products){
            for(Product product : products){
                this.deleteProduct(product.getId());
            }
        }

        categoryDAO.delete(cid);
    }

    public void deleteOrder(int oid){
        orderDAO.delete(oid);
    }

    public void deleteOrderItem(int oiid){
        orderItemDAO.delete(oiid);
    }

    // 删除需调用
    public void deleteProduct(int pid){
        List<PropertyValue> propertyValues = propertyValueDAO.list(pid);

        List<ProductImage> productSingleImages = productImageDAO.list(productDAO.get(pid), ProductImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(productDAO.get(pid), ProductImageDAO.type_detail);

        List<Review> reviews = reviewDAO.list(pid);
        List<OrderItem> orderItems = orderItemDAO.listByProduct(pid);

        if(null != propertyValues){
            for(PropertyValue propertyValue : propertyValues){
                this.deletePropertyValue(propertyValue.getId());
            }
        }

        if(null != productSingleImages){
            for(ProductImage productImage : productSingleImages){
                this.deleteProductimage(productImage.getId());
            }
        }

        if(null != productDetailImages){
            for(ProductImage productImage : productDetailImages){
                this.deleteProductimage(productImage.getId());
            }
        }

        if(null != reviews){
            for(Review review : reviews){
                this.deleteReview(review.getId());
            }
        }

        if(null != orderItems){
            for(OrderItem orderItem : orderItems){
                this.deleteOrderItem(orderItem.getId());
            }
        }

        productDAO.delete(pid);
    }

    public void deleteProductimage(int imageid){
        productImageDAO.delete(imageid);
    }

    // 删除需调用
    public void deleteProperty(int ptid){
        List<PropertyValue> propertyValues = propertyValueDAO.list(propertyDAO.get(ptid));

        if(null != propertyValues){
            for(PropertyValue propertyValue : propertyValues){
                this.deletePropertyValue(propertyValue.getId());
            }
        }

        propertyDAO.delete(ptid);
    }

    public void deletePropertyValue(int pvid){
        propertyValueDAO.delete(pvid);
    }

    public void deleteReview(int rid){
        reviewDAO.delete(rid);
    }
}
