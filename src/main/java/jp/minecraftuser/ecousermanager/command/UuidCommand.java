
package jp.minecraftuser.ecousermanager.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecousermanager.config.EcoUserManagerConfig;
import jp.minecraftuser.ecousermanager.db.EcoUserUUIDData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * uuidコマンドクラス
 * @author ecolight
 */
public class UuidCommand extends CommandFrame {
    private static EcoUserManagerConfig ecuConf = null;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public UuidCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        ecuConf = (EcoUserManagerConfig)conf;
        setAuthBlock(true);
        setAuthConsole(true);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecouser.uuid";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // ﾊﾟﾗﾒﾀなかったら自分
        String p;
        if (args.length == 0) {
            p = sender.getName();
        } else {
            p = args[0];
        }
        ArrayList<EcoUserUUIDData> list = ecuConf.getStore().getNameList(p);
        if (list == null) {
            sender.sendMessage("§d[情報]§fユーザー["+p+"]のUUID情報が取得できませんでした");
            return true;
        }

        sender.sendMessage("§d[情報]§f=== ユーザー名["+p+"]のUUID情報を表示します ===");
        sender.sendMessage("§d[情報]§fUUID["+ecuConf.getStore().latestUUID(p).toString()+"]");
        // リストで検出した分表示
        SimpleDateFormat sd = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss.SSS]");
        for(EcoUserUUIDData data : list) {
            sender.sendMessage("§d[情報]§fDate:"+sd.format(data.getTime())+" - "+data.getName());
        }
        return true;
    }

    /**
     * コマンド別タブコンプリート処理
     * @param sender コマンド送信者インスタンス
     * @param cmd コマンドインスタンス
     * @param string コマンド文字列
     * @param strings パラメタ文字列配列
     * @return 保管文字列配列
     */
    @Override
    protected List<String> getTabComplete(CommandSender sender, Command cmd, String string, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            for (Player p : plg.getServer().getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(p.getName());
                }
            }
        }
        list.add("[<offlinePlayerName>]");
        return list;
    }
}
