package tyra314.toolprogression.compat.gamestages;

import tyra314.toolprogression.config.ConfigHandler;

@SuppressWarnings({"unchecked", "CanBeFinal"})
public class GSHelper
{
    private static Class<?> ore_stages_api = null;
    private static Class<?> game_stages = null;

    static
    {
        try
        {
            ore_stages_api = Class.forName("net.darkhax.orestages.api.OreTiersAPI");
            game_stages = Class.forName("net.darkhax.gamestages.capabilities.PlayerDataHandler");
        }
        catch (ClassNotFoundException e)
        {
        }
    }

    public static boolean isLoaded()
    {
        return ConfigHandler.game_stages_compat && ore_stages_api != null && game_stages != null;
    }
}
