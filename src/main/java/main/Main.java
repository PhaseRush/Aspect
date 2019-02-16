package main;

import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import main.utility.metautil.BotUtils;
import main.utility.metautil.Global;

import java.time.Instant;
import java.util.List;

/**
 * Aspect -- Discord bot built with love
 *
 * @URL github.com/PhaseRush/Aspect
 * 2018/11/28
 */
public class Main {

    // Timekeeping
    public static long startTime;
    public static Instant startInstant;

    // IBM Translator
    public static LanguageTranslator translator;

    public static void main(String[] args){

        // ------------------------------------------------------------ //

        startTime = System.currentTimeMillis();
        startInstant = Instant.now();
        // ------------------------------------------------------------ //

        // Make an instance of global variables
        Global global = new Global();

        // Create all dispatch listeners
        List<Object> dispatchListeners = BotUtils.createListeners();

        // Register all listeners via the @EventSubscriber annotation which allows for organization and delegation of events
        global.getClient().getDispatcher().registerListeners(dispatchListeners.toArray());

        // Self Client login - finalize setup
        // Only login after all events are registered - otherwise some may be missed.
        global.getClient().login();

        // ------------------------------------------------------------ //

        System.out.println("Initialization time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}