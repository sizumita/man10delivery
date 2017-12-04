package red.man10.minedelivery;

/**
 * Created by sizumita on 2017/12/04.
 */
import org.bukkit.Bukkit;
import red.man10.minedelivery.MySQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
public class DeliveryData {
    private final Minedelivery plugin ;
    MySQLManager mysql = null;

    public DeliveryData(Minedelivery plugin) {
        this.plugin = plugin;
        this.mysql = new MySQLManager(plugin,"Delivery");
        mysql.execute(sqlCreateDeliveryTable);
    }

    public String getDeliveryTable() {
        String del = "delivery";
        return del;
    }

    public int createdelivery(UUID uuid1,UUID uuid2,String item){

        String senduser = Bukkit.getOfflinePlayer(uuid1).getName();
        String getuser = Bukkit.getOfflinePlayer(uuid2).getName();

        boolean ret = mysql.execute("insert into "+getDeliveryTable()+" values(0"
                +",'" + currentTime()
                +"','" + uuid1.toString()
                +"','" + uuid2.toString()
                +"','" + senduser
                +"','" + getuser
                +"','" + item
                +",NULL,NULL,NULL,NULL"

                +");");

        if(ret == false){
            return -1;
        }

        return getLatestId();

    }
    public int getLatestId(){

        int ret = -1;
        String sql = "select * from "+getDeliveryTable()+" order by id desc limit 1";

        ResultSet rs = mysql.query(sql);
        try
        {
            while(rs.next())
            {
                ret = rs.getInt("id");
            }
        }
        catch (SQLException e)
        {
            Bukkit.getLogger().info("Error executing a query: " + e.getErrorCode());
        }

        return ret;
    }
    public String currentTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss");
        String currentTime = sdf.format(date);
        return currentTime;
    }
    String sqlCreateDeliveryTable = "CREATE TABLE `delivery` (\n" +
            "  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,\n" +
            "  `datetime` datetime NOT NULL COMMENT '日付',\n" +
            "  `uuid1` varchar(40) NOT NULL COMMENT 'UUID',\n" +
            "  `uuid2` varchar(40) NOT NULL DEFAULT '' COMMENT 'UUID',\n" +
            "  `senduser` varchar(40) NOT NULL DEFAULT '' COMMENT 'UUID',\n" +
            "  `getuser` varchar(40) NOT NULL DEFAULT '' COMMENT 'UUID',\n" +
            "  `item` varchar(400) NOT NULL DEFAULT '' COMMENT 'Item',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;";

}


