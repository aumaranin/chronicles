package bmstu.chronicles.models;

public class Sponsor
{
    private int id;
    private String name;
    private String information;

    public Sponsor()
    {

    }

    public Sponsor(int id, String name, String information)
    {
        this.id = id;
        this.name = name;
        this.information = information;
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

    public String getInformation()
    {
        return information;
    }

    public void setInformation(String information)
    {
        this.information = information;
    }
}
