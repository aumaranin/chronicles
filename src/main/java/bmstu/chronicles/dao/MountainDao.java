package bmstu.chronicles.dao;

import bmstu.chronicles.models.Mountain;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Класс, содержащий функции для работы с таблицей "person"
@Component
public class MountainDao
{
    private static final String URL = "jdbc:postgresql://localhost:5432/chronicles1";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "goldland";

    //Создание и настройка соединения с базой данных
    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Метод для поиска всех гор, отсортированный по id
    public List<Mountain> index() {
        List<Mountain> mountains_list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM mountains ORDER BY id";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next())
            {
                Mountain mountain = new Mountain();

                mountain.setId(resultSet.getInt("id"));
                mountain.setName(resultSet.getString("name"));
                mountain.setCountry(resultSet.getString("country"));
                mountain.setHeight(resultSet.getInt("height"));
                mountain.setInformation(resultSet.getString("information"));
                mountains_list.add(mountain);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return mountains_list;
    }

    //Метод для записи горы в базу данных
    public void save(Mountain mountain) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO mountains (name, country, height, information) VALUES (?, ?, ?, ?)");
            prst.setString(1, mountain.getName());
            prst.setString(2, mountain.getCountry());
            prst.setInt(3, mountain.getHeight());
            prst.setString(4, mountain.getInformation());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для записи горы в базу данных под определенным id, т.е. - изменение горы
    public void save(int id, Mountain mountain) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE mountains SET name=?, country=?, height=?, information=? WHERE id=?");
            prst.setString(1, mountain.getName());
            prst.setString(2, mountain.getCountry());
            prst.setInt(3, mountain.getHeight());
            prst.setString(4, mountain.getInformation());
            prst.setInt(5, id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Функция получения из БД горной вершины по ID
    public Mountain show(int id)
    {
        Mountain mountain = null;
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM mountains WHERE id=?;");
            prst.setInt(1, id);
            ResultSet resultSet = prst.executeQuery();
            resultSet.next();
            mountain = new Mountain();
            mountain.setId(resultSet.getInt("id"));
            mountain.setName(resultSet.getString("name"));
            mountain.setCountry(resultSet.getString("country"));
            mountain.setHeight(resultSet.getInt("height"));
            mountain.setInformation(resultSet.getString("information"));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mountain;
    }

}
