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

    //Метод для поиска всех пользователей в базе данных, отсортированный по person_id
    public List<Person> index() {
        List<Person> people = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person ORDER BY person_id";
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
                person.setPhone(resultSet.getString("phone"));
                person.setRelative_phone(resultSet.getString("relative_phone"));
                person.setRank(resultSet.getString("rank"));
                person.setGender(resultSet.getString("gender"));
                people.add(person);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return people;
    }

    //Метод для поиска пользователей в базе данных, совершавших восхождения между двумя датами from - to
    // отсортированный по person_id
    public List<Person> index(String from, String to)
    {
        List<Person> people = new ArrayList<>();
        try
        {
            String req1 = "SELECT DISTINCT person.person_id, person.first_name, person.second_name, "
                        + "person.last_name, person.date_of_birth, person.password, "
                        + "person.role, person.login, person.enabled, person.gender, "
                        + "person.phone, person.rank, person.relative_phone "
                        + "FROM person "
                        + "INNER JOIN ascension_member ON person.person_id = ascension_member.person_id "
                        + "INNER JOIN ascension ON ascension_member.ascension_id = ascension.id "
                        + "INNER JOIN mountains ON ascension.mountain_id = mountains.id "
                        + "WHERE ascension.date > '" + from + "' AND ascension.date < '" + to + "' ORDER BY person.person_id;";
                        //+ "WHERE ascension.date > ? AND ascension.date < ? ORDER BY person.person_id;";

            PreparedStatement prst1 = connection.prepareStatement(req1);
            //prst1.setString(1, from.strip());
            //prst1.setString(2, to.strip());

            ResultSet resultSet = prst1.executeQuery();
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
                person.setPhone(resultSet.getString("phone"));
                person.setRelative_phone(resultSet.getString("relative_phone"));
                person.setRank(resultSet.getString("rank"));
                person.setGender(resultSet.getString("gender"));
                people.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return people;
    }

    //Метод для поиска пользователя в базе данных по person_id
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
            person.setPhone(resultSet.getString("phone"));
            person.setRelative_phone(resultSet.getString("relative_phone"));
            person.setRank(resultSet.getString("rank"));
            person.setGender(resultSet.getString("gender"));

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
            PreparedStatement prst = connection.prepareStatement("INSERT INTO person (first_name, second_name, last_name, date_of_birth, password, role, login, enabled, gender, phone, rank, relative_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prst.setString(1, person.getFirst_name());
            prst.setString(2, person.getSecond_name());
            prst.setString(3, person.getLast_name());
            prst.setDate(4, java.sql.Date.valueOf(person.getDate_of_birth()));
            prst.setString(5, person.getPassword());
            prst.setString(6, person.getRole());
            prst.setString(7, person.getLogin());
            prst.setBoolean(8, person.getEnabled());
            prst.setString(9, person.getGender());
            prst.setString(10, person.getPhone());
            prst.setString(11, person.getRank());
            prst.setString(12, person.getRelative_phone());
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод для записи пользователя в базу данных под определенным id, т.е. - изменение пользователя
    public void save(int id, Person person) {
        try
        {
            PreparedStatement prst = connection.prepareStatement("UPDATE person SET first_name=?, second_name=?, last_name=?, date_of_birth=?, password=?, role=?, login=?, enabled=?, gender=?, phone=?, rank=?, relative_phone=? WHERE person_id=?");
            prst.setString(1, person.getFirst_name());
            prst.setString(2, person.getSecond_name());
            prst.setString(3, person.getLast_name());
            prst.setDate(4, java.sql.Date.valueOf(person.getDate_of_birth()));
            prst.setString(5, person.getPassword());
            prst.setString(6, person.getRole());
            prst.setString(7, person.getLogin());
            prst.setBoolean(8, person.getEnabled());
            prst.setString(9, person.getGender());
            prst.setString(10, person.getPhone());
            prst.setString(11, person.getRank());
            prst.setString(12, person.getRelative_phone());
            prst.setInt(13, id);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
