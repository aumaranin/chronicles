package bmstu.chronicles.models;

public class Ascension
{
    private int id;
    private String name;

    private String date;

    private int mountain_id;

    private int leader_id;

    private String type_of_ascent;

    private String status;

    public Ascension()
    {

    }

    public Ascension(int id, String name, String date, int mountain_id, int leader_id, String type_of_ascent, String status)
    {
        this.id = id;
        this.name = name;
        this.date = date;
        this.mountain_id = mountain_id;
        this.leader_id = leader_id;
        this.type_of_ascent = type_of_ascent;
        this.status = status;
    }

    public String toString()
    {
        String res =  "id: " + id + "\n"
                + "Название: " + name + "\n"
                + "Дата: " + date + "\n"
                + "id горы: " + Integer.toString(mountain_id) + "\n"
                + "id инструктора: " + Integer.toString(leader_id) + "\n"
                + "Тип восхождения: " + type_of_ascent + "\n"
                + "Статус: " + status + "\n";
        return res;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getMountain_id()
    {
        return mountain_id;
    }

    public void setMountain_id(int mountain_id)
    {
        this.mountain_id = mountain_id;
    }

    public int getLeader_id()
    {
        return leader_id;
    }

    public void setLeader_id(int leader_id)
    {
        this.leader_id = leader_id;
    }

    public String getType_of_ascent()
    {
        return type_of_ascent;
    }

    public void setType_of_ascent(String type_of_ascent)
    {
        this.type_of_ascent = type_of_ascent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
