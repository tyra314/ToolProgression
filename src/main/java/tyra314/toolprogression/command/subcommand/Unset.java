package tyra314.toolprogression.command.subcommand;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.command.SubCommand;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Unset extends SubCommand
{
    @Override
    public String getName()
    {
        return "unset";
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

        if (item instanceof ItemTool)
        {
            ConfigHandler.toolOverwrites.unset(item);
            ConfigHandler.toolOverwrites.save();

            OverwrittenContent.tools.remove(item.getUnlocalizedName());

            sendMessage(sender, "deleted the tool overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            sendMessage(sender, "needs to restart, in order to restore the default.");

            return true;
        }

        if (item instanceof ItemBlock)
        {
            ItemBlock iblock = (ItemBlock) item;
            Block block = iblock.getBlock();

            // I don't know, why we shouldn't use that here. Enlighten me, if you do. Thanks.
            @SuppressWarnings("deprecation")
            IBlockState state = block.getStateFromMeta(item.getDamage(stack));

            ConfigHandler.blockOverwrites.unset(BlockHelper.getKeyString(state));
            ConfigHandler.blockOverwrites.save();

            OverwrittenContent.blocks.remove(block.getUnlocalizedName());

            sendMessage(sender, "deleted the block overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            sendMessage(sender, "needs to restart, in order to restore the default.");

            return true;
        }

        return false;
    }

    @Nonnull
    @Override
    public List<String> provideCompletions(String[] args)
    {
        List<String> completions = new ArrayList<>();

        if(args.length == 1 && getName().startsWith(args[0]))
        {
            completions.add(getName());
        }

        return completions;
    }

    @Nonnull
    @Override
    public String getSynopsis()
    {
        return getName();
    }
}
