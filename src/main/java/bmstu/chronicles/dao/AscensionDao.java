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
                    + "WHERE ascension.date > '" + from + "' AND ascension.date < '" + to + "' "
                    + "AND ascension.status='Завершено' ORDER BY date;";


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
            prst.setDate(4, java.sql.Date.valueOf(ascension.getDate()));
            prst.setString(5, ascension.getType_of_ascent());
            prst.setString(6, ascension.getStatus());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /*
    //Метод для записи восхождения в базу данных под определенным id, т.е. - изменение и
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

     */

    //Функция получения из БД восхождения по ID
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

    //Функция получения из БД восхождения по name и date
    public Ascension show(String name, String date)
    {
        Ascension ascension = null;
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM ascension WHERE (name=? and date=?);");
            prst.setString(1, name);
            prst.setDate(2, java.sql.Date.valueOf(date));
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

    //Функция получения из БД активных восхождений
    public List<Ascension> show_active()
    {
        List<Ascension> ascensions_list = new ArrayList<Ascension>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM ascension WHERE status = 'Активно';";
            ResultSet resultSet = statement.executeQuery(SQL);
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

    //Метод поиска членов восхождения по id восхождения
    public List<Person> getMembers(int asc_id)
    {
        List<Person> persons_list = new ArrayList<Person>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT person.person_id, person.first_name, person.second_name, " +
                    "person.last_name, person.date_of_birth, person.password, person.role, " +
                    "person.login, person.enabled, person.gender, person.phone, person.rank, " +
                    "person.relative_phone\n" +
                    "FROM person " +
                    "JOIN ascension_member ON person.person_id = ascension_member.person_id " +
                    "JOIN ascension ON ascension_member.ascension_id = ascension.id " +
                    "WHERE ascension.id = " + Integer.toString(asc_id) + " ORDER BY person.last_name;";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next())
            {
                Person person = new Person();
                person.setPerson_id(resultSet.getInt("person_id"));
                person.setFirst_name(resultSet.getString("first_name"));
                person.setSecond_name(resultSet.getString("second_name"));
                person.setLast_name(resultSet.getString("last_name"));
                person.setDate_of_birth(resultSet.getString("date_of_birth"));
                person.setPassword(resultSet.getString("password"));
                person.setRole(resultSet.getString("role"));
                person.setLogin(resultSet.getString("login"));
                person.setEnabled(resultSet.getBoolean("enabled"));
                person.setGender(resultSet.getString("gender"));
                person.setPhone(resultSet.getString("phone"));
                person.setRank(resultSet.getString("rank"));
                person.setRelative_phone(resultSet.getString("relative_phone"));
                persons_list.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return persons_list;
    }

    public void sign_person(int ascension_id, int person_id) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO ascension_member (ascension_id, person_id, status) VALUES (?, ?, 'Записан')");
            prst.setInt(1, ascension_id);
            prst.setInt(2, person_id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для изменения статуса восхождение на "Завершено"
    public void ascension_end(int asc_id)
    {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE ascension SET status='Завершено' WHERE id=?");
            prst.setInt(1, asc_id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для изменения статуса восхождение на "Отменено"
    public void ascension_cancel(int asc_id)
    {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE ascension SET status='Отменено' WHERE id=?");
            prst.setInt(1, asc_id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для изменения статуса всех участников восхождения на "Не выполнено"
    public void ascension_members_end(int asc_id, String result)
    {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE ascension_member SET status=? WHERE ascension_id=?");
            prst.setString(1, result);
            prst.setInt(2, asc_id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для изменения статуса участника восхождения по его ID на "Выполнено"
    public void ascension_members_end(int asc_id, int member_id, String result)
    {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE ascension_member SET status=? WHERE ascension_id=? AND person_id=?");
            prst.setString(1, "Выполнено");
            prst.setInt(2, asc_id);
            prst.setInt(3, member_id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для изменения статуса всех участников восхождения на "Не выполнено"
    public void ascension_members_cancel(int asc_id)
    {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "UPDATE ascension_member SET status='Не выполнено' WHERE ascension_id=?");
            prst.setInt(1, asc_id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
