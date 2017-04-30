package tyra314.toolprogression.harvest;

import java.util.HashMap;
import java.util.Map;

public class HarvestLevel
{
    public static final Map<Integer, HarvestLevel> levels = new HashMap<>();

    private String name;
    private int level;
    private String format = null;

    public static HarvestLevel readFromConfig(String config)
    {
        String[] tokens = config.split(":");
        if (tokens.length == 2)
        {
            int level = Integer.parseInt(tokens[0]);
            return new HarvestLevel(level, tokens[1]);
        } else if (tokens.length == 3)
        {
            int level = Integer.parseInt(tokens[0]);
            return new HarvestLevel(level, tokens[1], tokens[2]);
        }

        return null;
    }

    public HarvestLevel(int level, String name)
    {
        this.level = level;
        this.name = name;
    }

    public HarvestLevel(int level, String name, String format)
    {
        this.level = level;
        this.name = name;
        this.format = format;
    }

    public String getName()
    {
        return name;
    }

    public int getLevel()
    {
        return level;
    }

    public String getFormat()
    {
        return format;
    }

    public String getFormatted()
    {
        if (format != null)
        {
            return String.format(format, name);
        } else
        {
            return name;
        }
    }

}
