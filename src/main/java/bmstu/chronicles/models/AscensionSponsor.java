package bmstu.chronicles.models;

public class AscensionSponsor
{
    private int ascension_id;
    private int sponsor_id;
    private String deposit;

    public AscensionSponsor()
    {

    }

    public AscensionSponsor(int ascension_id, int sponsor_id, String deposit)
    {
        this.ascension_id = ascension_id;
        this.sponsor_id = sponsor_id;
        this.deposit = deposit;
    }

    public int getAscension_id()
    {
        return ascension_id;
    }

    public void setAscension_id(int ascension_id)
    {
        this.ascension_id = ascension_id;
    }

    public int getSponsor_id()
    {
        return sponsor_id;
    }

    public void setSponsor_id(int sponsor_id)
    {
        this.sponsor_id = sponsor_id;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public void setDeposit(String deposit)
    {
        this.deposit = deposit;
    }
}
