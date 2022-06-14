package bmstu.chronicles.models;

public class Mountain
{
    private int id;

    private String name;

    private String country;

    private int height;

    private String information;

    public Mountain()
    {

    }

    public Mountain(int id, String name, String country, int height, String information)
    {
        this.id = id;
        this.name = name;
        this.country = country;
        this.height = height;
        this.information = information;
    }

    public String toString()
    {
        String res =  "id: " + id + "\n"
                + "Название: " + name + "\n"
                + "Страна: " + country + "\n"
                + "Высота: " + Integer.toString(height) + "\n"
                + "Информация: " + information + "\n";
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

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
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
