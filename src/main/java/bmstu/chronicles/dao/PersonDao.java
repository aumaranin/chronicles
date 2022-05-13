package bmstu.chronicles.dao;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import bmstu.chronicles.models.Person;

//Класс, содержащий функции для работы с таблицей "person"
@Component
public class PersonDao
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

    //Метод для поиска пользователя в базе данных по id
    public List<Person> index() {
        List<Person> people = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
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
                people.add(person);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return people;
    }

    public Person show(int id) {
        Person person = null;
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM person WHERE person_id=?");
            prst.setInt(1, id);
            ResultSet resultSet = prst.executeQuery();
            resultSet.next();
            person = new Person();
            person.setPerson_id(resultSet.getInt("person_id"));
            person.setFirst_name(resultSet.getString("first_name"));
            person.setSecond_name(resultSet.getString("second_name"));
            person.setLast_name(resultSet.getString("last_name"));
            person.setDate_of_birth(resultSet.getString("date_of_birth"));
            person.setPassword(resultSet.getString("password"));
            person.setRole(resultSet.getString("role"));
            person.setLogin(resultSet.getString("login"));
            person.setEnabled(resultSet.getBoolean("enabled"));

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return person;
    }

    //Метод для поиска пользователя в базе данных по логину
    public Person show(String log) {
        Person person = null;
        try
        {
            PreparedStatement prst = connection.prepareStatement("SELECT * FROM person WHERE login=?");
            prst.setString(1, log);
            ResultSet resultSet = prst.executeQuery();
            resultSet.next();
            person = new Person();
            person.setPerson_id(resultSet.getInt("person_id"));
            person.setFirst_name(resultSet.getString("first_name"));
            person.setSecond_name(resultSet.getString("second_name"));
            person.setLast_name(resultSet.getString("last_name"));
            person.setDate_of_birth(resultSet.getString("date_of_birth"));
            person.setPassword(resultSet.getString("password"));
            person.setRole(resultSet.getString("role"));
            person.setLogin(resultSet.getString("login"));
            person.setEnabled(resultSet.getBoolean("enabled"));

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return person;
    }

    //Метод для записи пользователя в базу данных
    public void save(Person person) {
        try
        {
            PreparedStatement prst = connection.prepareStatement("INSERT INTO person (first_name, second_name, last_name, date_of_birth, password, role, login, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            prst.setString(1, person.getFirst_name());
            prst.setString(2, person.getSecond_name());
            prst.setString(3, person.getLast_name());
            prst.setDate(4, java.sql.Date.valueOf(person.getDate_of_birth()));
            prst.setString(5, person.getPassword());
            prst.setString(6, person.getRole());
            prst.setString(7, person.getLogin());
            prst.setBoolean(8, person.getEnabled());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
