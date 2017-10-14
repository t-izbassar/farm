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
package com.zerocracy.entry;

import com.github.mongobee.Mongobee;
import com.github.mongobee.exception.MongobeeException;
import com.mongodb.MongoClient;
import com.zerocracy.jstk.Farm;
import java.io.IOException;

/**
 * Apply Mongobee changes.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.18
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class ExtMongobee {

    /**
     * The farm.
     */
    private final Farm farm;

    /**
     * Ctor.
     * @param frm The farm
     */
    public ExtMongobee(final Farm frm) {
        this.farm = frm;
    }

    /**
     * Apply mongobee.
     * @throws IOException If fails
     */
    public void apply() throws IOException {
        try (final MongoClient client = new ExtMongo(this.farm).value()) {
            final Mongobee bee = new Mongobee(client);
            bee.setDbName("footprint");
            bee.setChangeLogsScanPackage(ExtMongo.class.getPackage().getName());
            if (bee.isExecutionInProgress()) {
                throw new IllegalStateException(
                    "MongoDB is busy, can't apply Mongobee changes"
                );
            }
            bee.execute();
        } catch (final MongobeeException ex) {
            throw new IOException(ex);
        }
    }

}