/**
 * Copyright (c) 2016 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.crews.github;

import com.jcabi.log.Logger;
import com.zerocracy.jstk.Farm;
import java.io.IOException;
import javax.json.JsonObject;

/**
 * Log and pass through.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class ReLogged implements Reaction {

    /**
     * Reaction.
     */
    private final Reaction reaction;

    /**
     * Ctor.
     * @param rtn Reaction
     */
    public ReLogged(final Reaction rtn) {
        this.reaction = rtn;
    }

    @Override
    public void react(final Farm farm, final JsonObject event)
        throws IOException {
        Logger.info(
            this,
            "GitHub (repo=%s, reason=%s)",
            event.getJsonObject("repository").getString("full_name"),
            event.getString("reason")
        );
        this.reaction.react(farm, event);
    }

}