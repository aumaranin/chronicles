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

    public String toString()
    {
        String res =  "Имя: " + first_name + "\n"
                + "Отчетсво: " + second_name + "\n" + "Фамилия: " + last_name + "\n"
                + "Дата рождения: " + date_of_birth + "\n" + "Логин: " + login + "\n"
                + "Пароль: " + password + "\n" + "Роль: " + role + "\n" + "enabled: " + enabled;
        return res;
    }
}
