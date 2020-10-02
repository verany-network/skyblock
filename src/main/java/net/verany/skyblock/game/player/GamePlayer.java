package net.verany.skyblock.game.player;

import com.google.gson.Gson;
import net.verany.Verany;
import net.verany.module.AbstractVeranyModule;
import net.verany.player.IVeranyPlayer;
import net.verany.skyblock.game.island.IIslandObject;
import net.verany.skyblock.game.island.IslandObject;
import net.verany.skyblock.game.player.data.PlayerData;
import net.verany.skyblock.game.player.data.member.IslandMember;
import net.verany.skyblock.game.player.kit.KitType;
import net.verany.skyblock.game.settings.rank.IslandRank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePlayer implements IVeranyPlayer<UUID> {

    private UUID uuid;
    private Player player;

    private double gems;

    private IIslandObject islandObject;
    private final List<IslandMember> members = new CopyOnWriteArrayList<>();

    @Override
    public void load(AbstractVeranyModule abstractVeranyModule, UUID uuid) {
        this.uuid = uuid;
        if (!Verany.exist(abstractVeranyModule, uuid, "skyBlockPlayer"))
            Verany.create(abstractVeranyModule, "skyBlockPlayer", new Gson().fromJson(new Gson().toJson(new PlayerData(uuid, "a", 0, new ArrayList<>())), Document.class));
        if (Bukkit.getPlayer(uuid) != null)
            player = Bukkit.getPlayer(uuid);

        gems = Verany.getObject(abstractVeranyModule, uuid, "skyBlockPlayer", "gems", Double.class);
        for (String s : Verany.getObjectList(abstractVeranyModule, uuid, "skyBlockPlayer", "members", String.class))
            members.add(new Gson().fromJson(s, IslandMember.class));

        islandObject = new IslandObject();
        islandObject.load(abstractVeranyModule, uuid);
    }

    @Override
    public void update(AbstractVeranyModule abstractVeranyModule) {
        Verany.update(abstractVeranyModule, uuid, "skyBlockPlayer", "gems", gems);
        List<String> member = new ArrayList<>();
        members.forEach(islandMember -> member.add(new Gson().toJson(islandMember)));
        Verany.update(abstractVeranyModule, uuid, "skyBlockPlayer", "members", member);

        islandObject.update(abstractVeranyModule);

    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    public void setKitItems(KitType kitItems) {
        for (ItemStack inventoryItem : kitItems.getInventoryItems()) {
            int amount = getMaxBuyableAmount(player, inventoryItem);
            if (amount <= 0)
                player.getWorld().dropItemNaturally(player.getLocation(), inventoryItem);
            else
                player.getInventory().addItem(inventoryItem);
        }
        for (int i = 0; i < player.getInventory().getArmorContents().length; i++) {
            ItemStack itemStack = player.getInventory().getArmorContents()[i];
            if (itemStack != null) {
                int amount = getMaxBuyableAmount(player, itemStack);
                if (amount <= 0)
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                else
                    player.getInventory().addItem(itemStack);
                player.getInventory().getArmorContents()[i] = null;
                player.getInventory().setArmorContents(player.getInventory().getArmorContents());
            }
        }
        player.getInventory().setArmorContents(kitItems.getArmorItems());
    }

    private int getMaxBuyableAmount(Player player, ItemStack item) {
        PlayerInventory playerInventory = player.getInventory();

        int amount = 0;
        for (ItemStack itemStack : playerInventory.getContents())
            if (itemStack != null && item.getType() == itemStack.getType())
                amount += itemStack.getAmount();

        return amount / item.getAmount();
    }

    public double getGems() {
        return gems;
    }

    public void setGems(double gems) {
        System.out.println(this.gems);
        this.gems = gems;
    }

    public List<IslandMember> getMembers() {
        return members;
    }

    public IslandMember getMemberByUUID(UUID uuid) {
        for (IslandMember member : members)
            if (member.getUuid().equals(uuid))
                return member;
        return null;
    }

    public List<IslandMember> getMembersByRank(IslandRank rank) {
        List<IslandMember> toReturn = new ArrayList<>();
        for (IslandMember member : members)
            if(member.getRank().equals(rank))
                toReturn.add(member);
        return toReturn;
    }

    public IIslandObject getIslandObject() {
        return islandObject;
    }
}
