package bmstu.chronicles.dao;

import bmstu.chronicles.models.Sponsor;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SponsorDao
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

    //Метод для поиска всех спонсоров, отсортированный по id
    public List<Sponsor> index() {
        List<Sponsor> sponsors_list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM sponsors ORDER BY id";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next())
            {
                Sponsor sponsor = new Sponsor();

                sponsor.setId(resultSet.getInt("id"));
                sponsor.setName(resultSet.getString("name"));
                sponsor.setInformation(resultSet.getString("information"));
                sponsors_list.add(sponsor);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sponsors_list;
    }

    //Метод для записи спонсора в базу данных
    public void save(Sponsor sponsor) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO sponsors (name, information) VALUES (?, ? )");
            prst.setString(1, sponsor.getName());
            prst.setString(2, sponsor.getInformation());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для записи спонсора в базу данных под определенным id, т.е. - изменение спонсора
    public void save(int id, Sponsor sponsor) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE sponsors SET name=?, information=? WHERE id=?");
            prst.setString(1, sponsor.getName());
            prst.setString(2, sponsor.getInformation());
            prst.setInt(3, id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Функция получения из БД спонсора по ID
    public Sponsor show(int id)
    {
        Sponsor sponsor = null;
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM sponsors WHERE id=?;");
            prst.setInt(1, id);
            ResultSet resultSet = prst.executeQuery();
            resultSet.next();
            sponsor = new Sponsor();
            sponsor.setId(resultSet.getInt("id"));
            sponsor.setName(resultSet.getString("name"));
            sponsor.setInformation(resultSet.getString("information"));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sponsor;
    }
}
