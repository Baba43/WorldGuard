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

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.baba43.lib.log.IReporter;
import de.baba43.lib.log.forms.FieldLog;
import de.baba43.serverapi.plugin.ServerAPI;

import java.util.HashSet;
import java.util.Set;

public class DeprecatedCallLogger {

    private final IReporter log = ServerAPI.getAPI().getReporter(WorldGuardPlugin.inst());
    private final Set<String> invalidCalls = new HashSet<>();

    private static DeprecatedCallLogger instance;

    public DeprecatedCallLogger() {
        instance = this;
    }

    public void logInvalidCall(String method) {
        StringBuilder sb = new StringBuilder();
        final StackTraceElement[] st = Thread.currentThread().getStackTrace();
        for (int i = 3; i < st.length; i++) {
            final StackTraceElement element = st[i];
            sb.append(element.toString());
            sb.append("\n");
        }
        final String call = sb.toString();
        if (invalidCalls.add(call)) {
            log.d("InvalidCall", new FieldLog("Invalid call to `" + method + "`")
                    .add("Stacktrace", call).getMailText());
        }
    }

    public static void logInvalidPlayerDomainLookup(String method) {
        instance.logInvalidCall(method);
    }
}
