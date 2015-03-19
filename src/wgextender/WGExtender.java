/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package wgextender;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import wgextender.commands.Commands;
import wgextender.regionprotect.ownormembased.IgniteByPlayer;
import wgextender.regionprotect.ownormembased.RestrictCommands;
import wgextender.regionprotect.regionbased.BlockBurn;
import wgextender.regionprotect.regionbased.EntityExplode;
import wgextender.regionprotect.regionbased.FireSpread;
import wgextender.regionprotect.regionbased.LiquidFlow;
import wgextender.regionprotect.regionbased.Pistons;
import wgextender.wgcommandprocess.WGRegionCommandWrapper;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGExtender extends JavaPlugin {

	private static WGExtender instance;
	public static WGExtender getInstance() {
		return instance;
	}

	private static Logger log;

	private Config config;
	private Commands commands;

	private WorldEditPlugin we = null;
	public WorldEditPlugin getWorldEdit() {
		return we;
	}

	private WorldGuardPlugin wg = null;
	public WorldGuardPlugin getWorldGuard() {
		return wg;
	}

	@Override
	public void onEnable() {
		instance = this;
		log = getLogger();
		we = JavaPlugin.getPlugin(WorldEditPlugin.class);
		wg = JavaPlugin.getPlugin(WorldGuardPlugin.class);
		config = new Config(this);
		config.loadConfig();
		commands = new Commands(config);
		getCommand("wgex").setExecutor(commands);
		getServer().getPluginManager().registerEvents(new RestrictCommands(config), this);
		getServer().getPluginManager().registerEvents(new LiquidFlow(config), this);
		getServer().getPluginManager().registerEvents(new IgniteByPlayer(config), this);
		getServer().getPluginManager().registerEvents(new FireSpread(config), this);
		getServer().getPluginManager().registerEvents(new BlockBurn(config), this);
		getServer().getPluginManager().registerEvents(new Pistons(config), this);
		getServer().getPluginManager().registerEvents(new EntityExplode(config), this);
		try {
			WGRegionCommandWrapper.inject(config);
		} catch (Throwable t) {
			log(Level.SEVERE, "Unable to inject WorldGuard region command wrapper, shutting down");
			t.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onDisable() {
		try {
			WGRegionCommandWrapper.uninject();
		} catch (Throwable t) {
			log(Level.SEVERE, "Unable to uninject WorldGuard region command wrapper, shutting down");
			t.printStackTrace();
			Bukkit.shutdown();
		}
		config = null;
		we = null;
		wg = null;
		instance = null;
	}

	public static void log(Level level, String message) {
		if (log != null) {
			log.log(Level.SEVERE, message);
		}
	}

}
