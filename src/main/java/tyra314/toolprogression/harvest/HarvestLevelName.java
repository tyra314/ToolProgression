package tyra314.toolprogression.harvest;

import java.util.HashMap;
import java.util.Map;

public class HarvestLevelName
{
    public static final Map<Integer, HarvestLevelName> levels = new HashMap<>();

    private final String name;
    private final int level;
    private String format = null;

    public static HarvestLevelName readFromConfig(String config)
    {
        String[] tokens = config.split(":");
        if (tokens.length == 2)
        {
            int level = Integer.parseInt(tokens[0]);
            return new HarvestLevelName(level, tokens[1]);
        } else if (tokens.length == 3)
        {
            int level = Integer.parseInt(tokens[0]);
            return new HarvestLevelName(level, tokens[1], tokens[2]);
        }

        return null;
    }

    public HarvestLevelName(int level, String name)
    {
        this.level = level;
        this.name = name;
    }

    @SuppressWarnings("WeakerAccess")
    public HarvestLevelName(int level, String name, String format)
    {
        this.level = level;
        this.name = name;
        this.format = format;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public String getName()
    {
        return name;
    }

    public int getLevel()
    {
        return level;
    }

    @SuppressWarnings("unused")
    public String getFormatString()
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
