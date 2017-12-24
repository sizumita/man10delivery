package red.man10.minedelivery;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.event.player.PlayerJoinEvent;


public final class Minedelivery extends JavaPlugin implements Listener {


    private ItemStack delitem;
    private ItemStack itemair;
    private JavaPlugin plugin;
    private String deliverycommand = null;
    private String acceptancecommand = null;
    private String commandname = null;
    DeliveryData data = new DeliveryData(this);






    @Override
    public void onEnable() {
        saveDefaultConfig();
        // config.ymlを読み込みます。
        FileConfiguration config = getConfig();
//        deliverycommand = config.getString("deliverycommand");
//        acceptancecommand = config.getString("acceptancecommand");
//        commandname = config.getString("commandname");
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }



    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        String itembase64 = data.count(id);
        if(itembase64 == null) {
            p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §l あなた宛の宅配物はありません。");

        } else {
            p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §l あなた宛の宅配物があります。");
            delitem = new ItemStack(Material.DIAMOND_HOE, 1, (short) 774);
            ItemMeta dicemeta = delitem.getItemMeta();
            dicemeta.setDisplayName("宅配のダンボール箱");
            dicemeta.setLore(Arrays.asList("§l右クリックで開けてみよう。"));
            delitem.setItemMeta(dicemeta);
            p.getInventory().addItem(delitem);
        }
      }




    public boolean onCommand(CommandSender sender, Command cmd,String commandLabel,String[] args){
        if(cmd.getName().equalsIgnoreCase("mpost")){

            if(args[0].equalsIgnoreCase("deli")) {
                itemair = new ItemStack(Material.AIR, 0);
                    if (args.length != 2) {
                        return false;
                    }
    //                Player t = Bukkit.getPlayer(args[1]);
                    Player t = (Player) sender;
                    if(t == null){
                        return false;
                    }
                    ItemStack items = t.getInventory().getItemInMainHand();

                    t.sendMessage(String.valueOf(items));
                    t.sendMessage(String.valueOf(itemair));
                    if(items.getType().equals(itemair.getType())){
                        t.sendMessage("§4§l そのアイテムは送れません！");
                        return true;
                    }

                    String string = itemStackArrayToBase64(new ItemStack[]{items});
                    UUID senduseruuid = t.getUniqueId();


                    OfflinePlayer getusername = Bukkit.getOfflinePlayer(args[1]);

                    if(String.valueOf(senduseruuid)==null) {

                        sender.sendMessage("null!");
                    }
                if (t.hasPermission("man10.sironeko.send")){

                    if (getusername.hasPlayedBefore()) {
    //                    DeliveryData data = new DeliveryData(this);
                        sender.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §r §b あなたのアイテムを配送する準備をしています、、。");

                        data.createdelivery(senduseruuid,getusername.getUniqueId(),string);


                        ItemStack air = new ItemStack(Material.AIR);
                        t.getInventory().setItemInMainHand(air);
                        sender.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §a 準備が完了しました。配送しています！またのご利用を、お待ちしています。");
                        getusername.getPlayer().sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §f§lあなたに荷物が届いています！お受け取りください。");
                        delitem = new ItemStack(Material.DIAMOND_HOE, 1, (short) 774);
                        ItemMeta dicemeta = delitem.getItemMeta();
                        dicemeta.setDisplayName("宅配のダンボール箱");
                        dicemeta.setLore(Arrays.asList("§l右クリックで開けてみよう。"));
                        delitem.setItemMeta(dicemeta);
                        t.getInventory().addItem(delitem);
                        getusername.getPlayer().sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §f§l受け取れなかった場合は、 /"
                                +commandname+" "+acceptancecommand+" で。");
                        return true;
                    } else {
                        sender.sendMessage("§4§l そのようなユーザーはいません!");
                        return true;
                    }

                }



            if(args[0].equalsIgnoreCase("ac")) {
                Player p = (Player) sender;
                UUID id = p.getUniqueId();
                if (t.hasPermission("man10.sironeko.acsept")){


                String itembase64 = data.count(id);
//                data.updatedel(p.getName());
                if(itembase64 == null) {
                    p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §l あなた宛の宅配物はありません。");

                }
                p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §a ご利用、ありがとうございました！");
                delitem = new ItemStack(Material.DIAMOND_HOE, 1, (short) 774);
                ItemMeta dicemeta = delitem.getItemMeta();
                dicemeta.setDisplayName("宅配のダンボール箱");
                dicemeta.setLore(Arrays.asList("§l右クリックで開けてみよう。"));
                delitem.setItemMeta(dicemeta);
                p.getInventory().addItem(delitem);
                return true;
                } else {
                    p.sendMessage("§4§l パーミッションがありません！");
                }
            }
        }


            return false;

            // 何かの処理
            // コマンドが実行された場合は、trueを返して当メソッドを抜ける。
//            sender.sendMessage("/sironeko [h] [username]")

        }

        return false;
        // コマンドが実行されなかった場合は、falseを返して当メソッドを抜ける。
    }

//
    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player p = event.getPlayer();
        String itembase64;
        if (p.hasPermission("man10.sironeko.acsept")){
        ItemStack handitem =p.getInventory().getItemInMainHand();
        delitem = new ItemStack(Material.DIAMOND_HOE, 1, (short) 774);
        ItemMeta dicemeta = delitem.getItemMeta();
        dicemeta.setDisplayName("宅配のダンボール箱");
        dicemeta.setLore(Arrays.asList("§l右クリックで開けてみよう。"));
        delitem.setItemMeta(dicemeta);
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
                && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().equals(delitem.getItemMeta())) {
            UUID id = p.getUniqueId();
            itembase64 = data.count(id);
            data.updatedel(p.getUniqueId());
            if(itembase64 == null) {

                ItemStack air = new ItemStack(Material.AIR);
                p.getInventory().setItemInMainHand(air);

                p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §4§l 中身は空だった、、、。");
            }
            try {
                p.getInventory().addItem(itemStackArrayFromBase64(itembase64));
                p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §f§l中からアイテムが出てきた！");
                String itembase = data.count(id);
                data.updatedel(p.getUniqueId());
                if(itembase == null) {

                    ItemStack air = new ItemStack(Material.AIR);
                    p.getInventory().setItemInMainHand(air);

                    p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §f§l もう中身はないようだ。");
                } else {
                    p.sendMessage("§e§l[§f§l 白猫ヤマト §e§l] §f§l まだ中にアイテムが入っているようだ。");

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        }

    }




    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    /**
     *
     * A method to serialize an {@link ItemStack} array to Base64 String.
     *
     * <p />
     *
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to serialize an inventory to Base64 string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     *
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException
     */
    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     *
     * <p />
     *
     * Base off of {@link #fromBase64(String)}.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */


    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }


}


