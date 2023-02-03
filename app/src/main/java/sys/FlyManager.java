package sys;

import android.util.Log;
import android.view.View;

import http.ShellUtil;

public class FlyManager {

    public static String HigherAirplaneModePref1 = "settings put global airplane_mode_on ";
    public static String HigherAirplaneModePref2 = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state ";



    /**
     * @ value =1 打开飞行模式  * @ value =2 关闭飞行模式  *  *
     */
    public static void setSettingsOnHigh(
            int value) {
        String commond = HigherAirplaneModePref1 + value + ";";
        if (value == 1)
            commond += HigherAirplaneModePref2 + "true";
        else
            commond += HigherAirplaneModePref2 + "false";
        String result = ShellUtil.runRootCmd(commond);
    }

}
