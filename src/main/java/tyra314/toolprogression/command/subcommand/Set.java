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
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.ToolOverwrite;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Set extends SubCommand
{
    @Override
    public String getName()
    {
        return "set";
    }

    @Override
    public boolean handleCommand(@Nonnull MinecraftServer server,
                                 @Nonnull ICommandSender sender,
                                 @Nonnull String[] args)
    {
        EntityPlayer player = server.getPlayerList().getPlayers().get(0);

        if (args.length < 3)
        {
            return false;
        }

        String itemclass = args[1];
        int level = Integer.parseInt(args[2]);

        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty())
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED +
                                                       "Can't set overwrite for an empty item."));
            return true;
        }

        Item item = stack.getItem();

        if (item instanceof ItemTool)
        {
            ToolOverwrite overwrite = ConfigHandler.toolOverwrites.get(item);

            if (overwrite == null)
            {
                overwrite = new ToolOverwrite();
            }

            overwrite.addOverwrite(itemclass, level);
            overwrite.apply(item);

            ConfigHandler.toolOverwrites.set(item, overwrite);
            ConfigHandler.toolOverwrites.save();

            OverwrittenContent.tools.put(item.getTranslationKey(), overwrite);

            sendMessage(sender, "added the tool overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            return true;
        }

        if (item instanceof ItemBlock)
        {
            ItemBlock iblock = (ItemBlock) item;
            Block block = iblock.getBlock();

            // I don't know, why we shouldn't use that here. Enlighten me, if you do. Thanks.
            @SuppressWarnings("deprecation")
            IBlockState state = block.getStateFromMeta(item.getDamage(stack));

            String key = BlockHelper.getKeyString(state);

            BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(key);

            if (overwrite == null)
            {
                overwrite = new BlockOverwrite(itemclass, level, true, BlockOverwrite.OverwriteSource.Single, key);
            }
            else
            {
                overwrite.addOverwrite(itemclass, level, true, key);
            }

            if(args.length >= 4)
            {
                overwrite.hardness = Float.parseFloat(args[3]);
            }

            overwrite.apply(state);

            ConfigHandler.blockOverwrites.set(key, overwrite);
            ConfigHandler.blockOverwrites.save();

            OverwrittenContent.blocks.put(block.getTranslationKey(), overwrite);

            sendMessage(sender, "added the block overwrite for '"
                                + TextFormatting.BOLD.toString() +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            return true;
        }

        return false;
    }


    @Nonnull
    @Override
    public String getSynopsis()
    {
        return getName() + " <toolclass> <level> [<hardness>]";
    }
}
