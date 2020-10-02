package net.verany.skyblock.game.player.data.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.skyblock.game.settings.rank.IslandRank;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class IslandMember {

    private final UUID uuid;
    private IslandRank rank;

}
