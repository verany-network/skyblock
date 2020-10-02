package net.verany.skyblock.game.settings.data;

import lombok.Getter;
import net.verany.skyblock.game.settings.rank.IslandRank;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IslandInteractionData {

    private final List<IslandRank> whitelistedRanks = new ArrayList<>();

}
