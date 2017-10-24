package tyra314.toolprogression.compat.gamestages;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import tyra314.toolprogression.config.ConfigHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
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
            e.printStackTrace();
        }
    }

    public static boolean isLoaded()
    {
        return ConfigHandler.game_stages_compat && ore_stages_api != null && game_stages != null;
    }

    public static IBlockState getStagedBlockState(EntityPlayer player, IBlockState state)
    {
        try
        {
            if (!(boolean) ore_stages_api.getMethod("hasReplacement", IBlockState.class)
                    .invoke(null, state))
            {
                return state;
            }

            Tuple<String, IBlockState> info =
                    (Tuple<String, IBlockState>) ore_stages_api.getMethod("getStageInfo",
                            IBlockState.class).invoke(null, state);

            Method getStageData = game_stages.getMethod("getStageData", EntityPlayer.class);
            Object stage_data = getStageData.invoke(null, player);

            if (!(boolean) getStageData.getReturnType()
                    .getMethod("hasUnlockedStage", String.class)
                    .invoke(stage_data, info.getFirst()))
            {
                return info.getSecond();
            }

        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
        }

        return state;
    }
}
