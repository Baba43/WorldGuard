/*
 * WorldGuard, a suite of tools for Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldGuard team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldguard.terraconia;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.resolver.ProfileService;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.bab43.playerapi.api.PlayerApi;
import de.bab43.playerapi.model.PlayerInfo;
import de.bab43.playerapi.plugin.PlayerAPIPlugin;
import de.baba43.lib.log.IReporter;
import de.baba43.serverapi.plugin.ServerAPI;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nullable;
import java.io.IOException;

public class TerraconiaProfileService implements ProfileService {

    private final IReporter log;
    private PlayerApi playerApi;
    private ProfileService fallback;

    public TerraconiaProfileService(ProfileService fallback) {
        this.log = ServerAPI.getAPI().getReporter(WorldGuardPlugin.inst());
        this.fallback = fallback;
        this.playerApi = PlayerAPIPlugin.getApi();
    }

    @Override
    public int getIdealRequestLimit() {
        return 0;
    }

    @Nullable
    @Override
    public Profile findByName(String playerName) throws IOException, InterruptedException {
        final PlayerInfo player = playerApi.getPlayerByName(playerName);
        if (player != null) {
            log.d("ProfileService", "Resolved: " + playerName);
            return new Profile(player.getUuid(), player.getName());
        } else {
            log.d("ProfileService", "Used fallback for: `" + playerName + "`");
            return fallback.findByName(playerName);
        }
    }

    @Override
    public ImmutableList<Profile> findAllByName(Iterable<String> playerNames) throws IOException, InterruptedException {
        ImmutableList.Builder<Profile> profiles = new ImmutableList.Builder<Profile>();
        for (String playerName : playerNames) {
            final Profile profile = findByName(playerName);
            if(profile != null) {
                profiles.add(profile);
            }
        }
        return profiles.build();
    }

    @Override
    public void findAllByName(Iterable<String> iterable, Predicate<Profile> predicate) throws IOException, InterruptedException {
        throw new NotImplementedException("findAllByName is not implemented");
    }
}
