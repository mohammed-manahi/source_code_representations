public class TribuSpawner {
	/**
	 * Is the round finish
	 */
	private boolean								finished;

	/**
	 * Health of zombie to spawn
	 */
	private int									health;
	/**
	 *  A zombie just spawned
	 */
	private boolean								justspawned;
	/**
	 * number of zombies to spawn
	 */
	private int									totalToSpawn;
	/**
	 * Tribu
	 */
	private final Tribu							plugin;
	/**
	 * Gonna start
	 */
	private boolean								starting;
	/**
	 * spawned zombies
	 */
	private int									alreadySpawned;
	/**
	 * Referenced zombies
	 */
	private final LinkedList<CraftTribuZombie>	zombies;

	/**
	 * Init the spawner
	 * @param instance of Tribu
	 */
	public TribuSpawner(final Tribu instance) {
		plugin = instance;
		alreadySpawned = 0;
		totalToSpawn = 5;
		finished = false;
		starting = true;
		health = 10;
		zombies = new LinkedList<CraftTribuZombie>();
	}

	/**
	 * Check that all referenced zombies are alive
	 * Useful to check if a zombie has been despawned (too far away, killed but not caught) 
	 * set finished if they are all dead
	 */
	public void checkZombies() {
		final Stack<CraftTribuZombie> toDelete = new Stack<CraftTribuZombie>();
		for (final CraftTribuZombie e : zombies)
			if (e == null || e.isDead()) toDelete.push(e);
		finished = toDelete.isEmpty();
		while (!toDelete.isEmpty())
			removedZombieCallback(toDelete.pop(), false);

	}

	/**
	 * Delete all zombies and prevent the spawner to continue spawning
	 */
	public void clearZombies() {
		for (final CraftTribuZombie zombie : zombies)
			zombie.remove();
		resetTotal();
		zombies.clear();
	}

	/**
	 * Despawn a killed zombie
	 * @param zombie zombie to unreference
	 * @param drops drops to clear
	 */
	public void despawnZombie(final CraftTribuZombie zombie, final List<ItemStack> drops) {
		if (zombies.remove(zombie)) {
			drops.clear();
			tryStartNextWave();
		}
		// Else The zombie may have been deleted by "removedZombieCallback"
		/*else {
			
			plugin.LogWarning("Unreferenced zombie despawned");
		}*/
	}

	/**
	 * Set that it's finish
	 */
	public void finishCallback() {
		finished = true;
	}

	// Debug command
	/**
	 * This is a debug command returning the location of a living zombie
	 * It prints info of this zombie on the console or a severe error
	 * @return location of a living zombie
	 */
	public Location getFirstZombieLocation() {
		if (alreadySpawned > 0)
			if (!zombies.isEmpty()) {
				plugin.LogInfo("Health : " + zombies.get(0).getHealth());
				plugin.LogInfo("LastDamage : " + zombies.get(0).getLastDamage());
				plugin.LogInfo("isDead : " + zombies.get(0).isDead());
				return zombies.get(0).getLocation();
			} else {
				plugin.getSpawnTimer().getState();
				plugin.LogSevere("There is " + zombies.size() + " zombie alive of " + alreadySpawned + "/" + totalToSpawn + " spawned . The wave is " + (finished ? "finished" : "in progress"));
				return null;
			}
		else
			return null;
	}

	/**
	 * Get the total quantity of zombie to spawn
	 * (Not counting zombies killed)
	 * @return total to spawn
	 */
	public int getMaxSpawn() {
		return totalToSpawn;
	}

	/**
	 * Get the number of zombie already spawned
	 * @return number of zombie already spawned
	 */
	public int getTotal() {
		return alreadySpawned;
	}

	/**
	 * Get the first spawn in a loaded chunk
	 * @return
	 */
	public Location getValidSpawn() {
		for (final Location curPos : plugin.getLevel().getActiveSpawns())
			if (curPos.getWorld().isChunkLoaded(curPos.getWorld().getChunkAt(curPos))) return curPos;
		plugin.LogInfo(plugin.getLocale("Warning.AllSpawnsCurrentlyUnloaded"));
		return null;

	}

	/**
	 * If the spawner should continue spawning
	 * @return
	 */
	public boolean haveZombieToSpawn() {