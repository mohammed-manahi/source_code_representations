
package graindcafe.tribu;

import graindcafe.tribu.Configuration.Constants;
import graindcafe.tribu.Configuration.TribuConfig;
import graindcafe.tribu.Executors.CmdDspawn;
import graindcafe.tribu.Executors.CmdIspawn;
import graindcafe.tribu.Executors.CmdTribu;
import graindcafe.tribu.Executors.CmdZspawn;
import graindcafe.tribu.Inventory.TribuInventory;
import graindcafe.tribu.Inventory.TribuTempInventory;
import graindcafe.tribu.Level.LevelFileLoader;
import graindcafe.tribu.Level.LevelSelector;
import graindcafe.tribu.Level.TribuLevel;
import graindcafe.tribu.Listeners.TribuBlockListener;
import graindcafe.tribu.Listeners.TribuEntityListener;
import graindcafe.tribu.Listeners.TribuPlayerListener;
import graindcafe.tribu.Listeners.TribuWorldListener;
import graindcafe.tribu.Rollback.ChunkMemory;
import graindcafe.tribu.TribuZombie.EntityTribuZombie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import me.graindcafe.gls.DefaultLanguage;
import me.graindcafe.gls.Language;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EntityZombie;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class Tribu extends JavaPlugin {

	public static String getExceptionMessage(final Exception e) {
		String message = e.getLocalizedMessage() + "\n";
		for (final StackTraceElement st : e.getStackTrace())
			message += "[" + st.getFileName() + ":" + st.getLineNumber() + "] " + st.getClassName() + "->" + st.getMethodName() + "\n";
		return message;
	}

	public static void messagePlayer(final CommandSender sender, final String message) {
		if (!message.isEmpty()) if (sender == null)
			Logger.getLogger("Minecraft").info(ChatColor.stripColor(message));
		else
			sender.sendMessage(message);
	}

	private int									aliveCount;
	private TribuBlockListener					blockListener;

	private TribuConfig							config;
	private TribuEntityListener					entityListener;
	private TribuInventory						inventorySave;
	private boolean								isRunning;
	private Language							language;
	private TribuLevel							level;

	private LevelFileLoader						levelLoader;

	private LevelSelector						levelSelector;
	private Logger								log;

	private ChunkMemory							memory;
	private TribuPlayerListener					playerListener;
	private HashMap<Player, PlayerStats>		players;

	private Random								rnd;
	private LinkedList<PlayerStats>				sortedStats;

	private TribuSpawner						spawner;
	private HashMap<Player, Location>			spawnPoint;
	private SpawnTimer							spawnTimer;
	private HashMap<Player, TribuTempInventory>	tempInventories;
	private int									waitingPlayers	= -1;

	private WaveStarter							waveStarter;
	private TribuWorldListener					worldListener;


	public void addDefaultPackages() {
		if (level != null && config.DefaultPackages != null) for (final Package pck : config.DefaultPackages)
			level.addPackage(pck);
	}

	public void addPlayer(final Player player) {
		if (player != null && !players.containsKey(player)) {

			if (config.PlayersStoreInventory) {
				inventorySave.addInventory(player);
				player.getInventory().clear();
			}
			final PlayerStats stats = new PlayerStats(player);
			players.put(player, stats);
			sortedStats.add(stats);
			messagePlayer(player, getLocale("Message.YouJoined"));
			if (waitingPlayers != 0) {
				waitingPlayers--;
				if (waitingPlayers == 0) // No need to delay if everyone is
											// playing
					if (config.PluginModeServerExclusive)
						startRunning();
					else
						startRunning(config.LevelStartDelay);
			} else if (getLevel() != null && isRunning) {
				player.teleport(level.getDeathSpawn());
				messagePlayer(player, language.get("Message.GameInProgress"));
			}
		}
	}


	public void addPlayer(final Player player, final double timeout) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				addPlayer(player);
			}
		}, Math.round(Constants.TicksBySecond * timeout));
	}

	public void broadcast(final String msg) {
		if (msg.isEmpty()) getServer().broadcastMessage(msg);
	}

	public void broadcast(final String languageNode, final Object... params) {
		broadcast(String.format(getLocale(languageNode), params));
	}

	public void broadcast(final String message, final String permission) {
		if (message.isEmpty()) getServer().broadcast(message, permission);
	}

	public void broadcast(final String languageNode, final String permission, final Object... params) {
		broadcast(String.format(getLocale(languageNode), params), permission);
	}

	public void checkAliveCount() {
		// log.info("checking alive count " + aliveCount);

		if (aliveCount == 0 && isRunning) {
			messagePlayers(language.get("Message.ZombieHavePrevailed"));
			messagePlayers(String.format(language.get("Message.YouHaveReachedWave"), String.valueOf(getWaveStarter().getWaveNumber())));
			stopRunning(true);
			if (getPlayersCount() != 0) getLevelSelector().startVote(Constants.VoteDelay);
		}
	}


	public TribuConfig config() {
		return config;
	}

	public int getAliveCount() {
		return aliveCount;
	}

	public ChunkMemory getChunkMemory() {
		return memory;
	}

	public TribuLevel getLevel() {
		return level;
	}

	public LevelFileLoader getLevelLoader() {
		return levelLoader;
	}

	public LevelSelector getLevelSelector() {
		return levelSelector;
	}

	public String getLocale(final String key) {
		return language.get(key);
	}

	public Player getNearestPlayer(final double x, final double y, final double z) {
		return getNearestPlayer(new Location(level.getInitialSpawn().getWorld(), x, y, z));
	}

	public Player getNearestPlayer(final Location location) {
		Player minPlayer = null;
		double minVal = Double.MAX_VALUE;
		double d;
		for (final Player p : players.keySet()) {
			d = location.distanceSquared(p.getLocation());
			if (minVal > d) {
				minVal = d;
				minPlayer = p;
			}
		}
		return minPlayer;
	}

	public Set<Player> getPlayers() {
		return players.keySet();
	}

	public int getPlayersCount() {
		return players.size();
	}

	public Player getRandomPlayer() {
		return sortedStats.get(rnd.nextInt(sortedStats.size())).getPlayer();
	}

	public LinkedList<PlayerStats> getSortedStats() {
		Collections.sort(sortedStats);
		return sortedStats;
	}

	public TribuSpawner getSpawner() {
		return spawner;
	}

	public SpawnTimer getSpawnTimer() {
		return spawnTimer;
	}

	public PlayerStats getStats(final Player player) {
		return players.get(player);
	}

	public WaveStarter getWaveStarter() {
		return waveStarter;
	}

	private void initLanguage() {
		DefaultLanguage.setAuthor("Graindcafe");
		DefaultLanguage.setName("English");
		DefaultLanguage.setVersion(Constants.LanguageFileVersion);
		DefaultLanguage.setLanguagesFolder(Constants.languagesFolder);
		DefaultLanguage.setLocales(new HashMap<String, String>() {
			private static final long	serialVersionUID	= 9166935722459443352L;
			{
				put("File.DefaultLanguageFile",
						"# This is your default language file \n# You should not edit it !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
				put("File.LanguageFileComplete", "# Your language file is complete\n");
				put("File.TranslationsToDo", "# Translations to do in this language file\n");
				put("Sign.Buy", "Buy");
				put("Sign.ToggleSpawner", "Spawn's switch");
				put("Sign.Spawner", "Zombie Spawner");
				put("Sign.HighscoreNames", "Top Names");
				put("Sign.HighscorePoints", "Top Points");
				put("Sign.TollSign", "Pay");
				put("Message.Stats", ChatColor.GREEN + "Ranking of  best zombies killers : ");
				put("Message.UnknownItem", ChatColor.YELLOW + "Sorry, unknown item");
				put("Message.ZombieSpawnList", ChatColor.GREEN + "%s");
				put("Message.ConfirmDeletion", ChatColor.YELLOW + "Please confirm the deletion of the %s level by redoing the command");
				put("Message.ThisOperationIsNotCancellable", ChatColor.RED + "This operation is not cancellable!");
				put("Message.LevelUnloaded", ChatColor.GREEN + "Level successfully unloaded");
				put("Message.InvalidVote", ChatColor.RED + "Invalid vote");
				put("Message.ThankyouForYourVote", ChatColor.GREEN + "Thank you for your vote");
				put("Message.YouCannotVoteAtThisTime", ChatColor.RED + "You cannot vote at this time");
				put("Message.LevelLoadedSuccessfully", ChatColor.GREEN + "Level loaded successfully");
				put("Message.LevelIsAlreadyTheCurrentLevel", ChatColor.RED + "Level %s is already the current level");
				put("Message.UnableToSaveLevel", ChatColor.RED + "Unable to save level, try again later");
				put("Message.UnableToCreatePackage", ChatColor.RED + "Unable to create package, try again later");
				put("Message.UnableToLoadLevel", ChatColor.RED + "Unable to load level");
				put("Message.NoLevelLoaded", ChatColor.YELLOW + "No level loaded, type '/tribu load' to load one,");
				put("Message.NoLevelLoaded2", ChatColor.YELLOW + "or '/tribu create' to create a new one,");
				put("Message.TeleportedToDeathSpawn", ChatColor.GREEN + "Teleported to death spawn");
				put("Message.DeathSpawnSet", ChatColor.GREEN + "Death spawn set.");
				put("Message.TeleportedToInitialSpawn", ChatColor.GREEN + "Teleported to initial spawn");
				put("Message.InitialSpawnSet", ChatColor.GREEN + "Initial spawn set.");
				put("Message.UnableToSaveCurrentLevel", ChatColor.RED + "Unable to save current level.");
				put("Message.LevelSaveSuccessful", ChatColor.GREEN + "Level save successful");
				put("Message.LevelCreated", ChatColor.GREEN + "Level " + ChatColor.LIGHT_PURPLE + "%s" + ChatColor.GREEN + " created");
				put("Message.UnableToDeleteLevel", ChatColor.RED + "Unable to delete current level.");
				put("Message.PackageCreated", ChatColor.RED + "Package created successfully");
				put("Message.LevelDeleted", ChatColor.GREEN + "Level deleted successfully.");
				put("Message.Levels", ChatColor.GREEN + "Levels: %s");
				put("Message.UnknownLevel", ChatColor.RED + "Unknown level: %s");
				put("Message.MaybeNotSaved", ChatColor.YELLOW + "Maybe you have not saved this level or you have not set anything in.");
				put("Message.ZombieModeEnabled", ChatColor.GREEN + "Zombie Mode enabled!");
				put("Message.ZombieModeDisabled", ChatColor.RED + "Zombie Mode disabled!");
				put("Message.SpawnpointAdded", ChatColor.GREEN + "Spawnpoint added");
				put("Message.SpawnpointRemoved", ChatColor.GREEN + "Spawnpoint removed");
				put("Message.InvalidSpawnName", ChatColor.RED + "Invalid spawn name");
				put("Message.TeleportedToZombieSpawn", ChatColor.GREEN + "Teleported to zombie spawn " + ChatColor.LIGHT_PURPLE + "%s");
				put("Message.UnableToGiveYouThatItem", ChatColor.RED + "Unable to give you that item...");
				put("Message.PurchaseSuccessfulMoney", ChatColor.GREEN + "Purchase successful." + ChatColor.DARK_GRAY + " Money: " + ChatColor.GRAY + "%s $");
				put("Message.YouDontHaveEnoughMoney", ChatColor.DARK_RED + "You don't have enough money for that!");
				put("Message.MoneyPoints", ChatColor.DARK_GRAY + "Money: " + ChatColor.GRAY + "%s $" + ChatColor.DARK_GRAY + " Points: " + ChatColor.GRAY + "%s");
				put("Message.GameInProgress", ChatColor.YELLOW + "Game in progress, you will spawn next round");
				put("Message.ZombieHavePrevailed", ChatColor.DARK_RED + "Zombies have prevailed!");
				put("Message.YouHaveReachedWave", ChatColor.RED + "You have reached wave " + ChatColor.YELLOW + "%s");
				put("Message.YouJoined", ChatColor.GOLD + "You joined the human strengths against zombies.");
				put("Message.YouLeft", ChatColor.GOLD + "You left the fight against zombies.");
				put("Message.TribuSignAdded", ChatColor.GREEN + "Tribu sign successfully added.");
				put("Message.TribuSignRemoved", ChatColor.GREEN + "Tribu sign successfully removed.");
				put("Message.ProtectedBlock", ChatColor.YELLOW + "Sorry, this sign is protected, please ask an operator to remove it.");
				put("Message.CannotPlaceASpecialSign", ChatColor.YELLOW + "Sorry, you cannot place a special signs, please ask an operator to do it.");
				put("Message.ConfigFileReloaded", ChatColor.GREEN + "Config files have been reloaded.");
				put("Message.PckNotFound", ChatColor.YELLOW + "Package %s not found in this level.");
				put("Message.PckNeedName", ChatColor.YELLOW + "You have to specify the name of the package.");
				put("Message.PckNeedOpen", ChatColor.YELLOW + "You have to open or create a package first.");
				put("Message.PckNeedId", ChatColor.YELLOW + "You have to specify the at least the id.");
				put("Message.PckNeedSubId", ChatColor.YELLOW + "You have to specify the id and subid.");
				put("Message.PckCreated", ChatColor.GREEN + "The package %s has been created.");
				put("Message.PckOpened", ChatColor.GREEN + "The package %s has been opened.");
				put("Message.PckSaved", ChatColor.GREEN + "The package %s has been saved and closed.");
				put("Message.PckRemoved", ChatColor.GREEN + "The package has been removed.");
				put("Message.PckItemDeleted", ChatColor.GREEN + "The item has been deleted.");
				put("Message.PckItemAdded", ChatColor.GREEN + "The item \"%s\" has been successfully added.");
				put("Message.PckItemAddFailed", ChatColor.YELLOW + "The item \"%s\" could not be added.");
				put("Message.PckList", ChatColor.GREEN + "Packages of this level : %s.");
				put("Message.PckNoneOpened", ChatColor.YELLOW + "none opened/specified");
				put("Message.LevelNotReady", ChatColor.YELLOW + "The level is not ready to run. Make sure you create/load a level and that it contains zombie spawns.");
				put("Message.Deny", ChatColor.RED + "A zombie denied your action, sorry.");
				put("Message.PlayerDied", ChatColor.LIGHT_PURPLE + "%s" + ChatColor.RED + " has died.");
				put("Message.PlayerRevive", ChatColor.GREEN + "You have been revived.");
				put("Message.PlayerDSpawnLeaveWarning", ChatColor.GOLD + "You cannot leave until a new round starts.");

				put("Message.AlreadyIn", ChatColor.YELLOW + "You are already in.");
				put("Message.Died", ChatColor.GRAY + "%s died.");
				put("Broadcast.GameStartingSoon", ChatColor.GRAY + "Game is starting in " + ChatColor.RED + "%.0f" + ChatColor.GRAY + " seconds!");
				put("Broadcast.GameStarting", ChatColor.DARK_RED + "Game is starting right now!");
				put("Broadcast.MapChosen", ChatColor.DARK_BLUE + "Level " + ChatColor.LIGHT_PURPLE + "%s" + ChatColor.DARK_BLUE + " has been chosen");
				put("Broadcast.MapVoteStarting", ChatColor.DARK_AQUA + "Level vote starting,");
				put("Broadcast.Type", ChatColor.DARK_AQUA + "Type ");
				put("Broadcast.SlashVoteForMap", ChatColor.GOLD + "'/tribu vote %s'" + ChatColor.DARK_AQUA + " for map " + ChatColor.BLUE + "%s");
				put("Broadcast.VoteClosingInSeconds", ChatColor.DARK_AQUA + "Vote closing in %s seconds");
				put("Broadcast.StartingWave", ChatColor.GRAY + "Starting wave " + ChatColor.DARK_RED + "%s" + ChatColor.GRAY + ", " + ChatColor.DARK_RED + "%s" + ChatColor.GRAY + " Zombies @ " + ChatColor.DARK_RED
						+ "%s" + ChatColor.GRAY + " health");
				put("Broadcast.Wave", ChatColor.DARK_GRAY + "Wave " + ChatColor.DARK_RED + "%s" + ChatColor.DARK_GRAY + " starting in " + ChatColor.DARK_RED + "%s" + ChatColor.DARK_GRAY + " seconds.");
				put("Broadcast.WaveComplete", ChatColor.GOLD + "Wave Complete");
				put("Info.LevelFound", ChatColor.YELLOW + "%s levels found");
				put("Info.Enable", ChatColor.WHITE + "Starting " + ChatColor.DARK_RED + "Tribu" + ChatColor.WHITE + " by Graindcafe, original author : samp20");
				put("Info.Disable", ChatColor.YELLOW + "Stopping Tribu");
				put("Info.LevelSaved", ChatColor.GREEN + "Level saved");
				put("Info.ChosenLanguage", ChatColor.YELLOW + "Chosen language : %s (default). Provided by : %s.");
				put("Info.LevelFolderDoesntExist", ChatColor.RED + "Level folder doesn't exist");
				put("Warning.AllSpawnsCurrentlyUnloaded", ChatColor.YELLOW + "All zombies spawns are currently unloaded.");
				put("Warning.UnableToSaveLevel", ChatColor.RED + "Unable to save level");
				put("Warning.ThisCommandCannotBeUsedFromTheConsole", ChatColor.RED + "This command cannot be used from the console");
				put("Warning.IOErrorOnFileDelete", ChatColor.RED + "IO error on file delete");
				put("Warning.LanguageFileOutdated", ChatColor.RED + "Your current language file is outdated");
				put("Warning.LanguageFileMissing", ChatColor.RED + "The chosen language file is missing");
				put("Warning.UnableToAddSign", ChatColor.RED + "Unable to add sign, maybe you've changed your locales, or signs' tags.");
				put("Warning.UnknownFocus", ChatColor.RED + "The string given for the configuration Zombies.Focus is not recognized : %s . It could be 'None','Nearest','Random','DeathSpawn','InitialSpawn'.");
				put("Warning.NoSpawns", ChatColor.RED + "You didn't set any zombie spawn.");
				put("Severe.CannotCopyLanguages", ChatColor.RED + "Cannot copy languages files.");
				put("Severe.TribuCantMkdir", ChatColor.RED + "Tribu can't make dirs so it cannot create the level directory, you would not be able to save levels ! You can't use Tribu !");
				put("Severe.WorldInvalidFileVersion", ChatColor.RED + "World invalid file version");
				put("Severe.WorldDoesntExist", ChatColor.RED + "World doesn't exist");
				put("Severe.ErrorDuringLevelLoading", ChatColor.RED + "Error during level loading : %s");
				put("Severe.ErrorDuringLevelSaving", ChatColor.RED + "Error during level saving : %s");
				put("Severe.PlayerHaveNotRetrivedHisItems", ChatColor.RED + "The player %s have not retrieved his items, they will be deleted ! Items list : %s");
				put("Severe.Exception", ChatColor.RED + "Exception: %s");

				put("Severe.PlayerDidntGetInvBack", ChatColor.RED + "didn't get his inventory back because he was returned null. (Maybe he was not in server?)");
				put("Prefix.Broadcast", "[Tribu] ");
				put("Prefix.Message", "");
				put("Prefix.Info", "[Tribu] ");
				put("Prefix.Warning", "[Tribu] ");
				put("Prefix.Severe", "[Tribu] ");
			}
		});
		if (config != null) {
			language = Language.init(config.PluginModeLanguage);
			if (language.isLoaded()) LogWarning(language.get("Warning.LanguageFileMissing"));
			if (language.isOutdated()) LogWarning(language.get("Warning.LanguageOutdated"));
			LogInfo(String.format(language.get("Info.ChosenLanguage"), language.getName(), language.getAuthor()));
		} else
			language = new DefaultLanguage();
		language.setPrefix("Message.", language.get("Prefix.Message"));
		language.setPrefix("Broadcast.", language.get("Prefix.Broadcast"));
		language.setPrefix("Info.", language.get("Prefix.Info"));
		language.setPrefix("Warning.", language.get("Prefix.Warning"));
		language.setPrefix("Severe.", language.get("Prefix.Severe"));
		Constants.MessageMoneyPoints = language.get("Message.MoneyPoints");
		Constants.MessageZombieSpawnList = language.get("Message.ZombieSpawnList");
	}

	public boolean isAlive(final Player player) {
		return players.get(player).isalive();
	}

	public boolean isInsideLevel(final Location loc) {

		return isInsideLevel(loc, false);
	}


	public boolean isInsideLevel(final Location loc, final boolean dontCheckRunning) {

		if ((dontCheckRunning || isRunning) && level != null)
			return config.PluginModeServerExclusive || loc.getWorld().equals(level.getInitialSpawn().getWorld()) && (config.PluginModeWorldExclusive || loc.distance(level.getInitialSpawn()) < config.LevelClearZone);
		else
			return false;
	}

	public boolean isPlaying(final Player p) {
		return players.containsKey(p);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void keepTempInv(final Player p, final ItemStack[] items) {
		// log.info("Keep " + items.length + " items for " +
		// p.getDisplayName());
		tempInventories.put(p, new TribuTempInventory(p, items));
	}


	protected void loadCustomConf() {
		loadCustomConf(level.getName() + ".yml", level.getInitialSpawn().getWorld().getName() + ".yml");
	}

	protected void loadCustomConf(final String levelName, final String worldName) {
		final TribuLevel level = getLevel();
		if (level == null) return;
		File worldFile = null, levelFile = null, worldDir, levelDir;
		worldDir = new File(Constants.perWorldFolder);
		levelDir = new File(Constants.perLevelFolder);
		if (!levelDir.exists()) levelDir.mkdirs();
		if (!worldDir.exists()) worldDir.mkdirs();

		for (final File file : levelDir.listFiles())
			if (file.getName().equalsIgnoreCase(levelName)) {
				levelFile = file;
				break;
			}
		for (final File file : worldDir.listFiles())
			if (file.getName().equalsIgnoreCase(worldName)) {
				worldFile = file;
				break;
			}
		if (levelFile != null)
			if (worldFile != null)
				config = new TribuConfig(levelFile, new TribuConfig(worldFile));
			else
				config = new TribuConfig(levelFile);
		else
			config = new TribuConfig();
		initLanguage();
		if (config.PluginModeServerExclusive) for (final Player p : getServer().getOnlinePlayers())
			this.addPlayer(p);
		if (config.PluginModeWorldExclusive && level != null) for (final Player d : level.getInitialSpawn().getWorld().getPlayers())
			this.addPlayer(d);
		if (config.PluginModeAutoStart) startRunning();

	}

	public void LogInfo(final String message) {
		log.info(message);
	}

	public void LogSevere(final String message) {
		log.severe(message);
	}

	public void LogWarning(final String message) {
		log.warning(message);

	}


	public void messagePlayer(final CommandSender sender, final String languageNode, final Object... params) {
		messagePlayer(sender, String.format(getLocale(languageNode), params));
	}


	public void messagePlayers(final String msg) {
		if (!msg.isEmpty()) for (final Player p : players.keySet())
			p.sendMessage(msg);
	}


	public void messagePlayers(final String languageNode, final Object... params) {
		messagePlayers(String.format(getLocale(languageNode), params));
	}

	@Override
	public void onDisable() {
		inventorySave.restoreInventories();
		players.clear();
		sortedStats.clear();
		memory.restoreAll();
		stopRunning();
		LogInfo(language.get("Info.Disable"));
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		rnd = new Random();
		final boolean mkdirs = Constants.rebuildPath(getDataFolder().getPath() + File.separatorChar);
		boolean langCopy = true;
		for (final String name : Constants.languages) {
			final InputStream fis = this.getClass().getResourceAsStream("/res/languages/" + name + ".yml");
			FileOutputStream fos = null;
			final File f = new File(Constants.languagesFolder + name + ".yml");
			{
				try {
					fos = new FileOutputStream(f);
					final byte[] buf = new byte[1024];
					int i = 0;
					if (f.canWrite() && fis.available() > 0) while ((i = fis.read(buf)) != -1)
						fos.write(buf, 0, i);
				} catch (final Exception e) {
					e.printStackTrace();
					langCopy = false;
				} finally {
					try {
						if (fis != null) fis.close();
						if (fos != null) fos.close();
					} catch (final Exception e) {
					}
				}
			}
		}
		try {
			@SuppressWarnings("rawtypes")
			final Class[] args = { Class.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE };
			final Method a = EntityTypes.class.getDeclaredMethod("a", args);
			a.setAccessible(true);

			a.invoke(a, EntityTribuZombie.class, "Zombie", 54, '\uafaf', 7969893);
			a.invoke(a, EntityZombie.class, "Zombie", 54, '\uafaf', 7969893);

		} catch (final Exception e) {
			setEnabled(false);
			e.printStackTrace();
			return;
		}
		// Before loading conf
		players = new HashMap<Player, PlayerStats>();
		// isRunning set to true to prevent start running at "loadCustomConf"
		isRunning = true;
		aliveCount = 0;
		level = null;
		// A language should be loaded BEFORE levelLoader uses
		initLanguage();
		levelLoader = new LevelFileLoader(this);
		// The level loader have to be ready
		reloadConf();
		isRunning = false;
		tempInventories = new HashMap<Player, TribuTempInventory>();
		inventorySave = new TribuInventory();
		spawnPoint = new HashMap<Player, Location>();
		sortedStats = new LinkedList<PlayerStats>();

		levelSelector = new LevelSelector(this);

		spawner = new TribuSpawner(this);
		spawnTimer = new SpawnTimer(this);
		waveStarter = new WaveStarter(this);

		// Create listeners
		playerListener = new TribuPlayerListener(this);
		entityListener = new TribuEntityListener(this);
		blockListener = new TribuBlockListener(this);
		worldListener = new TribuWorldListener(this);

		memory = new ChunkMemory();

		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(entityListener, this);
		getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(worldListener, this);

		getCommand("dspawn").setExecutor(new CmdDspawn(this));
		getCommand("zspawn").setExecutor(new CmdZspawn(this));
		getCommand("ispawn").setExecutor(new CmdIspawn(this));
		getCommand("tribu").setExecutor(new CmdTribu(this));
		if (!mkdirs) LogSevere(getLocale("Severe.TribuCantMkdir"));
		if (!langCopy) LogSevere(getLocale("Severe.CannotCopyLanguages"));
		LogInfo(language.get("Info.Enable"));
		if (config.PluginModeAutoStart) startRunning();
	}

	public void reloadConf() {
		// Reload the main config file from disk
		reloadConfig();
		// Parse again the file
		config = new TribuConfig(getConfig());
		// Create the file if it doesn't exist
		try {
			getConfig().save(Constants.configFile);
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
		// Before "loadCustom"
		if (config.PluginModeDefaultLevel != "") {
			final String worldName = levelLoader.getWorldName(config.PluginModeDefaultLevel);
			this.loadCustomConf(config.PluginModeDefaultLevel, worldName);
			setLevel(levelLoader.loadLevel(config.PluginModeDefaultLevel));
		}
		// After loading the level from main file
		else
			this.loadCustomConf();
	}


	public void removePlayer(final Player player) {
		if (player != null && players.containsKey(player)) {
			if (isAlive(player)) aliveCount--;
			if (!isRunning && waitingPlayers != -1 && waitingPlayers < config.LevelMinPlayers) waitingPlayers++;

			sortedStats.remove(players.get(player));
			inventorySave.restoreInventory(player);
			players.remove(player);
			Tribu.messagePlayer(player, getLocale("Message.YouLeft"));
			if (player.isOnline() && spawnPoint.containsKey(player)) player.setBedSpawnLocation(spawnPoint.remove(player));
			// check alive AFTER player remove
			checkAliveCount();
			// remove vote AFTER player remove
			levelSelector.removeVote(player);
			if (!player.isDead()) restoreInventory(player);

		}
	}


	public void resetedSpawnAdd(final Player p, final Location point) {
		spawnPoint.put(p, point);
	}

	public void restoreInventory(final Player p) {
		// log.info("Restore items for " + p.getDisplayName());
		inventorySave.restoreInventory(p);
	}

	public void restoreTempInv(final Player p) {
		// log.info("Restore items for " + p.getDisplayName());
		if (tempInventories.containsKey(p)) tempInventories.remove(p).restore();
	}

	public void revivePlayer(final Player player) {
		if (spawnPoint.containsKey(player)) player.setBedSpawnLocation(spawnPoint.remove(player));
		players.get(player).revive();
		if (config.WaveStartHealPlayers) player.setHealth(20);
		if (config.WaveStartFeedPlayers) player.setFoodLevel(20);
		restoreTempInv(player);
		aliveCount++;

	}


	public void revivePlayers(final boolean teleportAll) {
		aliveCount = 0;
		for (final Player player : players.keySet()) {
			revivePlayer(player);
			if (isRunning && level != null && (teleportAll || !isAlive(player))) player.teleport(level.getInitialSpawn());
		}
	}

	public void setDead(final Player player) {
		if (players.containsKey(player)) {
			if (isAlive(player)) {
				aliveCount--;
				final PlayerStats p = players.get(player);
				p.resetMoney();
				p.subtractmoney(config.StatsOnPlayerDeathMoney);
				p.subtractPoints(config.StatsOnPlayerDeathPoints);
				p.msgStats();
				messagePlayers("Message.Died", player.getName());
			}
			players.get(player).kill();
			if (getLevel() != null && isRunning) checkAliveCount();
		}
	}


	public void setLevel(final TribuLevel level) {
		this.level = level;
		this.loadCustomConf();
	}


	public boolean startRunning() {
		if (waitingPlayers == -1) {
			waitingPlayers = config.LevelMinPlayers - players.size();
			if (waitingPlayers < 0) waitingPlayers = 0;
		}
		if (!isRunning && getLevel() != null && waitingPlayers == 0) {
			// Before (next instruction) it will saves current default
			// packages to the level, saving theses packages with the level
			addDefaultPackages();
			// Make sure no data is lost if server decides to die
			// during a game and player forgot to /level save
			if (!getLevelLoader().saveLevel(getLevel()))
				LogWarning(language.get("Warning.UnableToSaveLevel"));
			else
				LogInfo(language.get("Info.LevelSaved"));
			if (getLevel().getSpawns().isEmpty()) {
				LogWarning(language.get("Warning.NoSpawns"));
				return false;
			}

			isRunning = true;
			if (config.PluginModeServerExclusive || config.PluginModeWorldExclusive)
				for (final LivingEntity e : level.getInitialSpawn().getWorld().getLivingEntities()) {
					if (!(e instanceof Player) && !(e instanceof Wolf) && !(e instanceof Villager)) e.damage(Integer.MAX_VALUE);
				}
			else
				for (final LivingEntity e : level.getInitialSpawn().getWorld().getLivingEntities())
					if ((e.getLocation().distance(level.getInitialSpawn())) < config.LevelClearZone && !(e instanceof Player) && !(e instanceof Wolf) && !(e instanceof Villager)) e.damage(Integer.MAX_VALUE);
			if (config.PlayersRollback) {
				// If there is a restoring operation currently, do it
				// quickly
				memory.getReady();
				memory.startCapturing();
				// Pre-cache level
				memory.add(level.getInitialSpawn().getChunk());
				memory.add(level.getDeathSpawn().getChunk());
				for (final Location l : level.getZombieSpawns())
					memory.add(l.getChunk());
			}
			getLevel().initSigns();
			sortedStats.clear();

			for (final PlayerStats stat : players.values()) {
				stat.resetPoints();
				stat.resetMoney();
				sortedStats.add(stat);
			}
			getWaveStarter().resetWave();
			revivePlayers(true);
			getWaveStarter().scheduleWave(Constants.TicksBySecond * config.WaveStartDelay);
		}
		return true;
	}


	public void startRunning(final float timeout) {
		final float step = (1f - timeout % 1);
		final Stack<Float> broadcastTime = new Stack<Float>();
		if (timeout > 2f) broadcastTime.push(2f);
		if (timeout > 3f) broadcastTime.push(3f);
		if (timeout > 4f) broadcastTime.push(4f);
		float i = 5;
		while (timeout > i) {
			broadcastTime.push(i);
			i += 5f;
		}
		broadcastTime.push(timeout);
		final int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			private float	counter	= timeout;

			@Override
			public void run() {
				if (counter <= 0f)
					startRunning();
				else if (broadcastTime.isEmpty())
					messagePlayers(getLocale("Broadcast.GameStarting"));
				else if (broadcastTime.peek() >= counter) messagePlayers("Broadcast.GameStartingSoon", broadcastTime.pop());
				counter -= step;
			}
		}, 0, Math.round(step * Constants.TicksBySecond));
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				getServer().getScheduler().cancelTask(taskId);
			}
		}, (long) (Math.ceil(timeout * Constants.TicksBySecond) + 1));

	}

	public void stopRunning() {
		stopRunning(false);
	}

	public void stopRunning(final boolean rerun) {
		getLevelSelector().cancelVote();
		if (isRunning) {
			isRunning = false;
			getSpawnTimer().stop();
			getWaveStarter().cancelWave();
			getSpawner().clearZombies();

			if (config.PlayersRollback) memory.startRestoring(this, config.AdvancedRestoringSpeed);
			level.finishSigns();
			// Teleports all players to spawn when game ends
			for (final Player p : players.keySet())
				p.teleport(level.getInitialSpawn());
			if (!rerun) {
				if (config.PlayersStoreInventory) inventorySave.restoreInventories();

				if (!config.PluginModeServerExclusive && !config.PluginModeWorldExclusive) players.clear();
			}
			waitingPlayers = -1;
		}

	}
}
package org.pushingpixels.trident;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.TimelineScenario.TimelineScenarioState;
import org.pushingpixels.trident.callback.RunOnUIThread;

class TimelineEngine {

	public static boolean DEBUG_MODE = false;

	private static TimelineEngine instance;

	private Set<Timeline> runningTimelines;

	enum TimelineOperationKind {
		PLAY, CANCEL, RESUME, SUSPEND, ABORT, END
	}

	class TimelineOperation {
		public TimelineOperationKind operationKind;

		Runnable operationRunnable;

		public TimelineOperation(TimelineOperationKind operationKind,
				Runnable operationRunnable) {
			this.operationKind = operationKind;
			this.operationRunnable = operationRunnable;
		}
	}

	private Set<TimelineScenario> runningScenarios;

	long lastIterationTimeStamp;


	static class FullObjectID {

		public Object mainObj;

		@SuppressWarnings("unchecked")
		public Comparable subID;

		@SuppressWarnings("unchecked")
		public FullObjectID(Object mainObj, Comparable subID) {
			this.mainObj = mainObj;
			this.subID = subID;
		}
		@Override
		public int hashCode() {
			int result = this.mainObj.hashCode();
			if (this.subID != null)
				result &= (this.subID.hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof FullObjectID) {
				FullObjectID cid = (FullObjectID) obj;
				try {
					boolean result = (this.mainObj == cid.mainObj);
					if (this.subID == null) {
						result = result && (cid.subID == null);
					} else {
						result = result
								&& (this.subID.compareTo(cid.subID) == 0);
					}
					return result;
				} catch (Exception exc) {
					return false;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return this.mainObj.getClass().getSimpleName() + ":" + this.subID;
		}
	}

	TridentAnimationThread animatorThread;

	private BlockingQueue<Runnable> callbackQueue;

	private TimelineCallbackThread callbackThread;

	class TridentAnimationThread extends Thread {
		public TridentAnimationThread() {
			super();
			this.setName("Trident pulse source thread");
			this.setDaemon(true);
		}


		@Override
		public final void run() {
			TridentConfig.PulseSource pulseSource = TridentConfig.getInstance()
					.getPulseSource();
			lastIterationTimeStamp = System.currentTimeMillis();
			while (true) {
				pulseSource.waitUntilNextPulse();
				updateTimelines();
				// engine.currLoopId++;
			}
		}

		@Override
		public void interrupt() {
			System.err.println("Interrupted");
			super.interrupt();
		}
	}

	private class TimelineCallbackThread extends Thread {
		public TimelineCallbackThread() {
			super();
			this.setName("Trident callback thread");
			this.setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				try {
					Runnable runnable = callbackQueue.take();
					runnable.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	private TimelineEngine() {
		this.runningTimelines = new HashSet<Timeline>();
		this.runningScenarios = new HashSet<TimelineScenario>();

		this.callbackQueue = new LinkedBlockingQueue<Runnable>();
		this.callbackThread = this.getCallbackThread();
	}

	public synchronized static TimelineEngine getInstance() {
		if (TimelineEngine.instance == null) {
			TimelineEngine.instance = new TimelineEngine();
		}
		return TimelineEngine.instance;
	}

	void updateTimelines() {
		synchronized (LOCK) {
			if ((this.runningTimelines.size() == 0)
					&& (this.runningScenarios.size() == 0)) {
				this.lastIterationTimeStamp = System.currentTimeMillis();
				return;
			}

			long passedSinceLastIteration = (System.currentTimeMillis() - this.lastIterationTimeStamp);
			if (passedSinceLastIteration < 0) {
				// ???
				passedSinceLastIteration = 0;
			}
			if (DEBUG_MODE) {
				System.out.println("Elapsed since last iteration: "
						+ passedSinceLastIteration + "ms");
			}

			// System.err.println("Periodic update on "
			// + this.runningTimelines.size() + " timelines; "
			// + passedSinceLastIteration + " ms passed since last");
			// for (Timeline t : runningTimelines) {
			// if (t.mainObject != null
			// && t.mainObject.getClass().getName().indexOf(
			// "ProgressBar") >= 0) {
			// continue;
			// }
			// System.err.println("\tTimeline @"
			// + t.hashCode()
			// + " ["
			// + t.getName()
			// + "] on "
			// + (t.mainObject == null ? "null" : t.mainObject
			// .getClass().getName()));
			// }
			for (Iterator<Timeline> itTimeline = this.runningTimelines
					.iterator(); itTimeline.hasNext();) {
				Timeline timeline = itTimeline.next();
				if (timeline.getState() == TimelineState.SUSPENDED)
					continue;

				boolean timelineWasInReadyState = false;
				if (timeline.getState() == TimelineState.READY) {
					if ((timeline.timeUntilPlay - passedSinceLastIteration) > 0) {
						// still needs to wait in the READY state
						timeline.timeUntilPlay -= passedSinceLastIteration;
						continue;
					}

					// can go from READY to PLAYING
					timelineWasInReadyState = true;
					timeline.popState();
					this.callbackCallTimelineStateChanged(timeline,
							TimelineState.READY);
				}

				boolean hasEnded = false;
				if (DEBUG_MODE) {
					System.out.println("Processing " + timeline.id + "["
							+ timeline.mainObject.getClass().getSimpleName()
							+ "] from " + timeline.durationFraction
							+ ". Callback - "
							+ (timeline.callback == null ? "no" : "yes"));
				}
				// Component comp = entry.getKey();

				// at this point, the timeline must be playing
				switch (timeline.getState()) {
				case PLAYING_FORWARD:
					if (!timelineWasInReadyState) {
						timeline.durationFraction = timeline.durationFraction
								+ (float) passedSinceLastIteration
								/ (float) timeline.duration;
					}
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					if (DEBUG_MODE) {
						System.out
								.println("Timeline position: "
										+ ((long) (timeline.durationFraction * timeline.duration))
										+ "/" + timeline.duration + " = "
										+ timeline.durationFraction);
					}
					if (timeline.durationFraction > 1.0f) {
						timeline.durationFraction = 1.0f;
						timeline.timelinePosition = 1.0f;
						if (timeline.isLooping) {
							boolean stopLoopingAnimation = timeline.toCancelAtCycleBreak;
							int loopsToLive = timeline.repeatCount;
							if (loopsToLive > 0) {
								loopsToLive--;
								stopLoopingAnimation = stopLoopingAnimation
										|| (loopsToLive == 0);
								timeline.repeatCount = loopsToLive;
							}
							if (stopLoopingAnimation) {
								// end looping animation
								hasEnded = true;
								itTimeline.remove();
							} else {
								if (timeline.repeatBehavior == Timeline.RepeatBehavior.REVERSE) {
									timeline
											.replaceState(TimelineState.PLAYING_REVERSE);
									if (timeline.cycleDelay > 0) {
										timeline.pushState(TimelineState.READY);
										timeline.timeUntilPlay = timeline.cycleDelay;
									}
									this.callbackCallTimelineStateChanged(
											timeline,
											TimelineState.PLAYING_FORWARD);
								} else {
									timeline.durationFraction = 0.0f;
									timeline.timelinePosition = 0.0f;
									if (timeline.cycleDelay > 0) {
										timeline.pushState(TimelineState.READY);
										timeline.timeUntilPlay = timeline.cycleDelay;
										this.callbackCallTimelineStateChanged(
												timeline,
												TimelineState.PLAYING_FORWARD);
									} else {
										// it's still playing forward, but lets
										// the app code know
										// that the new loop has begun
										this.callbackCallTimelineStateChanged(
												timeline,
												TimelineState.PLAYING_FORWARD);
									}
								}
							}
						} else {
							hasEnded = true;
							itTimeline.remove();
						}
					}
					break;
				case PLAYING_REVERSE:
					if (!timelineWasInReadyState) {
						timeline.durationFraction = timeline.durationFraction
								- (float) passedSinceLastIteration
								/ (float) timeline.duration;
					}
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					// state.timelinePosition = state.timelinePosition
					// - stepFactor
					// * state.fadeStep.getNextStep(state.timelineKind,
					// state.timelinePosition,
					// state.isPlayingForward, state.isLooping);
					if (DEBUG_MODE) {
						System.out
								.println("Timeline position: "
										+ ((long) (timeline.durationFraction * timeline.duration))
										+ "/" + timeline.duration + " = "
										+ timeline.durationFraction);
					}
					if (timeline.durationFraction < 0) {
						timeline.durationFraction = 0.0f;
						timeline.timelinePosition = 0.0f;
						if (timeline.isLooping) {
							boolean stopLoopingAnimation = timeline.toCancelAtCycleBreak;
							int loopsToLive = timeline.repeatCount;
							if (loopsToLive > 0) {
								loopsToLive--;
								stopLoopingAnimation = stopLoopingAnimation
										|| (loopsToLive == 0);
								timeline.repeatCount = loopsToLive;
							}
							if (stopLoopingAnimation) {
								// end looping animation
								hasEnded = true;
								itTimeline.remove();
							} else {
								timeline
										.replaceState(TimelineState.PLAYING_FORWARD);
								if (timeline.cycleDelay > 0) {
									timeline.pushState(TimelineState.READY);
									timeline.timeUntilPlay = timeline.cycleDelay;
								}
								this.callbackCallTimelineStateChanged(timeline,
										TimelineState.PLAYING_REVERSE);
							}
						} else {
							hasEnded = true;
							itTimeline.remove();
						}
					}
					break;
				default:
					throw new IllegalStateException("Timeline cannot be in "
							+ timeline.getState() + " state");
				}
				if (hasEnded) {
					if (DEBUG_MODE) {
						System.out.println("Ending " + timeline.id + " on "
								// + timeline.timelineKind.toString()
								+ " in state " + timeline.getState().name()
								+ " at position " + timeline.durationFraction);
					}
					TimelineState oldState = timeline.getState();
					timeline.replaceState(TimelineState.DONE);
					this.callbackCallTimelineStateChanged(timeline, oldState);
					timeline.popState();
					if (timeline.getState() != TimelineState.IDLE) {
						throw new IllegalStateException(
								"Timeline should be IDLE at this point");
					}
					this.callbackCallTimelineStateChanged(timeline,
							TimelineState.DONE);
				} else {
					if (DEBUG_MODE) {
						System.out.println("Calling " + timeline.id + " on "
						// + timeline.timelineKind.toString() + " at "
								+ timeline.durationFraction);
					}
					this.callbackCallTimelinePulse(timeline);
				}
			}

			if (this.runningScenarios.size() > 0) {
				// System.err.println(Thread.currentThread().getName()
				// + " : updating");
				for (Iterator<TimelineScenario> it = this.runningScenarios
						.iterator(); it.hasNext();) {
					TimelineScenario scenario = it.next();
					if (scenario.state == TimelineScenarioState.DONE) {
						it.remove();
						this.callbackCallTimelineScenarioEnded(scenario);
						continue;
					}
					Set<TimelineScenario.TimelineScenarioActor> readyActors = scenario
							.getReadyActors();
					if (readyActors != null) {
						// if (readyActors.size() > 0)
						// System.out.println("Scenario : " + scenario.state +
						// ":"
						// + readyActors.size());
						for (TimelineScenario.TimelineScenarioActor readyActor : readyActors) {
							readyActor.play();
						}
					}
				}
			}
			// System.err.println("Periodic update done");

			// this.nothingTracked = (this.runningTimelines.size() == 0);
			this.lastIterationTimeStamp = System.currentTimeMillis();
		}
	}

	private void callbackCallTimelineStateChanged(final Timeline timeline,
			final TimelineState oldState) {
		final TimelineState newState = timeline.getState();
		final float durationFraction = timeline.durationFraction;
		final float timelinePosition = timeline.timelinePosition;
		Runnable callbackRunnable = new Runnable() {
			@Override
			public void run() {
				boolean shouldRunOnUIThread = false;
				Class<?> clazz = timeline.callback.getClass();
				while ((clazz != null) && !shouldRunOnUIThread) {
					shouldRunOnUIThread = clazz
							.isAnnotationPresent(RunOnUIThread.class);
					clazz = clazz.getSuperclass();
				}
				if (shouldRunOnUIThread && (timeline.uiToolkitHandler != null)) {
					timeline.uiToolkitHandler.runOnUIThread(
							timeline.mainObject, new Runnable() {
								public void run() {
									timeline.callback.onTimelineStateChanged(
											oldState, newState,
											durationFraction, timelinePosition);
								}
							});
				} else {
					timeline.callback.onTimelineStateChanged(oldState,
							newState, durationFraction, timelinePosition);
				}
			}
		};
		this.callbackQueue.add(callbackRunnable);
	}

	private void callbackCallTimelinePulse(final Timeline timeline) {
		final float durationFraction = timeline.durationFraction;
		final float timelinePosition = timeline.timelinePosition;
		Runnable callbackRunnable = new Runnable() {
			@Override
			public void run() {
				boolean shouldRunOnUIThread = false;
				Class<?> clazz = timeline.callback.getClass();
				while ((clazz != null) && !shouldRunOnUIThread) {
					shouldRunOnUIThread = clazz
							.isAnnotationPresent(RunOnUIThread.class);
					clazz = clazz.getSuperclass();
				}
				if (shouldRunOnUIThread && (timeline.uiToolkitHandler != null)) {
					timeline.uiToolkitHandler.runOnUIThread(
							timeline.mainObject, new Runnable() {
								public void run() {
									// System.err.println("Timeline @"
									// + timeline.hashCode());
									timeline.callback.onTimelinePulse(
											durationFraction, timelinePosition);
								}
							});
				} else {
					// System.err.println("Timeline @" + timeline.hashCode());
					timeline.callback.onTimelinePulse(durationFraction,
							timelinePosition);
				}
			}
		};
		this.callbackQueue.add(callbackRunnable);
	}

	private void callbackCallTimelineScenarioEnded(
			final TimelineScenario timelineScenario) {
		Runnable callbackRunnable = new Runnable() {
			@Override
			public void run() {
				timelineScenario.callback.onTimelineScenarioDone();
			}
		};
		this.callbackQueue.offer(callbackRunnable);
	}


	private Timeline getRunningTimeline(Timeline timeline) {
		synchronized (LOCK) {
			if (this.runningTimelines.contains(timeline))
				return timeline;
			return null;
		}
	}

	private void addTimeline(Timeline timeline) {
		synchronized (LOCK) {
			FullObjectID cid = new FullObjectID(timeline.mainObject,
					timeline.secondaryId);
			timeline.fullObjectID = cid;
			this.runningTimelines.add(timeline);
			// this.nothingTracked = false;
			if (DEBUG_MODE) {
				System.out.println("Added (" + timeline.id + ") on "
						+ timeline.fullObjectID + "]. Fade "
						// + timeline.timelineKind.toString() + " with state "
						+ timeline.getState().name() + ". Callback - "
						+ (timeline.callback == null ? "no" : "yes"));
			}
		}
	}

	void play(Timeline timeline, boolean reset, long msToSkip) {
		synchronized (LOCK) {
			getAnimatorThread();

			// see if it's already tracked
			Timeline existing = this.getRunningTimeline(timeline);
			if (existing == null) {
				TimelineState oldState = timeline.getState();
				timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
				if (timeline.timeUntilPlay < 0) {
					timeline.durationFraction = (float) -timeline.timeUntilPlay
							/ (float) timeline.duration;
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					timeline.timeUntilPlay = 0;
				} else {
					timeline.durationFraction = 0.0f;
					timeline.timelinePosition = 0.0f;
				}
				timeline.pushState(TimelineState.PLAYING_FORWARD);
				timeline.pushState(TimelineState.READY);
				this.addTimeline(timeline);

				this.callbackCallTimelineStateChanged(timeline, oldState);
			} else {
				TimelineState oldState = existing.getState();
				if (oldState == TimelineState.READY) {
					// the timeline remains READY, but after that it will be
					// PLAYING_FORWARD
					existing.popState();
					existing.replaceState(TimelineState.PLAYING_FORWARD);
					existing.pushState(TimelineState.READY);
				} else {
					// change the timeline state
					existing.replaceState(TimelineState.PLAYING_FORWARD);
					if (oldState != existing.getState()) {
						this.callbackCallTimelineStateChanged(timeline,
								oldState);
					}
				}
				if (reset) {
					existing.durationFraction = 0.0f;
					existing.timelinePosition = 0.0f;
					this.callbackCallTimelinePulse(existing);
				}
			}
		}
	}

	void playScenario(TimelineScenario scenario) {
		synchronized (LOCK) {
			getAnimatorThread();
			Set<TimelineScenario.TimelineScenarioActor> readyActors = scenario
					.getReadyActors();

			// System.err.println(Thread.currentThread().getName() +
			// " : adding");
			this.runningScenarios.add(scenario);
			for (TimelineScenario.TimelineScenarioActor readyActor : readyActors) {
				readyActor.play();
			}
		}
	}

	void playReverse(Timeline timeline, boolean reset, long msToSkip) {
		synchronized (LOCK) {
			getAnimatorThread();
			if (timeline.isLooping) {
				throw new IllegalArgumentException(
						"Timeline must not be marked as looping");
			}

			// see if it's already tracked
			Timeline existing = this.getRunningTimeline(timeline);
			if (existing == null) {
				TimelineState oldState = timeline.getState();
				timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
				if (timeline.timeUntilPlay < 0) {
					timeline.durationFraction = 1.0f
							- (float) -timeline.timeUntilPlay
							/ (float) timeline.duration;
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					timeline.timeUntilPlay = 0;
				} else {
					timeline.durationFraction = 1.0f;
					timeline.timelinePosition = 1.0f;
				}
				timeline.pushState(TimelineState.PLAYING_REVERSE);
				timeline.pushState(TimelineState.READY);

				this.addTimeline(timeline);
				this.callbackCallTimelineStateChanged(timeline, oldState);
			} else {
				TimelineState oldState = existing.getState();
				if (oldState == TimelineState.READY) {
					// the timeline remains READY, but after that it will be
					// PLAYING_REVERSE
					existing.popState();
					existing.replaceState(TimelineState.PLAYING_REVERSE);
					existing.pushState(TimelineState.READY);
				} else {
					// change the timeline state
					existing.replaceState(TimelineState.PLAYING_REVERSE);
					if (oldState != existing.getState()) {
						this.callbackCallTimelineStateChanged(timeline,
								oldState);
					}
				}
				if (reset) {
					existing.durationFraction = 1.0f;
					existing.timelinePosition = 1.0f;
					this.callbackCallTimelinePulse(existing);
				}
			}
		}
	}

	void playLoop(Timeline timeline, long msToSkip) {
		synchronized (LOCK) {
			getAnimatorThread();
			if (!timeline.isLooping) {
				throw new IllegalArgumentException(
						"Timeline must be marked as looping");
			}

			// see if it's already tracked
			Timeline existing = this.getRunningTimeline(timeline);
			if (existing == null) {
				TimelineState oldState = timeline.getState();
				timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
				if (timeline.timeUntilPlay < 0) {
					timeline.durationFraction = (float) -timeline.timeUntilPlay
							/ (float) timeline.duration;
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					timeline.timeUntilPlay = 0;
				} else {
					timeline.durationFraction = 0.0f;
					timeline.timelinePosition = 0.0f;
				}
				timeline.pushState(TimelineState.PLAYING_FORWARD);
				timeline.pushState(TimelineState.READY);
				timeline.toCancelAtCycleBreak = false;

				this.addTimeline(timeline);
				this.callbackCallTimelineStateChanged(timeline, oldState);
			} else {
				existing.toCancelAtCycleBreak = false;
				existing.repeatCount = timeline.repeatCount;
			}
		}
	}

	public void cancelAllTimelines() {
		synchronized (LOCK) {
			getAnimatorThread();
			for (Timeline timeline : this.runningTimelines) {
				TimelineState oldState = timeline.getState();
				while (timeline.getState() != TimelineState.IDLE)
					timeline.popState();
				timeline.pushState(TimelineState.CANCELLED);
				this.callbackCallTimelineStateChanged(timeline, oldState);
				timeline.popState();
				this.callbackCallTimelineStateChanged(timeline,
						TimelineState.CANCELLED);
			}
			this.runningTimelines.clear();
			this.runningScenarios.clear();
		}
	}

	private TridentAnimationThread getAnimatorThread() {
		if (this.animatorThread == null) {
			this.animatorThread = new TridentAnimationThread();
			this.animatorThread.start();
		}
		return this.animatorThread;
	}


	private TimelineCallbackThread getCallbackThread() {
		if (this.callbackThread == null) {
			this.callbackThread = new TimelineCallbackThread();
			this.callbackThread.start();
		}
		return this.callbackThread;
	}

	private void cancelTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			this.runningTimelines.remove(timeline);
			TimelineState oldState = timeline.getState();
			while (timeline.getState() != TimelineState.IDLE)
				timeline.popState();
			timeline.pushState(TimelineState.CANCELLED);
			this.callbackCallTimelineStateChanged(timeline, oldState);
			timeline.popState();
			this.callbackCallTimelineStateChanged(timeline,
					TimelineState.CANCELLED);
		}
	}

	private void endTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			this.runningTimelines.remove(timeline);
			TimelineState oldState = timeline.getState();
			float endPosition = timeline.timelinePosition;
			while (timeline.getState() != TimelineState.IDLE) {
				TimelineState state = timeline.popState();
				if (state == TimelineState.PLAYING_FORWARD)
					endPosition = 1.0f;
				if (state == TimelineState.PLAYING_REVERSE)
					endPosition = 0.0f;
			}
			timeline.durationFraction = endPosition;
			timeline.timelinePosition = endPosition;
			timeline.pushState(TimelineState.DONE);
			this.callbackCallTimelineStateChanged(timeline, oldState);
			timeline.popState();
			this.callbackCallTimelineStateChanged(timeline, TimelineState.DONE);
		}
	}


	private void abortTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			this.runningTimelines.remove(timeline);
			while (timeline.getState() != TimelineState.IDLE)
				timeline.popState();
		}
	}

	private void suspendTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			TimelineState oldState = timeline.getState();
			if ((oldState != TimelineState.PLAYING_FORWARD)
					&& (oldState != TimelineState.PLAYING_REVERSE)
					&& (oldState != TimelineState.READY)) {
				return;
			}
			timeline.pushState(TimelineState.SUSPENDED);
			this.callbackCallTimelineStateChanged(timeline, oldState);
		}
	}

	private void resumeTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			TimelineState oldState = timeline.getState();
			if (oldState != TimelineState.SUSPENDED)
				return;
			timeline.popState();
			this.callbackCallTimelineStateChanged(timeline, oldState);
		}
	}

	void runTimelineOperation(Timeline timeline,
			TimelineOperationKind operationKind, Runnable operationRunnable) {
		synchronized (LOCK) {
			this.getAnimatorThread();
			switch (operationKind) {
			case CANCEL:
				this.cancelTimeline(timeline);
				return;
			case END:
				this.endTimeline(timeline);
				return;
			case RESUME:
				this.resumeTimeline(timeline);
				return;
			case SUSPEND:
				this.suspendTimeline(timeline);
				return;
			case ABORT:
				this.abortTimeline(timeline);
				return;
			}
			operationRunnable.run();
		}
	}

	void runTimelineScenario(TimelineScenario timelineScenario,
			Runnable timelineScenarioRunnable) {
		synchronized (LOCK) {
			this.getAnimatorThread();
			timelineScenarioRunnable.run();
		}
	}

	static final Object LOCK = new Object();
}
/*
 * dfh.treepath -- a generic tree querying library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.treepath;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import dfh.grammar.GrammarException;
import dfh.grammar.Match;
import dfh.grammar.MatchTest;
import dfh.grammar.Matcher;
import dfh.grammar.Options;
import dfh.treepath.PathGrammar.Axis;

/**
 * An expert on trees. A {@link Forester} answers questions about the variety of
 * trees it knows provided these quests are asked in the form of tree path
 * expressions.
 * <p>
 * Foresters are path factories. Their chief method if interest is
 * {@link #path(String)}.
 * 
 * @author David F. Houghton - Apr 18, 2012
 * 
 * @param <N>
 *            the variety of node in the trees understood by the
 *            {@link Forester}
 */
public abstract class Forester<N> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * A cache retaining the mappings from varieties of {@link Forester} to the
	 * attributes they can handle. This is used to accelerate construction by
	 * caching the results of reflective code.
	 */
	protected final static Map<Class<? extends Forester<?>>, Map<String, InstanceWrapper>> attributeCache = new HashMap<Class<? extends Forester<?>>, Map<String, InstanceWrapper>>();
	protected transient Map<String, InstanceWrapper> attributes;
	final NodeTest<N>[] ignore;
	/**
	 * A place for the log attribute to send its logging.
	 */
	transient private PrintStream loggingStream = System.err;

	/**
	 * Initializes the map from attributes to methods and records the node types
	 * to ignore.
	 * 
	 * @param ignorable
	 *            node types to ignore; none ignored if parameter is null or
	 *            empty
	 */
	@SuppressWarnings("unchecked")
	public Forester(NodeTest<N>... ignorable) {
		if (ignorable == null || ignorable.length == 0)
			ignore = new NodeTest[0];
		else {
			Set<NodeTest<N>> set = new HashSet<NodeTest<N>>();
			for (NodeTest<N> t : ignorable)
				set.add(t);
			ignore = set.toArray(new NodeTest[set.size()]);
		}
	}

	/**
	 * Make sure this Forester knows its attributes.
	 */
	protected void init() {
		if (attributes == null) {
			attributes = getAttributes();
		}
	}

	/**
	 * Returns attributes handled, checking {@link #attributeCache} before
	 * discovering them by reflection.
	 * 
	 * @return attributes handled by forester
	 */
	@SuppressWarnings("unchecked")
	protected final Map<String, InstanceWrapper> getAttributes() {
		synchronized (attributeCache) {
			Map<String, InstanceWrapper> map = attributeCache.get(this
					.getClass());
			if (map == null) {
				map = new HashMap<String, InstanceWrapper>();
				Class<?> icl = null;
				try {
					icl = Class.forName("java.util.Collection");
				} catch (ClassNotFoundException e) {
					throw new PathException(e);
				}
				Class<?> cz = getClass();
				while (Forester.class.isAssignableFrom(cz)) {
					for (Method m : cz.getDeclaredMethods()) {
						int mods = m.getModifiers();
						if (!Modifier.isPrivate(mods)) {
							Attribute a = m.getAnnotation(Attribute.class);
							if (a != null) {
								String name = a.value();
								if (name.length() == 0)
									name = m.getName();
								if (map.containsKey(name))
									continue;
								Class<?>[] pts = m.getParameterTypes();
								if (pts.length < 3)
									throw new PathException(
											"ill-formed attribute @"
													+ name
													+ "; every attribute must have at least a node, collection, and index parameter");
								if (!icl.isAssignableFrom(pts[1]))
									throw new PathException(
											"the second parameter for attribute @"
													+ name
													+ " must represent the collection of nodes of which the context node is a member");
								if (!Index.class.isAssignableFrom(pts[2]))
									throw new PathException(
											"the third parameter for attribute @"
													+ name
													+ " must be an instance of dfh.treepath.Index");
								if (m.getReturnType() == Void.TYPE)
									throw new PathException("attribute @"
											+ name
											+ " does not return any value");
								m.setAccessible(true);
								map.put(name, wrapMethod(m));
							}
						}
					}
					cz = cz.getSuperclass();
				}
				attributeCache.put(
						(Class<? extends Forester<?>>) this.getClass(), map);
			}
			return map;
		}
	}

	/**
	 * {@link MatchTest} used in compiling the path parse tree into a
	 * {@link Path}.
	 */
	protected static final MatchTest pathMt = new MatchTest() {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean test(Match m) {
			return m.rule().label().id.equals("path");
		}
	};

	/**
	 * Translates the return value of an {@link Attribute} into a boolean value.
	 * {@link Boolean} objects are interpreted via
	 * {@link Boolean#booleanValue()}. {@link Collection} objects are true if
	 * {@link Collection#isEmpty()} is false. All other values are true if they
	 * are not null.
	 * 
	 * @param o
	 * @return whether the value returned by an attribute evaluates to
	 *         <code>true</code>
	 */
	protected boolean attribToBoolean(Object o) {
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue();
		if (o instanceof Collection<?>)
			return !((Collection<?>) o).isEmpty();
		return o != null;
	}

	/**
	 * Translates a node and an axis into the appropriate collection of
	 * candidate nodes.
	 * 
	 * @param n
	 *            context node relative to which the axis will be examined
	 * @param a
	 *            axis to walk
	 * @param t
	 *            node test to locate desired nodes on the specified axis
	 * @param i
	 *            tree idnex
	 * @return those nodes on the given axis that pass the test
	 */
	protected Collection<N> axis(N n, Axis a, NodeTest<N> t, Index<N> i) {
		switch (a) {
		case child:
			return children(n, t, i);
		case ancestor:
			return ancestors(n, t, i);
		case ancestorOrSelf:
			List<N> list = ancestors(n, t, i);
			if (t.passes(n, i))
				list.add(n);
			return list;
		case descendant:
			return descendants(n, t, i);
		case descendantOrSelf:
			list = new ArrayList<N>(descendants(n, t, i));
			if (t.passes(n, i))
				list.add(n);
			return list;
		case following:
			return following(n, t, i);
		case followingSibling:
			return followingSiblings(n, t, i);
		case preceding:
			return preceding(n, t, i);
		case precedingSibling:
			return precedingSiblings(n, t, i);
		case sibling:
			return siblings(n, t, i);
		case siblingOrSelf:
			return siblingsOrSelf(n, t, i);
		case leaf:
			return leaves(n, t, i);
		case self:
			if (t.passes(n, i)) {
				list = new ArrayList<N>(1);
				list.add(n);
				return list;
			}
			return Collections.emptyList();
		case parent:
			N parent = parent(n, i);
			if (t.passes(parent, i)) {
				list = new ArrayList<N>(1);
				list.add(parent);
				return list;
			}
			return Collections.emptyList();
		default:
			throw new PathException(Forester.class
					+ " not written to handle axis " + a);
		}
	}

	/**
	 * Implements the siblings axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node type of interest
	 * @param i
	 *            tree index
	 * @return siblings of context node
	 */
	protected Collection<N> siblings(N n, NodeTest<N> t, Index<N> i) {
		List<N> siblings = siblings(parent(n, i), i);
		if (siblings.isEmpty())
			return siblings;
		List<N> list = new ArrayList<N>(siblings.size());
		for (N s : siblings)
			if (t.passes(s, i))
				list.add(s);
		return list;
	}

	/**
	 * Implements the siblings-or-self axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node type of interest
	 * @param i
	 *            tree index
	 * @return context node and its siblings in tree order
	 */
	protected Collection<N> siblingsOrSelf(N n, NodeTest<N> t, Index<N> i) {
		if (i.isRoot(n)) {
			List<N> list = new ArrayList<N>(1);
			if (t.passes(n, i))
				list.add(n);
			return list;
		}
		List<N> siblings = kids(parent(n, i), i);
		List<N> list = new ArrayList<N>(siblings.size());
		for (N s : siblings)
			if (t.passes(s, i))
				list.add(s);
		return list;
	}

	/**
	 * Implements child axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            nodes of interest
	 * @param i
	 *            tree index
	 * @return children of context node
	 */
	protected List<N> children(N n, NodeTest<N> t, Index<N> i) {
		List<N> children = kids(n, i);
		if (children.isEmpty())
			return children;
		List<N> list = new ArrayList<N>(children.size());
		for (N c : children) {
			if (t.passes(c, i))
				list.add(c);
		}
		return list;
	}

	/**
	 * Compiles a path expression into a {@link Path}.
	 * 
	 * @param path
	 *            path expression
	 * @return compiled path expression
	 */
	public final Path<N> path(String path) {
		if (path == null)
			throw new PathException("path expression cannot be null");
		try {
			Matcher m = PathGrammar.g.matches(path, new Options()
					.keepRightmost(true).study(false));
			Match n = m.match();
			if (n == null) {
				StringBuilder b = new StringBuilder("could not parse ");
				b.append(path)
						.append(" as a tree path; check syntax at offset marked by '<HERE>': ");
				n = m.rightmostMatch();
				if (n == null)
					b.append("<HERE>").append(path);
				else if (n.end() == path.length())
					b.append(path).append("<HERE>");
				else {
					b.append(path.substring(0, n.end()));
					b.append("<HERE>");
					b.append(path.substring(n.end()));
				}
				throw new PathException(b.toString());
			}
			init();
			return path(n);
		} catch (GrammarException e) {
			throw new PathException("failed to compile path " + path, e);
		}
	}

	/**
	 * Used to build relative paths.
	 * 
	 * @param n
	 * @return relative path
	 */
	final Path<N> path(Match n) {
		List<Match> paths = n.closest(pathMt);
		@SuppressWarnings("unchecked")
		Selector<N>[][] selectors = new Selector[paths.size()][];
		for (int i = 0; i < selectors.length; i++) {
			selectors[i] = makePath(paths.get(i));
		}
		return new Path<N>(this, selectors);
	}

	private static final MatchTest segmentMT = new MatchTest() {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean test(Match m) {
			return m.hasLabel("segment");
		}
	};
	private static final MatchTest anameMT = new MatchTest() {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean test(Match m) {
			return m.rule().label().id.equals("axis_name");
		}
	};

	private final Selector<N>[] makePath(Match match) {
		List<Match> subsequentSteps = match.closest(segmentMT);
		@SuppressWarnings("unchecked")
		Selector<N>[] path = new Selector[subsequentSteps.size()];
		int i = 0;
		for (Match m : subsequentSteps) {
			path[i] = makeStep(m, i++ == 0);
		}
		return path;
	}

	private final Selector<N> makeStep(Match fs, boolean first) {
		Match slash, step;
		slash = fs.children()[0];
		step = fs.children()[1];
		switch (slash.length()) {
		case 0:
			return makeRelativeStep(step);
		case 1:
			return first ? makeRootStep(step) : makeRelativeStep(step);
		case 2:
			return slash.group().charAt(1) == '/' ? makeGlobalStep(step, first)
					: makeClosestStep(step);
		default:
			throw new PathException("unexpected step separator in path: "
					+ slash.group());
		}
	}

	private final Selector<N> makeClosestStep(Match step) {
		Match predicates = step.children()[1];
		Match tagMatch = step.children()[0].children()[0].children()[1];
		String s = tagMatch.group();
		if ("*".equals(s))
			return new ClosestWildcard<N>(predicates, this);
		else if (s.charAt(0) == '~') {
			return new ClosestMatching<N>(cleanMatch(s), predicates, this);
		} else
			return new ClosestTag<N>(unescape(s), predicates, this);
	}

	/**
	 * Makes a <code>//foo</code> step.
	 * 
	 * @param step
	 *            the node from the path parse tree representing the step
	 * @param first
	 * @return the {@link Selector} representing the step
	 */
	private final Selector<N> makeGlobalStep(Match step, boolean first) {
		Match tagMatch = step.children()[0], predicates = step.children()[1];
		String s = tagMatch.group();
		if ("*".equals(s))
			return new AnywhereWildcard<N>(predicates, this, first);
		else if (s.charAt(0) == '~') {
			return new AnywhereMatching<N>(cleanMatch(s), predicates, this, first);
		} else
			return new AnywhereTag<N>(unescape(s), predicates, this, first);
	}

	/**
	 * Remove escape characters
	 * 
	 * @param s
	 * @return the string minus \ (unless escaped)
	 */
	private final String unescape(String s) {
		return s.replaceAll("\\\\(.)", "$1");
	}

	private final Selector<N> makeRootStep(Match step) {
		Match predicates = step.children()[1];
		step = step.children()[0].children()[0];
		if (step.rule().label().id.equals("abbreviated")) {
			step = step.children()[1];
			switch (step.length()) {
			case 1:
				return new RootSelector<N>(predicates, this);
			case 2:
				new PathException(
						"/.. is ill-formed; the root node has no parent");
			default:
				return new IdSelector<N>(step, predicates);
			}
		} else {
			Match axisMatch = step.children()[0], tagMatch = step.children()[1];
			String s = tagMatch.group();
			if (axisMatch.length() == 0) {
				if ("*".equals(s))
					return new RootWildcard<N>(predicates, this);
				else if (s.charAt(0) == '~') {
					s = cleanMatch(s);
					return new RootMatching<N>(s, predicates, this);
				} else
					return new RootTag<N>(unescape(s), predicates, this);
			} else {
				String aname = axisMatch.first(anameMT).group();
				if ("*".equals(s))
					return new RootAxisWildcard<N>(aname, predicates, this);
				else if (s.charAt(0) == '~') {
					s = cleanMatch(s);
					return new RootAxisMatching<N>(aname, s, predicates, this);
				} else
					return new RootAxisTag<N>(aname, unescape(s), predicates,
							this);
			}
		}
	}

	/**
	 * Converts a ~&lt;chars&gt;~ expression into a string that can be compiled
	 * into a pattern.
	 * 
	 * @param s
	 * @return
	 */
	private String cleanMatch(String s) {
		s = s.substring(1, s.length() - 1).replaceAll("~~", "~");
		return s;
	}

	private final Selector<N> makeRelativeStep(Match step) {
		Match predicates = step.children()[1];
		step = step.children()[0].children()[0];
		if (step.rule().label().id.equals("abbreviated")) {
			step = step.children()[1];
			switch (step.length()) {
			case 1:
				return new SelfSelector<N>(predicates, this);
			case 2:
				new ParentSelector<N>(predicates, this);
			default:
				return new IdSelector<N>(step, predicates);
			}
		} else {
			Match axisMatch = step.children()[0], tagMatch = step.children()[1];
			String s = tagMatch.group();
			if (axisMatch.length() == 0) {
				if ("*".equals(s))
					return new AxisWildcard<N>("child", predicates, this);
				else if (s.charAt(0) == '~') {
					return new ChildMatching<N>(cleanMatch(s), predicates, this);
				} else
					return new ChildTag<N>(unescape(s), predicates, this);
			} else {
				String aname = axisMatch.first(anameMT).group();
				if ("*".equals(s))
					return new AxisWildcard<N>(aname, predicates, this);
				else if (s.charAt(0) == '~') {
					return new AxisMatching<N>(aname, cleanMatch(s), predicates, this);
				} else
					return new AxisTag<N>(aname, unescape(s), predicates, this);
			}
		}
	}

	/**
	 * A boolean attribute that evaluates to true if the context node is a leaf.
	 * 
	 * @param n
	 *            context node
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index
	 * @return whether n is a leaf
	 */
	@Attribute(value = "leaf", description = "whether the context node is a leaf")
	protected boolean isLeaf(N n, Collection<N> c, Index<N> i) {
		List<N> children = kids(n, i);
		if (children.isEmpty())
			return true;
		return false;
	}

	/**
	 * An attribute for selecting a member from a collection of nodes returned
	 * by a path. E.g., <code>&#064;pick(foo//bar, 1)</code>. The index is
	 * zero-based.
	 * 
	 * @param n
	 *            context node
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index
	 * @param from
	 *            a collection of candidates selected by some path
	 * @param index
	 *            the index in the collection selected from corresponding to the
	 *            node selected
	 * @return the node selected, or <code>null</code> if there is no
	 *         appropriate node
	 */
	@Attribute(description = "picks a node from a collection")
	protected final N pick(N n, Collection<N> c, Index<N> i,
			Collection<N> from, int index) {
		int j = index;
		if (from.isEmpty())
			return null;
		if (index < 0)
			j = from.size() + index;
		if (j < 0)
			return null;
		if (j >= from.size())
			return null;
		if (from instanceof List<?>)
			return ((List<N>) from).get(j);
		int in = 0;
		for (Iterator<N> it = from.iterator(); it.hasNext();) {
			N next = it.next();
			if (in++ == j)
				return next;
		}
		return null;
	}

	/**
	 * An attribute for selecting a member from a collection of nodes returned
	 * by a path. E.g., &#064;size(foo//bar).
	 * 
	 * @param n
	 *            context node
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index
	 * @param from
	 *            a candidate node set selected by a path
	 * @return the number of nodes selected by the path
	 */
	@Attribute(description = "the size of the node collection")
	protected int size(N n, Collection<N> c, Index<N> i, Collection<N> from) {
		return from.size();
	}

	/**
	 * A boolean attribute that evalues to true if the context node is the tree
	 * root.
	 * 
	 * @param n
	 *            context node
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index
	 * @return whether n is the tree root
	 */
	@Attribute(value = "root", description = "whether the context node is the root")
	protected boolean isRoot(N n, Collection<N> c, Index<N> i) {
		return i.isRoot(n);
	}

	/**
	 * @param n
	 * @param c
	 * @param i
	 * @return the identifying string, if any, of this node
	 */
	@Attribute(description = "the nodes string id, if any")
	protected String id(N n, Collection<N> c, Index<N> i) {
		return i.id(n);
	}

	/**
	 * An attribute whose value is always <code>null</code>. This attribute
	 * cannot be overridden.
	 * 
	 * @param n
	 *            context node; required for method signature but ignored
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index; required for method signature but ignored
	 * @return <code>null</code>
	 */
	@Attribute(value = "null", description = "the null value")
	protected final Object Null(N n, Collection<N> c, Index<N> i) {
		return null;
	}

	/**
	 * An attribute whose value is always <code>true</code>. This attribute
	 * cannot be overridden.
	 * 
	 * @param n
	 *            context node; required for method signature but ignored
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index; required for method signature but ignored
	 * @return <code>true</code>
	 */
	@Attribute(value = "true", description = "the true value")
	protected final Boolean True(N n, Collection<N> c, Index<N> i) {
		return Boolean.TRUE;
	}

	/**
	 * An attribute whose value is always <code>false</code>. This attribute
	 * cannot be overridden.
	 * 
	 * @param n
	 *            context node; required for method signature but ignored
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index
	 * @return <code>false</code>
	 */
	@Attribute(value = "false", description = "the false value")
	protected final Boolean False(N n, Collection<N> c, Index<N> i) {
		return Boolean.FALSE;
	}

	/**
	 * An attribute that returns the context node itself.
	 * 
	 * @param n
	 *            context node
	 * @param c
	 *            collection of which context node is a member; required for
	 *            method signature but ignored
	 * @param i
	 *            tree index; required for method signature but ignored
	 * @return the context node n
	 */
	@Attribute(value = "this", description = "the context node")
	protected final N This(N n, Collection<N> c, Index<N> i) {
		return n;
	}

	protected List<N> leaves(N n, NodeTest<N> t, Index<N> i) {
		List<N> children = kids(n, i);
		if (children.isEmpty()) {
			if (!t.passes(n, i))
				return Collections.emptyList();
			List<N> leaves = new ArrayList<N>(1);
			leaves.add(n);
			return leaves;
		}
		List<N> leaves = new ArrayList<N>();
		for (N child : children)
			leaves.addAll(leaves(child, t, i));
		return leaves;
	}

	/**
	 * Ancestors of context node; implements ancestor axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node types of interest
	 * @param i
	 *            tree index
	 * @return ancestors of context node from youngest to oldest
	 */
	protected List<N> ancestors(N n, NodeTest<N> t, Index<N> i) {
		LinkedList<N> ancestors = new LinkedList<N>();
		N o = n;
		while (!isRoot(o, null, i)) {
			N parent = parent(o, i);
			if (t.passes(parent, i))
				ancestors.addFirst(parent);
			o = parent;
		}
		return ancestors;
	}

	/**
	 * Implements the descendant axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node types of interest
	 * @param i
	 *            tree index
	 * @return descendants of context node
	 */
	protected Collection<N> descendants(N n, NodeTest<N> t, Index<N> i) {
		List<N> children = kids(n, i);
		if (children.isEmpty())
			return children;
		List<N> descendants = new LinkedList<N>();
		for (N child : children) {
			if (!isLeaf(child, null, i))
				descendants.addAll(descendants(child, t, i));
			if (t.passes(child, i))
				descendants.add(child);
		}
		return descendants;
	}

	/**
	 * Implements /> expression.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            nodes of interest
	 * @param i
	 *            tree index
	 * @return nearest nodes of interest dominated by the context node
	 */
	protected Collection<N> closest(N n, NodeTest<N> t, Index<N> i) {
		if (t.passes(n, i)) {
			List<N> list = new ArrayList<N>(1);
			list.add(n);
			return list;
		}
		List<N> children = kids(n, i);
		if (children.isEmpty())
			return children;
		List<N> closest = new LinkedList<N>();
		for (N child : children)
			closest.addAll(closest(child, t, i));
		return closest;
	}

	/**
	 * Implements the preceding-sibling axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node types of interest
	 * @param i
	 *            tree index
	 * @return preceding siblings of context node
	 */
	protected List<N> precedingSiblings(N n, NodeTest<N> t, Index<N> i) {
		if (isRoot(n, null, i))
			return Collections.emptyList();
		List<N> siblings = kids(parent(n, i), i);
		if (siblings.size() == 1)
			return Collections.emptyList();
		List<N> precedingSiblings = new ArrayList<N>(siblings.size() - 1);
		for (N sib : siblings) {
			if (sib == n)
				break;
			if (t.passes(sib, i))
				precedingSiblings.add(sib);
		}
		return precedingSiblings;
	}

	/**
	 * Obtains context node and its siblings in their tree order.
	 * 
	 * @param n
	 *            context node
	 * @param i
	 *            tree index
	 * @return context node and its siblings
	 */
	protected List<N> siblings(N n, Index<N> i) {
		if (isRoot(n, null, i))
			return Collections.emptyList();
		List<N> siblings = kids(parent(n, i), i);
		if (siblings.size() == 1)
			return Collections.emptyList();
		List<N> sibs = new ArrayList<N>(siblings.size() - 1);
		for (N s : siblings) {
			if (s != n)
				sibs.add(s);
		}
		return sibs;
	}

	/**
	 * Implements following-sibling axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node types of interest
	 * @param i
	 *            tree index
	 * @return siblings following context node
	 */
	protected List<N> followingSiblings(N n, NodeTest<N> t, Index<N> i) {
		if (isRoot(n, null, i))
			return Collections.emptyList();
		List<N> siblings = kids(parent(n, i), i);
		if (siblings.size() == 1)
			return Collections.emptyList();
		List<N> followingSiblings = new ArrayList<N>(siblings.size() - 1);
		boolean add = false;
		for (N sib : siblings) {
			if (add && t.passes(sib, i))
				followingSiblings.add(sib);
			else
				add = sib == n;
		}
		return followingSiblings;
	}

	/**
	 * Implements preceding axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node types of interest
	 * @param i
	 *            tree index
	 * @return nodes preceding the context node in the tree
	 */
	@SuppressWarnings("unchecked")
	protected Collection<N> preceding(N n, NodeTest<N> t, Index<N> i) {
		if (isRoot(n, null, i))
			return Collections.emptyList();
		Collection<N> preceding = new LinkedList<N>();
		Collections.emptyList();
		List<N> ancestors = ancestors(n, (NodeTest<N>) TrueTest.test(), i);
		for (N a : ancestors.subList(1, ancestors.size())) {
			for (N p : precedingSiblings(a, (NodeTest<N>) TrueTest.test(), i)) {
				preceding.addAll(descendants(p, t, i));
				if (t.passes(p, i))
					preceding.add(p);
			}
		}
		for (N p : precedingSiblings(n, (NodeTest<N>) TrueTest.test(), i)) {
			preceding.addAll(descendants(p, t, i));
			if (t.passes(p, i))
				preceding.add(p);
		}
		return preceding;
	}

	/**
	 * Implements following axis.
	 * 
	 * @param n
	 *            context node
	 * @param t
	 *            node types of interest
	 * @param i
	 *            tree index
	 * @return nodes following the context node in the tree
	 */
	@SuppressWarnings("unchecked")
	protected Collection<N> following(N n, NodeTest<N> t, Index<N> i) {
		if (isRoot(n, null, i))
			return Collections.emptyList();
		Collection<N> following = new LinkedList<N>();
		List<N> ancestors = ancestors(n, (NodeTest<N>) TrueTest.test(), i);
		for (N a : ancestors.subList(1, ancestors.size())) {
			for (N p : followingSiblings(a, (NodeTest<N>) TrueTest.test(), i)) {
				following.addAll(descendants(p, t, i));
				if (t.passes(p, i))
					following.add(p);
			}
		}
		for (N p : followingSiblings(n, (NodeTest<N>) TrueTest.test(), i)) {
			following.addAll(descendants(p, t, i));
			if (t.passes(p, i))
				following.add(p);
		}
		return following;
	}

	/**
	 * Returns the index of the context node in the context collection.
	 * 
	 * @param n
	 * @return index of n in its context collection; -1 if n is root
	 */
	@Attribute(description = "the index of the context node in the context collection")
	protected int index(N n, Collection<N> c, Index<N> in) {
		if (isRoot(n, null, in))
			return -1;
		List<N> siblings = kids(parent(n, in), in);
		for (int i = 0, lim = siblings.size(); i < lim; i++) {
			N o = siblings.get(i);
			if (o == n)
				return i;
		}
		return -1;
	}

	/**
	 * All the children of the context node regardless of whether we are
	 * ignoring any node types.
	 * 
	 * @param n
	 *            context node
	 * @param i
	 *            tree index
	 * @return the children of n
	 */
	protected abstract List<N> children(N n, Index<N> i);

	/**
	 * Like {@link #children(Object, Index)}, but it tests the nodes against the
	 * node tests in {@link #ignore}.
	 * 
	 * @param n
	 *            context node
	 * @param i
	 *            tree index
	 * @return children remaining after dropping those to be ignored
	 */
	protected final List<N> kids(N n, Index<N> i) {
		List<N> children = children(n, i);
		if (children == null)
			return Collections.emptyList();
		if (ignore.length == 0 || children.isEmpty())
			return children;
		List<N> kids = new ArrayList<N>(children.size());
		OUTER: for (N c : children) {
			for (NodeTest<N> t : ignore)
				if (t.passes(c, i))
					continue OUTER;
			kids.add(c);
		}
		return kids;
	}

	/**
	 * Defines what it means for a node in this tree to have a particular tag --
	 * the "b" in the path expression "//b".
	 * 
	 * @param n
	 *            context node
	 * @param tag
	 *            tag
	 * @return whether the node bears the given tag
	 */
	protected abstract boolean hasTag(N n, String tag);

	/**
	 * Defines what it means for a node in this tree to have a tag matching the
	 * specified pattern. See {@link #hasTag(Object, String)}.
	 * 
	 * @param n
	 *            context node
	 * @param p
	 *            tag pattern
	 * @return whether the pattern matches the node's tag or tags
	 */
	protected abstract boolean matchesTag(N n, Pattern p);

	/**
	 * Obtains the parent of the context node.
	 * 
	 * @param n
	 *            context node
	 * @param i
	 *            tree index
	 * @return the parent of n
	 */
	protected abstract N parent(N n, Index<N> i);

	protected N root(N n, Index<N> i) {
		return i.root;
	}


	public Index<N> index(N root) {
		return new Index<N>(root, this);
	}

	public Map<String, String[]> attributes() {
		init();
		Map<String, String[]> m = new TreeMap<String, String[]>();
		for (InstanceWrapper wm : attributes.values()) {
			Method method = wm.method();
			Attribute a = method.getAnnotation(Attribute.class);
			String name = a.value();
			if ("".equals(name))
				name = method.getName();
			String[] description = {
					a.description(),
					method.toString()
							.replaceFirst("\\((?:[^,]++,){2}[^,)]++", "\\(...")
							.replaceAll("java\\.lang\\.", "") };
			m.put(name, description);
		}
		return m;
	}

	public PrintStream getLoggingStream() {
		return loggingStream == null ? System.err : loggingStream;
	}

	public void setLoggingStream(PrintStream loggingStream) {
		this.loggingStream = loggingStream;
	}

	@Attribute(description = "records parameters to a log stream")
	protected final Boolean log(N n, Collection<N> c, Index<N> i, Object... msg) {
		for (Object o : msg)
			getLoggingStream().println(o);
		return Boolean.TRUE;
	}

	public Object attribute(N node, String name, Object... parameters) {
		return attribute(node, name, null, null, parameters);
	}

	public Object attribute(N node, String name, Collection<N> c, Index<N> i,
			Object... parameters) {
		if (node == null)
			throw new PathException(
					"attributes cannot be evaluated on null nodes");
		init();
		InstanceWrapper wm = attributes.get(name);
		if (wm == null)
			throw new PathException("unknown attribute: " + name);
		if (i == null) {
			i = index(node);
		}
		if (c == null) {
			c = new ArrayList<N>(1);
			c.add(node);
		}
		if (!i.indexed())
			i.index();
		try {
			List<Object> parameterList = new ArrayList<Object>(
					parameters.length + 3);
			parameterList.add(node);
			parameterList.add(c);
			parameterList.add(i);
			for (Object o : parameters)
				parameterList.add(o);
			return wm.method().invoke(wm.instance(this),
					parameterList.toArray());
		} catch (Exception e) {
			throw new PathException("could not evaluate attribute " + name
					+ " with node, index, collection, and parameters provided",
					e);
		}
	}


	@Attribute(description = "unique id of context node representing its position in its tree")
	protected final String uid(N n, Collection<N> c, Index<N> i) {
		List<Integer> list = new ArrayList<Integer>();
		N node = n;
		while (node != i.root) {
			N parent = i.f.parent(node, i);
			List<N> children = i.f.children(parent, i);
			for (int in = 0, lim = children.size(); in < lim; in++) {
				N child = children.get(in);
				if (child == node) {
					list.add(in);
					break;
				}
			}
			node = parent;
		}
		if (list.isEmpty())
			return "/";
		StringBuilder b = new StringBuilder();
		for (int in = list.size() - 1; in >= 0; in--)
			b.append('/').append(list.get(in));
		return b.toString();
	}


	@Attribute(description = "converts anything into an attribute")
	protected final Object echo(N n, Collection<N> c, Index<N> i, Object o) {
		return o;
	}


	InstanceWrapper wrapMethod(Method m) {
		return new InstanceWrapper(this, m);
	}

	public void mixin(Class<? extends AttributeLibrary<N>>... mixins) {
		init();
		for (Class<? extends AttributeLibrary<N>> mixin : mixins) {
			try {
				AttributeLibrary<N> al = mixin.newInstance();
				al.init();
				for (Entry<String, InstanceWrapper> e : al.attributes
						.entrySet()) {
					if (!attributes.containsKey(e.getKey()))
						attributes.put(e.getKey(), e.getValue());
				}
			} catch (InstantiationException e) {
				throw new PathException(
						"failed to create a reference instance of attribute library "
								+ mixin, e);
			} catch (IllegalAccessException e) {
				throw new PathException(
						"failed to create a reference instance of attribute library "
								+ mixin, e);
			}
		}
	}
}
