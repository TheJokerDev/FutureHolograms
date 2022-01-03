package me.TheJokerDev.other.utils;

import lombok.experimental.UtilityClass;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.utils.reflect.ReflectionUtil;
import me.TheJokerDev.other.utils.reflect.Version;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

@UtilityClass
public class Common {

	private static final Pattern SPACING_CHARS_REGEX;
	public static final Version SERVER_VERSION;
	public static String PREFIX;

	static {
		SPACING_CHARS_REGEX = Pattern.compile("[_ \\-]+");
		SERVER_VERSION = Version.valueOf(ReflectionUtil.getVersion());
		PREFIX = "&8[&3DecentHolograms&8] &7";
	}

	/*
	 * 	Log
	 */

	/**
	 * Log a message into console.
	 *
	 * @param message The message.
	 */
	public static void log(String message) {
		log(Level.INFO, message);
	}

	/**
	 * Log a message into console.
	 * <p>
	 *     This method formats given arguments in the message.
	 * </p>
	 *
	 * @param message The message.
	 * @param args The arguments
	 */
	public static void log(String message, Object... args) {
		log(String.format(message, args));
	}

	/**
	 * Log a message into console.
	 *
	 * @param level Level of this message.
	 * @param message The message.
	 */
	public static void log(Level level, String message) {
		Bukkit.getServer().getLogger().log(level, "[DecentHolograms] " + message);
	}

	/**
	 * Log a message into console.
	 * <p>
	 *     This method formats given arguments in the message.
	 * </p>
	 *
	 * @param level Level of this message.
	 * @param message The message.
	 * @param args The arguments.
	 */
	public static void log(Level level, String message, Object... args) {
		log(level, String.format(message, args));
	}

	/*
	 * 	Debug
	 */

	/**
	 * Print an object into console.
	 *
	 * @param o Object to print.
	 */
	public static void debug(Object o) {
		System.out.println(o);
	}

	/*
	 * 	Tell
	 */

	/**
	 * Send a message to given CommandSender.
	 * <p>
	 *     This method will colorize the message.
	 * </p>
	 *
	 * @param player The CommandSender receiving the message.
	 * @param message The message.
	 */
	public static void tell(CommandSender player, String message) {
		player.sendMessage(Utils.ct
				(message));
	}

	/**
	 * Send a message to given CommandSender.
	 * <p>
	 *     This method will colorize the message and formats given arguments to the message.
	 * </p>
	 *
	 * @param player The CommandSender receiving the message.
	 * @param message The message.
	 * @param args The arguments.
	 */
	public static void tell(CommandSender player, String message, Object... args) {
		tell(player, String.format(message, args));
	}

	public static String removeSpacingChars(String string) {
		return SPACING_CHARS_REGEX.matcher(string).replaceAll("");
	}

	private static int[] splitVersion(String version) {
		String[] spl = version == null ? null : version.split("\\.");
		if (spl == null || spl.length < 3) {
			return new int[0];
		}
		int[] arr = new int[spl.length];
		for (int i = 0; i < spl.length; i++) {
			arr[i] = parseInt(spl[i]);
		}
		return arr;
	}

	private static int parseInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

}