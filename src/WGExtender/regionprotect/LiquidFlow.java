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

package WGExtender.regionprotect;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import WGExtender.Config;
import WGExtender.Main;
import WGExtender.utils.WGRegionUtils;

public class LiquidFlow implements Listener {

	private Main main;
	private Config config;

	
	public LiquidFlow(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onLiquidFlow(BlockFromToEvent e)
	{
		Block b = e.getBlock();
		if (b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA)
		{
			if (config.blocklavaflow)
			{
				if (!WGRegionUtils.isInTheSameRegion(main.wg, b.getLocation(), e.getToBlock().getLocation()))
				{
					e.setCancelled(true);
				}
			}
		} else
		if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER)
		{
			if (config.blockwaterflow)
			{
				if (!WGRegionUtils.isInTheSameRegion(main.wg, b.getLocation(), e.getToBlock().getLocation()))
				{
					e.setCancelled(true);
				}
			}
		}
	}
	
}
