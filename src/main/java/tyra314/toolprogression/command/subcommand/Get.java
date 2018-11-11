package tyra314.toolprogression.command.subcommand;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import tyra314.toolprogression.command.SubCommand;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.ToolOverwrite;

import javax.annotation.Nonnull;
import java.util.Map;

public class Get extends SubCommand
{
    @Override
    public String getName()
    {
        return "get";
    }

    @Override
    public boolean handleCommand(@Nonnull MinecraftServer server,
                                 @Nonnull ICommandSender sender,
                                 @Nonnull String[] args)
    {
        EntityPlayer player = server.getPlayerList().getPlayers().get(0);


        ItemStack stack = player.getHeldItemMainhand();


        if (stack.isEmpty())
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED +
                                                       "Can't unset overwrite for an empty item."));

            return true;
        }

        Item item = stack.getItem();

        if (item instanceof ItemBlock)
        {
            String key = BlockHelper.getKeyFromItemStack(stack);

            BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(key);

            if (overwrite == null)
            {
                sendMessage(sender, "There is no overwrite for this.");
                return true;
            }


            sendMessage(sender,
                    "Override: " +
                    overwrite.toolclass +
                    " (" +
                    String.valueOf(overwrite.level) +
                    ")");
            sendMessage(sender, "Source: " + overwrite.sourceKey);

            return true;
        }

        if (item instanceof ItemTool)
        {
            ToolOverwrite overwrite = ConfigHandler.toolOverwrites.get(item);

            if (overwrite == null)
            {
                sendMessage(sender, "There is no overwrite for this.");
                return true;
            }

            for (Map.Entry<String, Integer> harvestLevel : overwrite.harvest_levels.entrySet())
            {
                sendMessage(sender,
                        "Override: " +
                        harvestLevel.getKey() +
                        " (" +
                        String.valueOf(harvestLevel.getValue()) +
                        ")");
            }

            return true;
        }

        return false;
    }

    @Nonnull
    @Override
    public String getSynopsis()
    {
        return getName();
    }
}
