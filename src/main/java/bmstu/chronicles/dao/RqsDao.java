package bmstu.chronicles.dao;

import bmstu.chronicles.models.*;
import bmstu.chronicles.dao.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Класс, содержащий функции для работы с таблицей "person"
@Component
public class RqsDao
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

    //Метод, подсчитывающий количество восхождений на каждую гору пользователем по ID
    public PersonMntCount personMntCount(int person_id)
    {
        PersonDao personDao = new PersonDao();
        MountainDao mountainDao = new MountainDao();
        PersonMntCount pmc = new PersonMntCount();

        pmc.person = personDao.show(person_id);
        pmc.mountain_list = new ArrayList<>();
        pmc.count_list = new ArrayList<>();


        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "SELECT person.person_id, mountains.id, count(*) FROM mountains " +
                            "JOIN ascension ON mountains.id = ascension.mountain_id " +
                            "JOIN ascension_member ON ascension.id = ascension_member.ascension_id " +
                            "JOIN person ON ascension_member.person_id = person.person_id " +
                            "WHERE person.person_id = ? " +
                            "GROUP BY mountains.id, person.last_name, person.first_name, person.person_id;"
            );
            prst.setInt(1, person_id);
            ResultSet resultSet = prst.executeQuery();

            while(resultSet.next())
            {
                int mount_id = resultSet.getInt("id");
                int count = resultSet.getInt("count");

                Mountain mountain = mountainDao.show(mount_id);
                pmc.mountain_list.add(mountain);
                pmc.count_list.add(count);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return pmc;
    }

    //Метод, подсчитывающий все восхождения пользователя
    public PersonAscensions personAscensions(int person_id)
    {
        PersonAscensions personAscensions = new PersonAscensions();
        PersonDao personDao = new PersonDao();
        AscensionDao ascensionDao = new AscensionDao();
        MountainDao mountainDao = new MountainDao();

        //получаем данные о пользователе по его ID
        personAscensions.person = personDao.show(person_id);

        //создаем пустые списки о восхождении и горной вершине
        personAscensions.ascensions_list = new ArrayList<>();
        personAscensions.mountain_list = new ArrayList<>();
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "SELECT ascension.id AS asc_id, ascension.name, ascension.date, mountains.id AS mnt_id, mountains.name FROM mountains " +
                            "JOIN ascension ON mountains.id = ascension.mountain_id " +
                            "JOIN ascension_member ON ascension.id = ascension_member.ascension_id " +
                            "JOIN person ON ascension_member.person_id = person.person_id " +
                            "WHERE person.person_id = ? ORDER BY ascension.date;"
            );
            prst.setInt(1, person_id);
            ResultSet resultSet = prst.executeQuery();

            while(resultSet.next())
            {
                int mount_id = resultSet.getInt("mnt_id");
                int ascension_id = resultSet.getInt("asc_id");

                Mountain mountain = mountainDao.show(mount_id);
                Ascension ascension = ascensionDao.show(ascension_id);
                personAscensions.mountain_list.add(mountain);
                personAscensions.ascensions_list.add(ascension);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return personAscensions;
    }


}

