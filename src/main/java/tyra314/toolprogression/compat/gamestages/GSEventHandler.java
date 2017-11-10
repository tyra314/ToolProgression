package tyra314.toolprogression.compat.gamestages;

import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

public class GSEventHandler
{
    public static IBlockState getStagedBlockState(EntityPlayer player, IBlockState state)
    {
        if (!OreTiersAPI.hasReplacement(state))
        {
            return state;
        }

        Tuple<String, IBlockState> info = OreTiersAPI.getStageInfo(state);
        PlayerDataHandler.IStageData stage_data = PlayerDataHandler.getStageData(player);

        if (info != null && !stage_data.hasUnlockedStage(info.getFirst()))
        {
            return info.getSecond();
        }

        return state;
    }
}
