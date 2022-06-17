package bmstu.chronicles.models;

import java.util.Date;

public class Person
{
    private int person_id;

    private String first_name;

    private String second_name;

    private String last_name;

    private String date_of_birth;

    private String password;

    private String role;

    private String login;

    private Boolean enabled;

    private String gender;

    private String phone;

    private String relative_phone;

    private String rank;

    public Person()
    {

    }

    public Person(int person_id, String first_name, String second_name, String last_name, String date_of_birth, String password, String role, String login, Boolean enabled)
    {
        this.person_id = person_id;
        this.first_name = first_name;
        this.second_name = second_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.password = password;
        this.role = role;
        this.login = login;
        this.enabled = enabled;
    }

    public Person(int person_id, String first_name, String second_name, String last_name, String date_of_birth, String password, String role, String login, Boolean enabled, String gender, String phone, String relative_phone, String rank)
    {
        this.person_id = person_id;
        this.first_name = first_name;
        this.second_name = second_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.password = password;
        this.role = role;
        this.login = login;
        this.enabled = enabled;
        this.gender = gender;
        this.phone = phone;
        this.relative_phone = relative_phone;
        this.rank = rank;
    }

    public int getPerson_id()
    {
        return person_id;
    }

    public void setPerson_id(int person_id)
    {
        this.person_id = person_id;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getSecond_name()
    {
        return second_name;
    }

    public void setSecond_name(String second_name)
    {
        this.second_name = second_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getDate_of_birth()
    {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth)
    {
        this.date_of_birth = date_of_birth;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getRelative_phone() { return relative_phone; }

    public void setRelative_phone(String relative_phone) { this.relative_phone = relative_phone; }

    public String getRank() { return rank; }

    public void setRank(String rank) { this.rank = rank; }

    public String getRoleRus()
    {
        String res = null;
        if (role.equals("ROLE_ADMIN"))
            res = "Администратор";
        if (role.equals("ROLE_LEADER"))
            res = "Инструктор";
        if (role.equals("ROLE_CLIMBER"))
            res = "Альпинист";
        return res;
    }

    public String getStatus()
    {
        String res = null;
        if (enabled == true)
            res = "Активен";
        else
            res = "Заблокирован";
        return res;
    }

    public String toString()
    {
        String res =  "Имя: " + first_name + "\n"
                + "Отчетсво: " + second_name + "\n" + "Фамилия: " + last_name + "\n"
                + "Дата рождения: " + date_of_birth + "\n"
                + "Пол: " + gender + "\n"
                + "Спортивный разряд: " + rank + "\n"
                + "Телефон: " + phone + "\n"
                + "Телефон родственника: " + relative_phone + "\n"
                + "Логин: " + login + "\n"
                + "Пароль: " + password + "\n" + "Роль: " + role + "\n" + "enabled: " + enabled;
        return res;
    }

    public String name_age()
    {
        String res = last_name + " " + first_name + " (" + date_of_birth + ")";
        return res;
    }
}
