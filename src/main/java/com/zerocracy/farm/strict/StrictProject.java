/**
 * Copyright (c) 2016-2017 Zerocracy
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
package com.zerocracy.farm.strict;

import com.zerocracy.jstk.Item;
import com.zerocracy.jstk.Project;
import java.io.IOException;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;

/**
 * PMO project.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "origin")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class StrictProject implements Project {

    /**
     * Files that are allowed in PMO.
     */
    private static final Pattern PMO = Pattern.compile(
        String.join(
            "|",
            "(claims\\.xml)",
            "(catalog\\.xml)",
            "(bots\\.xml)",
            "(people\\.xml)",
            "(awards/[a-zA-Z0-9-]+\\.xml)",
            "(agenda/[a-zA-Z0-9-]+\\.xml)",
            "(projects/[a-zA-Z0-9-]+\\.xml)"
        )
    );

    /**
     * Files that are allowed in a regular project.
     */
    private static final Pattern PROJECT = Pattern.compile(
        String.join(
            "|",
            "(claims\\.xml)",
            "(roles\\.xml)",
            "(elections\\.xml)",
            "(estimates\\.xml)",
            "(wbs\\.xml)",
            "(orders\\.xml)",
            "(bans\\.xml)",
            "(test\\.txt)",
            "(precedences\\.xml)",
            "(milestones\\.xml)",
            "(impediments\\.xml)",
            "(boosts\\.xml)"
        )
    );

    /**
     * Origin project.
     */
    private final Project origin;

    /**
     * Ctor.
     * @param pkt Project
     */
    StrictProject(final Project pkt) {
        this.origin = pkt;
    }

    @Override
    public String toString() {
        return this.origin.toString();
    }

    @Override
    public Item acq(final String file) throws IOException {
        final boolean pmo = "PMO".equals(this.origin.toString());
        if (pmo && !StrictProject.PMO.matcher(file).matches()) {
            throw new IllegalArgumentException(
                String.format(
                    "File \"%s\" is not accessible in \"%s\"",
                    file, this.origin
                )
            );
        }
        if (!pmo && !StrictProject.PROJECT.matcher(file).matches()) {
            throw new IllegalArgumentException(
                String.format(
                    "File \"%s\" is not allowed in project \"%s\"",
                    file, this.origin
                )
            );
        }
        return this.origin.acq(file);
    }

}