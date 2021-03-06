/**
 * Copyright (c) 2016-2018 Zerocracy
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
package com.zerocracy.tk.profile;

import com.jcabi.matchers.XhtmlMatchers;
import com.zerocracy.Farm;
import com.zerocracy.Project;
import com.zerocracy.farm.fake.FkFarm;
import com.zerocracy.farm.fake.FkProject;
import com.zerocracy.farm.props.PropsFarm;
import com.zerocracy.pmo.Agenda;
import com.zerocracy.pmo.People;
import com.zerocracy.tk.RqWithUser;
import com.zerocracy.tk.TkApp;
import java.net.HttpURLConnection;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.takes.facets.hamcrest.HmRsStatus;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkAgenda}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.13
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class TkAgendaTest {

    @Test
    public void rendersXmlAgendaPage() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                new RsPrint(
                    new TkApp(farm).act(
                        new RqWithUser(
                            farm,
                            new RqFake(
                                new ListOf<>(
                                    "GET /u/Yegor256/agenda",
                                    "Host: www.example.com",
                                    "Accept: application/xml"
                                ),
                                ""
                            )
                        )
                    )
                ).printBody()
            ),
            XhtmlMatchers.hasXPaths("/page")
        );
    }

    @Test
    public void rendersHtmlAgendaPageForFirefox() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                new RsPrint(
                    new TkApp(farm).act(
                        new RqWithUser(
                            farm,
                            new RqFake(
                                new ListOf<>(
                                    "GET /u/yegor256/agenda",
                                    "Host: www.example.com",
                                    // @checkstyle LineLength (1 line)
                                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0"
                                ),
                                ""
                            )
                        )
                    )
                ).printBody()
            ),
            XhtmlMatchers.hasXPaths("//xhtml:body")
        );
    }

    @Test
    public void redirectsWhenAccessingNonexistentUsersAgenda()
        throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        new People(farm).bootstrap().touch("yegor256");
        MatcherAssert.assertThat(
            new RsPrint(
                new TkApp(farm).act(
                    new RqWithUser(
                        farm,
                        new RqFake("GET", "/u/foo-user/agenda")
                    )
                )
            ),
            new HmRsStatus(HttpURLConnection.HTTP_SEE_OTHER)
        );
    }

    @Test
    public void rendersAgendaPage() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        final Agenda agenda = new Agenda(farm, "yegor256").bootstrap();
        final Project project = new FkProject();
        final String job = "gh:test/test#2";
        agenda.add(project, job, "DEV");
        final String title = "Some random title is here";
        agenda.title(job, title);
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                new RsPrint(
                    new TkApp(farm).act(
                        new RqWithUser(
                            farm,
                            new RqFake(
                                new ListOf<>(
                                    "GET /u/yegor256/agenda",
                                    "Host: www.example.com"
                                ),
                                ""
                            )
                        )
                    )
                ).printBody()
            ),
            XhtmlMatchers.hasXPaths(
                String.format("//xhtml:td[.='%s']", title)
            )
        );
    }
}
