package bmstu.chronicles.dao;

import bmstu.chronicles.models.Ascension;
import bmstu.chronicles.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Класс, содержащий функции для работы с таблицей "person"
@Component
public class AscensionDao
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

    //Метод для поиска всех восхождений, отсортированный по id
    public List<Ascension> index() {
        List<Ascension> ascensions_list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM ascension ORDER BY id;";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next())
            {
                Ascension ascension = new Ascension();

                ascension.setId(resultSet.getInt("id"));
                ascension.setName(resultSet.getString("name"));
                ascension.setMountain_id(resultSet.getInt("mountain_id"));
                ascension.setLeader_id(resultSet.getInt("leader_id"));
                ascension.setDate(resultSet.getString("date"));
                ascension.setType_of_ascent(resultSet.getString("type_of_ascent"));
                ascension.setStatus(resultSet.getString("status"));
                ascensions_list.add(ascension);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ascensions_list;
    }

    //Метод для поиска всех восхождений в промежутке дат, отсортированный по id
    public List<Ascension> index(String from, String to)
    {
        List<Ascension> ascensions_list = new ArrayList<>();
        try
        {
            String req1 = "SELECT * FROM ascension "
                    + "WHERE ascension.date > '" + from + "' AND ascension.date < '" + to + "' ORDER BY id;";


            PreparedStatement prst1 = connection.prepareStatement(req1);
            ResultSet resultSet = prst1.executeQuery();
            while(resultSet.next())
            {
                Ascension ascension = new Ascension();

                ascension.setId(resultSet.getInt("id"));
                ascension.setMountain_id(resultSet.getInt("mountain_id"));
                ascension.setLeader_id(resultSet.getInt("leader_id"));
                ascension.setDate(resultSet.getString("date"));
                ascension.setType_of_ascent(resultSet.getString("type_of_ascent"));
                ascension.setStatus(resultSet.getString("status"));
                ascension.setName(resultSet.getString("name"));
                ascensions_list.add(ascension);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ascensions_list;
    }

    //Метод для записи восхождения в базу данных
    public void save(Ascension ascension) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO ascension (name, mountain_id, leader_id, date, type_of_ascent, status) VALUES (?, ?, ?, ?, ?, ?)");
            prst.setString(1, ascension.getName());
            prst.setInt(2, ascension.getMountain_id());
            prst.setInt(3, ascension.getLeader_id());
            prst.setString(4, ascension.getDate());
            prst.setString(5, ascension.getType_of_ascent());
            prst.setString(6, ascension.getStatus());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для записи горы в базу данных под определенным id, т.е. - изменение горы
    public void save(int id, Ascension ascension) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE ascension SET name=?, mountain_id=?, leader_id=?, date=?, type_of_ascent=?, status=? WHERE id=?");
            prst.setString(1, ascension.getName());
            prst.setInt(2, ascension.getMountain_id());
            prst.setInt(3, ascension.getLeader_id());
            prst.setString(4, ascension.getDate());
            prst.setString(5, ascension.getType_of_ascent());
            prst.setString(6, ascension.getStatus());
            prst.setInt(7, ascension.getId());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Функция получения из БД горной вершины по ID
    public Ascension show(int id)
    {
        Ascension ascension = null;
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM ascension WHERE id=?;");
            prst.setInt(1, id);
            ResultSet resultSet = prst.executeQuery();
            resultSet.next();
            ascension = new Ascension();
            ascension.setId(resultSet.getInt("id"));
            ascension.setName(resultSet.getString("name"));
            ascension.setMountain_id(resultSet.getInt("mountain_id"));
            ascension.setLeader_id(resultSet.getInt("leader_id"));
            ascension.setType_of_ascent(resultSet.getString("type_of_ascent"));
            ascension.setStatus(resultSet.getString("status"));
            ascension.setDate(resultSet.getString("date"));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ascension;
    }

}
