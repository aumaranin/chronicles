package bmstu.chronicles.dao;

import bmstu.chronicles.models.Mountain;
import bmstu.chronicles.models.Sponsor;
import bmstu.chronicles.models.SponsorDeposit;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class AscensionSponsorDao
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

    //Метод для записи вклада спонсора в восхождение
    public void save(int ascension_id, int sponsor_id, String deposit) {
        try
        {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO ascension_sponsor (ascension_id, sponsor_id, deposit) VALUES (?, ?, ?)");
            prst.setInt(1, ascension_id);
            prst.setInt(2, sponsor_id);
            prst.setString(3, deposit);
            prst.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Метод поиска спонсоров и вкладов по id восхождения
    public List<SponsorDeposit> getSponsors(int ascension_id)
    {
        List<SponsorDeposit> sponsorDeposits_list = new ArrayList<SponsorDeposit>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT sponsors.name, ascension_sponsor.deposit FROM sponsors "
                    + "JOIN ascension_sponsor ON sponsors.id=ascension_sponsor.sponsor_id "
                    + "WHERE ascension_sponsor.ascension_id = " + Integer.toString(ascension_id);
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next())
            {
                SponsorDeposit sponsorDeposit = new SponsorDeposit();

                sponsorDeposit.setSponsor_name(resultSet.getString("name"));
                sponsorDeposit.setDeposit(resultSet.getString("deposit"));
                sponsorDeposits_list.add(sponsorDeposit);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sponsorDeposits_list;
    }

}
