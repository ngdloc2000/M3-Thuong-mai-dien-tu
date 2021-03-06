package dao.product;

import config.Config;
import dao.shop.IShopDAO;
import dao.shop.ShopDAO;
import dao.type.ITypeDAO;
import dao.type.TypeDAO;
import model.Product;
import model.Shop;
import model.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProduct {
    public static final String FIND_PRODUCT_FOR_IDTYPE = "select * from product where product.idType = ?;";
    public final String FIND_ALL_PRODUCT = "select * from product";
    public final String ADD_PRODUCT = "insert into product (nameProduct, price, quantity, description, idType,idShop)" +
            "VALUE (?,?,?,?,?,?);";
    public final String FIND_BY_ID_PRODUCT = "select * from product where idProduct = ?;";
    public final String UPDATE_PRODUCT = "update product set nameProduct = ?, price =?," +
            " quantity = ?, description = ?, idType = ?, idShop = ?" +
            " where idProduct = ?;";
    public final String REMOVE_PRODUCT = "delete from product where idProduct = ?";


    private ITypeDAO typeDAO = new TypeDAO();
    private IShopDAO shopDAO = new ShopDAO();


    @Override
    public List<Product> findAll() {
        List<Product> productList = new ArrayList<>();
        try {
            Class.forName(Config.CLASS_FOR_NAME);
            Connection connection = DriverManager.getConnection(Config.MYSQL, Config.USERNAME, Config.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_PRODUCT);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int idProduct = rs.getInt("idProduct");
                String nameProduct = rs.getString("nameProduct");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                String description = rs.getString("description");
                int idType = rs.getInt("idType");
                int idShop = rs.getInt("idShop");
                Type type = typeDAO.findById(idType);
                Shop shop = shopDAO.findById(idShop);
                Product product = new Product(idProduct, nameProduct, price, quantity, description, idType, type, idShop, shop);
                productList.add(product);
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public void add(Product product) {
        try {
            Class.forName(Config.CLASS_FOR_NAME);
            Connection connection = DriverManager.getConnection(Config.MYSQL, Config.USERNAME, Config.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT);
            preparedStatement.setString(1, product.getNameProduct());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getImage());
            preparedStatement.setInt(5, product.getIdType());
            preparedStatement.setInt(6, product.getIdShop());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Product findById(int id) {
        Product product = null;
        try {
            Class.forName(Config.CLASS_FOR_NAME);
            Connection connection = DriverManager.getConnection(Config.MYSQL, Config.USERNAME, Config.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_PRODUCT);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String nameProduct = rs.getString("nameProduct");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                String description = rs.getString("description");
                int idType = rs.getInt("idType");
                Type type = typeDAO.findById(idType);
                int idShop = rs.getInt("idShop");
                Shop shop = shopDAO.findById(idShop);
                product = new Product(id, nameProduct, price, quantity, description, idType, type, idShop, shop);
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void update(int id, Product product) {
        try {
            Class.forName(Config.CLASS_FOR_NAME);
            Connection connection = DriverManager.getConnection(Config.MYSQL, Config.USERNAME, Config.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT);
            preparedStatement.setString(1, product.getNameProduct());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getImage());
            preparedStatement.setInt(5, product.getIdType());
            preparedStatement.setInt(6, product.getIdShop());
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        try {
            Class.forName(Config.CLASS_FOR_NAME);
            Connection connection = DriverManager.getConnection(Config.MYSQL, Config.USERNAME, Config.PASSWORD);
            PreparedStatement ps = connection.prepareStatement(REMOVE_PRODUCT);
            ps.setInt(1, id);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> findByIdType(int idType) {
        List<Product> list = new ArrayList<>();
        try {
            Class.forName(Config.CLASS_FOR_NAME);
            Connection connection = DriverManager.getConnection(Config.MYSQL, Config.USERNAME, Config.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCT_FOR_IDTYPE);
            preparedStatement.setInt(1,idType);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int idProduct = rs.getInt(1);
                String nameProduct = rs.getString(2);
                int price = rs.getInt(3);
                int quantity = rs.getInt(4);
                String description = rs.getString(5);
                int idType1 = rs.getInt(6);
                Type type = typeDAO.findById(idType1);
                int idShop = rs.getInt(7);
                Shop shop = shopDAO.findById(idShop);
                list.add(new Product(idProduct,nameProduct,price,quantity,description,idType1,type,idShop,shop));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
