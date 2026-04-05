package org.nattwenty.skillcraft;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

public class PerkSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        SharedSuggestionProvider source = context.getSource();

        for (Skilltree tree : Skillcraft.skillTreeList) {
            for (SkillCraftPerk perk : tree.getPerks()) {
                builder.suggest(perk.getSkillName());
            }
        }

        return builder.buildFuture();
    }
}