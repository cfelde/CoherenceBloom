/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.cfelde.coherence.bloom;

import com.tangosol.net.DefaultCacheServer;

/**
 *
 * @author Christian Felde (cfelde [at] cfelde [dot] com)
 */
public class StartProxy {
    public static void main(String... args) throws Exception {
        System.setProperty("tangosol.coherence.cacheconfig", "proxy-config.xml");
        System.setProperty("tangosol.coherence.distributed.localstorage", "false");

        DefaultCacheServer.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}
