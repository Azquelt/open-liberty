/**
 *
 */
package com.ibm.ws.example;

import static org.junit.Assert.assertEquals;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

import org.junit.Test;

import com.ibm.ws.applibtest.api.LibAccess;

import componenttest.app.FATServlet;

/**
 *
 */
@ApplicationScoped
@WebServlet("/test")
public class AppLibTestServlet extends FATServlet {

    /**  */
    private static final long serialVersionUID = 1L;

    @Inject
    LibAccess libAccess;

    @Test
    public void test() {
        assertEquals(5, libAccess.add(2, 3));
    }

}
