package bmstu.chronicles.dao;

import bmstu.chronicles.models.*;
import bmstu.chronicles.dao.*;
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

    //Метод для поиска всех гор, на которые совершались восхождения членами клуба
    public List<Mountain> show_asc_by_members() {
        List<Mountain> mountains_list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT DISTINCT mountains.id, mountains.name, mountains.country, mountains.height, mountains.information FROM mountains " +
                    "JOIN ascension ON mountains.id = ascension.mountain_id WHERE ascension.status='Завершено';";
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

    //Метод для подсчета количетсва альпинистов, совершивших восхождение на горные вершины
    public List<MountCount> mount_per_count() {
        List<MountCount> mountCount_list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            /*
            String SQL = "SELECT mountains.name, count(*) FROM mountains " +
                    "JOIN ascension ON mountains.id = ascension.mountain_id " +
                    "JOIN ascension_member am on ascension.id = am.ascension_id " +
                    "JOIN person p ON am.person_id = p.person_id " +
                    "WHERE ascension.status='Завершено' " +
                    "GROUP BY mountains.name " +
                    "ORDER BY mountains.name;";
             */
            String SQL = "SELECT mountains.name, count(*) FROM mountains " +
                    "JOIN ascension ON mountains.id = ascension.mountain_id " +
                    "JOIN ascension_member on ascension.id = ascension_member.ascension_id " +
                    "JOIN person p ON ascension_member.person_id = p.person_id " +
                    "WHERE ascension.status='Завершено' AND ascension_member.status='Выполнено' " +
                    "GROUP BY mountains.name " +
                    "ORDER BY mountains.name;";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next())
            {
                MountCount mountCount = new MountCount();
                mountCount.mountain_name = resultSet.getString("name");
                mountCount.count = resultSet.getInt("count");
                mountCount_list.add(mountCount);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return mountCount_list;
    }

    public List<Ascension> show_asc_by_date_for_mountain(int mountain_id)
    {
        List<Ascension> ascensions_list = new ArrayList<>();
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT ascension.id, ascension.name, " +
                    "ascension.type_of_ascent, ascension.date FROM ascension " +
                    "JOIN mountains ON ascension.mountain_id = mountains.id " +
                    "WHERE ascension.status = 'Завершено' AND ascension.mountain_id = ? " +
                    "ORDER BY ascension.date;");
            prst.setInt(1, mountain_id);
            ResultSet resultSet = prst.executeQuery();
            //resultSet.next();
            while(resultSet.next())
            {
                Ascension ascension = new Ascension();
                ascension.setId(resultSet.getInt("id"));
                ascension.setName(resultSet.getString("name"));
                ascension.setDate(resultSet.getString("date"));
                ascension.setType_of_ascent(resultSet.getString("type_of_ascent"));
                ascensions_list.add(ascension);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ascensions_list;
    }

}
