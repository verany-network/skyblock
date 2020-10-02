package net.verany.skyblock.game.player.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.skyblock.game.player.data.member.IslandMember;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PlayerData {

    private final UUID uuid;
    private String islandLocation;
    private int gems;
    private final List<IslandMember> members;

}
